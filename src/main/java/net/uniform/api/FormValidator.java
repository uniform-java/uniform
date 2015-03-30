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

/**
 * <p>
 * A form validator is like an element validator ({@link Validator}), but is applied to the whole form.
 * It's useful for applying validation logic that is dependent on the values of more than one element.
 * </p>
 * 
 * <p>A set of basic form validators are included in Uniform in the package {@code net.uniform.html.formvalidators}</p>
 * @author Eduardo Ramos
 */
public interface FormValidator {
    /**
     * Returns the validation errors for the form, if any.
     * @param form Form
     * @param formData Current form data as multi-value strings
     * @return List of errors (may be empty) or null
     */
    List<String> getValidationErrors(Form form, Map<String, List<String>> formData);
    
    /**
     * Indicates if this validator returning any error should prevent next form validators from being called.
     * @return True to stop next form validators on error, false otherwise
     */
    boolean breakChainOnError();
}
