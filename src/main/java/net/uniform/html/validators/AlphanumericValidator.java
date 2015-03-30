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
import net.uniform.impl.utils.UniformUtils;

/**
 * Validator for alphanumeric only single-value inputs.
 * @author Eduardo Ramos
 */
public class AlphanumericValidator implements Validator<Element> {

    @Override
    public List<String> getValidationErrors(Element element, List<String> value) {
        String firstValue = UniformUtils.firstValue(value);
        if (firstValue != null && !firstValue.isEmpty()) {
            if(!isAlphanumeric(firstValue)){
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.alnum.invalid"));
            }
        }
        
        return null;
    }

    public boolean isAlphanumeric(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
                return false;
        }
        return true;
    }
    
    @Override
    public boolean breakChainOnError() {
        return false;
    }
    
}
