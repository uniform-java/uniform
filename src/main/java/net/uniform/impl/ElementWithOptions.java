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
package net.uniform.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.uniform.api.html.Option;
import net.uniform.api.html.OptionGroup;

/**
 * Abstract element implementation that contains all generic logic that any element with options should have.
 *
 * @author Eduardo Ramos
 */
public abstract class ElementWithOptions extends AbstractHTMLElement {

    protected final Map<String, OptionGroup> optionGroups = new LinkedHashMap<>();//Keep order

    public ElementWithOptions(String id) {
        super(id);
    }

    /**
     * Adds an option group to this element.
     *
     * @param optionGroup New group with unique id and unique values in this element
     * @return This element
     */
    public ElementWithOptions addOptionGroup(OptionGroup optionGroup) {
        if (optionGroup == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }

        String groupId = optionGroup.getId();
        if (optionGroups.containsKey(groupId)) {
            throw new IllegalArgumentException("The group id '" + groupId + "' already present in this element");
        }

        Set<String> groupValues = optionGroup.getOptionValues();
        for (String groupValue : groupValues) {
            if (this.hasValue(groupValue)) {
                throw new IllegalArgumentException("The value '" + groupValue + "' is already present in this element");
            }
        }

        optionGroups.put(groupId, optionGroup);
        return this;
    }

    /**
     * Adds an option to the default option group of this element.
     *
     * @param option New option with unique value in this element
     * @return This element
     */
    public ElementWithOptions addOption(Option option) {
        return addOptionToGroup(option, null);
    }

    /**
     * Adds an option to the default option group of this element.
     *
     * @param value Unique value in this element
     * @param text Option text
     * @return This element
     */
    public ElementWithOptions addOption(Object value, String text) {
        return addOptionToGroup(value, text, null);
    }

    /**
     * Adds an option to the default option group of this element.
     *
     * @param value Unique value in this element
     * @param text Option text
     * @return This element
     */
    public ElementWithOptions addOption(String value, String text) {
        return addOptionToGroup(value, text, null);
    }

    /**
     * Adds an option to the group of this element with the given id. If the group is not found, it's created with null text.
     *
     * @param option New option with unique value in this element
     * @param groupId Id of the option group
     * @return This element
     */
    public ElementWithOptions addOptionToGroup(Option option, String groupId) {
        if (option == null) {
            throw new IllegalArgumentException("Option cannot be null");
        }

        for (OptionGroup group : optionGroups.values()) {
            if (group.hasValue(option.getValue())) {
                throw new IllegalArgumentException("The value '" + option.getValue() + "' is already present in this element");
            }
        }

        OptionGroup group = optionGroups.get(groupId);

        if (group == null) {
            group = new OptionGroup(groupId, null);
            this.addOptionGroup(group);
        }

        group.addOption(option);

        return this;
    }

    /**
     * Adds an option to the group of this element with the given id.
     * If the group is not found, it's created with null text.
     * @param value Unique value in this element
     * @param text Option text
     * @param groupId Id of the option group
     * @return This element
     */
    public ElementWithOptions addOptionToGroup(String value, String text, String groupId) {
        if (value == null) {
            value = "";
        }

        value = value.trim();

        this.addOptionToGroup(new Option(value, text), groupId);

        return this;
    }

     /**
     * Adds an option to the group of this element with the given id.
     * If the group is not found, it's created with null text.
     * @param value Unique value in this element
     * @param text Option text
     * @param groupId Id of the option group
     * @return This element
     */
    public ElementWithOptions addOptionToGroup(Object value, String text, String groupId) {
        String valueString = "";
        if (value != null) {
            valueString = value.toString();
        }

        return addOptionToGroup(valueString, text, groupId);
    }

    /**
     * Removes an option of this element by value.
     *
     * @param value Option value
     * @return This element
     */
    public ElementWithOptions removeOption(String value) {
        for (OptionGroup group : optionGroups.values()) {
            group.removeOption(value);
        }
        return this;
    }

    /**
     * Removes an option of this element.
     *
     * @param option Option
     * @return This element
     */
    public ElementWithOptions removeOption(Option option) {
        if (option == null) {
            throw new IllegalArgumentException("Option cannot be null");
        }

        for (OptionGroup group : optionGroups.values()) {
            group.removeOption(option);
        }
        return this;
    }

    /**
     * Returns all the options in this element, in list order.
     * @param includeDisabled Indicates if disabled options should be included in the list.
     * If a group itself is disabled, its options are also considered as disabled.
     * @return List of options
     */
    public List<Option> getOptions(boolean includeDisabled) {
        List< Option> result = new ArrayList<>();

        for (OptionGroup group : optionGroups.values()) {
            result.addAll(group.getOptions(includeDisabled));
        }

        return result;
    }

    /**
     * Returns all the options in this element, in list order.
     *
     * @return List of options
     */
    public List<Option> getOptions() {
        return this.getOptions(true);
    }
    
    /**
     * Returns all the options in this element that are not disabled, in list order.
     * If a group itself is disabled, its options are also considered as disabled.
     * @return List of options
     */
    public List<Option> getEnabledOptions() {
        return this.getOptions(false);
    }

    /**
     * Returns all the values of the options of this element.
     *
     * @return Set of values
     */
    public Set<String> getOptionsValues() {
        Set<String> result = new HashSet<>();
        for (OptionGroup group : optionGroups.values()) {
            result.addAll(group.getOptionValues());
        }

        return result;
    }

    /**
     * Returns all the values of the options of this element that are enabled.
     * If a group itself is disabled, its options are also considered as disabled.
     * @return Set of values of enabled options
     */
    public Set<String> getEnabledOptionValues() {
        Set<String> result = new HashSet<>();
        for (OptionGroup group : optionGroups.values()) {
            result.addAll(group.getEnabledOptionValues());
        }

        return result;
    }

    /**
     * Returns all option groups in this element
     *
     * @return List of option groups, never null
     */
    public List<OptionGroup> getOptionGroups() {
        return new ArrayList<>(optionGroups.values());
    }

    /**
     * Removes all options and option groups of this element.
     *
     * @return This element
     */
    public ElementWithOptions clearOptions() {
        optionGroups.clear();
        return this;
    }

    /**
     * Replaces all options of this element.
     *
     * @param options Options to use. {@link LinkedHashMap} to keep options original ordering.
     * @return This element
     */
    public ElementWithOptions setOptions(LinkedHashMap<String, String> options) {
        this.optionGroups.clear();
        for (Map.Entry<String, String> entry : options.entrySet()) {
            this.addOption(entry.getKey(), entry.getValue());
        }

        return this;
    }

    /**
     * Indicates if the element has a value in any of its groups.
     *
     * @param value Value
     * @return True if the value exists, false otherwise
     */
    public boolean hasValue(String value) {
        for (OptionGroup group : optionGroups.values()) {
            if (group.hasValue(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates if the element has an enabled value in any of its groups, that must also be enabled.
     *
     * @param value Value
     * @return True if the value exists and is enabled, false otherwise
     */
    public boolean hasValueEnabled(String value) {
        for (OptionGroup group : optionGroups.values()) {
            if (group.hasValueEnabled(value)) {
                return true;
            }
        }

        return false;
    }
}
