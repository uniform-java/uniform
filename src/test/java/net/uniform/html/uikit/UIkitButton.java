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
package net.uniform.html.uikit;

import net.uniform.html.elements.Button;

/**
 *
 * @author Eduardo Ramos
 */
public class UIkitButton extends Button {

    public UIkitButton(String id) {
        this(id, BUTTON_TYPE_BUTTON);
    }

    public UIkitButton(String id, String type) {
        super(id, type);
        this.addClass("uk-button");
    }
}
