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
package net.uniform.html.validators;

import java.util.Arrays;
import java.util.HashSet;
import net.uniform.api.Element;
import net.uniform.html.elements.EmptyElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class InSetValidatorTest {
    
    @Test
    public void test() {
        Element element = new EmptyElement("test");
        
        InSetValidator validator = new InSetValidator();
        validator = new InSetValidator(null);
        element.addValidator(validator);
        
        assertTrue(element.isValid());//No value, not required
        
        element.setValue("466ad4bc13");
        
        assertFalse(element.isValid());
        
        validator.addValidValue(element.getFirstValue());
        
        assertTrue(element.isValid());
        
        validator.removeValidValue(element.getFirstValue());
        
        assertFalse(element.isValid());
        
        validator.addValidValue("1");
        validator.addValidValue("2");
        validator.addValidValue("3");
        
        assertEquals(validator.getValuesSet(), new HashSet<>(Arrays.asList("1", "2", "3")));
        validator.setValidValues(null);
        validator.setValidValues(new HashSet<>(Arrays.asList("1", "2", "3")));
        
        element.setValue("1");
        assertTrue(element.isValid());
        element.setValue("2");
        assertTrue(element.isValid());
        element.setValue("3");
        assertTrue(element.isValid());
        element.setValue("4");
        assertFalse(element.isValid());
        
        element.setValue("1");
        validator.clearValidValues();
        assertFalse(element.isValid());
        
        validator.getValuesSet().add("1");//Test that this does not affect the internals of the validator
        assertFalse(element.isValid());
        
        element.removeValidator(validator);
        validator = new InSetValidator(new HashSet<>(Arrays.asList("1")));
        element.addValidator(validator);
        assertTrue(element.isValid());
    }
}
