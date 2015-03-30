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
package net.uniform.html.formvalidators;

import net.uniform.html.formvalidators.DateRangeValidator;
import java.util.HashMap;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.uniform.api.Form;
import net.uniform.html.HTMLForm;
import net.uniform.html.elements.DatePicker;
import net.uniform.html.elements.Input;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;

/**
 *
 * @author Eduardo Ramos
 */
public class DateRangeValidatorTest {
     @Test
    public void test(){
        Form form = new HTMLForm();
        
        form.addElement(new DatePicker("start").setLabel("Start date"));
        form.addElement(new DatePicker("end").setLabel("End date"));
        
        assertTrue(form.isValid());
        
         DateRangeValidator validator = new DateRangeValidator("start", "end");
        form.addValidator(validator);
        
        assertTrue(form.isValid());
        
        form.getElement("start").setRequired();
        
        assertFalse(form.isValid());
        
        form.populateSimple(new HashMap<String, Object>(){{
            put("start", "2015-03-28");
        }});
        
        assertTrue(form.isValid());
        
        form.populateSimple(new HashMap<String, Object>(){{
            put("end", "2015-03-27");
        }});
        
        assertFalse(form.isValid());
         assertHTMLEquals("<form method=\"POST\"><ul class=\"form-validation-errors\"><li>Invalid date range for 'Start date' and 'End date'</li></ul><label class=\"element-label\" for=\"start\">Start date</label><input id=\"start\" name=\"start\" required=\"required\" type=\"date\" value=\"2015-03-28\"><label class=\"element-label\" for=\"end\">End date</label><input id=\"end\" name=\"end\" type=\"date\" value=\"2015-03-27\"></form>", form.renderHTML());
         
         
        form.populateSimple(new HashMap<String, Object>(){{
            put("end", "2015-03-28");
        }});
        assertTrue(form.isValid());
        
        validator.setAllowSameDate(false);
        
        assertFalse(form.isValid());
        assertFalse(form.getValidationErrors().isEmpty());
        
         assertHTMLEquals("<form method=\"POST\"><ul class=\"form-validation-errors\"><li>Same date is not allowed for 'Start date' and 'End date'</li></ul><label class=\"element-label\" for=\"start\">Start date</label><input id=\"start\" name=\"start\" required=\"required\" type=\"date\" value=\"2015-03-28\"><label class=\"element-label\" for=\"end\">End date</label><input id=\"end\" name=\"end\" type=\"date\" value=\"2015-03-28\"></form>", form.renderHTML());
         
         form.populateSimple(new HashMap<String, Object>(){{
            put("end", "2015-03-29");
        }});
        assertTrue(form.isValid());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInvalid(){
        Form form = new HTMLForm();
        
        form.addElement(new Input("start").setLabel("Start date"));
        form.addElement(new DatePicker("end").setLabel("End date"));
        
         DateRangeValidator validator = new DateRangeValidator("start", "end");
        form.addValidator(validator);
        
        form.isValid();
    }
}
