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
 * Decorator for appending or prepending an HTML label for any element.
 *
 * @author Eduardo Ramos
 */
public class LabelDecorator extends AbstractSingleElementDecorator {

    public static final String DEFAULT_CLASS = "element-label";

    public static final String PROPERTY_ESCAPE = "escape";

    public static final String PROPERTY_PREPEND = "prepend";

    public LabelDecorator(String elementClass) {
        this.setProperty("class", elementClass);
        this.setProperty(PROPERTY_ESCAPE, true);
        this.setProperty(PROPERTY_PREPEND, true);
    }

    public LabelDecorator() {
        this(DEFAULT_CLASS);
    }

    @Override
    public List<SimpleHTMLTag> render(Element element, List<SimpleHTMLTag> rendered) {
        SimpleHTMLTag labelTag = new SimpleHTMLTag("label");

        String id = element.getProperty("id");

        String label = element.getLabelTranslated();

        if(id != null){
            labelTag.setProperty("for", id);
        }
        labelTag.setProperty("class", this.getStringProperty("class"));
        labelTag.setContent(label);

        Boolean escape = this.getBooleanProperty(PROPERTY_ESCAPE);
        labelTag.setEscapeContent(Boolean.TRUE.equals(escape));

        Boolean prepend = this.getBooleanProperty(PROPERTY_PREPEND);

        List<SimpleHTMLTag> result = new ArrayList<>();
        if (Boolean.FALSE.equals(prepend)) {
            result.addAll(rendered);
            result.add(labelTag);
        } else {
            result.add(labelTag);
            result.addAll(rendered);
        }

        return result;
    }

    public void setPrepend(Boolean prepend) {
        this.setProperty(PROPERTY_PREPEND, prepend);
    }

    public void setEscape(Boolean escape) {
        this.setProperty(PROPERTY_ESCAPE, escape);
    }

    public Boolean isPrepend() {
        return this.getBooleanProperty(PROPERTY_PREPEND);
    }

    public Boolean isEscape() {
        return this.getBooleanProperty(PROPERTY_ESCAPE);
    }
}
