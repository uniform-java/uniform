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

/**
 * <p>
 * A validator can be added to an {@link Element} to extend its validation logic.
 * </p>
 * 
 * <p>
 * A set of basic validators are included in Uniform in the package {@code net.uniform.html.validators}
 * </p>
 * @author Eduardo Ramos
 * @param <T> Type of element supported by this validator
 * @see Element
 */
public interface Validator<T extends Element> {

    /**
     * Returns the validation errors for the element data, if any.
     * @param element Element to validate
     * @param value Current list of values for the element
     * @return List of errors (may be empty) or null
     */
    List<String> getValidationErrors(T element, List<String> value);

    /**
     * Indicates if this validator returning any error should prevent next element validators from being called.
     * @return True to stop next element validators on error, false otherwise
     */
    boolean breakChainOnError();
}
