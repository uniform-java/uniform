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

import net.uniform.api.Renderer;
import net.uniform.html.renderers.MultioptionInputRenderer;
import net.uniform.html.validators.SingleOptionValidator;
import net.uniform.impl.MultioptionInputElement;

/**
 * Element that represents a list of HTML radio elements in a group (same name).
 *
 * @author Eduardo Ramos
 */
public class Radio extends MultioptionInputElement {

    public Radio(String id) {
        super(id);
        this.addValidator(new SingleOptionValidator());
    }

    @Override
    public Renderer getDefaultRenderer() {
        return new MultioptionInputRenderer(MultioptionInputRenderer.InputType.RADIO);
    }
}
