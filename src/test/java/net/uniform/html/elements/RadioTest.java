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
import net.uniform.api.html.Option;
import net.uniform.api.html.OptionGroup;
import net.uniform.html.decorators.ElementErrorsDecorator;
import net.uniform.impl.utils.HTMLRenderingUtils;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class RadioTest {

    @Test
    public void test() {
        Radio radio = new Radio("test");
        radio.addOption("1", "One");
        radio.addOption("2", "Two");
        radio.addOption("3", "Three");

        radio.setValue(" 2 ");//trim test
        radio.setRequired(true);

        radio.addDecorator(new ElementErrorsDecorator());

        String originalSeparator = radio.getSeparator();
        boolean originalPrepend = radio.isPrependOptionLabels();
        boolean originalEscape = radio.isEscapeOptionLabels();

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label> <label><input checked id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label>", HTMLRenderingUtils.render(radio.render()));

        //Test prepend
        radio.setPrependOptionLabels(true);

        assertHTMLEquals("<label>One<input id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\"></label> <label>Two<input checked id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\"></label> <label>Three<input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\"></label>", HTMLRenderingUtils.render(radio.render()));

        radio.setPrependOptionLabels(originalPrepend);

        //Test separator
        radio.setSeparator("<br/>");

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label><br/><label><input checked id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label><br/><label><input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label>", HTMLRenderingUtils.render(radio.render()));

        radio.setSeparator(originalSeparator);

        //Test validation
        radio.setValue("not-present");

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label>", HTMLRenderingUtils.render(radio.render()));

        //Perform validation:
        radio.getValidationErrors();
        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label><ul class=\"validation-errors\" data-id=\"test\"><li>Invalid value selected</li></ul>", HTMLRenderingUtils.render(radio.render()));

        //Test escape
        radio.reset();

        radio.addOption("4", "Four <i class=\"icon-test\"></i>");

        radio.setEscapeOptionLabels(false);

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label> <label><input id=\"test-4\" name=\"test\" required=\"required\" type=\"radio\" value=\"4\">Four <i class=\"icon-test\"></i></label>", HTMLRenderingUtils.render(radio.render()));

        radio.setEscapeOptionLabels(originalEscape);

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label> <label><input id=\"test-4\" name=\"test\" required=\"required\" type=\"radio\" value=\"4\">Four &lt;i class=\"icon-test\"&gt;&lt;/i&gt;</label>", HTMLRenderingUtils.render(radio.render()));

        //Test single data
        radio.setValue(Arrays.asList("1", "2"));
        assertEquals("1", radio.getFirstValue());
        assertEquals(Arrays.asList("1"), radio.getValue());

        //Test disabled options
        radio.addOption(new Option("x", "Disabled", false));

        radio.addOptionGroup(
                new OptionGroup("group", "Group", false)
                .addOption("y", "Group disabled 1")
                .addOption("z", "Group disabled 2")
        );

        assertHTMLEquals("<label><input checked id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label> <label><input id=\"test-4\" name=\"test\" required=\"required\" type=\"radio\" value=\"4\">Four &lt;i class=\"icon-test\"&gt;&lt;/i&gt;</label> <label><input disabled id=\"test-x\" name=\"test\" required=\"required\" type=\"radio\" value=\"x\">Disabled</label> <label><input disabled id=\"test-y\" name=\"test\" required=\"required\" type=\"radio\" value=\"y\">Group disabled 1</label> <label><input disabled id=\"test-z\" name=\"test\" required=\"required\" type=\"radio\" value=\"z\">Group disabled 2</label>", HTMLRenderingUtils.render(radio.render()));

        radio.setValue("x");

        assertFalse(radio.isValid());

        //All disabled:
        radio.reset();
        radio.setProperty("disabled", "disabled");

        assertHTMLEquals("<label><input disabled id=\"test-1\" name=\"test\" required=\"required\" type=\"radio\" value=\"1\">One</label> <label><input disabled id=\"test-2\" name=\"test\" required=\"required\" type=\"radio\" value=\"2\">Two</label> <label><input disabled id=\"test-3\" name=\"test\" required=\"required\" type=\"radio\" value=\"3\">Three</label> <label><input disabled id=\"test-4\" name=\"test\" required=\"required\" type=\"radio\" value=\"4\">Four &lt;i class=\"icon-test\"&gt;&lt;/i&gt;</label> <label><input disabled id=\"test-x\" name=\"test\" required=\"required\" type=\"radio\" value=\"x\">Disabled</label> <label><input disabled id=\"test-y\" name=\"test\" required=\"required\" type=\"radio\" value=\"y\">Group disabled 1</label> <label><input disabled id=\"test-z\" name=\"test\" required=\"required\" type=\"radio\" value=\"z\">Group disabled 2</label>", HTMLRenderingUtils.render(radio.render()));
    }

}
