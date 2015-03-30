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
import net.uniform.html.HTMLForm;

/**
 * <p>A form represents an HTML form which contains elements, being user input controls or any other kind of HTML tag.</p>
 * 
 * Any form is able to:
 * <ul>
 * <li>Hold a list of elements, which are later <b>rendered in insertion order</b> (see {@link #addElement(net.uniform.api.Element)})</li>
 * <li>Hold decorators that enclose form elements or even other decorators (form decorators can be nested), which are later <b>rendered in insertion order</b> (see {@link #startDecorator(java.lang.String, net.uniform.api.Decorator)})</li>
 * <li>Hold a list of form validators ({@link FormValidator}) to be used in form validation (see {@link #getValidationErrors()})</li>
 * <li>Define a list of default decorators to use in added elements depending on their class (see {@link #addDefaultDecoratorForElementClass(java.lang.Class, net.uniform.api.Decorator)})</li>
 * <li>Define a list of default renderers to use in added elements, depending on their class (see {@link #setDefaultRendererForElementClass(java.lang.Class, net.uniform.api.Renderer)})</li>
 * <li>Render the whole HTML form that contains the rendered and decorated elements (see {@link #renderHTML()})</li>
 * <li>Auto populate the values entered by the user in many ways (see {@link #populate(java.util.Map)}, {@link #populateSimple(java.util.Map)} and {@link #populateBean(java.lang.Object)})</li>
 * <li>Extract the values in the form in many ways (see {@link #getFormData()}, {@link #getFormDataConvertedToElementValueTypes()} and other {@code getFormData...} methods)</li>
 * <li>Hold {@code String} properties indexed by name, to be used when rendering the form. A form will put all the properties in the main HTML form tag as attributes</li>
 * </ul>
 * 
 * The way that default decorators and renderers work is:
 * <ol>
 * <li>An element is added to the form</li>
 * <li>If the element has decorators/a renderer already defined, nothing changes</li>
 * <li>Otherwise, with the class of the element, default decorators/a renderer is looked up in the defaults</li>
 * <li>If any default decorator/renderer configuration for that class is found (even an empty list), that configuration is used for the element</li>
 * <li>This lookup is repeated going up the class hierarchy of the element until some configuration is found or the {@link Element} class is reached without success</li>
 * </ol>
 * 
 * <p>
 * For example, you could configure default decorators for {@code Element.class}, which will be applied to all elements.
 * Then, you could configure a different set of decorators (or a empty list of decorators by adding {@code null} decorator)
 * for an especific element class to override certain special elements.
 * </p>
 * 
 * <p>The default form implementation that you should use and extend is {@link HTMLForm}</p>
 * 
 * @author Eduardo Ramos
 * @see Element
 * @see Decorator
 * @see FormValidator
 * @see HTMLForm
 */
public interface Form {
    public static final String FORM_LEVEL_VALIDATION_ERRORS_INDEX = null;
    
    /**
     * Adds an element to the form. The element id should be unique in the form.
     * Default decorators are added to the element if it does not have any decorator, and default decorators are defined for its class.
     * Default renderer is set to the element if it does not have a renderer, and a default renderer is defined for its class.
     * @param element Element to add
     * @return This form
     */
    Form addElement(Element element);
    
    /**
     * Adds an element to the form. The element id should be unique in the form.
     * Default decorators are added to the element if it does not have any decorator, default decorators are defined for its class, and {@code useDefaultDecorators} parameter is {@code true}.
     * Default renderer is set to the element if it does not have a renderer, and a default renderer is defined for its class.
     * @param element Element to add
     * @param useDefaultDecorators Indicates if default decorators should be taken into account when adding the element
     * @return This form
     */
    Form addElement(Element element, boolean useDefaultDecorators);
    
    /**
     * Adds several elements doing the same as {@link #addElement(net.uniform.api.Element)} method.
     * @param elements Elements to add
     * @return This form.
     */
    Form addElements(Element... elements);
    
    /**
     * Adds several elements doing the same as {@link #addElement(net.uniform.api.Element, boolean)} method.
     * @param useDefaultDecorators Indicates if default decorators should be taken into account when adding the elements
     * @param elements Elements to add
     * @return This form.
     */
    Form addElements(boolean useDefaultDecorators, Element... elements);
    
    /**
     * Removes an element from the form.
     * @param element Element to remove
     * @return This form
     */
    Form removeElement(Element element);
    
    /**
     * Removes an element from the form by id.
     * @param id Id of the element
     * @return This element
     */
    Form removeElement(String id);
    
    /**
     * Returns an element previously added to the form by id.
     * @param id Id of the element
     * @return Found element or null if not existing
     */
    Element getElement(String id);
    
    /**
     * Returns an index of all elements in this form indexed by id.
     * @return Index of form elements
     */
    Map<String, Element> getElements();
    
    /**
     * Adds a default decorator for the given element class, to be used when adding elements to the form without any decorators.
     * @param clazz Element class <b>and subclasses</b> to affect
     * @param decorator Decorator to add. Can be null to explicitely set no default decorators for the element class
     * @return This form
     */
    Form addDefaultDecoratorForElementClass(Class<? extends Element> clazz, Decorator decorator);
    
    /**
     * Replaces the default decorators list for the given element class, to be used when adding elements to the form without any decorators.
     * @param clazz Element class <b>and subclasses</b> to affect
     * @param decorators List of decorators. Can be null or an empty list to explicitely set no default decorators for the element class
     * @return This form
     */
    Form setDefaultDecoratorsForElementClass(Class<? extends Element> clazz, List<Decorator> decorators);
    
    /**
     * Returns the default decorators set for the given element class.
     * @param clazz Element class
     * @return List of decorators or null if no configuration found
     */
    List<Decorator> getDefaultDecoratorsForElementClass(Class<? extends Element> clazz);
    
    /**
     * Removes any specific default decorators configuration for the given element class in this form, if existing.
     * @param clazz Element class
     * @return This form
     */
    Form removeDefaultDecoratorsForElementClass(Class<? extends Element> clazz);
    
    /**
     * If the element class or superclasses decorators has been explicitely set with {@link #addDefaultDecoratorForElementClass(java.lang.Class, net.uniform.api.Decorator)},
     * this returns a list of decorators, otherwise returns null.
     * @param clazz Element class
     * @return A list of decorators or null
     */
    List<Decorator> getDefaultDecoratorsAppliedToElementClass(Class<? extends Element> clazz);
    
    /**
     * Sets a default renderer for the given element class, to be used when adding elements to the form without a renderer.
     * @param clazz Element class <b>and subclasses</b> to affect
     * @param renderer Renderer to use. Can be null to explicitely set no default decorators for the element class
     * @return This form
     */
    Form setDefaultRendererForElementClass(Class<? extends Element> clazz, Renderer renderer);
    
     /**
     * Returns the default renderer set for the given element class.
     * @param clazz Element class
     * @return Renderer or null
     */
    Renderer getDefaultRendererForElementClass(Class<? extends Element> clazz);
    
    /**
     * Removes any specific default render configuration for the given element class in this form, if existing.
     * @param clazz Element class
     * @return This form
     */
    Form removeDefaultRendererForElementClass(Class<? extends Element> clazz);
    
    /**
     * If the element class or superclasses renderer has been explicitely set with {@link #setDefaultRendererForElementClass(java.lang.Class, net.uniform.api.Renderer)},
     * this returns a renderer, otherwise returns null.
     * @param clazz Element class
     * @return Renderer or null
     */
    Renderer getDefaultRendererAppliedToElementClass(Class<? extends Element> clazz);
    
    /**
     * Starts a form decorator that will be applied to any HTML tags produced between this and the
     * corresponding call to {@link #endDecorator()} method.
     * 
     * Form decorators can be nested.
     * 
     * You can start a decorator within another decorator, and you can add any number of elements (even none) between the call to {@code startDecorator} and {@code endDecorator}.
     * @param id Decorator id
     * @param decorator Decorator to start
     * @return This form
     */
    Form startDecorator(String id, Decorator decorator);
    
    /**
     * Ends the current open decorator.
     * Form decorators can be nested.
     * @return This form.
     */
    Form endDecorator();
    
    /**
     * Removes a decorator from the form by id, if existing.
     * @param id Decorator id
     * @return This form
     */
    Form removeDecorator(String id);
    
    /**
     * Returns a decorator from the form by id, if existing.
     * @param id Decorator id
     * @return Decorator or null if not found
     */
    Decorator getDecorator(String id);
    
    /**
     * Returns an index of all form decorators by id.
     * @return Form decorators index
     */
    Map<String, Decorator> getDecorators();
    
    /**
     * Renders the whole form as an HTML string.
     * @return Complete HTML form
     */
    String renderHTML();
    
    /**
     * Renders the whole form as a list of {@link SimpleHTMLTag}
     * @return Complete form tags list
     */
    List<SimpleHTMLTag> render();
    
    /**
     * Performs validation on all the form elements and te form itself.
     * @return True if no validation errors were found, false otherwise
     */
    boolean isValid();
    
    /**
     * Returns any the validation errors in the form indexed by element id.
     * It also includes the form level validation errors in the {@code null} key, if any.
     * @return Index of validation errors, may be empty. Will only contain keys for elements with errors
     */
    Map<String, List<String>> getValidationErrors();
    
    /**
     * Returns the values of all the form elements as lists of strings.
     * Data is indexed by element <b>name</b> (normally the same as the element id).
     * @return Form data as list of string values.
     */
    Map<String, List<String>> getFormDataMultivalue();
    
    /**
     * Returns the values of all the form elements as strings or lists of strings (dependin on each element {@code isMultiValue}).
     * Data is indexed by element <b>name</b> (normally the same as the element id).
     * @return Form data as a string or list of string values.
     */
    Map<String, Object> getFormData();
    
    /**
     * Returns the values of all the form elements as their target value type.
     * Conversion for simple types is done by the form, according to the element {@code getValueType()} method.
     * 
     * Complex type conversion can also be done by elements implementing the {@link ElementWithValueConversion} interface.
     * 
     * Data is indexed by element <b>name</b> (normally the same as the element id).
     * @return Form data as any target type.
     */
    Map<String, Object> getFormDataConvertedToElementValueTypes();
    
    /**
     * Does the same as {@link #getFormDataConvertedToElementValueTypes()} but automatically sets any matching
     * bean property to the given object, by looking at the each property name and type class.
     * @param bean Bean object to fill data
     * @return This form
     */
    Form getFormDataIntoBean(Object bean);
    
    /**
     * Returns the same as {@link #getFormDataMultivalue() ()} but for a single element, by element id.
     * @param elementId Element id
     * @return List of String values or null
     */
    List<String> getElementMultivalue(String elementId);
    
      /**
     * Returns the same as {@link #getFormData()} but for a single element, by element id.
     * @param elementId Element id
     * @return List of String values, a single String or null, dependin on the element value type
     */
    Object getElementValue(String elementId);

    /**
     * Returns the same as {@link #getFormDataConvertedToElementValueTypes()} but for a single element, by element id.
     * @param elementId Element id
     * @return Any value, converted depending on the element value type
     */
    Object getElementValueConvertedToValueType(String elementId);
    
    /**
     * Sets the multi-string value of an element by id.
     * @param elementId Element id
     * @param value Value to set
     * @return This form
     */
    Form setElementValue(String elementId, List<String> value);
    
    /**
     * Sets the single-string value of an element by id.
     * @param elementId Element id
     * @param value Value to set
     * @return This form
     */
    Form setElementValue(String elementId, String value);
    
    /**
     * Sets the value of an element by id doing the same as {@link #populateSimple(java.util.Map)}, by converting the object to a list of Strings or a single String.
     * @param elementId Element id
     * @param value Value to set
     * @return This form
     */
    Form setElementValue(String elementId, Object value);
    
    /**
     * Populates form elements <b>by name</b> calling their {@code populate} method.
     * Input data must be a list of Strings or null for every element.
     * @param formData Form data indexed by element name
     * @return This form
     */
    Form populate(Map<String, List<String>> formData);
    
    /**
     * Populates form elements <b>by name</b> calling their {@code populate} method.
     * Input data can be any type for each element, or null.
     * 
     * If the object for an element is an array or collection, it is converted to a list of Strings, otherwise it is converted to a single String.
     * @param formData Form data indexed by element name
     * @return This form
     */
    Form populateSimple(Map<String, Object> formData);
    
    /**
     * Does the same as {@link #populateSimple(java.util.Map)}
     * but automatically extracting all accessible bean properties from the given object.
     * @param bean Bean object
     * @return This form
     */
    Form populateBean(Object bean);
    
    /**
     * Returns all properties in this form.
     * @return Properties index
     */
    Map<String, String> getProperties();
    
    /**
     * Returns a property in this form by key
     * @param key Property key
     * @return This form
     */
    String getProperty(String key);
    
    /**
     * Sets the value of a property in this form.
     * @param key Property key
     * @param value Property value
     * @return This form
     */
    Form setProperty(String key, String value);
    
    /**
     * Removes a property from this form, if existing.
     * @param key Property key
     * @return This form
     */
    Form removeProperty(String key);
    
    /**
     * Returns the form validators in this form.
     * @return List of validators, never null
     */
    List<FormValidator> getValidators();
    
    /**
     * Adds a form validator to the form.
     * @param validator Form validator to add
     * @return This form
     */
    Form addValidator(FormValidator validator);
    
    /**
     * Removes a validator from this form.
     * @param validator Form validator to remove
     * @return This form
     */
    Form removeValidator(FormValidator validator);
    
    /**
     * Removes all validators from this form.
     * @return This form.
     */
    Form clearValidators();
    
    /**
     * Replaces the list of validators in this form.
     * @param validators List of form validators to put
     * @return This form
     */
    Form setValidators(List<FormValidator> validators);
    
    /**
     * Resets the values and validation performed status of the form an all its elements.
     * @return This form
     */
    Form reset();
    
    /**
     * Indicates if the validation has been peformed on this form by calling {@link #isValid()} or {@link #getValidationErrors()} methods.
     * @return True for validation performed status, false otherwise
     */
    boolean validationPerformed();
    
    /**
     * Sets the validation performed status of this form and all its elements to false.
     * @return This form
     */
    Form clearValidation();

    /**
     * Indicates it element translation will be enabled automatically when adding elements to the form.
     * By default, auto enable elements translation is false.
     * @return True if elements translation will be auto enabled when adding elements
     */
    boolean isAutoEnableElementsTranslation();

    /**
     * Sets or unsets the auto enabling of elements translation when adding them to the form.
     * By default, auto enable elements translation is false.
     * @param autoEnableElementsTranslation True for auto enabling
     * @return This form
     */
    Form setAutoEnableElementsTranslation(boolean autoEnableElementsTranslation);
}
