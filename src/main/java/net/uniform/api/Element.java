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
package net.uniform.api;

import java.util.List;
import java.util.Map;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.html.elements.Hidden;
import net.uniform.html.elements.Multicheckbox;
import net.uniform.html.elements.Multiselect;
import net.uniform.html.validators.RequiredValidator;
import net.uniform.impl.AbstractElement;

/**
 * <p>An {@code Element} represents any kind of item contained in a {@link Form}</p>
 * <p>Every element has an id (unique and mandatory) and a label (optional).</p>
 * 
 * All elements are able to:
 * <ul>
 * <li>Hold a list of <b>{@code String}</b> values</li>
 * <li>Declare if the element is multivalue (otherwise the list of values will contain at most one value). For example, a {@link Multiselect} is a type of element that can hold many values</li>
 * <li>Define a type for their values to be converted later in the form (with {@code getFormDataConvertedToElementValueTypes} method)</li>
 * <li>Validate its own values with the {@code getValidationErrors} method</li>
 * <li>Hold a list of validators responsible of validating the element values in the previous method</li>
 * <li>Rendering itself into HTML with the {@code render} method</li>
 * <li>Hold an optional {@link Renderer} responsible of rendering the main HTML of the element in the {@code render} method</li>
 * <li>Define a default renderer to use when the default renderer is null</li>
 * <li>Hold a list of decorators for extending its rendered HTML in the {@code render} method</li>
 * <li>Hold a list of filters that are applied to any value before it's set to the element</li>
 * <li>Hold {@code String} properties indexed by name, to be used when rendering the element. Normally, a renderer will put all the properties in the main HTML tag as attributes, but this depends on the element and the renderer</li>
 * </ul>
 * 
 * <p>
 * Unless explicitely changed with {@link #setProperty(java.lang.String, java.lang.String)}, the id and name HTML attributes of an element will correspond with the id of the element.
 * </p>
 * 
 * <p>A set of common elements are included in Uniform in the package {@code net.uniform.html.elements}</p>
 * 
 * <p>The default abstract element implementation that you should use and extend is {@link AbstractElement}</p>
 * @author Eduardo Ramos
 */
public interface Element {
    
    /**
     * Returns the id of the element.
     * @return Id of the element
     */
    String getId();
    
    /**
     * Sets the label of the element.
     * @param label New label value
     * @return This element
     */
    Element setLabel(String label);
    
    /**
     * Sets the description of the element.
     * @param description New description
     * @return This element
     */
    Element setDescription(String description);
    
    /**
     * Returns the label of the element (never translated).
     * @return Label of the element
     */
    String getLabel();
    
    /**
     * Returns the description of the element (never translated).
     * @return Description of the element
     */
    String getDescription();
    
    /**
     * Returns the label of the element but, if translation is enabled ({@link #isTranslationEnabled()}), tries to find its translation in the current {@link TranslationEngineContext}.
     * If no translation is found, it returns the label value.
     * This should be used by any class that needs the label of the element for the final HTML.
     * @return Translated or original label depending on element configuration
     */
    String getLabelTranslated();
    
    /**
     * Returns the description of the element but, if translation is enabled ({@link #isTranslationEnabled()}), tries to find its translation in the current {@link TranslationEngineContext}.
     * If no translation is found, it returns the description value.
     * This should be used by any class that needs the description of the element for the final HTML.
     * @return Translated or original description depending on element configuration
     */
    String getDescriptionTranslated();
    
    /**
     * Indicates if the translation of label and description is enabled for this element.
     * By default translation is disabled.
     * This is changed with the {@link #setTranslationEnabled()} method
     * @return True if translation is enabled, false otherwise
     */
    boolean isTranslationEnabled();
    
    /**
     * Enables or disables the element label and description translation.
     * By default translation is disabled.
     * @param enable Boolean indicating if the translation has to be enabled
     * @return This element
     */
    Element setTranslationEnabled(boolean enable);
    
    /**
     * Enables the element label and description translation.
     * @return This element
     */
    Element setTranslationEnabled();
    
    /**
     * <p>
     * Returns the type of the values of this element, to be used when converting values in the form (for example in {@code getFormDataConvertedToElementValueTypes} method).
     * Most element will return {@code String.class}
     * Some elements will offer a {@code setValueType} method to change the value returned by this.
     * </p>
     * 
     * <p>
     * If the returned type is null, the element will never by populated by a form, and its values will never be included in extracted form data with methods such as {@code getFormData}.
     * </p>
     * 
     * @return Value type class or null
     */
    Class<?> getValueType();
    
    /**
     * Indicates if this element is multi-value (for example, a {@link Multiselect} or {@link Multicheckbox}) or not.
     * This is used by a form to determine how to extract data of its elements in methods such as {@code getFormData}
     * @return True if the element is multi-value, false otherwise
     */
    boolean isMultiValue();
    
    /**
     * Returns current the list of values hold by the element.
     * The return value can be null. Only multi-value elements will return a list of more than one value.
     * @return List of values or null
     */
    List<String> getValue();
    
    /**
     * Returns the first value hold by the element or null.
     * @return First value or null
     */
    String getFirstValue();
    
    /**
     * Sets the list of values to be hold by the element.
     * A single-value element will ignore all values but the first.
     * <b>This will never be called by a form on its own during data population, {@link #populate(java.util.List)} method will be called instead</b>
     * @param value List of values or null
     * @return This element
     */
    Element setValue(List<String> value);
    
    /**
     * Sets a single value to be hold by the element.
     * It's converted to a list of one value by the element.
     * <b>This will never be called by a form on its own during data population, {@link #populate(java.util.List)} method will be called instead</b>
     * @param value Single value or null
     * @return This element
     */
    Element setValue(String value);
    
     /**
     * <p>
     * Sets the list of values to be hold by the element.
     * This should only be used by a form during data population.
     * </p>
     * 
     * <p>
     * This methods exists in order to make elements aware that the the data is being set by the form population (user input values),
     * and be able to ignore the value if the element has the {@code disabled} property, or just ignore any value in special elements like {@link Hidden}, where the user should not be able to change the value of the element.
     * </p>
     * 
     * <p>
     * A single-value element will ignore all values but the first.
     * </p>
     * @param value List of values or null
     * @return This element
     */
    Element populate(List<String> value);
    
     /**
     * Returns all the properties of the element indexed by key.
     * @return All properties in a map, will never be null
     */
    Map<String, String> getProperties();
    
    /**
     * Obtains a property of the element by key.
     * @param key Property key
     * @return Propert value
     */
    String getProperty(String key);
    
    /**
     * Indicates if the element has a value for the property (even null).
     * @param key Property key
     * @return True if a value is set, false otherwise
     */
    boolean hasProperty(String key);
    
    /**
     * Sets the value of a property of the element by key.
     * Normally, an element renderer will put all the properties in the main HTML tag as attributes, but this depends on the element and the renderer.
     * @param key Property key
     * @param value Property value
     * @return This element
     */
    Element setProperty(String key, String value);
    
    /**
     * Removes a property of the element by key.
     * @param key Property key
     * @return This element
     */
    Element removeProperty(String key);
    
    /**
     * Returns the list of validators to be used in the validation of this element.
     * @return List of validators, never null
     */
    List<Validator> getValidators();
    
    /**
     * Adds a validator to the element, to be used in the {@link #getValidationErrors()} method.
     * @param validator New validator
     * @return This element
     */
    Element addValidator(Validator validator);
    
    /**
     * Removes a validator from the element, if existing.
     * @param validator Validator to remove
     * @return This element
     */
    Element removeValidator(Validator validator);
    
    /**
     * Removes all validators from the element.
     * @return This element
     */
    Element clearValidators();
    
    /**
     * Replaces all validators of the element with a new list of validators.
     * @param validators New list of validators
     * @return This element
     */
    Element setValidators(List<Validator> validators);
    
    /**
     * Returns the renderer to be used by this element in the {@link #render()} method,
     * or null to use the default renderer returned by {@link #getDefaultRenderer()}.
     * @return Renderer or null
     */
    Renderer getRenderer();
    
    /**
     * Sets the renderer to be used by this element in the {@link #render()} method.
     * @param renderer New renderer or null to use default renderer
     * @return This element
     */
    Element setRenderer(Renderer renderer);
    
    /**
     * Returns the default renderer of this element, 
     * to be used by this element in the {@link #render()} method when no specific renderer has been set.
     * @return Default renderer or null
     */
    Renderer getDefaultRenderer();
    
    /**
     * Returns the list of decorators to be used in the rendering of this element.
     * @return List of decorators, never null
     */
    List<Decorator> getDecorators();
    
    /**
     * Adds a decorator to the element, to be used in the {@link #render()} method.
     * Decorators are called in list order, after the base element is renderer with its {@code Renderer}
     * @param decorator New decorator
     * @return This element
     */
    Element addDecorator(Decorator decorator);
    
    /**
     * Replaces the list of decorators of this element.
     * @param decorators New list of decorators
     * @return This element
     */
    Element setDecorators(List<Decorator> decorators);
    
    /**
     * Returns the first decorator in this element with the given class, or null if not found.
     * @param clazz Decorator class
     * @return Decorator or null
     */
    Decorator getDecorator(Class<?> clazz);
    
    /**
     * Returns the last decorator in this element with the given class, or null if not found.
     * @param clazz Decorator class
     * @return Decorator or null
     */
    Decorator getLastDecorator(Class<?> clazz);
    
    /**
     * Removes a decorator of this element, if present.
     * @param decorator Decorator to remove
     * @return This element
     */
    Element removeDecorator(Decorator decorator);
    
    /**
     * Finds the first decorator of this element with the given class and sets the given property value.
     * Throws an exception if no decorator with that class is found.
     * @param clazz Decorator class
     * @param key Property key
     * @param value Property value
     */
    void setDecoratorProperty(Class<?> clazz, String key, Object value);
    
    /**
     * Finds the last decorator of this element with the given class and sets the given property value.
     * Throws an exception if no decorator with that class is found.
     * @param clazz Decorator class
     * @param key Property key
     * @param value Property value
     */
    void setLastDecoratorProperty(Class<?> clazz, String key, Object value);
    
    /**
     * Removes all decorators from this element.
     * @return This element
     */
    Element clearDecorators();
    
    /**
     * Returns the list of filters to be used when setting or populating a value to this element.
     * @return List of filters, never null
     */
    List<Filter> getFilters();
    
    /**
     * Adds a filter to be used when setting or populating a value to this element.
     * @param filter New filter
     * @return This element
     */
    Element addFilter(Filter filter);
    
    /**
     * Removes a filter from this element.
     * @param filter Filter to remove
     * @return This element
     */
    Element removeFilter(Filter filter);
    
    /**
     * Removes all filters from this element.
     * @return This element
     */
    Element clearFilters();
    
    /**
     * Replaces the list of filters of this element.
     * @param filters New list of filters
     * @return This element
     */
    Element setFilters(List<Filter> filters);
    
    /**
     * Sets the required flag of this element to the given value.
     * If an element is required, a {@link RequiredValidator} will be used in its validation even if it's not in the list of validators.
     * <b>By default, elements are not required.</b>
     * @param required Required flag
     * @return This element
     */
    Element setRequired(boolean required);
    
    /**
     * Sets the required flag of this element to true.
     * If an element is required, a {@link RequiredValidator} will be used in its validation even if it's not in the list of validators.
     * <b>By default, elements are not required.</b>
     * @return This element
     */
    Element setRequired();

    /**
     * Indicates if this element is required.
     * If an element is required, a {@link RequiredValidator} will be used in its validation even if it's not in the list of validators.
     * @return True if the element is required, false otherwise
     */
    boolean isRequired();
    
    /**
     * Validates the value(s) of this element with the list of validators and required flag.
     * @return List of errors (may be empty) or null
     */
    List<String> getValidationErrors();
    
    /**
     * Returns true if {@link #getValidationErrors()} returns no errors
     * @return True if element valid, false otherwise
     */
    boolean isValid();
    
    /**
     * Indicates if the validation has been performed with this element ({@link #getValidationErrors()} method has been called).
     * @return True if validation has been performed, false otherwise
     */
    boolean validationPerformed();
    
    /**
     * Sets the validation performed status of this element to false.
     * @return This element
     */
    Element clearValidation();
    
    /**
     * Renders the list of HTML tags of this element, using the set render or the default renderer if the set renderer is null.
     * It also applies any decorators in the element, in list order.
     * 
     * Renders the element without the context of a form, like calling {@link #render(net.uniform.api.Form)} with a null form.
     * @return List of HTML tags for the element, after decoration
     */
    List<SimpleHTMLTag> render();
    
    /**
     * Renders the list of HTML tags of this element, using the set render or the default renderer if the set renderer is null.
     * It also applies any decorators in the element, in list order.
     * 
     * The form is passed to the decorators of the element.
     * @param form Form context for this element or null for no form context
     * @return List of HTML tags for the element, after decoration
     */
    List<SimpleHTMLTag> render(Form form);
    
    /**
     * Resets the values and validation state of the element.
     * @return This element
     */
    Element reset();
}
