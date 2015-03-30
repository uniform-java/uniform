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
import net.uniform.api.Element;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.Validator;

/**
 * Validator for any single or multi value element to ensure that a not null and not empty value after trimming is entered.
 * @author Eduardo Ramos
 */
public class RequiredValidator implements Validator<Element> {

    @Override
    public List<String> getValidationErrors(Element element, List<String> value) {

        if (value == null || value.isEmpty()) {
            return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.required.invalid"));
        }

        //For required, all values must not be empty (after trimming):
        for (String val : value) {
            if (val == null || val.trim().isEmpty()) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.required.invalid"));
            }
        }

        return null;
    }

    @Override
    public boolean breakChainOnError() {
        return true;
    }
}
