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

import java.util.Objects;
import net.uniform.html.elements.Radio;
import net.uniform.html.elements.Select;

/**
 * Represents an option of an element that can contain options, such as a {@link Select} or a {@link Radio}.
 * An option holds:
 * <ul>
 * <li>A value, not null and unique in the element</li>
 * <li>A user friendly text, not null</li>
 * <li>Enabled state</li>
 * </ul>
 *
 * @author Eduardo Ramos
 */
public class Option {

    private final String value;

    private final String text;

    private final boolean enabled;

    public Option(String value, String text, boolean enabled) {
        this.value = value;
        this.text = text;
        this.enabled = enabled;

        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
    }

    public Option(String value, String text) {
        this(value, text, true);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Option{" + "value=" + value + ", text=" + text + ", enabled=" + enabled + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.value);
        hash = 11 * hash + Objects.hashCode(this.text);
        hash = 11 * hash + (this.enabled ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Option other = (Option) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (this.enabled != other.enabled) {
            return false;
        }
        return true;
    }
}
