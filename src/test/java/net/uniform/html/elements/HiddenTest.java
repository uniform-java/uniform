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

import java.util.HashMap;
import net.uniform.html.HTMLForm;
import net.uniform.impl.utils.HTMLRenderingUtils;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class HiddenTest {
    
    @Test
    public void test() {
        Hidden hidden = new Hidden("my-hidden");
        hidden.setValue("myHiddenValue");
        
        assertHTMLEquals("<input id=\"my-hidden\" name=\"my-hidden\" value=\"myHiddenValue\" type=\"hidden\"/>", HTMLRenderingUtils.render(hidden.render()));
        
        HTMLForm form = new HTMLForm();
        form.addElement(hidden, false);
        form.populateSimple(new HashMap<String, Object>(){{
            put("my-hidden", "populateValueIgnored");
        }});
        
        assertHTMLEquals("<form method=\"POST\"><input id=\"my-hidden\" name=\"my-hidden\" value=\"myHiddenValue\" type=\"hidden\"/></form>", form.renderHTML());
    }
}
