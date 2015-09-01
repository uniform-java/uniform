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

import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.uniform.api.Element;
import net.uniform.html.elements.EmptyElement;

/**
 *
 * @author Eduardo Ramos
 */
public class RegexValidatorTest {
    
    @Test
    public void test() {
        Element element = new EmptyElement("test");
        
        RegexValidator validator = new RegexValidator("[A-za-z]+");
        element.addValidator(validator);
        
        assertTrue(element.isValid());//No value, not required
        
        element.setValue("466ad4bc13");
        
        assertFalse(element.isValid());
        
        element.setValue("abcdejkl");
        
        assertTrue(element.isValid());
        
        element.setValue("A 2112 - text _ ");
        assertFalse(element.isValid());
        
        validator.setRegexPattern("[A-za-z0-9 -_]+");
        
        assertTrue(element.isValid());
        
        String literal = ".#~$%()[]";
        validator.setRegexPattern(Pattern.quote(literal));
        
        element.setValue(literal);
        assertTrue(element.isValid());
        
        element.setValue(literal+"more");
        assertFalse(element.isValid());
    }
}
