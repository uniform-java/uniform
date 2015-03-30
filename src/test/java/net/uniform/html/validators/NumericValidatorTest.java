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

import net.uniform.html.validators.NumericValidator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.uniform.api.Element;
import net.uniform.html.elements.EmptyElement;

/**
 *
 * @author Eduardo Ramos
 */
public class NumericValidatorTest {
    
    @Test
    public void test() {
        Element element = new EmptyElement("test");
        
        NumericValidator validator = new NumericValidator(true);//Allow decimals
        element.addValidator(validator);
        
        assertTrue(element.getValidationErrors().isEmpty());//No value, not required
        
        element.setValue("abc");
        
        assertFalse(element.getValidationErrors().isEmpty());
        
        element.setValue("1");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("-1");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("2.4");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue(".4");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("10e2");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("-.1");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("1.");
        assertTrue(element.getValidationErrors().isEmpty());
        
        //Numeric ranges:
        validator.setGreaterThan(0);
        element.setValue("0");
        
        assertFalse(element.getValidationErrors().isEmpty());
        
        element.setValue("1");
        
        assertTrue(element.getValidationErrors().isEmpty());
        
        validator.setMinInclusive(true);
        
        assertTrue(element.getValidationErrors().isEmpty());
        
        validator.setLessThanOrEqual(5);
        
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("5");
        
        assertTrue(element.getValidationErrors().isEmpty());
        
        validator.setLessThan(5);
        
        assertFalse(element.getValidationErrors().isEmpty());
        
        validator.removeMax();
        validator.removeMin();
        
        assertTrue(element.getValidationErrors().isEmpty());
        
        //Disallow decimals:
        validator.setAllowDecimals(false);
        
        element.setValue("1");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("-1");
        assertTrue(element.getValidationErrors().isEmpty());
        
        element.setValue("2.4");
        assertFalse(element.getValidationErrors().isEmpty());
        
        element.setValue(".4");
        assertFalse(element.getValidationErrors().isEmpty());
        
        element.setValue("10e2");
        assertFalse(element.getValidationErrors().isEmpty());
        
        element.setValue("-.1");
        assertFalse(element.getValidationErrors().isEmpty());
        
        element.setValue("1.");
        assertFalse(element.getValidationErrors().isEmpty());
        
        element.setValue("4");
        
        validator.setGreaterThan(4);
        
        assertFalse(element.getValidationErrors().isEmpty());
        
        validator.setMin(null);
        
        validator.setLessThanOrEqual(4);
        
        assertTrue(element.getValidationErrors().isEmpty());
    }
}
