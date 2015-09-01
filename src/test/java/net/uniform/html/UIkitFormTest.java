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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import static junit.framework.Assert.assertFalse;
import net.uniform.api.Element;
import net.uniform.html.elements.Button;
import net.uniform.html.elements.DatePicker;
import net.uniform.html.elements.HTMLElement;
import net.uniform.html.elements.Input;
import net.uniform.html.elements.Select;
import net.uniform.html.uikit.UIkitButton;
import net.uniform.html.uikit.UIkitForm;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class UIkitFormTest {

    @Test
    public void testRender() {
        UIkitForm form = new UIkitForm();
        
        form.addClass("uk-form-stacked");
        
        HTMLElement header = new HTMLElement("header", "h2");
        header.setProperty("class", "header-class");
        header.setContent("Unescaped content <i class=\"uk-icon-user\"></i>");
        form.addElement(header);

        //Input
        Input input = new Input("field1");
        input.setLabel("Field 1");
        input.setProperty("class", "class");
        input.addClass("class2");
        input.setValue("value1");
        input.setRequired(true);
        form.addElement(input);

        //Select
        Select select = new Select("selectId");
        select.setLabel("Field 2");
        select.addOption("", "---");
        select.addOption("1", "One");
        select.addOption("2", "Two");
        select.setProperty("data-id", select.getId());
        select.setProperty("id", "field2");

        form.addElement(select);
        
        final Date now = new Date();
        
        DatePicker datePicker = new DatePicker("date");
        datePicker.setLabel("Some date (optional)");
        datePicker.setValue(now);
        form.addElement(datePicker);
        
        final SimpleDateFormat sdf = datePicker.getDateFormat();
        
        Button submit = new UIkitButton("submit", Button.BUTTON_TYPE_SUBMIT);
        submit.addClass("uk-button-primary");
        submit.setEscape(false);
        submit.setLabel("Submit");
        
        form.addElement(submit);
        
        //Add some validation errors:
        form.setElementValue("selectId", "invalid-value");
        
        assertEquals(new HashMap<String, String>(){{
            put("field1", "value1");
            put("selectId", "invalid-value");
            put("date", sdf.format(now));
        }}, form.getFormData());
        
        assertFalse(form.isValid());
        
        assertHTMLEquals("<form class=\"uk-form uk-form-stacked\" method=\"POST\"><h2 class=\"header-class\">Unescaped content <i class=\"uk-icon-user\"></i></h2><div class=\"uk-form-row\"><label class=\"uk-form-label\" for=\"field1\">Field 1</label><div class=\"uk-form-controls\"><input class=\"class class2\" id=\"field1\" name=\"field1\" required=\"required\" type=\"text\" value=\"value1\"></div></div><div class=\"uk-form-row\"><label class=\"uk-form-label\" for=\"field2\">Field 2</label><div class=\"uk-form-controls\"><select class=\" uk-form-danger\" data-id=\"selectId\" id=\"field2\" name=\"selectId\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option></select></div><ul class=\"uk-text-danger\" data-id=\"field2\"><li>Invalid value selected</li></ul></div><div class=\"uk-form-row\"><label class=\"uk-form-label\" for=\"date\">Some date (optional)</label><div class=\"uk-form-controls\"><input data-uk-datepicker=\"{weekstart:0, format:'YYYY-MM-DD'}\" id=\"date\" name=\"date\" type=\"text\" value=\""+sdf.format(now)+"\"></div></div><button class=\"uk-button uk-button-primary\" id=\"submit\" type=\"submit\">Submit</button></form>", form.renderHTML());
    }
    
    @Test
    public void testDefaults(){
        UIkitForm form = new UIkitForm();
        
        //Decorators
        assertNotNull(form.getDefaultDecoratorsForElementClass(Element.class));
        
        assertNotNull(form.getDefaultDecoratorsForElementClass(Button.class));
        assertEquals(0, form.getDefaultDecoratorsForElementClass(Button.class).size());
        
        assertNotNull(form.getDefaultDecoratorsAppliedToElementClass(Button.class));
        assertEquals(0, form.getDefaultDecoratorsAppliedToElementClass(Button.class).size());
        
        assertNotNull(form.getDefaultDecoratorsForElementClass(Element.class));
        assertNotNull(form.getDefaultDecoratorsAppliedToElementClass(Input.class));
        assertNotNull(form.getDefaultDecoratorsAppliedToElementClass(Select.class));
        
        //Renderers
        assertNull(form.getDefaultRendererForElementClass(Element.class));
        assertNull(form.getDefaultRendererForElementClass(Input.class));
        assertNull(form.getDefaultRendererForElementClass(Select.class));
        assertNotNull(form.getDefaultRendererForElementClass(DatePicker.class));
        assertNotNull(form.getDefaultRendererAppliedToElementClass(DatePicker.class));
    }
    
    @Test
    public void testAddWithoutDefaultDecorators(){
        UIkitForm form = new UIkitForm();
        
        //Input
        Input input1 = new Input("field1");
        input1.setLabel("Field 1");
        input1.setProperty("class", "class");
        input1.addClass("class1");
        input1.setValue("value1");
        input1.setRequired(true);
        
        //Input
        Input input2 = new Input("field2");
        input2.setLabel("Field 2");
        input2.setProperty("class", "class");
        input2.addClass("class2");
        input2.setValue("value2");
        input2.setRequired(true);
        
        form.addElements(false, input1, input2);
        
        assertHTMLEquals("<form class=\"uk-form\" method=\"POST\"><input class=\"class class1\" id=\"field1\" name=\"field1\" required=\"required\" type=\"text\" value=\"value1\"><input class=\"class class2\" id=\"field2\" name=\"field2\" required=\"required\" type=\"text\" value=\"value2\"></form>", form.renderHTML());
        
        form.removeElement("field1");
        
        assertHTMLEquals("<form class=\"uk-form\" method=\"POST\"><input class=\"class class2\" id=\"field2\" name=\"field2\" required=\"required\" type=\"text\" value=\"value2\"></form>", form.renderHTML());
        form.removeElement(input2);
        
        assertHTMLEquals("<form class=\"uk-form\" method=\"POST\"></form>", form.renderHTML());
        
        form.addElement(input2, false);
        form.addElement(input1, false);
        
        assertHTMLEquals("<form class=\"uk-form\" method=\"POST\"><input class=\"class class2\" id=\"field2\" name=\"field2\" required=\"required\" type=\"text\" value=\"value2\"><input class=\"class class1\" id=\"field1\" name=\"field1\" required=\"required\" type=\"text\" value=\"value1\"></form>", form.renderHTML());
        
    }
}
