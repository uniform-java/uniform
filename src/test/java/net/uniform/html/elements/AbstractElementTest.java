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
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import net.uniform.api.Element;

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
    
    @Test(expected = IllegalArgumentException.class)
    public void testRequiredId() {
        Element elem = new EmptyElement(null);
    }
}
