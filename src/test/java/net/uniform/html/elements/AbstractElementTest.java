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
import net.uniform.api.Decorator;
import net.uniform.api.Element;
import net.uniform.api.Filter;
import net.uniform.html.decorators.HTMLTagDecorator;
import net.uniform.html.decorators.LabelDecorator;
import net.uniform.html.filters.StringTrim;
import net.uniform.html.validators.StringLengthValidator;
import net.uniform.impl.AbstractElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class AbstractElementTest {

    @Test
    public void testElement() {
        Element elemMulti = new EmptyElement("multiValue") {

            @Override
            public boolean isMultiValue() {
                return true;
            }
        };

        Element elemSingle = new EmptyElement("singleValue") {

            @Override
            public boolean isMultiValue() {
                return false;
            }
        };

        elemMulti.setValue(Arrays.asList("test-val1", "test-val2"));
        elemSingle.setValue(Arrays.asList("test-val1", "test-val2"));

        assertEquals(elemMulti.getValue(), Arrays.asList("test-val1", "test-val2"));
        assertEquals(elemSingle.getValue(), Arrays.asList("test-val1"));

        elemMulti.setValue("test-val2");
        elemSingle.setValue("test-val2");

        assertEquals(elemMulti.getValue(), Arrays.asList("test-val2"));
        assertEquals(elemSingle.getValue(), Arrays.asList("test-val2"));
    }

    @Test
    public void testProperties() {
        Element elem = new EmptyElement("id");
        assertNotNull(elem.getProperties());
        assertTrue(elem.getProperties().isEmpty());
        
        elem.setProperty("prop1", "val1");
        assertFalse(elem.getProperties().isEmpty());
        elem.getProperties().clear();//Test we cannot mess with class internals
        assertFalse(elem.getProperties().isEmpty());
    }
    
    @Test
    public void testDecorators() {
        Element elem = new EmptyElement("id");
        assertNotNull(elem.getDecorators());
        assertTrue(elem.getDecorators().isEmpty());
        
        LabelDecorator labelDecorator = new LabelDecorator();
        elem.addDecorator(labelDecorator);
        assertFalse(elem.getDecorators().isEmpty());
        elem.getDecorators().clear();//Test we cannot mess with class internals
        assertFalse(elem.getDecorators().isEmpty());
        
        assertNotNull(elem.getDecorator(LabelDecorator.class));
        elem.removeDecorator(labelDecorator);
        assertNull(elem.getDecorator(LabelDecorator.class));
        
        LabelDecorator labelDecorator2 = new LabelDecorator();
        elem.addDecorator(labelDecorator);
        elem.addDecorator(labelDecorator2);
        elem.setDecoratorProperty(LabelDecorator.class, "key", "value");
        elem.setLastDecoratorProperty(LabelDecorator.class, "key", "value2");
        assertEquals(elem.getDecorator(LabelDecorator.class).getProperty("key"), "value");
        assertEquals(elem.getLastDecorator(LabelDecorator.class).getProperty("key"), "value2");
        
        elem.clearDecorators();
        assertNotNull(elem.getDecorators());
        assertTrue(elem.getDecorators().isEmpty());
        
        elem.setDecorators(Arrays.asList((Decorator) new LabelDecorator(), new HTMLTagDecorator("div")));
        assertEquals(elem.getDecorators().size(), 2);
        
        elem.setDecorators(null);
        assertNotNull(elem.getDecorators());
        assertTrue(elem.getDecorators().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFirstDecoratorNotFound() {
        Element elem = new EmptyElement("id");
        elem.setDecoratorProperty(HTMLTagDecorator.class, "key", "value");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLastDecoratorNotFound() {
        Element elem = new EmptyElement("id");
        elem.setLastDecoratorProperty(HTMLTagDecorator.class, "key", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredId() {
        Element elem = new EmptyElement(null);
    }

    @Test
    public void testFilters() {
        Element elem = new EmptyElement("id");
        assertNotNull(elem.getFilters());
        assertTrue(elem.getFilters().isEmpty());
        
        Filter filter = new StringTrim();
        elem.addFilter(filter);
        assertFalse(elem.getFilters().isEmpty());
        elem.getFilters().clear();//Test we cannot mess with class internals
        assertFalse(elem.getFilters().isEmpty());
        elem.removeFilter(filter);
        assertTrue(elem.getFilters().isEmpty());
        
        elem.clearFilters();
        assertNotNull(elem.getFilters());
        assertTrue(elem.getFilters().isEmpty());
        
        elem.setFilters(Arrays.asList((Filter) new StringTrim(), new StringTrim()));
        assertEquals(elem.getFilters().size(), 2);
        
        elem.setFilters(null);
        assertNotNull(elem.getFilters());
        assertTrue(elem.getFilters().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRequiredValueType() {
        AbstractElement elem = new EmptyElement("elem");
        elem.setValueType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyInvalid() {
        AbstractElement elem = new EmptyElement("elem");
        elem.setProperty(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPropertyInvalid() {
        AbstractElement elem = new EmptyElement("elem");
        elem.setProperty("", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyAfterTrimPropertyInvalid() {
        AbstractElement elem = new EmptyElement("elem");
        elem.setProperty("  ", "test");
    }

    @Test
    public void testLowerCasePropertyNames() {
        Element elem = new EmptyElement("id");

        elem.setProperty("TITLE", "Test");
        assertTrue(elem.hasProperty("TITLE"));
        assertTrue(elem.hasProperty("title"));
        assertTrue(elem.hasProperty("Title"));
        assertEquals(elem.getProperty("title"), "Test");

        elem.setProperty("title", "Test2");
        assertTrue(elem.hasProperty("TITLE"));
        assertTrue(elem.hasProperty("title"));
        assertTrue(elem.hasProperty("Title"));
        assertEquals(elem.getProperty("TITLE"), "Test2");

        assertEquals(elem.getProperties().get("title"), "Test2");
    }
    
    @Test
    public void testValidation(){
        Element elem = new EmptyElement("id");
        assertNotNull(elem.getValidators());
        assertTrue(elem.getValidators().isEmpty());
        
        elem.addValidator(new StringLengthValidator(5, 10));
        assertFalse(elem.getValidators().isEmpty());
        elem.getValidators().clear();//Test we cannot mess with class internals
        assertFalse(elem.getValidators().isEmpty());
        
        elem.setValue("1");
        assertFalse(elem.validationPerformed());
        assertFalse(elem.isValid());
        assertTrue(elem.validationPerformed());
        
        elem.clearValidation();
        elem.setValue("12345");
        assertFalse(elem.validationPerformed());
        assertTrue(elem.isValid());
        assertTrue(elem.validationPerformed());
        
        elem.clearValidators();
        assertNotNull(elem.getValidators());
        assertTrue(elem.getValidators().isEmpty());
    }
    
    @Test
    public void testLabelAndDescription(){
        Element elem = new EmptyElement("id");
        assertNull(elem.getLabel());
        assertNull(elem.getDescription());
        
        elem.setLabel("label");
        elem.setDescription("desc");
        
        assertEquals(elem.getLabel(), "label");
        assertEquals(elem.getDescription(), "desc");
    }
}
