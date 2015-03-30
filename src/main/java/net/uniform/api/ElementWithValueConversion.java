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

import net.uniform.html.elements.DatePicker;

/**
 * This interface extends an {@link Element} features so it's able to override the conversion of its String values to any target type with any custom logic.
 * This is used in the form {@code getFormDataConvertedToElementValueTypes} method.
 * @author Eduardo Ramos
 * @param <T> Type to convert values
 * @see Element
 */
public interface ElementWithValueConversion<T> extends Element {
    /**
     * Returns the final value for the list of values hold by the element.
     * This is used for example by {@link DatePicker} element to return date objects instead of Strings.
     * @return Value with the type declared by the element
     */
    public T getConvertedValue();
}
