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

import java.util.HashMap;
import net.uniform.api.Element;
import net.uniform.html.HTMLForm;
import net.uniform.html.decorators.ElementErrorsDecorator;
import net.uniform.html.decorators.HTMLTagDecorator;
import net.uniform.html.decorators.LabelDecorator;
import net.uniform.html.elements.Button;
import net.uniform.html.elements.DatePicker;

/**
 *
 * @author Eduardo Ramos
 */
public class UIkitForm extends HTMLForm {

    public UIkitForm() {
        super();
        this.addClass("uk-form");
    }

    @Override
    protected void setupDefaultDecorators() {
        addDefaultDecoratorForElementClass(Element.class, new UIkitErrorsDecorator());

        addDefaultDecoratorForElementClass(Element.class, new HTMLTagDecorator("div", new HashMap<String, Object>() {
            {
                put("class", "uk-form-controls");
            }
        }));
        addDefaultDecoratorForElementClass(Element.class, new LabelDecorator("uk-form-label"));
        addDefaultDecoratorForElementClass(Element.class, new ElementErrorsDecorator("uk-text-danger"));
        addDefaultDecoratorForElementClass(Element.class, new HTMLTagDecorator("div", new HashMap<String, Object>() {
            {
                put("class", "uk-form-row");
            }
        }));

        //No decorators for button in this form:
        setDefaultDecoratorsForElementClass(Button.class, null);
    }

    @Override
    protected void setupDefaultRenderers() {
        setDefaultRendererForElementClass(DatePicker.class, new UIkitDatepickerRenderer());
    }
}
