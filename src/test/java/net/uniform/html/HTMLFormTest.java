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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.uniform.api.Decorator;
import net.uniform.api.Element;
import net.uniform.api.Form;
import net.uniform.api.TranslationEngineContext;
import net.uniform.exceptions.UniformException;
import net.uniform.html.beans.FormBeanGettersAndSetters;
import net.uniform.html.beans.FormBeanMixed;
import net.uniform.html.beans.FormBeanPublic;
import net.uniform.html.beans.FormBeanPublic2;
import net.uniform.html.beans.FormBeanPublic3;
import net.uniform.html.decorators.HTMLTagDecorator;
import net.uniform.html.decorators.LabelDecorator;
import net.uniform.html.elements.Checkbox;
import net.uniform.html.elements.Input;
import net.uniform.html.elements.Multiselect;
import net.uniform.html.elements.Select;
import net.uniform.impl.translation.SimpleTranslationEngine;
import static net.uniform.testutils.HTMLTest.assertHTMLEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class HTMLFormTest {

    private HTMLForm form;

    @Before
    public void setUp() {
        form = new HTMLForm();

        form.startDecorator("superSuperGroup", new HTMLTagDecorator("div", new HashMap<String, Object>() {
            {
                this.put("id", "superSuperGroup");
            }
        }));

        form.startDecorator("superGroup", new HTMLTagDecorator("fieldset", new HashMap<String, Object>() {
            {
                this.put("id", "sg");
            }
        }));
        
        Decorator superGroupDecorator = form.getDecorator("superGroup");
        assertNotNull(superGroupDecorator);
        
        superGroupDecorator.setProperty("id", "superGroup");

        //Input
        Input input = new Input("field1");
        input.setProperty("name", "inputName");
        input.setLabel("Some Input <i class=\"icon-unescaped-test\"></i>");
        input.setProperty("class", "class");
        input.setValue("To be replaced");
        form.addElement(input);

        input.setDecoratorProperty(LabelDecorator.class, LabelDecorator.PROPERTY_ESCAPE, false);
        input.setDecoratorProperty(LabelDecorator.class, "class", "test-label-class");

        form.startDecorator("group1", new HTMLTagDecorator("div", new HashMap<String, Object>() {
            {
                this.put("id", "group1");
                this.put("class", "class1");
                this.put("data-abc", "abc");
            }
        }));

        //Select
        Select select = new Select("selectId");
        select.setProperty("name", "selectName");
        select.setLabel("Some Input <i class=\"icon-escaped-test\"></i>");
        select.addOption("", "---");
        select.addOption("1", "One");
        select.addOption("2", "Two");
        select.setProperty("data-id", select.getId());
        select.setProperty("id", "field2");
        select.setValueType(Integer.class);

        //Multiselect
        Multiselect multi = new Multiselect("multi");
        multi.setLabel("Multi test");
        multi.addOption("1", "One");
        multi.addOption("2", "Two");
        multi.addOption("3", "Three");
        multi.addOption("4", "Four");
        multi.setValueType(Long.class);

        form.addElements(select, multi);

        select.setDecoratorProperty(LabelDecorator.class, LabelDecorator.PROPERTY_ESCAPE, true);
        select.setDecoratorProperty(LabelDecorator.class, "class", "test-label-class");

        form.endDecorator();

        multi.setDecoratorProperty(LabelDecorator.class, LabelDecorator.PROPERTY_ESCAPE, true);
        multi.setDecoratorProperty(LabelDecorator.class, "class", "test-label-class");

        form.startDecorator("group2", new HTMLTagDecorator("div", new HashMap<String, Object>() {
            {
                this.put("id", "group2");
                this.put("class", "class2");
            }
        }));

        //Checkbox
        Checkbox checkbox = new Checkbox("chk");
        checkbox.setLabel("CHK");

        assertFalse(checkbox.isChecked());
        checkbox.setChecked(true);
        assertTrue(checkbox.isChecked());

        form.addElement(checkbox);

        checkbox.setDecoratorProperty(LabelDecorator.class, LabelDecorator.PROPERTY_ESCAPE, true);
        checkbox.setDecoratorProperty(LabelDecorator.class, "class", "test-label-class");
        checkbox.setDecoratorProperty(LabelDecorator.class, LabelDecorator.PROPERTY_PREPEND, false);

        form.endDecorator();//Group 2

        form.endDecorator();//Super group

        form.endDecorator();//Super super group

        form.populateSimple(new HashMap<String, Object>() {
            {
                put("inputName", "Testing...");
                put("selectName", null);
                put("multi", Arrays.asList("1", "4"));
                put("chk", Arrays.asList("true"));
            }
        });
        
        Map<String, Element> expectedElementsMap = new HashMap<>();
        expectedElementsMap.put("field1", input);
        expectedElementsMap.put("selectId", select);
        expectedElementsMap.put("multi", multi);
        expectedElementsMap.put("chk", checkbox);
        assertEquals(expectedElementsMap, form.getElements());
    }

    @Test
    public void testRender() {
        assertHTMLEquals("<form method=\"POST\"><div id=\"superSuperGroup\"><fieldset id=\"superGroup\"><label class=\"test-label-class\" for=\"field1\">Some Input <i class=\"icon-unescaped-test\"></i></label><input class=\"class\" id=\"field1\" name=\"inputName\" type=\"text\" value=\"Testing...\"/><div class=\"class1\" data-abc=\"abc\" id=\"group1\"><label class=\"test-label-class\" for=\"field2\">Some Input &lt;i class=\"icon-escaped-test\"&gt;&lt;/i&gt;</label><select data-id=\"selectId\" id=\"field2\" name=\"selectName\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option></select><label class=\"test-label-class\" for=\"multi\">Multi test</label><select id=\"multi\" multiple=\"multiple\" name=\"multi\"><option selected=\"selected\" value=\"1\">One</option><option value=\"2\">Two</option><option value=\"3\">Three</option><option selected=\"selected\" value=\"4\">Four</option></select></div><div class=\"class2\" id=\"group2\"><input checked=\"checked\" id=\"chk\" name=\"chk\" type=\"checkbox\" value=\"true\"/><label class=\"test-label-class\" for=\"chk\">CHK</label></div></fieldset></div></form>", form.renderHTML());

        //Now test removing all decorators:
        Map<String, Decorator> decorators = form.getDecorators();
        for (String decoratorId : decorators.keySet()) {
            form.removeDecorator(decoratorId);
        }

        //Normal form
        form.setProperty("method", HTMLForm.METHOD_GET);
        assertHTMLEquals("<form method=\"GET\"><label class=\"test-label-class\" for=\"field1\">Some Input <i class=\"icon-unescaped-test\"></i></label><input class=\"class\" id=\"field1\" name=\"inputName\" type=\"text\" value=\"Testing...\"/><label class=\"test-label-class\" for=\"field2\">Some Input &lt;i class=\"icon-escaped-test\"&gt;&lt;/i&gt;</label><select data-id=\"selectId\" id=\"field2\" name=\"selectName\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option></select><label class=\"test-label-class\" for=\"multi\">Multi test</label><select id=\"multi\" multiple=\"multiple\" name=\"multi\"><option selected=\"selected\" value=\"1\">One</option><option value=\"2\">Two</option><option value=\"3\">Three</option><option selected=\"selected\" value=\"4\">Four</option></select><input checked=\"checked\" id=\"chk\" name=\"chk\" type=\"checkbox\" value=\"true\"/><label class=\"test-label-class\" for=\"chk\">CHK</label></form>", form.renderHTML());
        form.setProperty("method", HTMLForm.METHOD_POST);
        assertHTMLEquals("<form method=\"POST\"><label class=\"test-label-class\" for=\"field1\">Some Input <i class=\"icon-unescaped-test\"></i></label><input class=\"class\" id=\"field1\" name=\"inputName\" type=\"text\" value=\"Testing...\"/><label class=\"test-label-class\" for=\"field2\">Some Input &lt;i class=\"icon-escaped-test\"&gt;&lt;/i&gt;</label><select data-id=\"selectId\" id=\"field2\" name=\"selectName\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option></select><label class=\"test-label-class\" for=\"multi\">Multi test</label><select id=\"multi\" multiple=\"multiple\" name=\"multi\"><option selected=\"selected\" value=\"1\">One</option><option value=\"2\">Two</option><option value=\"3\">Three</option><option selected=\"selected\" value=\"4\">Four</option></select><input checked=\"checked\" id=\"chk\" name=\"chk\" type=\"checkbox\" value=\"true\"/><label class=\"test-label-class\" for=\"chk\">CHK</label></form>", form.renderHTML());

        //Form with validation errors:
        form.getElement("selectId").setRequired(true);
        form.getElement("multi").setValue(Arrays.asList("a", "b", "c", "3"));
        assertFalse(form.isValid());
        assertHTMLEquals("<form method=\"POST\"><label class=\"test-label-class\" for=\"field1\">Some Input <i class=\"icon-unescaped-test\"></i></label><input class=\"class\" id=\"field1\" name=\"inputName\" type=\"text\" value=\"Testing...\"/><label class=\"test-label-class\" for=\"field2\">Some Input &lt;i class=\"icon-escaped-test\"&gt;&lt;/i&gt;</label><select data-id=\"selectId\" id=\"field2\" name=\"selectName\" required=\"required\"><option value=\"\">---</option><option value=\"1\">One</option><option value=\"2\">Two</option></select><ul class=\"validation-errors\" data-id=\"field2\"><li>A value is required</li></ul><label class=\"test-label-class\" for=\"multi\">Multi test</label><select id=\"multi\" multiple=\"multiple\" name=\"multi\"><option value=\"1\">One</option><option value=\"2\">Two</option><option selected=\"selected\" value=\"3\">Three</option><option value=\"4\">Four</option></select><ul class=\"validation-errors\" data-id=\"multi\"><li>Invalid value selected</li></ul><input checked=\"checked\" id=\"chk\" name=\"chk\" type=\"checkbox\" value=\"true\"/><label class=\"test-label-class\" for=\"chk\">CHK</label></form>", form.renderHTML());
    }

    @Test
    public void testData() {
        //Form data all lists:
        assertEquals(new HashMap<String, List<String>>() {
            {
                put("inputName", Arrays.asList("Testing..."));
                put("selectName", null);
                put("multi", Arrays.asList("1", "4"));
                put("chk", Arrays.asList("true"));
            }
        }, form.getFormDataMultivalue());

        //Form data without all lists:
        assertEquals(new HashMap<String, Object>() {
            {
                put("inputName", "Testing...");
                put("selectName", null);
                put("multi", Arrays.asList("1", "4"));
                put("chk", "true");
            }
        }, form.getFormData());

        //Data types conversion:
        Select select = (Select) form.getElement("selectId").setValue("2");
        form.getElement("chk").setValue("");

        assertEquals(new HashMap<String, Object>() {
            {
                put("inputName", "Testing...");
                put("selectName", 2);
                put("multi", Arrays.asList(1L, 4L));
                put("chk", false);
            }
        }, form.getFormDataConvertedToElementValueTypes());

        Multiselect multi = (Multiselect) form.getElement("multi");
        select.setValueType(Double.class);
        multi.setValueType(BigDecimal.class);

        assertEquals(new HashMap<String, Object>() {
            {
                put("inputName", "Testing...");
                put("selectName", 2.0);
                put("multi", Arrays.asList(new BigDecimal(1), new BigDecimal(4)));
                put("chk", false);
            }
        }, form.getFormDataConvertedToElementValueTypes());

        assertEquals(Arrays.asList("1", "4"), form.getElementValue("multi"));
        assertEquals(Arrays.asList("1", "4"), form.getElementMultivalue("multi"));
        assertEquals(Arrays.asList(new BigDecimal(1), new BigDecimal(4)), form.getElementValueConvertedToValueType("multi"));

        //Test that a null value should be an empty list:
        form.setElementValue("multi", (String) null);

        form.getFormDataConvertedToElementValueTypes();

        assertEquals(new HashMap<String, Object>() {
            {
                put("inputName", "Testing...");
                put("selectName", 2.0);
                put("multi", new ArrayList<BigDecimal>());
                put("chk", false);
            }
        }, form.getFormDataConvertedToElementValueTypes());

        assertEquals("2", form.getElementValue("selectId"));
        assertEquals(Arrays.asList("2"), form.getElementMultivalue("selectId"));
        assertEquals(2.0, form.getElementValueConvertedToValueType("selectId"));

        assertEquals(null, form.getElementValue("multi"));
        assertEquals(null, form.getElementMultivalue("multi"));
        assertEquals(new ArrayList<String>(), form.getElementValueConvertedToValueType("multi"));
    }

    @Test(expected = UniformException.class)
    public void testTypeConversionError() {
        Input input = (Input) form.getElement("field1");
        input.setValueType(Integer.class);
        form.getFormDataConvertedToElementValueTypes();
    }

    @Test
    public void testPopulateBean() {
        form.reset();

        FormBeanGettersAndSetters bean = new FormBeanGettersAndSetters("1", "2", Arrays.asList("3"), false);
        form.populateBean(bean);

        assertEquals(new HashMap<String, Object>() {
            {
                put("inputName", "1");
                put("selectName", "2");
                put("multi", Arrays.asList("3"));
                put("chk", "false");
            }
        }, form.getFormData());

        FormBeanPublic bean2 = new FormBeanPublic("4", "5", "6", true);
        form.populateBean(bean2);

        assertEquals(new HashMap<String, Object>() {
            {
                put("inputName", "4");
                put("selectName", "5");
                put("multi", Arrays.asList("6"));
                put("chk", "true");
            }
        }, form.getFormData());
    }

    
    @Test
    public void testGetFormDataIntoBean() {
        form.reset();

        form.setElementValue("field1", "1");
        form.setElementValue("selectId", "2");
        form.setElementValue("multi", Arrays.asList("3"));
        form.setElementValue("chk", false);
        

        FormBeanGettersAndSetters expectedBean1 = new FormBeanGettersAndSetters("1", "2", null, false);//Field 2 is compatible with an string conversion but generic list type of field 3 (String) is not compatible with the form type (Long)
        FormBeanPublic expectedBean2 = new FormBeanPublic("1", "2", null, false);//Note field 3 is not of the same type
        FormBeanMixed expectedBean3 = new FormBeanMixed("1", 2, Arrays.asList((Number) 3L), false);//Every field is type compatible (superclass list type)
        FormBeanPublic2 expectedBean4 = new FormBeanPublic2("1", 2, Arrays.asList(3L), false);//Every field is type compatible (raw list type)
        FormBeanPublic3 expectedBean5 = new FormBeanPublic3("1", 2, Arrays.asList(3L), false);//Every field is type compatible (same class list type)
        
        FormBeanGettersAndSetters resultBean1 = new FormBeanGettersAndSetters(null, null, null, true);
        FormBeanPublic resultBean2 = new FormBeanPublic(null, null, null, true);
        FormBeanMixed resultBean3 = new FormBeanMixed(null, null, null, true);
        FormBeanPublic2 resultBean4 = new FormBeanPublic2(null, null, null, true);
        FormBeanPublic3 resultBean5 = new FormBeanPublic3(null, null, null, true);
        
        
        form.getFormDataIntoBean(resultBean1);
        form.getFormDataIntoBean(resultBean2);
        form.getFormDataIntoBean(resultBean3);
        form.getFormDataIntoBean(resultBean4);
        form.getFormDataIntoBean(resultBean5);
        
        assertEquals(expectedBean1, resultBean1);
        assertEquals(expectedBean2, resultBean2);
        assertEquals(expectedBean3, resultBean3);
        assertEquals(expectedBean4, resultBean4);
        assertEquals(expectedBean5, resultBean5);
    }
    
    @Test
    public void testDefaults() {
        assertNotNull(form.getDefaultDecoratorsForElementClass(Element.class));
        assertNotNull(form.getDefaultDecoratorsAppliedToElementClass(Input.class));
        assertNotNull(form.getDefaultDecoratorsAppliedToElementClass(Select.class));

        List<Decorator> original = form.getDefaultDecoratorsForElementClass(Element.class);
        form.removeDefaultDecoratorsForElementClass(Element.class);

        assertNull(form.getDefaultDecoratorsForElementClass(Element.class));
        assertNull(form.getDefaultDecoratorsAppliedToElementClass(Input.class));
        assertNull(form.getDefaultDecoratorsAppliedToElementClass(Select.class));

        form.setDefaultDecoratorsForElementClass(Element.class, original);
        assertNotNull(form.getDefaultDecoratorsForElementClass(Element.class));
        assertNotNull(form.getDefaultDecoratorsAppliedToElementClass(Input.class));
        assertNotNull(form.getDefaultDecoratorsAppliedToElementClass(Select.class));
    }

    @Test
    public void testPopulateDisabled() {
        form.reset();
        form.getElement("field1").setProperty("disabled", "disabled");
        form.getElement("selectId").setProperty("disabled", "disabled");
        form.getElement("multi").setProperty("disabled", "disabled");

        form.setElementValue("field1", "test");
        form.setElementValue("selectId", "2");
        form.setElementValue("multi", Arrays.asList("1", "2"));

        HashMap<String, Object> values1 = new HashMap<String, Object>() {
            {
                put("inputName", "test");
                put("selectName", "2");
                put("multi", Arrays.asList("1", "2"));
                put("chk", null);
            }
        };

        assertEquals(values1, form.getFormData());

        HashMap<String, Object> values2 = new HashMap<String, Object>() {
            {
                put("inputName", "new");
                put("selectName", "5");
                put("multi", new String[]{"6", "7"});//Test array population
                put("chk", null);
            }
        };

        //Populate won't affect disabled elements:
        form.populateSimple(values2);
        assertEquals(new HashMap<String, Object>(){{
            put("inputName", null);
            put("selectName", null);
            put("multi", null);
            put("chk", null);
        }}, form.getFormData());

        form.getElement("field1").removeProperty("disabled");
        form.getElement("selectId").removeProperty("disabled");
        form.getElement("multi").removeProperty("disabled");

        form.populateSimple(values2);
        values2.put("multi", Arrays.asList(new String[]{"6", "7"}));//Array is converted to list 
        assertEquals(values2, form.getFormData());
    }

    @Test
    public void testAutoTranslation() {
        TranslationEngineContext.getTranslationEngine().setLocale(SimpleTranslationEngine.DEFAULT_LOCALE);

        Form translationForm = new HTMLForm();
        translationForm.setAutoEnableElementsTranslation(true);

        translationForm.addElement(new Input("test").setLabel("uniform.test.resource3"));

        assertHTMLEquals("<form method=\"POST\"><label class=\"element-label\" for=\"test\">Label test</label><input id=\"test\" name=\"test\" type=\"text\" value=\"\"></form>", translationForm.renderHTML());

        translationForm.setAutoEnableElementsTranslation(false);

        translationForm.addElement(new Input("test2").setLabel("uniform.test.resource3"));

        assertHTMLEquals("<form method=\"POST\"><label class=\"element-label\" for=\"test\">Label test</label><input id=\"test\" name=\"test\" type=\"text\" value=\"\"><label class=\"element-label\" for=\"test2\">uniform.test.resource3</label><input id=\"test2\" name=\"test2\" type=\"text\" value=\"\"></form>", translationForm.renderHTML());

        Locale spanish = new Locale("es");
        TranslationEngineContext.getTranslationEngine().setLocale(spanish);

        assertHTMLEquals("<form method=\"POST\"><label class=\"element-label\" for=\"test\">Prueba label</label><input id=\"test\" name=\"test\" type=\"text\" value=\"\"><label class=\"element-label\" for=\"test2\">uniform.test.resource3</label><input id=\"test2\" name=\"test2\" type=\"text\" value=\"\"></form>", translationForm.renderHTML());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatedElementId(){
        Input input = new Input("field1");
        form.addElement(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatedFormDecoratorId(){
        form.startDecorator("dec1", new HTMLTagDecorator("div"));
        form.startDecorator("dec2", new HTMLTagDecorator("div"));
        form.startDecorator("dec3", new HTMLTagDecorator("div"));
        form.startDecorator("dec1", new HTMLTagDecorator("div"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDecoratorNotOpen(){
        form.endDecorator();
        
        form.render();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDecoratorNotClosed(){
        form.startDecorator("dec1", new HTMLTagDecorator("div"));
        
        form.render();
    }
}
