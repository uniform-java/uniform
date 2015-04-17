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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.uniform.api.Form;
import net.uniform.html.HTMLForm;
import net.uniform.impl.utils.HTMLRenderingUtils;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;

/**
 *
 * @author Eduardo Ramos
 */
public class CheckboxTest {
    
    @Test
    public void test() {
        Checkbox chk = new Checkbox("chk");
        chk.setProperty("name", "checkbox");
        chk.setChecked(true);
        
        assertTrue(chk.getConvertedValue());
        
        assertHTMLEquals("<input checked=\"checked\" id=\"chk\" name=\"checkbox\" type=\"checkbox\" value=\"true\"/>", HTMLRenderingUtils.render(chk.render()));
        chk.setProperty("value", "on");
        
        assertHTMLEquals("<input id=\"chk\" name=\"checkbox\" type=\"checkbox\" value=\"on\"/>", HTMLRenderingUtils.render(chk.render()));
        
        chk.setValue("on");
        
        assertHTMLEquals("<input checked=\"checked\" id=\"chk\" name=\"checkbox\" type=\"checkbox\" value=\"on\"/>", HTMLRenderingUtils.render(chk.render()));
        
        
        //Test data:
        chk.setValue("off");
        assertNotNull(chk.getConvertedValue());
        assertFalse(chk.getConvertedValue());
        
        Form form = new HTMLForm();
        form.addElement(chk);
        
        assertEquals(
                new HashMap<String, Object>(){{
                    put("checkbox", "off");
                }},
                form.getFormData()
        );
        
        assertEquals(
                new HashMap<String, Object>(){{
                    put("checkbox", false);
                }},
                form.getFormDataConvertedToElementValueTypes()
        );
        
        chk.setValue((String) null);
        
        assertEquals(
                new HashMap<String, Object>(){{
                    put("checkbox", null);
                }},
                form.getFormData()
        );
        
        assertEquals(
                new HashMap<String, Object>(){{
                    put("checkbox", false);
                }},
                form.getFormDataConvertedToElementValueTypes()
        );
        
        chk.setValue("on");
        
        assertEquals(
                new HashMap<String, Object>(){{
                    put("checkbox", true);
                }},
                form.getFormDataConvertedToElementValueTypes()
        );
    }
}
