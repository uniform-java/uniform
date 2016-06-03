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
import java.util.HashMap;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.exceptions.UniformException;
import net.uniform.html.HTMLForm;
import net.uniform.html.decorators.HTMLTagDecorator;
import net.uniform.impl.utils.HTMLRenderingUtils;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class HTMLElementTest {

    @Test
    public void test() {
        HTMLElement element = new HTMLElement("test", new SimpleHTMLTag("div"));

        assertNull(element.getValueType());

        element.setValue(Arrays.asList("1"));
        element.setValue("1");
        element.populate(Arrays.asList("1"));
        assertNull(element.getValue());

        assertEquals("div", element.getName());

        element.setName("span");
        assertEquals("span", element.getName());

        assertNull(element.getContent());

        element.setContent("Content");
        assertEquals("Content", element.getContent());

        assertTrue(element.getSubTags() == null || element.getSubTags().isEmpty());

        element.setContent(null);
        element.addSubTag(new SimpleHTMLTag("p").setContent("Subtag 1 content"));
        element.addSubTag(new SimpleHTMLTag("h4").setContent("Subtag 2 content"));

        assertHTMLEquals("<span><p>Subtag 1 content</p><h4>Subtag 2 content</h4></span>", HTMLRenderingUtils.render(element.render()));

        element.setProperty("class", "test-class");
        element.setProperty("id", "test-id");

        assertEquals("test-id", element.getProperty("id"));

        HashMap<String, String> props = new HashMap<String, String>() {
            {
                put("id", "test-id");
                put("class", "test-class");
            }
        };
        assertEquals(props, element.getProperties());

        assertHTMLEquals("<span id=\"test-id\" class=\"test-class\"><p>Subtag 1 content</p><h4>Subtag 2 content</h4></span>", HTMLRenderingUtils.render(element.render()));

        props.put("data-test", "\"other value\"");
        element.setProperties(props);

        assertEquals(props, element.getProperties());

        assertTrue(element.isEscapeContent());
        element.setEscapeContent(false);
        assertFalse(element.isEscapeContent());

        element.setContent("<p>Don't escape me</p>");

        assertHTMLEquals("<span class=\"test-class\" data-test=\"&quot;other value&quot;\" id=\"test-id\"><p>Don't escape me</p></span>", HTMLRenderingUtils.render(element.render()));

        assertTrue(element.getValidationErrors() == null || element.isValid());

        //Other element:
        HTMLElement element2 = new HTMLElement("test-2");
        element2.setContent("Tag content simple");

        assertHTMLEquals("Tag content simple", HTMLRenderingUtils.render(element2.render()));
    }

    @Test
    public void testNoFormDefaultDecorators() {
        HTMLForm form = new HTMLForm();

        HTMLElement element = new HTMLElement("test", new SimpleHTMLTag("span").setContent("Content"));

        //No default decorators should be added here for this type of element:        
        form.addElement(element);
        assertHTMLEquals("<form method='POST'><span>Content</span></form>", form.renderHTML());

        //Actually add a decorator
        element.addDecorator(new HTMLTagDecorator("div"));
        assertHTMLEquals("<form method='POST'><div><span>Content</span></div></form>", form.renderHTML());
    }

    @Test(expected = UniformException.class)
    public void testUnsupportedTypeChange() {
        HTMLElement element = new HTMLElement("test", new SimpleHTMLTag("div"));
        element.setValueType(String.class);
    }
}
