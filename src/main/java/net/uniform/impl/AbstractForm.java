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
package net.uniform.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import net.uniform.api.Decorator;
import net.uniform.api.Element;
import net.uniform.api.ElementWithValueConversion;
import net.uniform.api.Form;
import net.uniform.api.FormValidator;
import net.uniform.api.Renderer;
import net.uniform.api.Validator;
import net.uniform.api.annotations.IgnoreDefaultFormDecorators;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.exceptions.UniformException;
import net.uniform.impl.utils.HTMLRenderingUtils;
import net.uniform.impl.utils.UniformUtils;

/**
 * Abstract form implementation that contains all generic logic that any form should have.
 *
 * @author Eduardo Ramos
 */
public abstract class AbstractForm implements Form {

    protected final Map<String, Element> formElements;
    protected final Map<String, Decorator> formDecorators;
    protected final Map<String, String> properties;
    protected final List<FormValidator> validators;
    protected boolean validationPeformed = false;
    protected boolean autoEnableElementsTranslation = false;

    protected Map<Class<? extends Element>, Renderer> defaultRenderers;
    protected Map<Class<? extends Element>, List<Decorator>> defaultElementDecorators;

    protected final List<Object> renderingParts;

    protected final Stack<String> openDecorators;

    public AbstractForm() {
        this.formElements = new HashMap<>();
        this.properties = new HashMap<>();
        this.validators = new ArrayList<>();
        this.defaultRenderers = new HashMap<>();
        this.defaultElementDecorators = new HashMap<>();

        this.formDecorators = new HashMap<>();
        this.renderingParts = new ArrayList<>();

        this.openDecorators = new Stack<>();

        init();
    }

    private void init() {
        setupDefaultRenderers();
        setupDefaultDecorators();
        setupTopFormDecorators();
    }

    protected abstract void setupDefaultRenderers();

    protected abstract void setupDefaultDecorators();

    protected abstract void setupTopFormDecorators();

    @Override
    public Form addElement(Element element, boolean useDefaultDecorators) {
        String id = element.getId();
        if (id == null) {
            throw new IllegalArgumentException("Element id cannot be null");
        }

        if (this.formElements.containsKey(id)) {
            throw new IllegalArgumentException("Element id '" + id + "' already in use");
        }

        if (element.getRenderer() == null) {
            Renderer formRendererForElement = this.getDefaultRendererAppliedToElement(element);
            if (formRendererForElement != null) {
                element.setRenderer(formRendererForElement);
            }
        }

        if (useDefaultDecorators && !element.getClass().isAnnotationPresent(IgnoreDefaultFormDecorators.class)) {
            List<Decorator> decorators = element.getDecorators();
            if (decorators == null || decorators.isEmpty()) {
                element.setDecorators(this.buildDefaultDecoratorsForElement(element));
            }
        }

        if (isAutoEnableElementsTranslation()) {
            element.setTranslationEnabled(true);
        }

        this.formElements.put(id, element);
        this.renderingParts.add(element);
        return this;
    }

    @Override
    public Form addElement(Element element) {
        this.addElement(element, true);
        return this;
    }

    @Override
    public Form addElements(Element... elements) {
        for (Element element : elements) {
            this.addElement(element);
        }

        return this;
    }

    @Override
    public Form addElements(boolean useDefaultDecorators, Element... elements) {
        for (Element element : elements) {
            this.addElement(element, useDefaultDecorators);
        }
        return this;
    }

    @Override
    public Form addDefaultDecoratorForElementClass(Class<? extends Element> clazz, Decorator decorator) {
        if (!this.defaultElementDecorators.containsKey(clazz)) {
            this.defaultElementDecorators.put(clazz, new ArrayList<Decorator>());
        }

        if (decorator != null) {
            this.defaultElementDecorators.get(clazz).add(decorator);
        }

        return this;
    }

    @Override
    public Form setDefaultDecoratorsForElementClass(Class<? extends Element> clazz, List<Decorator> decorators) {
        ArrayList<Decorator> list = new ArrayList<>();

        if (decorators != null) {
            for (Decorator decorator : decorators) {
                list.add(decorator);
            }
        }
        this.defaultElementDecorators.put(clazz, list);

        return this;
    }

    @Override
    public List<Decorator> getDefaultDecoratorsForElementClass(Class<? extends Element> clazz) {
        if (this.defaultElementDecorators.containsKey(clazz)) {
            List<Decorator> result = this.defaultElementDecorators.get(clazz);

            if (result == null) {
                result = new ArrayList<>();
            }

            return new ArrayList<>(result);
        } else {
            return null;
        }
    }

    @Override
    public Form removeDefaultDecoratorsForElementClass(Class<? extends Element> clazz) {
        this.defaultElementDecorators.remove(clazz);
        return this;
    }

    @Override
    public Form setDefaultRendererForElementClass(Class<? extends Element> clazz, Renderer renderer) {
        this.defaultRenderers.put(clazz, renderer);
        return this;
    }

    @Override
    public Renderer getDefaultRendererForElementClass(Class<? extends Element> clazz) {
        return this.defaultRenderers.get(clazz);
    }

    @Override
    public Form removeDefaultRendererForElementClass(Class<? extends Element> clazz) {
        return this.setDefaultRendererForElementClass(clazz, null);
    }

    private static final String END_DECORATOR_PREFIX = "END_DECORATOR_";

    private List<Decorator> buildDefaultDecoratorsForElement(Element element) {
        List<Decorator> elementClassDecorators = getDefaultDecoratorsAppliedToElementClass(element.getClass());

        if (elementClassDecorators != null) {
            List<Decorator> originalDecorators = new ArrayList<>();
            originalDecorators.addAll(elementClassDecorators);

            List<Decorator> copiedDecorators = new ArrayList<>();
            for (Decorator original : originalDecorators) {
                if (original != null) {
                    Class<?> clazz = original.getClass();
                    try {
                        Decorator copy = (Decorator) clazz.newInstance();
                        copy.setProperties(original.getProperties());

                        copiedDecorators.add(copy);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new IllegalStateException("The decorator with class '" + clazz.getName() + "' does not have a default constructor, cannot use as a default decorator", e);
                    }
                }
            }

            return copiedDecorators;
        } else {
            return null;
        }
    }

    @Override
    public List<Decorator> getDefaultDecoratorsAppliedToElementClass(Class<? extends Element> clazz) {
        if (defaultElementDecorators.containsKey(clazz)) {
            List<Decorator> decorators = defaultElementDecorators.get(clazz);

            if (decorators == null) {
                //If we are explicitely set a null value, it means we don't want default global decorators for the element.
                //In that case, return an empty list instead, so we are able to detect that
                decorators = new ArrayList<>();
            }

            return decorators;
        } else {
            Class<?> superClass = clazz.getSuperclass();
            if (Element.class.isAssignableFrom(superClass)) {
                return getDefaultDecoratorsAppliedToElementClass((Class<? extends Element>) superClass);
            } else {
                //No specific decorators for the class, return whatever is configured for all elements
                return defaultElementDecorators.get(Element.class);
            }
        }
    }

    private Renderer getDefaultRendererAppliedToElement(Element element) {
        return getDefaultRendererAppliedToElementClass(element.getClass());
    }

    @Override
    public Renderer getDefaultRendererAppliedToElementClass(Class<? extends Element> clazz) {
        if (defaultRenderers.containsKey(clazz)) {
            return defaultRenderers.get(clazz);
        } else {
            Class<?> superClass = clazz.getSuperclass();
            if (Element.class.isAssignableFrom(superClass)) {
                return getDefaultRendererAppliedToElementClass((Class<? extends Element>) superClass);
            } else {
                //No specific decorators for the class, return whatever is configured for all elements
                return defaultRenderers.get(Element.class);
            }
        }
    }

    @Override
    public Form removeElement(Element element) {
        this.removeElement(element.getId());

        return this;
    }

    @Override
    public Form removeElement(String id) {
        Element removed = this.formElements.remove(id);

        if (removed != null) {
            renderingParts.remove(removed);
        }
        return this;
    }

    @Override
    public Map<String, Element> getElements() {
        return new HashMap<>(formElements);
    }

    @Override
    public Element getElement(String id) {
        return this.formElements.get(id);
    }

    @Override
    public Form startDecorator(String id, Decorator decorator) {
        if (formDecorators.containsKey(id)) {
            throw new IllegalArgumentException("Decorator id '" + id + "' already in use");
        }

        this.formDecorators.put(id, decorator);
        this.renderingParts.add(decorator);
        this.openDecorators.add(id);
        return this;
    }

    @Override
    public Form endDecorator() {
        if (openDecorators.isEmpty()) {
            throw new IllegalArgumentException("No open decorators in the form");
        }

        this.renderingParts.add(END_DECORATOR_PREFIX + openDecorators.pop());
        return this;
    }

    @Override
    public Form removeDecorator(String id) {
        Decorator removed = this.formDecorators.remove(id);

        if (removed != null) {
            renderingParts.remove(removed);
            for (Object part : renderingParts) {
                if (part instanceof String && ((String) part).equals(END_DECORATOR_PREFIX + id)) {
                    this.renderingParts.remove(part);
                    break;
                }
            }
        }
        return this;
    }

    @Override
    public Decorator getDecorator(String id) {
        return this.formDecorators.get(id);
    }

    @Override
    public Map<String, Decorator> getDecorators() {
        return new HashMap<>(formDecorators);
    }

    @Override
    public List<SimpleHTMLTag> render() {
        if (!this.openDecorators.isEmpty()) {
            throw new IllegalStateException("There are decorators not closed: " + openDecorators.peek());
        }

        List<SimpleHTMLTag> tags = render(this.renderingParts);

        return tags;
    }

    private List<SimpleHTMLTag> render(List<Object> renderingParts) {
        Stack<Decorator> activeDecorators = new Stack<>();
        Stack<List<SimpleHTMLTag>> activeDecoratorsContent = new Stack<>();
        Stack<List<Element>> activeDecoratorsElements = new Stack<>();

        List<SimpleHTMLTag> result = new ArrayList<>();
        for (Object obj : renderingParts) {
            if (obj instanceof Decorator) {
                activeDecorators.push((Decorator) obj);
                activeDecoratorsContent.push(new ArrayList<SimpleHTMLTag>());
                activeDecoratorsElements.push(new ArrayList<Element>());
            } else if (obj instanceof Element) {
                Element element = (Element) obj;

                List<SimpleHTMLTag> elementResult = element.render(this);
                if (elementResult != null) {
                    if (activeDecoratorsContent.isEmpty()) {
                        addAllNotNull(elementResult, result);
                    } else {
                        addAllNotNull(elementResult, activeDecoratorsContent.peek());
                        activeDecoratorsElements.peek().add(element);
                    }
                }
            } else if (obj instanceof String && ((String) obj).startsWith(END_DECORATOR_PREFIX)) {
                Decorator decorator = activeDecorators.pop();
                List<SimpleHTMLTag> decoratorContentTags = activeDecoratorsContent.pop();
                List<Element> decoratorContentElements = activeDecoratorsElements.pop();

                List<SimpleHTMLTag> decoratorResult = decorator.render(this, decoratorContentElements, decoratorContentTags);
                if (activeDecoratorsContent.isEmpty()) {
                    addAllNotNull(decoratorResult, result);
                } else {
                    addAllNotNull(decoratorResult, activeDecoratorsContent.peek());
                    activeDecoratorsElements.peek().addAll(decoratorContentElements);
                }
            } else if (obj != null) {
                throw new IllegalStateException("Unknown rendering object class " + obj.getClass().getName());
            } else {
                throw new IllegalArgumentException("Unexpected null rendering object");
            }
        }

        return result;
    }

    private void addAllNotNull(List<SimpleHTMLTag> source, List<SimpleHTMLTag> dest) {
        for (SimpleHTMLTag tag : source) {
            if (tag != null) {
                dest.add(tag);
            }
        }
    }

    @Override
    public String renderHTML() {
        return HTMLRenderingUtils.render(this.render());
    }

    @Override
    public boolean isValid() {
        return this.getValidationErrors().isEmpty();
    }

    @Override
    public Map<String, List<String>> getValidationErrors() {
        Map<String, List<String>> globalErrors = new HashMap<>();

        for (Map.Entry<String, Element> entry : formElements.entrySet()) {
            String elementId = entry.getKey();
            Element element = entry.getValue();

            if (element.getValueType() == null) {
                continue;//Skip elements without data type
            }

            List<Validator> elementValidators = element.getValidators();

            if (elementValidators != null) {
                List<String> elementErrors = element.getValidationErrors();
                if (elementErrors != null && !elementErrors.isEmpty()) {
                    globalErrors.put(elementId, elementErrors);
                }
            }
        }

        List<String> formErrors = this.getFormValidationErrors();
        if (formErrors != null && !formErrors.isEmpty()) {
            globalErrors.put(FORM_LEVEL_VALIDATION_ERRORS_INDEX, formErrors);
        }

        this.validationPeformed = true;

        return globalErrors;
    }

    protected List<String> getFormValidationErrors() {
        List<String> formErrors = new ArrayList<>();

        List<FormValidator> formValidators = this.getValidators();
        if (formValidators != null) {
            Map<String, List<String>> formData = this.getFormDataMultivalue();
            for (FormValidator formValidator : formValidators) {
                List<String> validatorErrors = formValidator.getValidationErrors(this, formData);
                if (validatorErrors != null && !validatorErrors.isEmpty()) {
                    formErrors.addAll(validatorErrors);

                    if (formValidator.breakChainOnError()) {
                        break;
                    }
                }
            }
        }

        return formErrors;
    }

    @Override
    public Map<String, List<String>> getFormDataMultivalue() {
        Map<String, List<String>> formData = new HashMap<>();

        for (Element element : formElements.values()) {
            String name = element.getProperty("name");

            if (name != null && !name.trim().isEmpty() && element.getValueType() != null) {//Skip elements without data type or name
                if (formData.containsKey(name)) {
                    throw new IllegalStateException("Name '" + name + "' cannot be repeated in the form");
                }

                List<String> value = element.getValue();
                if (!element.isMultiValue() && value != null && value.size() > 1) {
                    value = value.subList(0, 1);
                }
                formData.put(name, value);
            }
        }

        return formData;
    }

    @Override
    public Map<String, Object> getFormData() {
        Map<String, List<String>> formDataMultivalue = getFormDataMultivalue();
        Map<String, Element> elementsIndexByName = elementsIndexByName();

        Map<String, Object> formData = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : formDataMultivalue.entrySet()) {
            String name = entry.getKey();
            List<String> value = entry.getValue();

            Element element = elementsIndexByName.get(name);

            if (element.isMultiValue()) {
                formData.put(name, value);
            } else {
                formData.put(name, UniformUtils.firstValue(value));
            }
        }

        return formData;
    }

    @Override
    public Map<String, Object> getFormDataConvertedToElementValueTypes() {
        Map<String, List<String>> formDataMultivalue = getFormDataMultivalue();
        Map<String, Element> elementsIndexByName = elementsIndexByName();

        Map<String, Object> formDataConverted = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : formDataMultivalue.entrySet()) {
            String name = entry.getKey();

            Element element = elementsIndexByName.get(name);
            formDataConverted.put(name, getElementValueConvertedToValueType(element));
        }

        return formDataConverted;
    }

    @Override
    public List<String> getElementMultivalue(String elementId) {
        if (this.formElements.containsKey(elementId)) {
            return this.getElement(elementId).getValue();
        } else {
            throw new IllegalArgumentException("Element with id " + elementId + " not found in the form");
        }
    }

    @Override
    public Object getElementValue(String elementId) {
        if (this.formElements.containsKey(elementId)) {
            Element element = getElement(elementId);
            if (element.isMultiValue()) {
                return element.getValue();
            } else {
                return element.getFirstValue();
            }
        } else {
            throw new IllegalArgumentException("Element with id " + elementId + " not found in the form");
        }
    }

    @Override
    public Object getElementValueConvertedToValueType(String elementId) {
        Element element = getElement(elementId);
        if (element != null) {
            return getElementValueConvertedToValueType(element);
        } else {
            throw new IllegalArgumentException("Element with id " + elementId + " not found in the form");
        }
    }

    /**
     * Converts the value of the given element to its target value type. Simple types conversion is supported by the form. For other complex types the form relies on the element implementing
     * {@link ElementWithValueConversion} interface.
     *
     * @param element Element to extract converted value
     * @return Converted value
     */
    private Object getElementValueConvertedToValueType(Element element) {
        Class<?> elementValueType = element.getValueType();
        if (elementValueType == null) {
            return null;
        }

        List<String> values = element.getValue();
        if (element instanceof ElementWithValueConversion) {
            Object converted = ((ElementWithValueConversion) element).getConvertedValue();

            if (converted != null && !elementValueType.isAssignableFrom(converted.getClass())) {
                throw new IllegalStateException("The value type for the element with id " + element.getId() + " is " + elementValueType.getName() + " but the converted value returned by its ElementWithValueConversion implementation is " + converted.getClass().getName());
            }

            return converted;
        } else {
            if (values != null) {
                List<Object> convertedValues = new ArrayList<>();

                for (String value : values) {
                    Object convertedValue = convertBasicValue(value, elementValueType);
                    convertedValues.add(convertedValue);
                }

                if (element.isMultiValue()) {
                    return convertedValues;
                } else {
                    return UniformUtils.firstValue(convertedValues);
                }
            } else {
                if (element.isMultiValue()) {
                    return new ArrayList<>();
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public Form setElementValue(String elementId, List<String> value) {
        if (this.formElements.containsKey(elementId)) {
            Element element = getElement(elementId);
            element.setValue(value);
            return this;
        } else {
            throw new IllegalArgumentException("Element with id " + elementId + " not found in the form");
        }
    }

    @Override
    public Form setElementValue(String elementId, String value) {
        return setElementValue(elementId, value != null ? Arrays.asList(value) : null);
    }

    @Override
    public Form setElementValue(String elementId, Object value) {
        List<String> valueList = convertObjectToListOfStringValues(value);
        return this.setElementValue(elementId, valueList);
    }

    @Override
    public Form getFormDataIntoBean(Object bean) {
        Map<String, Object> formData = getFormDataConvertedToElementValueTypes();
        UniformUtils.fillBeanProperties(bean, formData);

        return this;
    }

    @Override
    public Form populate(Map<String, List<String>> formData) {
        this.reset();
        if (formData == null) {
            return this;
        }

        Map<String, Element> elementsIndexByName = this.elementsIndexByName();

        for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
            String name = entry.getKey();
            List<String> value = entry.getValue();

            if (elementsIndexByName.containsKey(name)) {
                Element element = elementsIndexByName.get(name);

                Class<?> valueType = element.getValueType();
                boolean skipPopulate = valueType == null || element.hasProperty("disabled");//Skip elements without data type or disabled

                if (!skipPopulate) {
                    if (element.isMultiValue()) {
                        element.populate(value);
                    } else {
                        if (value == null) {
                            element.populate(value);
                        } else {
                            if (value.size() > 1) {
                                value = value.subList(0, 1);
                            }
                            element.populate(value);//Only first value of list since the element is not multivalue
                        }
                    }
                }
            }
        }

        return this;
    }

    @Override
    public Form populateSimple(Map<String, Object> formData) {
        this.reset();
        if (formData == null) {
            return this;
        }

        Map<String, List<String>> populateData = new HashMap<>();

        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            List<String> valueList = convertObjectToListOfStringValues(value);

            populateData.put(name, valueList);
        }

        return populate(populateData);
    }

    @Override
    public Form populateBean(Object bean) {
        if (bean != null) {
            Map<String, Object> formData = UniformUtils.getBeanProperties(bean);
            return this.populateSimple(formData);
        } else {
            return this;
        }
    }

    /**
     * Converts any input object to a list of string values. If the input object is a collection or an array, then each element in the input is treated as a separate object to be added to the result.
     * In any other case, simple {@code toString()} method of the object is used.
     *
     * @param value Input object
     * @return List of string values
     */
    private List<String> convertObjectToListOfStringValues(Object value) {
        if (value != null) {
            List<String> valueList = new ArrayList<>();
            if (value instanceof Collection) {
                Collection valueCollection = (Collection) value;
                for (Object val : valueCollection) {
                    if (val == null) {
                        valueList.add(null);
                    } else {
                        valueList.add(val.toString());
                    }
                }

            } else if (value instanceof Object[]) {
                Object[] valueArray = (Object[]) value;
                for (Object val : valueArray) {
                    if (val == null) {
                        valueList.add(null);
                    } else {
                        valueList.add(val.toString());
                    }
                }
            } else {
                valueList.add(value.toString());
            }
            
            return valueList;
        }else{
            return null;
        }
    }

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public Form setProperty(String key, String value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public Form removeProperty(String key) {
        properties.remove(key);
        return this;
    }

    @Override
    public List<FormValidator> getValidators() {
        return new ArrayList<>(validators);
    }

    @Override
    public Form addValidator(FormValidator validator) {
        validators.add(validator);
        return this;
    }

    @Override
    public Form removeValidator(FormValidator validator) {
        validators.remove(validator);
        return this;
    }

    @Override
    public Form clearValidators() {
        validators.clear();
        return this;
    }

    @Override
    public Form setValidators(List<FormValidator> validators) {
        this.validators.clear();
        if(validators != null){
            this.validators.addAll(validators);
        }
        return this;
    }

    @Override
    public Form reset() {
        for (Element element : formElements.values()) {
            element.reset();
        }

        this.validationPeformed = false;

        return this;
    }

    @Override
    public boolean validationPerformed() {
        return validationPeformed;
    }

    @Override
    public Form clearValidation() {
        validationPeformed = false;
        for (Element element : formElements.values()) {
            element.clearValidation();
        }
        return this;
    }

    @Override
    public boolean isAutoEnableElementsTranslation() {
        return autoEnableElementsTranslation;
    }

    @Override
    public Form setAutoEnableElementsTranslation(boolean autoEnableElementsTranslation) {
        this.autoEnableElementsTranslation = autoEnableElementsTranslation;
        return this;
    }

    @Override
    public String toString() {
        return this.renderHTML();
    }

    /**
     * Creates an index of the form element by their name. Makes sure that the names are not repeated and are valid.
     *
     * @return Index of elements by name
     */
    protected Map<String, Element> elementsIndexByName() {
        Map<String, Element> index = new HashMap<>();
        for (Element element : formElements.values()) {
            String name = element.getProperty("name");
            if (name != null && !name.trim().isEmpty()) {
                if (index.containsKey(name)) {
                    throw new IllegalStateException("Name '" + name + "' cannot be repeated in the form");
                }

                if (name.contains("[") && name.contains("]")) {
                    throw new IllegalStateException("Name '" + name + "' cannot be in array form");
                }

                index.put(name, element);
            }
        }

        return index;
    }

    /**
     * Converts a basic value to the given basic type (numeric and simple types), if possible. If conversion is not possible, an exception will be thrown.
     *
     * @param value Input value
     * @param type Target type
     * @return Converted value
     */
    protected static Object convertBasicValue(String value, Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        if (String.class.equals(type)) {
            return value;
        }

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            value = value.trim();
            if (Byte.class.equals(type)) {
                return Byte.parseByte(value);
            } else if (Short.class.equals(type)) {
                return Short.parseShort(value);
            } else if (Integer.class.equals(type)) {
                return Integer.parseInt(value);
            } else if (Long.class.equals(type)) {
                return Long.parseLong(value);
            } else if (BigInteger.class.equals(type)) {
                return new BigInteger(value);
            } else if (Float.class.equals(type)) {
                return Float.parseFloat(value);
            } else if (Double.class.equals(type)) {
                return Double.parseDouble(value);
            } else if (BigDecimal.class.equals(type)) {
                return new BigDecimal(value);
            } else if (Boolean.class.equals(type)) {
                return Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            throw new UniformException(String.format("Error while converting value %s to data type %s. Make sure the element has correct values and/or validators", value, type.getName()), e);
        }

        throw new UnsupportedOperationException("Could not convert value to unknown type: " + type.getName());
    }
}
