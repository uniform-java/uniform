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
import java.util.HashSet;
import java.util.LinkedHashMap;
import net.uniform.api.html.Option;
import net.uniform.api.html.OptionGroup;
import net.uniform.html.decorators.ElementErrorsDecorator;
import net.uniform.impl.utils.HTMLRenderingUtils;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class SelectTest {

    @Test
    public void test() {
        Select select = new Select("test");
        select.addOption(null, "---");//Empty string option
        select.addOption("1", "One");
        select.addOption("2", "Two");
        select.addOption("3", "Three");

        select.setValue(" 2 ");//trim test
        select.setRequired(true);

        select.addDecorator(new ElementErrorsDecorator());

        assertHTMLEquals("<select id=\"test\" name=\"test\" required=\"required\"><option value=\"\">---</option><option value=\"1\">One</option><option selected=\"selected\" value=\"2\">Two</option><option value=\"3\">Three</option></select>", HTMLRenderingUtils.render(select.render()));

        select.setValue("not-present");

        assertHTMLEquals("<select id=\"test\" name=\"test\" required=\"required\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option><option value=\"3\">Three</option></select>", HTMLRenderingUtils.render(select.render()));

        //Perform validation:
        select.getValidationErrors();
        assertHTMLEquals("<select id=\"test\" name=\"test\" required=\"required\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option><option value=\"3\">Three</option></select><ul class=\"validation-errors\" data-id=\"test\"><li>Invalid value selected</li></ul>", HTMLRenderingUtils.render(select.render()));

        //Test single data
        select.setValue(Arrays.asList("1", "2"));
        assertEquals("1", select.getFirstValue());
        assertEquals(Arrays.asList("1"), select.getValue());

        //Options management
        assertEquals(select.getOptionsValues().size(), 4);
        assertTrue(select.hasValue("2"));
        select.removeOption("2");
        assertFalse(select.hasValue("2"));
        assertEquals(select.getOptionsValues().size(), 3);

        assertTrue(select.hasValueEnabled("1"));
        select.removeOption(new Option("1", "any"));
        assertFalse(select.hasValueEnabled("1"));
        select.addOption(new Option("1", "one", false));
        assertFalse(select.hasValueEnabled("1"));
        assertTrue(select.hasValue("1"));

        select.clearOptions();
        assertTrue(select.getOptions().isEmpty());
    }

    @Test
    public void testGroups() {
        Select select = new Select("test");
        select.addOption("1", "One");
        select.addOption("2", "Two");
        select.addOption(new Option("3", "Three", false));

        OptionGroup empty = new OptionGroup("empty", "Empty group");
        select.addOptionGroup(empty);

        OptionGroup g1 = new OptionGroup("g1", "Group one");
        g1.addOption(new Option("4", "Four"));
        select.addOptionGroup(g1);
        select.addOptionToGroup(new Option("5", "Five", false), "g1");
        select.addOptionToGroup("6", "Six", "g1");

        select.addOptionGroup(new OptionGroup("g2", "Group 2"));
        select.addOptionToGroup("7", "Seven", "g2");
        select.addOptionToGroup("8", "Eight", "g2");

        OptionGroup disabled = new OptionGroup("disabled", "Disabled group", false);
        disabled.addOption(new Option("9", "Nine"));
        select.addOptionGroup(disabled);

        assertEquals(
                Arrays.asList(
                        new Option("1", "One"),
                        new Option("2", "Two"),
                        new Option("3", "Three", false),
                        new Option("4", "Four"),
                        new Option("5", "Five", false),
                        new Option("6", "Six"),
                        new Option("7", "Seven"),
                        new Option("8", "Eight"),
                        new Option("9", "Nine")
                ),
                select.getOptions()
        );
        assertEquals(
                Arrays.asList(
                        new Option("1", "One"),
                        new Option("2", "Two"),
                        new Option("4", "Four"),
                        new Option("6", "Six"),
                        new Option("7", "Seven"),
                        new Option("8", "Eight")
                ),
                select.getEnabledOptions()
        );
        assertEquals(
                new HashSet<String>() {
            {
                add("1");
                add("2");
                add("3");
                add("4");
                add("5");
                add("6");
                add("7");
                add("8");
                add("9");
            }
        },
                select.getOptionsValues()
        );
        assertEquals(
                new HashSet<String>() {
            {
                add("1");
                add("2");
                add("4");
                add("6");
                add("7");
                add("8");
            }
        },
                select.getEnabledOptionValues()
        );

        assertHTMLEquals("<select id=\"test\" name=\"test\"><option value=\"1\">One</option><option value=\"2\">Two</option><option disabled value=\"3\">Three</option><optgroup id=\"test-g1\" label=\"Group one\"><option value=\"4\">Four</option><option disabled value=\"5\">Five</option><option value=\"6\">Six</option></optgroup><optgroup id=\"test-g2\" label=\"Group 2\"><option value=\"7\">Seven</option><option value=\"8\">Eight</option></optgroup><optgroup disabled id=\"test-disabled\" label=\"Disabled group\"><option value=\"9\">Nine</option></optgroup></select>", HTMLRenderingUtils.render(select.render()));

        //Replace all options:
        select.setOptions(new LinkedHashMap<String, String>() {
            {
                put("1", "uno");
                put("", "empty");
            }
        });
        assertHTMLEquals("<select id=\"test\" name=\"test\"><option value=\"1\">uno</option><option value=\"\">empty</option></select>", HTMLRenderingUtils.render(select.render()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoRepeatedOptions() {
        Select select = new Select("test");
        select.addOption("1", "One");
        select.addOption("2", "Two");
        select.addOption("1", "Uno");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullOption() {
        Select select = new Select("test");
        select.removeOption((Option) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullOption2() {
        Select select = new Select("test");
        select.addOption((Option) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullOption3() {
        Select select = new Select("test");
        select.addOptionToGroup((Option) null, "group");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullOptionGroup() {
        Select select = new Select("test");
        select.addOptionGroup(null);
    }

    @Test
    public void testAutoUnexistingOptionGroups() {
        Select select = new Select("test");
        select.addOptionToGroup("value1", "text1", "groupId1");
        select.addOptionToGroup("value2", "text2", "groupId2");
        assertEquals("<select id=\"test\" name=\"test\"><option value=\"value1\">text1</option><option value=\"value2\">text2</option></select>", HTMLRenderingUtils.render(select.render()));//Option groups without label don't show as groups
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoRepeatedOptionGroup() {
        Select select = new Select("test");
        select.addOptionToGroup("value1", "text1", "groupId1");
        select.addOptionToGroup("value2", "text2", "groupId2");
        select.addOptionGroup(new OptionGroup("groupId1", "repeated id group name"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoRepeatedValueInOptionGroup() {
        Select select = new Select("test");
        select.addOptionToGroup("value1", "text1", "groupId1");
        select.addOptionToGroup("value2", "text2", "groupId2");
        select.addOptionGroup(new OptionGroup("groupId3", "label").addOption("value1", "repeated"));
    }
}
