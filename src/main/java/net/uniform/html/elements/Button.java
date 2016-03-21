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

import java.util.List;
import net.uniform.api.Element;
import net.uniform.api.Renderer;
import net.uniform.api.annotations.IgnoreDefaultFormDecorators;
import net.uniform.exceptions.UniformException;
import net.uniform.html.renderers.ButtonRenderer;
import net.uniform.impl.AbstractHTMLElement;

/**
 * Element that represents an HTML Button.
 * @author Eduardo Ramos
 */
@IgnoreDefaultFormDecorators
public class Button extends AbstractHTMLElement {
    
    public static final String BUTTON_TYPE_SUBMIT = "submit";
    public static final String BUTTON_TYPE_BUTTON = "button";
    public static final String BUTTON_TYPE_RESET = "reset";
    
    public static final String DEFAULT_BUTTON_TYPE = BUTTON_TYPE_BUTTON;
    
    public static final boolean DEFAULT_ESCAPE = true;
    
    protected boolean escape = DEFAULT_ESCAPE;

    public Button(String id) {
        this(id, DEFAULT_BUTTON_TYPE);
    }
    
    public Button(String id, String type) {
        super(id);
        this.setProperty("type", type);
        this.removeProperty("name");
        this.setEscape(DEFAULT_ESCAPE);
    }

    @Override
    public Class<?> getValueType() {
        return null;
    }
    
    @Override
    public void setValueType(Class<?> valueType) {
        throw new UniformException("Button does not support value type change");
    }

    @Override
    public Element populate(List<String> value) {
        return this;
    }
    
    @Override
    public Element setValue(String value) {
        return this;
    }

    @Override
    public Element setValue(List<String> value) {
        return this;
    }

    @Override
    public Element reset() {
        return this;
    }

    @Override
    public Renderer getDefaultRenderer() {
        return new ButtonRenderer();
    }
    
    /**
     * Sets the escape HTML for this button's inner text (label).
     * @param escape True for enabling escaping of HTML, false for disabling it
     */
    public final void setEscape(boolean escape){
        this.escape = escape;
    }
    
    /**
     * Returns the escape HTML flag for this button's inner text.
     * @return True if escape is enabled, false otherwise
     */
    public final boolean isEscape(){
        return escape;
    }
}
