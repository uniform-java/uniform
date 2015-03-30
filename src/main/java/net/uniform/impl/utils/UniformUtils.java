/* 
 * Copyright 2015 Eduardo Ramos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.uniform.impl.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.uniform.exceptions.UniformException;

/**
 * Utils class for Uniform
 * @author Eduardo Ramos
 */
public class UniformUtils {
    
    /**
     * Returns the first value of a list, or null if the list is empty.
     * @param <T> List type
     * @param list Input list
     * @return First value or null
     */
    public static <T> T firstValue(List<T> list){
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
    
    /**
     * Returns an index of all the accessible properties of a Java bean object.
     * @param bean Input object
     * @return Index of properties, never null
     */
    public static Map<String, Object> getBeanProperties(Object bean){
        Map<String, Object> props = new HashMap<>();
        
        if(bean == null){
            return props;
        }
        
        Class<?> clazz = bean.getClass();
        try {
            //Public fields:
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                String name = field.getName();
                if(!name.equals("class")){
                    props.put(field.getName(), field.get(bean));
                }
            }
            
            //Getters:
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor desc : info.getPropertyDescriptors()) {
                Method readMethod = desc.getReadMethod();
                String name = desc.getName();
                if(readMethod != null && !name.equals("class")){
                    props.put(desc.getName(), readMethod.invoke(bean));
                }
            }
            
            return props;
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new UniformException("Error while getting bean object properties of class" + clazz.getName(), ex);
        }
    }
    
    /**
     * Injects the values of the given properties into the given Java bean object.
     * It makes sure that object class properties names match and their types are compatible.
     * @param bean Input object
     * @param props Properties to inject
     */
    public static void fillBeanProperties(Object bean, Map<String, Object> props){
        if(bean == null || props == null){
            return;
        }
        
        Class<?> clazz = bean.getClass();
        try {
            //Public fields:
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                String name = field.getName();
                
                if(!name.equals("class")){
                    if(props.containsKey(name)){
                        Object value = props.get(name);
                        
                        Class<?> fieldClass = field.getType();
                        if(value != null){
                            Class<?> valueClass = value.getClass();
                            
                            if(fieldClass.isAssignableFrom(valueClass)){
                                field.set(bean, value);
                            }
                        }else{
                            //Cannot set null to primitive
                            if(!fieldClass.isPrimitive()){
                                field.set(bean, value);
                            }
                        }
                    }
                }
            }
            
            //Getters:
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor desc : info.getPropertyDescriptors()) {
                Method writeMethod = desc.getWriteMethod();
                String name = desc.getName();
                if(writeMethod != null && !name.equals("class")){
                    if(props.containsKey(name)){
                        Object value = props.get(name);
                        
                        Class<?> fieldClass = desc.getPropertyType();
                        if(value != null){
                            Class<?> valueClass = value.getClass();
                            
                            if(fieldClass.isAssignableFrom(valueClass)){
                                writeMethod.invoke(bean, value);
                            }
                        }else{
                            //Cannot set null to primitive
                            if(!fieldClass.isPrimitive()){
                                writeMethod.invoke(bean, value);
                            }
                        }
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new UniformException("Error while setting bean object properties of class" + clazz.getName(), ex);
        }
    }
}
