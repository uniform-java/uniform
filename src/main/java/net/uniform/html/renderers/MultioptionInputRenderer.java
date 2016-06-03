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
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.uniform.api.Renderer;
import net.uniform.api.html.Option;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.html.elements.Multicheckbox;
import net.uniform.html.elements.Radio;
import net.uniform.impl.MultioptionInputElement;

/**
 * Default renderer for multi input elements such as {@link Multicheckbox} and {@link Radio}.
 *
 * @author Eduardo Ramos
 */
public class MultioptionInputRenderer implements Renderer<MultioptionInputElement> {

    public enum InputType {
        RADIO("radio"),
        CHECKBOX("checkbox");

        private final String type;

        InputType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    protected final InputType inputType;

    public MultioptionInputRenderer(InputType inputType) {
        if (inputType == null) {
            throw new IllegalArgumentException("Input type cannot be null");
        }

        this.inputType = inputType;
    }

    @Override
    public List<SimpleHTMLTag> render(MultioptionInputElement multi) {
        List<SimpleHTMLTag> result = new ArrayList<>();

        boolean required = multi.isRequired();

        List<String> currentValues = multi.getValue();
        if (currentValues == null) {
            currentValues = new ArrayList<>();
        }

        List<Option> options = multi.getOptions();//We don't support option groups here
        Set<String> enabledValues = multi.getEnabledOptionValues();//Do this to take into account disabled groups also, not only disabled options

        boolean prependOptionLabels = multi.isPrependOptionLabels();
        boolean escapeOptionLabels = multi.isEscapeOptionLabels();

        String separator = multi.getSeparator();
        SimpleHTMLTag separatorTag = null;
        if (separator != null) {
            separatorTag = new SimpleHTMLTag();
            separatorTag.setEscapeContent(false);
            separatorTag.setContent(separator);
        }

        Map<String, String> finalProps = multi.getProperties();

        //Finally add each option:
        for (Option option : options) {
            result.add(getOptionTag(option, enabledValues, finalProps, required, currentValues, prependOptionLabels, escapeOptionLabels));
            if (separatorTag != null) {
                result.add(separatorTag);
            }
        }

        if (separator != null) {
            result.remove(result.size() - 1);//Remove unnecesary separator after last element
        }

        return result;
    }

    protected SimpleHTMLTag getOptionTag(Option option, Set<String> enabledValues, Map<String, String> properties, boolean required, List<String> currentValues, boolean prependOptionLabels, boolean escapeOptionLabels) {
        String optionValue = option.getValue();
        String optionText = option.getText();

        SimpleHTMLTag inputTag = new SimpleHTMLTag("input");
        inputTag.setProperties(properties);
        inputTag.setProperty("type", inputType.getType());
        inputTag.setProperty("value", optionValue);
        if (properties.containsKey("id")) {
            //Concat the id of the element with the option keys to generate unique ids
            //Also remove any space from input ids
            String optionId = properties.get("id") + "-" + (optionValue.replaceAll(" +", "-"));
            inputTag.setProperty("id", optionId);
        }

        if (currentValues.contains(optionValue)) {
            inputTag.setProperty("checked", "checked");
        }

        if (!enabledValues.contains(optionValue)) {
            inputTag.setProperty("disabled", "disabled");
        }

        if (required && inputType == InputType.RADIO) {
            inputTag.setProperty("required", "required");//We cannot use required in multi-checkbox or it will require to mark all options
        }

        SimpleHTMLTag enclosingTag = new SimpleHTMLTag("label");
        SimpleHTMLTag labelTextTag = new SimpleHTMLTag();
        labelTextTag.setContent(optionText);
        labelTextTag.setEscapeContent(escapeOptionLabels);

        if (prependOptionLabels) {
            enclosingTag.addSubTag(labelTextTag);
            enclosingTag.addSubTag(inputTag);
        } else {
            enclosingTag.addSubTag(inputTag);
            enclosingTag.addSubTag(labelTextTag);
        }

        return enclosingTag;
    }
}
