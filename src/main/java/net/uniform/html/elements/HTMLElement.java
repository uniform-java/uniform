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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.uniform.api.Element;
import net.uniform.api.Renderer;
import net.uniform.api.annotations.IgnoreDefaultFormDecorators;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.exceptions.UniformException;
import net.uniform.impl.AbstractElement;

/**
 * Help element that is able to render any {@link SimpleHTMLTag}.
 * Useful for adding arbitrary HTML into a form.
 * @author Eduardo Ramos
 */
@IgnoreDefaultFormDecorators
public class HTMLElement extends AbstractElement {

    private final SimpleHTMLTag elementTag;

    public static final boolean DEFAULT_ESCAPE_CONTENT = false;

    public HTMLElement(String id) {
        super(id);
        SimpleHTMLTag tag = new SimpleHTMLTag();
        tag.setEscapeContent(DEFAULT_ESCAPE_CONTENT);
        this.elementTag = tag;
    }

    public HTMLElement(String id, SimpleHTMLTag tag) {
        super(id);
        this.elementTag = tag;
    }

    public HTMLElement(String id, String tagName) {
        super(id);
        SimpleHTMLTag tag = new SimpleHTMLTag(tagName);
        tag.setEscapeContent(DEFAULT_ESCAPE_CONTENT);
        this.elementTag = tag;
    }

    @Override
    public Class<?> getValueType() {
        return null;
    }

    @Override
    public void setValueType(Class<?> valueType) {
        throw new UniformException("HTMLElement does not support value type change");
    }

    @Override
    public Element setValue(List<String> value) {
        return this;
    }

    @Override
    public Element setValue(String value) {
        return this;
    }

    @Override
    public List<String> getValue() {
        return null;
    }

    public HTMLElement setName(String name) {
        elementTag.setName(name);
        return this;
    }

    public HTMLElement setContent(String content) {
        elementTag.setContent(content);
        return this;
    }

    public String getName() {
        return elementTag.getName();
    }

    public String getContent() {
        return elementTag.getContent();
    }

    public List<SimpleHTMLTag> getSubTags() {
        return elementTag.getSubTags();
    }

    public HTMLElement addSubTag(SimpleHTMLTag tag) {
        this.elementTag.addSubTag(tag);
        return this;
    }

    @Override
    public HTMLElement setProperty(String key, String value) {
        elementTag.setProperty(key, value);
        return this;
    }

    public HTMLElement setProperties(Map<String, String> properties) {
        elementTag.setProperties(properties);
        return this;
    }

    @Override
    public String getProperty(String key) {
        return elementTag.getProperty(key);
    }

    @Override
    public Map<String, String> getProperties() {
        return elementTag.getProperties();
    }

    public HTMLElement setEscapeContent(boolean escapeContent) {
        elementTag.setEscapeContent(escapeContent);
        return this;
    }

    public boolean isEscapeContent() {
        return elementTag.isEscapeContent();
    }

    @Override
    public Renderer getDefaultRenderer() {
        return new Renderer<HTMLElement>() {

            @Override
            public List<SimpleHTMLTag> render(HTMLElement element) {
                return Arrays.asList(new SimpleHTMLTag(elementTag));
            }
        };
    }

    @Override
    public List<String> getValidationErrors() {
        return null;
    }
}
