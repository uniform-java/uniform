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
import net.uniform.api.Form;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.impl.AbstractDecorator;

/**
 * Decorator for appending or prepending a list of form validation errors to the form content.
 *
 * @author Eduardo Ramos
 */
public class FormErrorsDecorator extends AbstractDecorator {

    public static final String DEFAULT_ERRORS_LIST_CLASS = "form-validation-errors";

    public static final String PROPERTY_PREPEND = "prepend";

    public static final String PROPERTY_CLASS = "class";

    public FormErrorsDecorator(String errorsListClass) {
        this.setProperty(PROPERTY_CLASS, errorsListClass);
    }

    public FormErrorsDecorator() {
        this(DEFAULT_ERRORS_LIST_CLASS);
    }

    @Override
    public List<SimpleHTMLTag> render(Form form, List<Element> elements, List<SimpleHTMLTag> rendered) {
        if (!form.validationPerformed()) {
            return rendered;
        }

        List<String> errors = form.getValidationErrors().get(Form.FORM_LEVEL_VALIDATION_ERRORS_INDEX);
        if (errors == null || errors.isEmpty()) {
            return rendered;
        } else {
            SimpleHTMLTag errorsListTag = new SimpleHTMLTag("ul");

            errorsListTag.setProperty("class", this.getStringProperty("class"));

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
