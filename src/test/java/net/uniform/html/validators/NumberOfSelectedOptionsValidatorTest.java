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
import java.util.List;
import net.uniform.html.elements.Multiselect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class NumberOfSelectedOptionsValidatorTest {
    
    @Test
    public void test() {
        Multiselect element = new Multiselect("test");
        element.addOption(1, "one");
        element.addOption(2, "two");
        element.addOption(3, "three");
        
        NumberOfSelectedOptionsValidator validator = new NumberOfSelectedOptionsValidator();
        element.addValidator(validator);
        
        assertTrue(element.isValid());//No value, not required
        
        element.setValue("1");
        
        assertTrue(element.isValid());//No min or max config
        
        validator.setMin(2);
        
        assertFalse(element.isValid());
        
        validator.setMin(1);
        
        assertEquals((long) validator.getMin(), 1);
        
        assertTrue(element.isValid());
        
        validator.setMin(null);
        assertTrue(element.isValid());
        
        validator.setMax(2);
        
        assertEquals((long) validator.getMax(), 2);
        
        element.setValue(Arrays.asList("1", "2"));
        assertTrue(element.isValid());
        
        element.setValue(Arrays.asList("1", "2", "3"));
        assertFalse(element.isValid());
        
        element.removeValidator(validator);
        validator = new NumberOfSelectedOptionsValidator(2);
        element.addValidator(validator);
        assertFalse(element.isValid());
        
        element.removeValidator(validator);
        validator = new NumberOfSelectedOptionsValidator(3, 4);
        element.addValidator(validator);
        assertTrue(element.isValid());
        
        element.setValue((List<String>) null);
        assertTrue(element.isValid());
        
        element.setValue((String) null);
        assertTrue(element.isValid());
    }
}
