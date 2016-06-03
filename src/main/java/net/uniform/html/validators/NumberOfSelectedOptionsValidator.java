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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.Validator;
import net.uniform.impl.ElementWithOptions;
import net.uniform.impl.utils.UniformUtils;

/**
 * Validator for any element with options to ensure that a minimum and/or a maximum number of elements is selected.
 *
 * @author Eduardo Ramos
 */
public class NumberOfSelectedOptionsValidator implements Validator<ElementWithOptions> {

    private Integer min = null;
    private Integer max = null;

    public NumberOfSelectedOptionsValidator() {
    }

    public NumberOfSelectedOptionsValidator(int max) {
        this.max = max;
    }

    public NumberOfSelectedOptionsValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public List<String> getValidationErrors(ElementWithOptions element, List<String> values) {
        if (min == null && max == null) {
            return null;//Nothing to validate
        }

        //Only validate if there is at least one value and it's not empty
        if (values == null || values.isEmpty()) {//May be not required
            return null;
        }

        if (values.size() == 1) {
            String firstValue = UniformUtils.firstValue(values);
            if (firstValue == null || firstValue.isEmpty()) {
                return null;
            }
        }

        Set<String> uniqueSelectedValues = new HashSet<>();

        for (String value : values) {
            uniqueSelectedValues.add(value);
        }

        int count = uniqueSelectedValues.size();
        if (min != null && count < min) {
            return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.numberofoptions.min", min));
        }

        if (max != null && count > max) {
            return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.numberofoptions.max", max));
        }

        return null;
    }

    @Override
    public boolean breakChainOnError() {
        return true;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
