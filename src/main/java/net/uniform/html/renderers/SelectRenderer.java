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
package net.uniform.html.renderers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.uniform.api.Renderer;
import net.uniform.api.html.Option;
import net.uniform.api.html.OptionGroup;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.html.elements.Multiselect;
import net.uniform.html.elements.Select;
import net.uniform.impl.ElementWithOptions;

/**
 * Default renderer for {@link Select} and {@link Multiselect} elements.
 *
 * @author Eduardo Ramos
 */
public class SelectRenderer implements Renderer<ElementWithOptions> {

    @Override
    public List<SimpleHTMLTag> render(ElementWithOptions select) {
        SimpleHTMLTag selectTag = new SimpleHTMLTag("select");
        selectTag.setProperties(select.getProperties());

        if (select.isMultiValue()) {
            selectTag.setProperty("multiple", "multiple");
        }

        if (select.isRequired()) {
            selectTag.setProperty("required", "required");
        }

        Set<String> currentValues = new HashSet<>();
        List<String> valuesList = select.getValue();
        if (valuesList != null) {
            currentValues.addAll(valuesList);
        }

        List<OptionGroup> optionGroups = select.getOptionGroups();

        for (OptionGroup optionGroup : optionGroups) {
            if (optionGroup.hasOptions()) {
                List<SimpleHTMLTag> tags = this.renderOptionGroup(select, optionGroup, currentValues);

                for (SimpleHTMLTag tag : tags) {
                    selectTag.addSubTag(tag);
                }
            }
        }

        List<SimpleHTMLTag> result = new ArrayList<>();
        result.add(selectTag);

        return result;
    }

    private List<SimpleHTMLTag> renderOptionGroup(ElementWithOptions select, OptionGroup optionGroup, Set<String> currentValues) {
        SimpleHTMLTag groupTag;

        String id = optionGroup.getId();
        String label = optionGroup.getText();
        if (id != null && !id.trim().isEmpty() && label != null && !label.trim().isEmpty()) {
            groupTag = new SimpleHTMLTag("optgroup");
            groupTag.setProperty("id", select.getId() + "-" + id);
            groupTag.setProperty("label", label);
            if (!optionGroup.isEnabled()) {
                groupTag.setProperty("disabled", "disabled");
            }
        } else {
            groupTag = new SimpleHTMLTag();//Empty tag, not rendered
        }

        for (Option option : optionGroup.getOptions()) {
            String optionText = option.getText();
            String optionValue = option.getValue();
            SimpleHTMLTag optionTag = new SimpleHTMLTag("option", optionText);
            optionTag.setProperty("value", optionValue);

            if (currentValues.contains(optionValue)) {
                optionTag.setProperty("selected", "selected");
            }

            if (!option.isEnabled()) {
                optionTag.setProperty("disabled", "disabled");
            }

            groupTag.addSubTag(optionTag);
        }

        List<SimpleHTMLTag> result = new ArrayList<>();
        result.add(groupTag);

        return result;
    }

}
