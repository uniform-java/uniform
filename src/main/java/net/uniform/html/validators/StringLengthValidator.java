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
 * Validator for any single-value element to ensure that the input string is within a min and/or max character length.
 *
 * @author Eduardo Ramos
 */
public class StringLengthValidator implements Validator<Element> {

    protected Integer minLength;
    protected Integer maxLength;

    public StringLengthValidator(Integer minLength, Integer maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public StringLengthValidator(Integer maxLength) {
        this(null, maxLength);
    }

    public StringLengthValidator() {
    }

    @Override
    public List<String> getValidationErrors(Element element, List<String> value) {

        String firstValue = UniformUtils.firstValue(value);
        if (firstValue != null && !firstValue.isEmpty()) {
            if (minLength != null && firstValue.length() < minLength) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.stringlength.min", minLength));
            }

            if (maxLength != null && firstValue.length() > maxLength) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.stringlength.max", maxLength));
            }

        }

        return null;
    }

    @Override
    public boolean breakChainOnError() {
        return false;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public StringLengthValidator setMinLength(Integer minLength) {
        this.minLength = minLength;
        return this;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public StringLengthValidator setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }
}
