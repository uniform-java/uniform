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
package net.uniform.html;

import java.util.HashMap;
import java.util.Map;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.impl.utils.HTMLRenderingUtils;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;

/**
 *
 * @author Eduardo Ramos
 */
public class HTMLRenderingUtilsTest {

    @org.junit.Test
    public void testRenderTag() {
        Map<String, String> properties = new HashMap<>();
        properties.put("id", "test");
        properties.put("name", "abc");
        properties.put("data-event", "event");
        
        SimpleHTMLTag input = new SimpleHTMLTag("input", properties);
        assertHTMLEquals("<input data-event=\"event\" id=\"test\" name=\"abc\"/>", HTMLRenderingUtils.render(input));
        
        SimpleHTMLTag textarea = new SimpleHTMLTag("textarea", properties, "TESTING");
        assertHTMLEquals("<textarea data-event=\"event\" id=\"test\" name=\"abc\">TESTING</textarea>", HTMLRenderingUtils.render(textarea));
        
        SimpleHTMLTag arbitraryContent = new SimpleHTMLTag(null, "<div>arbitrary content</div>");
        arbitraryContent.setEscapeContent(false);
        assertHTMLEquals("<div>arbitrary content</div>", HTMLRenderingUtils.render(arbitraryContent));
        
        SimpleHTMLTag arbitraryElementList = new SimpleHTMLTag();
        arbitraryElementList.addSubTag(input);
        arbitraryElementList.addSubTag(textarea);
        arbitraryElementList.addSubTag(arbitraryContent);
        assertHTMLEquals(
                HTMLRenderingUtils.render(input)+HTMLRenderingUtils.render(textarea)+HTMLRenderingUtils.render(arbitraryContent), 
                HTMLRenderingUtils.render(arbitraryElementList)
        );
    }
    
}
