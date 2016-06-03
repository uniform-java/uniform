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
public class MulticheckboxTest {

    @Test
    public void test() {
        Multicheckbox multiCheck = new Multicheckbox("test");
        multiCheck.addOption("1", "One");
        multiCheck.addOption("2", "Two");
        multiCheck.addOption(" 3", "Three");//Trim test

        multiCheck.setValue(" 2 ");//trim test
        multiCheck.setRequired(true);

        multiCheck.addDecorator(new ElementErrorsDecorator());

        String originalSeparator = multiCheck.getSeparator();
        boolean originalPrepend = multiCheck.isPrependOptionLabels();
        boolean originalEscape = multiCheck.isEscapeOptionLabels();

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\">One</label> <label><input checked id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\">Three</label>", HTMLRenderingUtils.render(multiCheck.render()));

        //Test prepend
        multiCheck.setPrependOptionLabels(true);

        assertHTMLEquals("<label>One<input id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\"></label> <label>Two<input checked id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\"></label> <label>Three<input id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\"></label>", HTMLRenderingUtils.render(multiCheck.render()));

        multiCheck.setPrependOptionLabels(originalPrepend);

        //Test separator
        multiCheck.setSeparator("<br/>");

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\">One</label><br/><label><input checked id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\">Two</label><br/><label><input id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\">Three</label>", HTMLRenderingUtils.render(multiCheck.render()));

        multiCheck.setSeparator(originalSeparator);

        //Test validation
        multiCheck.setValue("not-present");

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\">Three</label>", HTMLRenderingUtils.render(multiCheck.render()));

        //Perform validation:
        multiCheck.getValidationErrors();
        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\">Three</label><ul class=\"validation-errors\" data-id=\"test\"><li>Invalid value selected</li></ul>", HTMLRenderingUtils.render(multiCheck.render()));

        //Test escape
        multiCheck.reset();

        multiCheck.addOption("4", "Four <i class=\"icon-test\"></i>");

        multiCheck.setEscapeOptionLabels(false);

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\">Three</label> <label><input id=\"test-4\" name=\"test\" type=\"checkbox\" value=\"4\">Four <i class=\"icon-test\"></i></label>", HTMLRenderingUtils.render(multiCheck.render()));

        multiCheck.setEscapeOptionLabels(originalEscape);

        assertHTMLEquals("<label><input id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\">Two</label> <label><input id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\">Three</label> <label><input id=\"test-4\" name=\"test\" type=\"checkbox\" value=\"4\">Four &lt;i class=\"icon-test\"&gt;&lt;/i&gt;</label>", HTMLRenderingUtils.render(multiCheck.render()));

        //Test multiple data
        multiCheck.setValue(Arrays.asList("1", "3"));
        assertEquals(Arrays.asList("1", "3"), multiCheck.getValue());

        //Test disabled options
        multiCheck.addOption(new Option("x", "Disabled", false));

        multiCheck.addOptionGroup(
                new OptionGroup("group", "Group", false)
                .addOption("y", "Group disabled 1")
                .addOption("z", "Group disabled 2")
        );

        assertHTMLEquals("<label><input checked id=\"test-1\" name=\"test\" type=\"checkbox\" value=\"1\">One</label> <label><input id=\"test-2\" name=\"test\" type=\"checkbox\" value=\"2\">Two</label> <label><input checked id=\"test-3\" name=\"test\" type=\"checkbox\" value=\"3\">Three</label> <label><input id=\"test-4\" name=\"test\" type=\"checkbox\" value=\"4\">Four &lt;i class=\"icon-test\"&gt;&lt;/i&gt;</label> <label><input disabled id=\"test-x\" name=\"test\" type=\"checkbox\" value=\"x\">Disabled</label> <label><input disabled id=\"test-y\" name=\"test\" type=\"checkbox\" value=\"y\">Group disabled 1</label> <label><input disabled id=\"test-z\" name=\"test\" type=\"checkbox\" value=\"z\">Group disabled 2</label>", HTMLRenderingUtils.render(multiCheck.render()));

        multiCheck.setValue(Arrays.asList("y", "z"));

        assertFalse(multiCheck.isValid());
    }

}
