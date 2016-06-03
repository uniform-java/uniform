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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.uniform.html.elements.Radio;
import net.uniform.html.elements.Select;

/**
 * <p>
 * An option group represents a group of options of an element that can contain options, such as a {@link Select} or a {@link Radio}.
 * Option values cannot be repeated in the option group.
 * </p>
 * 
 * An option group holds:
 * <ul>
 * <li>An id, unique in the element</li>
 * <li>A user friendly text</li>
 * <li>Enabled state</li>
 * </ul>
 * 
 * <p>An option group with null id and text is considered as the default group of the element</p>
 * @author Eduardo Ramos
 */
public class OptionGroup {

    private final Map<String, Option> options = new LinkedHashMap<>();//Keep order

    private final String id;

    private final String text;

    private final boolean enabled;

    public OptionGroup(String id, String text, boolean enabled) {
        this.id = id;
        this.text = text;
        this.enabled = enabled;
    }

    public OptionGroup(String id, String text) {
        this(id, text, true);
    }

    public OptionGroup() {
        this(null, null, true);
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Adds an option to the option group.
     *
     * @param value Value, not null and not repeated in the group
     * @param text Text, not null
     * @return This group
     */
    public OptionGroup addOption(String value, String text) {
        return this.addOption(new Option(value, text));
    }

    /**
     * Adds an option to the option group.
     *
     * @param option Option, with value not repeated in the group
     * @return This group
     */
    public OptionGroup addOption(Option option) {
        if (option == null) {
            throw new IllegalArgumentException("Option cannot be null");
        }

        String value = option.getValue();

        if (options.containsKey(value)) {
            throw new IllegalArgumentException("An option with value '" + value + "' already exists in this group");
        }

        options.put(value, option);
        return this;
    }

    /**
     * Removes an option from this group by value, if present
     *
     * @param value Option value to remove
     * @return This group
     */
    public OptionGroup removeOption(String value) {
        if (value == null) {
            value = "";
        }

        options.remove(value);
        return this;
    }

    /**
     * Removes an option from this gruop, if present
     *
     * @param option Option to remove
     * @return This group
     */
    public OptionGroup removeOption(Option option) {
        if (option == null) {
            throw new IllegalArgumentException("Option cannot be null");
        }

        options.remove(option.getValue());
        return this;
    }

    /**
     * Indicates if the group has an option with the given value.
     *
     * @param value Option value to check
     * @return True if the option for the value exists
     */
    public boolean hasValue(String value) {
        return options.containsKey(value);
    }

    /**
     * Indicates if the group has an option with the given value, which is also enabled.
     * This group must also be enabled.
     * @param value Option value to check
     * @return True if the option for the value exists, it's enabled and this group is enabled
     */
    public boolean hasValueEnabled(String value) {
        return enabled && options.containsKey(value) && options.get(value).isEnabled();
    }

    /**
     * Returns the option in this group with the given value, if present.
     *
     * @param value Option value
     * @return Option or null
     */
    public Option getOption(String value) {
        return options.get(value);
    }

    /**
     * Returns all the options in this group, in list order.
     * @param includeDisabled Indicates if disabled options should be included in the list.
     * If the group itself is disabled, its options are also considered as disabled.
     * @return List of options
     */
    public List<Option> getOptions(boolean includeDisabled) {
        List<Option> result = new ArrayList<>(); //Keep order

        if (!includeDisabled && !enabled) {
            return result;//This group is disabled entirely
        }

        for (Option option : options.values()) {
            if (includeDisabled || option.isEnabled()) {
                result.add(option);
            }
        }

        return result;
    }

    /**
     * Returns all the options in this group, in list order.
     *
     * @return List of options
     */
    public List<Option> getOptions() {
        return this.getOptions(true);
    }

    /**
     * Returns the options in this group that are not disabled, in list order.
     * If the group itself is disabled, options are also considered as disabled.
     * @return List of enabled options
     */
    public List<Option> getEnabledOptions() {
        return this.getOptions(false);
    }

    /**
     * Returns all the values of the options of this group
     *
     * @return Set of values
     */
    public Set<String> getOptionValues() {
        return options.keySet();
    }

    /**
     * Returns all the values of the options of this group that are enabled.
     * If the group itself is disabled, its options are also considered as disabled.
     * @return Set of values of enabled options
     */
    public Set<String> getEnabledOptionValues() {
        Set<String> result = new HashSet<>();
        if (!enabled) {
            return result;//This group is disabled entirely
        }

        for (Option option : options.values()) {
            if (option.isEnabled()) {
                result.add(option.getValue());
            }
        }

        return result;
    }

    /**
     * Removes all options from this group.
     *
     * @return This group
     */
    public OptionGroup clearOptions() {
        options.clear();
        return this;
    }

    /**
     * Indicates if the group has any option.
     *
     * @return True if the group has any option, false otherwise
     */
    public boolean hasOptions() {
        return !options.isEmpty();
    }

    @Override
    public String toString() {
        return "OptionGroup{" + "options=" + options + ", id=" + id + ", text=" + text + ", enabled=" + enabled + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.options);
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.text);
        hash = 37 * hash + (this.enabled ? 1 : 0);
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
        final OptionGroup other = (OptionGroup) obj;
        if (!Objects.equals(this.options, other.options)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
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
