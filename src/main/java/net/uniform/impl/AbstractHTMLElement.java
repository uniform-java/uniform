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
package net.uniform.impl;

import net.uniform.html.filters.RemoveControlCharacters;
import net.uniform.html.filters.StringTrim;

/**
 * Default HTML element implementation.
 * 
 * Extends the abstract implementation in order to:
 * <ul>
 * <li>Automatically set the id as the {@code id} and {@code name} HTML attributes</li>
 * <li>Add filters for trimming and removing control characters from any input value</li>
 * </ul>
 * @author Eduardo Ramos
 */
public abstract class AbstractHTMLElement extends AbstractElement {

    public AbstractHTMLElement(String id) {
        super(id);
        this.setProperty("id", id);
        this.setProperty("name", id);
    }

    @Override
    protected void setupDefaultFilters() {
        this.addFilter(new StringTrim());
        this.addFilter(new RemoveControlCharacters());
    }
    
    /**
     * Adds the given class to the {@code class} property of this element.
     * @param clazz HTML class to add
     * @return This element
     */
    public AbstractHTMLElement addClass(String clazz){
        String currentClass = this.getProperty("class");
        if(currentClass != null){
            currentClass += " " + clazz;
        }else{
            currentClass = clazz;
        }
        this.setProperty("class", currentClass);
        
        return this;
    }
}
