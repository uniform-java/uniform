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
import net.uniform.api.Element;
import net.uniform.html.elements.EmptyElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class RequiredValidatorTest {
    
    @Test
    public void test() {
        Element element = new EmptyElement("test");
        element.setRequired(true);
        
        assertFalse(element.isValid());
        element.setValue((String) null);
        assertFalse(element.isValid());
        
        element.setValue(Arrays.asList("1", "2", null));
        assertFalse(element.isValid());
        
        element.setValue("1");
        assertTrue(element.isValid());
    }
}
