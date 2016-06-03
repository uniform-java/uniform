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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.uniform.api.Element;
import net.uniform.api.Form;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.impl.AbstractDecorator;

/**
 * Decorator for enclosing any form content into an HTML tag with custom properties as attributes.
 *
 * @author Eduardo Ramos
 */
public class HTMLTagDecorator extends AbstractDecorator {

    public static final String PROPERTY_TAG_NAME = "tagName";

    public HTMLTagDecorator() {
    }

    public HTMLTagDecorator(String tagName) {
        this.setProperty(PROPERTY_TAG_NAME, tagName);
    }

    public HTMLTagDecorator(String tagName, Map<String, Object> properties) {
        this.setProperties(properties);
        this.setProperty(PROPERTY_TAG_NAME, tagName);
    }

    @Override
    public List<SimpleHTMLTag> render(Form form, List<Element> elements, List<SimpleHTMLTag> rendered) {
        String tagName = this.properties.get(PROPERTY_TAG_NAME) != null ? this.properties.get(PROPERTY_TAG_NAME).toString() : null;
        if (tagName == null) {
            throw new IllegalArgumentException(PROPERTY_TAG_NAME + " cannot be null");
        }

        Map<String, Object> finalProps = new HashMap<>(properties);
        finalProps.remove(PROPERTY_TAG_NAME);

        SimpleHTMLTag enclosingTag = new SimpleHTMLTag(tagName);
        for (Map.Entry<String, Object> prop : finalProps.entrySet()) {
            String name = prop.getKey();
            String value = prop.getValue() != null ? prop.getValue().toString() : null;

            enclosingTag.setProperty(name, value);
        }

        for (SimpleHTMLTag tag : rendered) {
            enclosingTag.addSubTag(tag);
        }

        List<SimpleHTMLTag> result = new ArrayList<>();
        result.add(enclosingTag);
        return result;
    }

    public final void setTagName(String tagName) {
        this.setProperty(PROPERTY_TAG_NAME, tagName);
    }

    public String getTagName() {
        return this.getStringProperty(PROPERTY_TAG_NAME);
    }
}
