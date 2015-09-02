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
package net.uniform.api.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.uniform.impl.utils.HTMLRenderer;
import net.uniform.impl.utils.HTMLRenderingUtils;
import net.uniform.impl.utils.UniformUtils;

/**
 * A Simple HTML tag is able to:
 * <ul>
 * <li>Have a tag name (optional) to enclose the content or subtags. If tag name is null, only the content or subtags is rendered</li>
 * <li>Have a list of properties to be rendered as attributes of the enclosing tag</li>
 * <li>Have a text content (escaped or not) or a list of subtags</li>
 * </ul>
 * 
 * <p><b>If any not null text content is set, subtags will be ignored</b></p>
 * 
 * <p>These tags can be rendered as HTML String with the {@link HTMLRenderer} class.</p>
 * 
 * <p>All property names will be converted to lower-case</p>
 *
 * @author Eduardo Ramos
 * @see HTMLRenderingUtils
 * @see HTMLRenderer
 */
public class SimpleHTMLTag {

    /**
     * Empty properties to avoid creating many maps where not necessary.
     */
    private final Map<String, String> EMPTY_PROPERTIES = Collections.unmodifiableMap(new HashMap<String, String>());
    /**
     * Empty list of tags to avoid creating many lists where not necessary.
     */
    private final List<SimpleHTMLTag> EMPTY_TAGS = Collections.unmodifiableList(new ArrayList<SimpleHTMLTag>());

    protected String name;
    protected Map<String, String> properties;
    protected List<SimpleHTMLTag> subTags;
    protected String content = null;
    private boolean escapeContent = true;

    public SimpleHTMLTag() {
    }

    public SimpleHTMLTag(String name) {
        this.name = name;
    }

    public SimpleHTMLTag(String name, Map<String, String> properties) {
        this.name = name;
        this.properties = new HashMap<>(properties);
    }

    public SimpleHTMLTag(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public SimpleHTMLTag(String name, Map<String, String> properties, String content) {
        this.name = name;
        this.properties = new HashMap<>(properties);
        this.content = content;
    }

    /**
     * Copy constructor for a tag an its subtags.
     * @param tag Input tag to create a copy
     */
    public SimpleHTMLTag(SimpleHTMLTag tag) {
        this.name = tag.name;
        this.properties = tag.properties != null ? new HashMap<>(tag.properties) : null;
        this.content = tag.content;
        this.escapeContent = tag.escapeContent;

        if (tag.subTags != null) {
            this.subTags = new ArrayList<>();
            for (SimpleHTMLTag subTag : tag.subTags) {
                this.subTags.add(new SimpleHTMLTag(subTag));
            }
        }
    }

    /**
     * Sets the tag name.
     * @param name Tag name
     * @return This tag
     */
    public SimpleHTMLTag setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the string content of the tag.
     * @param content Content
     * @return This tag
     */
    public SimpleHTMLTag setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Returns the name of this tag.
     * @return Tag name or null for content-only tags
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the text content of this tag.
     * @return Text content of this tag or null
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the subtags of this tag, if any
     * @return List of tags, never null
     */
    public List<SimpleHTMLTag> getSubTags() {
        if (subTags == null) {
            return EMPTY_TAGS;
        }

        return new ArrayList<>(subTags);
    }
    
    /**
     * Clears all the subtags
     */
    public void clearSubTags() {
        subTags = null;
    }

    /**
     * Adds a subtag to this tag.
     * If the tag has a text content, any subtag will be ignored.
     * @param tag Subtag to add
     * @return This tag
     */
    public SimpleHTMLTag addSubTag(SimpleHTMLTag tag) {
        if (tag == this) {
            throw new IllegalArgumentException("The tag cannot be its own parent");
        }

        if (subTags == null) {
            subTags = new ArrayList<>();
        }

        subTags.add(tag);

        return this;
    }

    /**
     * Sets a property of the tag by key.
     * @param key Property key
     * @param value Property value
     * @return This tag
     */
    public SimpleHTMLTag setProperty(String key, String value) {
        key = UniformUtils.checkPropertyNameAndLowerCase(key);
        
        if (properties == null) {
            properties = new HashMap<>();
        }

        properties.put(key, value);

        return this;
    }

    /**
     * Removes a property of the tag by key.
     * @param key Propery key
     * @return This tag
     */
    public SimpleHTMLTag removeProperty(String key) {
        key = UniformUtils.checkPropertyNameAndLowerCase(key);
        
        if (properties != null) {
            properties.remove(key);
        }
        return this;
    }

    /**
     * Replaces all properties of the tag with a map of properties indexed by key.
     * @param properties New properties
     * @return This tag
     */
    public SimpleHTMLTag setProperties(Map<String, String> properties) {
        if (properties == null || properties.isEmpty()) {
            this.properties = null;
        } else {
            this.properties = new HashMap<>();
            
            for (Entry<String, String> entry : properties.entrySet()) {
                String key = entry.getKey();
                key = UniformUtils.checkPropertyNameAndLowerCase(key);
                
                this.properties.put(key, entry.getValue());
            }
        }

        return this;
    }

    /**
     * Returns a property of this tag by key, if present.
     * @param key Property key
     * @return Property value or null
     */
    public String getProperty(String key) {
        key = UniformUtils.checkPropertyNameAndLowerCase(key);
        
        if (properties == null) {
            return null;
        }
        return properties.get(key);
    }

    /**
     * Returns all the properties in this tag.
     * @return A properties index by key, never null
     */
    public Map<String, String> getProperties() {
        if (properties == null) {
            return EMPTY_PROPERTIES;
        }

        return new HashMap<>(properties);
    }

    /**
     * Sets the escape HTML flag for this tag's content.
     * By default escape is enabled.
     * @param escapeContent Escape flag
     * @return This tag
     */
    public SimpleHTMLTag setEscapeContent(boolean escapeContent) {
        this.escapeContent = escapeContent;

        return this;
    }

    /**
     * Indicates if the escape HTML flag for this tag's content is active.
     * By default escape is enabled.
     * @return Escape HTML flag
     */
    public boolean isEscapeContent() {
        return escapeContent;
    }

    @Override
    public String toString() {
        return HTMLRenderingUtils.render(this);
    }
}
