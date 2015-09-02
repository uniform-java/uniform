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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.uniform.api.Decorator;
import net.uniform.api.Element;
import net.uniform.api.Filter;
import net.uniform.api.Form;
import net.uniform.api.Renderer;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.Validator;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.html.validators.RequiredValidator;
import net.uniform.impl.utils.HTMLRenderingUtils;
import net.uniform.impl.utils.UniformUtils;

/**
 * Abstract element implementation that contains all generic logic that any element should have.
 * @author Eduardo Ramos
 */
public abstract class AbstractElement implements Element {
    
    protected final Map<String, String> properties;
    protected final List<Validator> validators;
    protected final List<Filter> filters;
    protected List<Decorator> decorators;
    protected final String id;
    protected String label;
    protected String description;
    protected boolean translationEnabled;

    protected List<String> value;
    protected Renderer renderer;
    
    private boolean required = false;
    private boolean validationPerformed = false;
    private Class<?> valueType = String.class;

    public AbstractElement(String id) {
        this.properties = new HashMap<>();
        this.validators = new ArrayList<>();
        this.filters = new ArrayList<>();
        this.decorators = new ArrayList<>();
        this.id = id;
        
        if(id == null){
            throw new IllegalArgumentException("id cannot be null");
        }
        
        init();
    }
    
    private void init() {
        setupDefaultFilters();
    }
    
    protected void setupDefaultFilters(){
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLabelTranslated() {
        if(isTranslationEnabled()){
            return label != null ? TranslationEngineContext.getTranslationEngine().translateWithDefault(label, label) : null;
        }else{
            return label;
        }
    }

    @Override
    public String getDescriptionTranslated() {
        if(isTranslationEnabled()){
            return description != null ? TranslationEngineContext.getTranslationEngine().translateWithDefault(description, description) : null;
        }else{
            return description;
        }
    }
    
    @Override
    public Element setLabel(String label) {
        this.label = label;

        return this;
    }

    @Override
    public Element setDescription(String description) {
        this.description = description;

        return this;
    }

    @Override
    public List<String> getValue() {
        return value;
    }
    
    @Override
    public String getFirstValue(){
        return UniformUtils.firstValue(value);
    }

    @Override
    public Element setValue(List<String> value) {
        if(value == null || value.isEmpty()){
            this.value = null;
            return this;
        }
        
        if(!isMultiValue() && value.size() > 1){
            value = value.subList(0, 1);
        }
        
        List<String> filteredValues = new ArrayList<>();

        for (String current : value) {
            for (Filter filter : filters) {
                if(filter != null){
                    current = filter.filter(current);
                }
            }

            filteredValues.add(current);
        }

        this.value = filteredValues;

        return this;
    }

    @Override
    public Element setValue(String value) {
        return this.setValue(Arrays.asList(value));
    }

    @Override
    public boolean isMultiValue() {
        return false;
    }
    
    @Override
    public Element populate(List<String> value) {
        if(!hasProperty("disabled")){
            setValue(value);
        }
        
        return this;
    }

    @Override
    public boolean hasProperty(String key) {
        key = UniformUtils.checkPropertyNameAndLowerCase(key);
        
        return properties.containsKey(key);
    }
    
    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public String getProperty(String key) {
        key = UniformUtils.checkPropertyNameAndLowerCase(key);
        
        return properties.get(key);
    }

    @Override
    public Element setProperty(String key, String value) {
        key = UniformUtils.checkPropertyNameAndLowerCase(key);
        
        properties.put(key, value);
        return this;
    }

    @Override
    public Element removeProperty(String key) {
        key = UniformUtils.checkPropertyNameAndLowerCase(key);
        
        properties.remove(key);
        return this;
    }

    @Override
    public List<Validator> getValidators() {
        return new ArrayList<>(validators);
    }

    @Override
    public Element addValidator(Validator validator) {
        validators.add(validator);
        return this;
    }

    @Override
    public Element removeValidator(Validator validator) {
        validators.remove(validator);
        return this;
    }

    @Override
    public Element clearValidators() {
        validators.clear();
        return this;
    }

    @Override
    public Element setValidators(List<Validator> validators) {
        this.validators.clear();
        if(validators != null){
            this.validators.addAll(validators);
        }
        return this;
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public Element setRenderer(Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    @Override
    public List<Decorator> getDecorators() {
        return new ArrayList<>(decorators);
    }

    @Override
    public Element addDecorator(Decorator decorator) {
        decorators.add(decorator);
        return this;
    }

    @Override
    public Element setDecorators(List decorators) {
        this.decorators.clear();
        if (decorators != null) {
            this.decorators.addAll(decorators);
        }
        return this;
    }

    @Override
    public Element removeDecorator(Decorator decorator) {
        this.decorators.remove(decorator);
        return this;
    }

    @Override
    public Element clearDecorators() {
        this.decorators.clear();
        return this;
    }

    @Override
    public Decorator getDecorator(Class<?> clazz) {
        for (Decorator decorator : decorators) {
            if (decorator != null && decorator.getClass().equals(clazz)) {
                return decorator;
            }
        }

        return null;
    }

    @Override
    public Decorator getLastDecorator(Class<?> clazz) {
        Decorator last = null;
        
        for (Decorator decorator : decorators) {
            if (decorator != null && decorator.getClass().equals(clazz)) {
                last = decorator;
            }
        }

        return last;
    }

    @Override
    public void setDecoratorProperty(Class<?> clazz, String key, Object value) {
        Decorator decorator = this.getDecorator(clazz);
        if (decorator != null) {
            decorator.setProperty(key, value);
        } else {
            throw new IllegalArgumentException("Could not find decorator with class " + clazz.getName() + " for the element " + this.getId());
        }
    }
    
    @Override
    public void setLastDecoratorProperty(Class<?> clazz, String key, Object value) {
        Decorator decorator = this.getLastDecorator(clazz);
        if (decorator != null) {
            decorator.setProperty(key, value);
        } else {
            throw new IllegalArgumentException("Could not find decorator with class " + clazz.getName() + " for the element " + this.getId());
        }
    }

    @Override
    public Element setRequired(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public Element setRequired() {
        return setRequired(true);
    }
    
    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public List<String> getValidationErrors() {
        validationPerformed = true;

        List<String> currentValue = value;
        List<String> elementErrors = new ArrayList<>();

        List<Validator> finalValidators = new ArrayList<>();
        if (required) {
            finalValidators.add(new RequiredValidator());
        }
        finalValidators.addAll(validators);

        for (Validator validator : finalValidators) {
            List<String> validatorErrors = validator.getValidationErrors(this, currentValue);
            if (validatorErrors != null && !validatorErrors.isEmpty()) {
                elementErrors.addAll(validatorErrors);

                if (validator.breakChainOnError()) {
                    break;
                }
            }
        }

        return elementErrors;
    }

    @Override
    public boolean isValid() {
        List<String> errors = getValidationErrors();
        return errors == null || errors.isEmpty();
    }

    @Override
    public List<Filter> getFilters() {
        return new ArrayList<>(filters);
    }

    @Override
    public Element addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public Element removeFilter(Filter filter) {
        filters.remove(filter);
        return this;
    }

    @Override
    public Element clearFilters() {
        filters.clear();
        return this;
    }

    @Override
    public Element setFilters(List<Filter> filters) {
        this.filters.clear();
        if(filters != null){
            this.filters.addAll(filters);
        }
        return this;
    }

    @Override
    public boolean validationPerformed() {
        return validationPerformed;
    }

    @Override
    public Element clearValidation() {
        validationPerformed = false;
        return this;
    }
    
    protected Renderer getRendererToApply(){
        if(renderer != null){
            return renderer;
        }else{
            return getDefaultRenderer();
        }
    }

    @Override
    public List<SimpleHTMLTag> render(Form form) {
        List<SimpleHTMLTag> elementTags;

        Renderer rendererToApply = getRendererToApply();
        if (rendererToApply == null) {
            throw new IllegalStateException("Element '" + id + "' does not have a renderer");
        }

        elementTags = rendererToApply.render(this);

        if (elementTags == null) {
            elementTags = new ArrayList<>();
        }

        if (decorators != null) {
            List<Element> elementList = Arrays.asList(new Element[]{this});
            
            for (Decorator decorator : decorators) {
                elementTags = decorator.render(form, elementList, Collections.unmodifiableList(elementTags));
            }
        }

        if (elementTags == null || elementTags.isEmpty()) {
            elementTags = new ArrayList<>();
        }

        return elementTags;
    }

    @Override
    public List<SimpleHTMLTag> render() {
        return this.render(null);
    }

    @Override
    public Element reset() {
        if(!hasProperty("disabled")){
            this.setValue((List<String>) null);
        }
        this.validationPerformed = false;

        return this;
    }
    
    @Override
    public Class<?> getValueType() {
        return valueType;
    }

    @Override
    public void setValueType(Class<?> valueType) {
        if(valueType == null){
            throw new IllegalArgumentException("valueType cannot be null");
        }
        this.valueType = valueType;
    }

    @Override
    public boolean isTranslationEnabled() {
        return translationEnabled;
    }

    @Override
    public Element setTranslationEnabled(boolean translationEnabled) {
        this.translationEnabled = translationEnabled;
        return this;
    }

    @Override
    public Element setTranslationEnabled() {
        return setTranslationEnabled(true);
    }

    public boolean getBooleanProperty(String key) {
        String val = this.getProperty(key);

        if (val != null) {
            return Boolean.valueOf(val.toLowerCase());
        } else {
            return false;//False by default
        }
    }

    public Integer getIntegerProperty(String key) {
        String val = this.getProperty(key);

        if (val != null) {
            try {
                Integer integer = Integer.parseInt(val);
                return integer;
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        Renderer rendererToApply = getRendererToApply();
        if(rendererToApply != null){
            return HTMLRenderingUtils.render(this.render());
        }else{
            return getClass().getName() + ":" + properties.toString();
        }
    }
}
