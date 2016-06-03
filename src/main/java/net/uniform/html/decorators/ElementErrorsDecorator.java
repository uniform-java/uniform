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
package net.uniform.html.decorators;

import java.util.ArrayList;
import java.util.List;
import net.uniform.api.Element;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.impl.AbstractSingleElementDecorator;

/**
 * Decorator for appending or prepending a list of validation errors to any element.
 *
 * @author Eduardo Ramos
 */
public class ElementErrorsDecorator extends AbstractSingleElementDecorator {

    public static final String DEFAULT_ERRORS_LIST_CLASS = "validation-errors";

    public static final String PROPERTY_CLASS = "class";

    public static final String PROPERTY_PREPEND = "prepend";

    public ElementErrorsDecorator(String errorsListClass) {
        this.setProperty(PROPERTY_CLASS, errorsListClass);
        this.setPrepend(false);
    }

    public ElementErrorsDecorator() {
        this(DEFAULT_ERRORS_LIST_CLASS);
    }

    @Override
    public List<SimpleHTMLTag> render(Element element, List<SimpleHTMLTag> rendered) {
        if (!element.validationPerformed()) {
            return rendered;
        }

        List<String> errors = element.getValidationErrors();
        if (errors.isEmpty()) {
            return rendered;
        } else {
            SimpleHTMLTag errorsListTag = new SimpleHTMLTag("ul");

            errorsListTag.setProperty("class", this.getStringProperty("class"));
            errorsListTag.setProperty("data-id", element.getProperty("id"));

            for (String error : errors) {
                errorsListTag.addSubTag(new SimpleHTMLTag("li", error));
            }

            List<SimpleHTMLTag> result = new ArrayList<>();

            if (this.getBooleanProperty(PROPERTY_PREPEND)) {
                result.add(errorsListTag);
                result.addAll(rendered);
            } else {
                result.addAll(rendered);
                result.add(errorsListTag);
            }

            return result;
        }
    }

    public final void setPrepend(Boolean prepend) {
        this.setProperty(PROPERTY_PREPEND, prepend);
    }

    public Boolean isPrepend() {
        return this.getBooleanProperty(PROPERTY_PREPEND);
    }

    public final void setErrorsClass(String clazz) {
        this.setProperty(PROPERTY_CLASS, clazz);
    }

    public String getErrorsClass() {
        return this.getStringProperty(PROPERTY_CLASS);
    }
}
