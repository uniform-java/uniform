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
package net.uniform.html.validators;

import java.util.Arrays;
import java.util.List;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.Validator;
import net.uniform.html.elements.Radio;
import net.uniform.html.elements.Select;
import net.uniform.impl.ElementWithOptions;
import net.uniform.impl.utils.UniformUtils;

/**
 * Validator for single-value elements with options to ensure that any input option is valid (defined in the element) and not disabled.
 * Used by default in {@link Select} and {@link Radio} elements.
 * @author Eduardo Ramos
 */
public class SingleOptionValidator implements Validator<ElementWithOptions> {

    @Override
    public List<String> getValidationErrors(ElementWithOptions element, List<String> values) {
        String firstValue = UniformUtils.firstValue(values);
        if (firstValue != null && !firstValue.isEmpty()) {//May be not required

            if (!element.hasValueEnabled(firstValue)) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.select.invalid"));
            }
        }

        return null;
    }

    @Override
    public boolean breakChainOnError() {
        return true;
    }
}
