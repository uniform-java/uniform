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
import net.uniform.html.decorators.ElementErrorsDecorator;
import net.uniform.impl.utils.HTMLRenderingUtils;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class MultiselectTest {

    @Test
    public void test() {
        Multiselect select = new Multiselect("test");
        select.addOption("", "---");
        select.addOption("1", "One");
        select.addOption("2", "Two");
        select.addOption("3", "Three");

        List<String> validValue = Arrays.asList("1", "3");
        select.setValue(validValue);
        select.setRequired(true);

        assertEquals(validValue, select.getValue());

        select.addDecorator(new ElementErrorsDecorator());

        assertHTMLEquals("<select id=\"test\" multiple=\"multiple\" name=\"test\" required=\"required\"><option value=\"\">---</option><option selected=\"selected\" value=\"1\">One</option><option value=\"2\">Two</option><option selected=\"selected\" value=\"3\">Three</option></select>", HTMLRenderingUtils.render(select.render()));

        select.setValue("not-present");
        assertEquals(Arrays.asList("not-present"), select.getValue());

        assertHTMLEquals("<select id=\"test\" multiple=\"multiple\" name=\"test\" required=\"required\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option><option value=\"3\">Three</option></select>", HTMLRenderingUtils.render(select.render()));

        select.getValidationErrors();//Perform validation
        assertHTMLEquals("<select id=\"test\" multiple=\"multiple\" name=\"test\" required=\"required\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option><option value=\"3\">Three</option></select><ul class=\"validation-errors\" data-id=\"test\"><li>Invalid value selected</li></ul>", HTMLRenderingUtils.render(select.render()));

        select.reset();//Clear state
        select.setValue(Arrays.asList("1", "2", "2"));//Valid but repeated value

        select.getValidationErrors();//Perform validation
        assertHTMLEquals("<select id=\"test\" multiple=\"multiple\" name=\"test\" required=\"required\"><option value=\"\">---</option><option value=\"1\" selected>One</option><option value=\"2\" selected>Two</option><option value=\"3\">Three</option></select><ul class=\"validation-errors\" data-id=\"test\"><li>Values cannot be repeated</li></ul>", HTMLRenderingUtils.render(select.render()));

        //Test multi data
        select.setValue(Arrays.asList("1", "2"));
        assertEquals("1", select.getFirstValue());
        assertEquals(Arrays.asList("1", "2"), select.getValue());
    }

}
