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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.uniform.exceptions.UniformException;

/**
 * Utils class for Uniform
 *
 * @author Eduardo Ramos
 */
public class UniformUtils {

    /**
     * Returns the first value of a list, or null if the list is empty.
     *
     * @param <T> List type
     * @param list Input list
     * @return First value or null
     */
    public static <T> T firstValue(List<T> list) {
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Returns an index of all the accessible properties of a Java bean object.
     *
     * @param bean Input object
     * @return Index of properties, never null
     */
    public static Map<String, Object> getBeanProperties(Object bean) {
        Map<String, Object> props = new HashMap<>();

        if (bean == null) {
            return props;
        }

        Class<?> clazz = bean.getClass();
        try {
            //Public fields:
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                String name = field.getName();
                if (!name.equals("class")) {
                    props.put(field.getName(), field.get(bean));
                }
            }

            //Getters:
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor desc : info.getPropertyDescriptors()) {
                Method readMethod = desc.getReadMethod();
                String name = desc.getName();
                if (readMethod != null && !name.equals("class")) {
                    props.put(desc.getName(), readMethod.invoke(bean));
                }
            }

            return props;
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new UniformException("Error while getting bean object properties of class" + clazz.getName(), ex);
        }
    }
    
    private static final Map<String, Class<?>> EMPTY_MAP = new HashMap<>();

    /**
     * Injects the values of the given properties into the given Java bean object. It makes sure that object class properties names match and their types are compatible.
     *
     * @param bean Input object
     * @param props Properties to inject
     */
    public static void fillBeanProperties(Object bean, Map<String, Object> props) {
        fillBeanProperties(bean, props, EMPTY_MAP);
    }
    
    /**
     * Injects the values of the given properties into the given Java bean object. It makes sure that object class properties names match and their types are compatible.
     *
     * @param bean Input object
     * @param props Properties to inject
     * @param collectionsGenericTypes Generic types for each collection value in {@code props}
     */
    public static void fillBeanProperties(Object bean, Map<String, Object> props, Map<String, Class<?>> collectionsGenericTypes) {
        if (bean == null || props == null) {
            return;
        }
        
        if(collectionsGenericTypes == null){
            collectionsGenericTypes = EMPTY_MAP;
        }

        Class<?> clazz = bean.getClass();
        try {
            //Public fields:
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                String name = field.getName();

                if (!name.equals("class")) {
                    if (props.containsKey(name)) {
                        Object value = props.get(name);

                        Class<?> fieldClass = field.getType();
                        if (value != null) {
                            Class<?> valueClass = value.getClass();

                            if (fieldClass.isAssignableFrom(valueClass)) {
                                if(Collection.class.isAssignableFrom(fieldClass)){
                                    //Double check for collections when we have the type info:
                                    if(collectionsGenericTypes.containsKey(name)){
                                        if(isGenericTypeCompatible(field.getType(), collectionsGenericTypes.get(name))){
                                            field.set(bean, value);
                                        }
                                    }else{
                                        //We have no info, assume the generic type is correct
                                        field.set(bean, value);
                                    }
                                }else{
                                    field.set(bean, value);
                                }
                            } else if (isWrapperAndPrimitivePair(fieldClass, valueClass)) {
                                field.set(bean, value);
                            } else if(fieldClass.equals(String.class) && isPrimitiveOrWrapper(valueClass)){
                                //Try to convert to string for simple types only
                                field.set(bean, value.toString());
                            }
                        } else {
                            //Cannot set null to primitive
                            if (!fieldClass.isPrimitive()) {
                                field.set(bean, value);
                            }
                        }
                    }
                }
            }

            //Setters:
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor desc : info.getPropertyDescriptors()) {
                Method writeMethod = desc.getWriteMethod();
                String name = desc.getName();
                if (writeMethod != null && !name.equals("class")) {
                    if (props.containsKey(name)) {
                        Object value = props.get(name);

                        Class<?> fieldClass = desc.getPropertyType();
                        if (value != null) {
                            Class<?> valueClass = value.getClass();

                            if (fieldClass.isAssignableFrom(valueClass)) {
                                if(Collection.class.isAssignableFrom(fieldClass)){
                                    //Double check for collections when we have the type info:
                                    if(collectionsGenericTypes.containsKey(name)){
                                        if(isGenericTypeCompatible(writeMethod.getGenericParameterTypes()[0], collectionsGenericTypes.get(name))){
                                            writeMethod.invoke(bean, value);
                                        }
                                    }else{
                                        //We have no info, assume the generic type is correct
                                        writeMethod.invoke(bean, value);
                                    }
                                }else{
                                    writeMethod.invoke(bean, value);
                                }
                                
                            } else if (isWrapperAndPrimitivePair(fieldClass, valueClass)) {
                                writeMethod.invoke(bean, value);
                            } else if(fieldClass.equals(String.class) && isPrimitiveOrWrapper(valueClass)){
                                //Try to convert to string for simple types only
                                writeMethod.invoke(bean, value.toString());
                            }
                        } else {
                            //Cannot set null to primitive
                            if (!fieldClass.isPrimitive()) {
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
    
    private static boolean isGenericTypeCompatible(Type type, Class<?> genericClass){
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType)type;
            return pType.getActualTypeArguments().length == 1 && ((Class<?>) pType.getActualTypeArguments()[0]).isAssignableFrom(genericClass);
        } else {
            return true;//No generic, raw collection, Object is compatible with any type
        }
    }

    private static boolean isWrapperAndPrimitivePair(Class<?> c1, Class<?> c2) {
        if(c1 == null || c2 == null){
            return false;
        }
        
        if(c1.isPrimitive()){
            return PRIMITIVES_TO_WRAPPERS.get(c1).equals(c2);
        }else if(c2.isPrimitive()){
            return PRIMITIVES_TO_WRAPPERS.get(c2).equals(c1);
        }
        
        return false;
    }
    
    private static boolean isPrimitive(Class<?> type){
        return PRIMITIVES_TO_WRAPPERS.containsKey(type);
    }
    
    private static boolean isWrapper(Class<?> type){
        return PRIMITIVES_TO_WRAPPERS.containsValue(type);
    }
    
    private static boolean isPrimitiveOrWrapper(Class<?> type){
        return isPrimitive(type) || isWrapper(type);
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<>();
            
        static {
            PRIMITIVES_TO_WRAPPERS.put(boolean.class, Boolean.class);
            PRIMITIVES_TO_WRAPPERS.put(byte.class, Byte.class);
            PRIMITIVES_TO_WRAPPERS.put(char.class, Character.class);
            PRIMITIVES_TO_WRAPPERS.put(double.class, Double.class);
            PRIMITIVES_TO_WRAPPERS.put(float.class, Float.class);
            PRIMITIVES_TO_WRAPPERS.put(int.class, Integer.class);
            PRIMITIVES_TO_WRAPPERS.put(long.class, Long.class);
            PRIMITIVES_TO_WRAPPERS.put(short.class, Short.class);
            PRIMITIVES_TO_WRAPPERS.put(void.class, Void.class);
        }
}
