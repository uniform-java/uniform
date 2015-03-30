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
package net.uniform.html.elements;

import net.uniform.api.ElementWithValueConversion;
import net.uniform.api.Renderer;
import net.uniform.exceptions.UniformException;
import net.uniform.html.renderers.CheckboxRenderer;
import net.uniform.impl.AbstractHTMLElement;

/**
 * Element that represents an HTML checkbox.
 * @author Eduardo Ramos
 */
public class Checkbox extends AbstractHTMLElement implements ElementWithValueConversion<Boolean> {

    public Checkbox(String id) {
        super(id);
        this.setProperty("type", "checkbox");
        this.setProperty("value", "1");
    }

    @Override
    public Class<?> getValueType() {
        return Boolean.class;
    }
    
     @Override
    public void setValueType(Class<?> valueType) {
        throw new UniformException("Checkbox does not support value type change");
    }
    
    /**
     * Checks or unchecks this checkbox element.
     * @param checked True for checked, false for not checked
     * @return This element
     */
    public Checkbox setChecked(boolean checked){
        if(checked){
            String valueEnabledString = this.getProperty("value");
            this.setValue(valueEnabledString);
        }else{
            this.setValue("");
        }
        
        return this;
    }
    
    /**
     * Indicates if the element is considered checked,
     * that is, the value is the same as the one configured with {@link #setEnabledValueString(java.lang.String)}.
     * @return True if checked, false otherwise
     */
    public boolean isChecked(){
         String valueEnabledString = getEnabledValueString();
        
        String currentValue = this.getFirstValue();
        return valueEnabledString.equals(currentValue);
    }
    
    /**
     * Sets the value string considered as checked/enabled.
     * @param value Checked value, {@code "1"} by default
     * @return This element
     */
    public Checkbox setEnabledValueString(String value){
        boolean checked = this.isChecked();
        this.setProperty("value", value);
        this.setChecked(checked);
        return this;
    }
    
    /**
     * Returns the value string considered as checked/enabled.
     * @return Checked value, {@code "1"} by default
     */
    public String getEnabledValueString(){
        String valueEnabledString = this.getProperty("value");
        if(valueEnabledString == null){
            valueEnabledString = "1";
        }
        
        return valueEnabledString;
    }

    /**
     * Returns the value of this checkbox as a boolean, indicating if the element is checked or not.
     * @return True when checked, false otherwise
     */
    @Override
    public Boolean getConvertedValue() {
        return this.isChecked();
    }

    @Override
    public Renderer getDefaultRenderer() {
        return new CheckboxRenderer();
    }
}
