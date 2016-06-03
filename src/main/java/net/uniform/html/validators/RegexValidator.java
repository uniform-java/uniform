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
import java.util.regex.Pattern;
import net.uniform.api.Element;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.Validator;
import net.uniform.impl.utils.UniformUtils;

/**
 * Validator for any single-value element to conform any given regular expression.
 *
 * @author Eduardo Ramos
 */
public class RegexValidator implements Validator<Element> {

    protected Pattern regexPattern;

    public RegexValidator(Pattern regexPattern) {
        if (regexPattern == null) {
            throw new IllegalArgumentException("Regex pattern cannot be null");
        }
        this.regexPattern = regexPattern;
    }

    public RegexValidator(String regexPattern) {
        if (regexPattern == null) {
            throw new IllegalArgumentException("Regex pattern cannot be null");
        }
        this.regexPattern = Pattern.compile(regexPattern);
    }

    @Override
    public List<String> getValidationErrors(Element element, List<String> value) {
        String firstValue = UniformUtils.firstValue(value);
        if (firstValue != null && !firstValue.isEmpty()) {
            if (!regexPattern.matcher(firstValue).matches()) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.regex.invalid"));
            }
        }

        return null;
    }

    @Override
    public boolean breakChainOnError() {
        return true;
    }

    public Pattern getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(Pattern regexPattern) {
        if (regexPattern == null) {
            throw new IllegalArgumentException("Regex pattern cannot be null");
        }
        this.regexPattern = regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        if (regexPattern == null) {
            throw new IllegalArgumentException("Regex pattern cannot be null");
        }
        this.regexPattern = Pattern.compile(regexPattern);
    }
}
