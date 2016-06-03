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
import net.uniform.html.decorators.ElementErrorsDecorator;

/**
 * <p>
 * A decorator is used to alter any previously rendered HTML, normally in order to extend it.
 * It can be applied to a single {@link Element} by using the {@code addDecorator} method.
 * It can also be applied to a group of elements in a {@link Form} by using the {@code startDecorator} and {@code endDecorator} method, defining the decorated range.
 * </p>
 * 
 * <p>For example, a decorator like {@link ElementErrorsDecorator} adds any validation errors in the element to the HTML rendered by the element.</p>
 * 
 * <p>Decorators can be chained, so a decorator may receive HTML produced by an element and other decorators executed before this decorator.</p>
 * @author Eduardo Ramos
 */
public interface Decorator {

    /**
     * Decorate the previously generated HTML.
     *
     * @param form Form of the element, might be null
     * @param elements List of elements being decorated. It will be a list of 1 element for an decorator applied to a single element.
     * @param rendered Rendered HTML so far
     * @return Decorated HTML
     */
    List<SimpleHTMLTag> render(Form form, List<Element> elements, List<SimpleHTMLTag> rendered);

    /**
     * Obtains a property of the decorator by key.
     *
     * @param key Property key
     * @return Propert value
     */
    Object getProperty(String key);

    /**
     * Indicates if the decorator has a value for the property (even null).
     *
     * @param key Property key
     * @return True if a value is set, false otherwise
     */
    boolean hasProperty(String key);

    /**
     * Sets the value of a property of the decorator by key.
     *
     * @param key Property key
     * @param value Property value
     * @return This decorator
     */
    Decorator setProperty(String key, Object value);

    /**
     * Removes a property of the decorator by key.
     *
     * @param key Property key
     * @return This decorator
     */
    Decorator removeProperty(String key);

    /**
     * Sets the values of all properties of the decorator.
     *
     * @param properties Property values indexed by key
     * @return This decorator
     */
    Decorator setProperties(Map<String, Object> properties);

    /**
     * Returns all the properties of the decorator indexed by key.
     *
     * @return All properties in a map, will never be null
     */
    Map<String, Object> getProperties();

    /**
     * Removes all the properties of the decorator.
     *
     * @return This decorator
     */
    Decorator clearProperties();
}
