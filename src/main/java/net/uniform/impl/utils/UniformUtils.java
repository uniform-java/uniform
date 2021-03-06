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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private final static double EPSILON = 1e-9;

    public static boolean equalsEpsilon(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Makes sure that a property name is not empty an is lower-case. Throws an {@code IllegalArgumentException} if the key is null or empty after trimming.
     *
     * @param key Original key
     * @return Trimmed and lower-case key
     */
    public static String checkPropertyNameAndLowerCase(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        key = key.trim().toLowerCase();

        if (key.isEmpty()) {
            throw new IllegalArgumentException("key cannot be empty");
        }

        return key;
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

        if (collectionsGenericTypes == null) {
            collectionsGenericTypes = EMPTY_MAP;
        }

        Class<?> beanClass = bean.getClass();
        try {
            //Public fields:
            Field[] fields = beanClass.getFields();
            for (Field field : fields) {
                String name = field.getName();

                if (!name.equals("class")) {
                    if (props.containsKey(name)) {
                        Object value = props.get(name);
                        Class fieldClass = field.getType();
                        
                        Type fieldGenericType = null;
                        try {
                            fieldGenericType = field.getGenericType();
                        } catch (Exception e) {
                            //Not a generic
                        }
                        Object preparedValue = prepareValueForField(name, value, fieldClass, collectionsGenericTypes, fieldGenericType);
                        
                        if(preparedValue != CANNOT_FILL_VALUE){
                            field.set(bean, preparedValue);
                        }
                    }
                }
            }

            //Setters:
            BeanInfo info = Introspector.getBeanInfo(beanClass);
            for (PropertyDescriptor desc : info.getPropertyDescriptors()) {
                Method writeMethod = desc.getWriteMethod();
                String name = desc.getName();
                if (writeMethod != null && !name.equals("class")) {
                    if (props.containsKey(name)) {
                        Object value = props.get(name);
                        Class fieldClass = desc.getPropertyType();
                        
                        Type fieldGenericType = null;
                        try {
                            fieldGenericType = writeMethod.getGenericParameterTypes()[0];
                        } catch (Exception e) {
                            //Not a generic
                        }
                        Object preparedValue = prepareValueForField(name, value, fieldClass, collectionsGenericTypes, fieldGenericType);
                        
                        if(preparedValue != CANNOT_FILL_VALUE){
                            writeMethod.invoke(bean, preparedValue);
                        }
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            throw new UniformException("Error while setting bean object properties of class" + beanClass.getName(), ex);
        }
    }
    
    private static final Object CANNOT_FILL_VALUE = new Object();
    
    private static Object prepareValueForField(String name, Object value, Class fieldClass, Map<String, Class<?>> collectionsGenericTypes, Type fieldGenericType) throws InstantiationException, IllegalAccessException{
        if (value != null) {
            Class valueClass = value.getClass();

            if (fieldClass.isAssignableFrom(valueClass)) {
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    //Double check for collections when we have the type info:
                    if (collectionsGenericTypes.containsKey(name)) {
                        if (isGenericTypeCompatible(fieldGenericType, collectionsGenericTypes.get(name))) {
                            return value;
                        }
                    } else {
                        //We have no info, assume the generic type is correct
                        return value;
                    }
                } else {
                    return value;
                }
            } else if (isWrapperAndPrimitivePair(fieldClass, valueClass)) {
                return value;
            } else if (fieldClass.equals(String.class) && isPrimitiveOrWrapper(valueClass)) {
                //Try to convert to string for simple types only
                return value.toString();
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                //The bean field type is a collection of the data value type
                //We fill the collection with a single element if possible:

                Collection collection = reflectionCollectionInstance(fieldClass);
                if (collection != null) {
                    //Double check for collections when we have the type info:
                    if (collectionsGenericTypes.containsKey(name)) {
                        if (isGenericTypeCompatible(fieldGenericType, collectionsGenericTypes.get(name))) {
                            if (value instanceof Collection) {
                                collection.addAll((Collection) value);
                            } else {
                                collection.add(value);
                            }

                            return collection;
                        }
                    } else {
                        //We have no info, assume the generic type is correct
                        if (value instanceof Collection) {
                            collection.addAll((Collection) value);
                        } else {
                            collection.add(value);
                        }
                        
                        return collection;
                    }
                }
            }
        } else if (!fieldClass.isPrimitive()) {//Cannot set null to primitive
            return value;
        }
        
        return CANNOT_FILL_VALUE;
    }

    private static boolean isGenericTypeCompatible(Type type, Class<?> genericClass) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return pType.getActualTypeArguments().length == 1 && ((Class<?>) pType.getActualTypeArguments()[0]).isAssignableFrom(genericClass);
        } else {
            return true;//No generic, raw collection, Object is compatible with any type
        }
    }

    private static boolean isWrapperAndPrimitivePair(Class<?> c1, Class<?> c2) {
        if (c1 == null || c2 == null) {
            return false;
        }

        if (c1.isPrimitive()) {
            return PRIMITIVES_TO_WRAPPERS.get(c1).equals(c2);
        } else if (c2.isPrimitive()) {
            return PRIMITIVES_TO_WRAPPERS.get(c2).equals(c1);
        }

        return false;
    }

    private static boolean isPrimitive(Class<?> type) {
        return PRIMITIVES_TO_WRAPPERS.containsKey(type);
    }

    private static boolean isWrapper(Class<?> type) {
        return PRIMITIVES_TO_WRAPPERS.containsValue(type);
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
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

    private static Collection reflectionCollectionInstance(Class fieldClass) throws InstantiationException, IllegalAccessException {
        if (fieldClass.isInterface()) {
            //Cannot instantiate an interface, just try with some typical types:
            if (fieldClass.equals(List.class)) {
                return new ArrayList();
            } else if (fieldClass.equals(Set.class)) {
                return new LinkedHashSet();
            } else {
                return null;
            }
        } else {
            return (Collection) fieldClass.newInstance();
        }
    }
}
