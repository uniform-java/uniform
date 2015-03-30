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
 * Validator for any single-value numeric input.
 * It supports:
 * <ul>
 * <li>Allowing decimals or not</li>
 * <li>A minimum allowed value (inclusive or not)</li>
 * <li>A maximum allowed value (inclusive or not)</li>
 * </ul>
 * @author Eduardo Ramos
 */
public class NumericValidator implements Validator<Element>{
    
    private boolean allowDecimals;
    private Double min;
    private boolean minInclusive = true;
    private Double max;
    private boolean maxInclusive = true;

    public NumericValidator(boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
    }

    @Override
    public List<String> getValidationErrors(Element element, List<String> value) {
        String firstValue = UniformUtils.firstValue(value);
        if (firstValue == null || firstValue.isEmpty()) {
            return null;
        }
        
        double numberValue;
        try {
            firstValue = firstValue.trim();
            if(allowDecimals){
                    numberValue = Double.parseDouble(firstValue);
            }else {
                    numberValue = Long.parseLong(firstValue);
            }
        } catch (NumberFormatException e) {
            String numberTypeName;
            if(allowDecimals){
                    numberTypeName = "number";
            }else{
                    numberTypeName = "integer";
            }
            return translate("uniform.validators.numeric.invalid", firstValue, numberTypeName);
        }
        
        if(min != null){
            if(minInclusive){
                if(numberValue < min){
                    return translate("uniform.validators.numeric.greaterequal", firstValue, min);
                }
            }else{
                if(numberValue <= min){
                    return translate("uniform.validators.numeric.greater", firstValue, min);
                }
            }
        }
        
        if(max != null){
            if(maxInclusive){
                if(numberValue > max){
                    return translate("uniform.validators.numeric.lessequal", firstValue, max);
                }
            }else{
                if(numberValue >= max){
                    return translate("uniform.validators.numeric.less", firstValue, max);
                }
            }
        }
        
        return null;
    }
    
    protected List<String> translate(String code, Object... args){
        return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate(code, args));
    }

    @Override
    public boolean breakChainOnError() {
        return true;
    }

    public boolean isAllowDecimals() {
        return allowDecimals;
    }

    public void setAllowDecimals(boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public boolean isMinInclusive() {
        return minInclusive;
    }

    public void setMinInclusive(boolean minInclusive) {
        this.minInclusive = minInclusive;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public boolean isMaxInclusive() {
        return maxInclusive;
    }

    public void setMaxInclusive(boolean maxInclusive) {
        this.maxInclusive = maxInclusive;
    }
    
    public void setGreaterThan(double min){
        this.setMin(min);
        this.setMinInclusive(false);
    }
    
    public void setGreaterThanOrEqual(double min){
        this.setMin(min);
        this.setMinInclusive(true);
    }
    
    public void setLessThan(double max){
        this.setMax(max);
        this.setMaxInclusive(false);
    }
    
    public void setLessThanOrEqual(double max){
        this.setMax(max);
        this.setMaxInclusive(true);
    }
    
    public void removeMin(){
        this.min = null;
    }
    
    public void removeMax(){
        this.max = null;
    }
}
