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
import net.uniform.api.Element;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.Validator;
import net.uniform.impl.utils.UniformUtils;

/**
 * Validator for single or multi value elements that must be in a given set.
 * @author Eduardo Ramos
 */
public class InSetValidator implements Validator<Element> {

    protected final Set<String> valuesSet = new HashSet<>();

    public InSetValidator() {
    }
    
    public InSetValidator(Set<String> validValues) {
        if(validValues != null){
            this.valuesSet.addAll(validValues);
        }
    }

    @Override
    public List<String> getValidationErrors(Element element, List<String> value) {
        String firstValue = UniformUtils.firstValue(value);
        if (firstValue != null && !firstValue.isEmpty()) {
            for (String val : value) {
                if(!valuesSet.contains(val)){
                    return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.inset.invalid", val));
                }
            }
        }

        return null;
    }

    @Override
    public boolean breakChainOnError() {
        return true;
    }

    public void setValidValues(Set<String> validValues) {
        this.valuesSet.clear();
        if(validValues != null){
            this.valuesSet.addAll(validValues);
        }
    }
    
    public void addValidValue(String value){
        this.valuesSet.add(value);
    }
    
    public void removeValidValue(String value){
        this.valuesSet.remove(value);
    }
    
    public void clearValidValues(){
        this.valuesSet.clear();
    }

    public Set<String> getValuesSet() {
        return new HashSet<>(valuesSet);
    }
}
