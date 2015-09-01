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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.uniform.api.Element;
import net.uniform.html.elements.EmptyElement;

/**
 *
 * @author Eduardo Ramos
 */
public class AlphanumericValidatorTest {
    
    @Test
    public void test() {
        Element element = new EmptyElement("test");
        
        AlphanumericValidator validator = new AlphanumericValidator();
        element.addValidator(validator);
        
        assertTrue(element.isValid());//No value, not required
        
        element.setValue("466ad4bc13");
        
        assertTrue(element.isValid());
        
        element.setValue("Other val %&$");
        
        assertFalse(element.isValid());
    }
}
