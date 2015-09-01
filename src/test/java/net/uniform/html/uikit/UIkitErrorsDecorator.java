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

import java.util.List;
import net.uniform.api.Element;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.impl.AbstractSingleElementDecorator;

/**
 *
 * @author Eduardo Ramos
 */
public class UIkitErrorsDecorator extends AbstractSingleElementDecorator {

    public UIkitErrorsDecorator() {
    }

    @Override
    protected List<SimpleHTMLTag> render(Element element, List<SimpleHTMLTag> rendered) {
        if (element.validationPerformed() && !element.isValid()) {
            String currentClass = rendered.get(0).getProperty("class");
            if (currentClass == null) {
                currentClass = "";
            }

            rendered.get(0).setProperty("class", currentClass + " uk-form-danger");
        }

        return rendered;
    }
}
