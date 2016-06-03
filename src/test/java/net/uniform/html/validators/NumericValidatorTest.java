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

import net.uniform.api.Element;
import net.uniform.html.elements.EmptyElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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

        assertTrue(element.isValid());//No value, not required

        element.setValue("abc");

        assertFalse(element.isValid());

        element.setValue("1");
        assertTrue(element.isValid());

        element.setValue("-1");
        assertTrue(element.isValid());

        element.setValue("2.4");
        assertTrue(element.isValid());

        element.setValue(".4");
        assertTrue(element.isValid());

        element.setValue("10e2");
        assertTrue(element.isValid());

        element.setValue("-.1");
        assertTrue(element.isValid());

        element.setValue("1.");
        assertTrue(element.isValid());

        //Numeric ranges:
        validator.setGreaterThan(0);
        element.setValue("0");

        assertFalse(element.isValid());

        element.setValue("1");

        assertTrue(element.isValid());

        validator.setMinInclusive(true);

        assertTrue(element.isValid());

        validator.setLessThanOrEqual(5);

        assertTrue(element.isValid());

        element.setValue("5.0");

        assertTrue(element.isValid());

        validator.setLessThan(5);

        assertFalse(element.isValid());

        validator.removeMax();
        validator.removeMin();

        assertTrue(element.isValid());

        //Disallow decimals:
        assertTrue(validator.isAllowDecimals());
        validator.setAllowDecimals(false);
        assertFalse(validator.isAllowDecimals());

        element.setValue("1");
        assertTrue(element.isValid());

        element.setValue("-1");
        assertTrue(element.isValid());

        element.setValue("2.4");
        assertFalse(element.isValid());

        element.setValue(".4");
        assertFalse(element.isValid());

        element.setValue("10e2");
        assertFalse(element.isValid());

        element.setValue("-.1");
        assertFalse(element.isValid());

        element.setValue("1.");
        assertFalse(element.isValid());

        element.setValue("4");

        validator.setGreaterThan(4);
        assertFalse(validator.isMinInclusive());

        assertFalse(element.isValid());

        validator.setMin(null);

        validator.setLessThanOrEqual(4);
        assertTrue(validator.isMaxInclusive());

        assertEquals(validator.getMax(), 4.0, 0);
        assertTrue(element.isValid());
        assertNull(validator.getMin());

        validator.removeMin();
        assertEquals(validator.getMin(), null);
        validator.setGreaterThanOrEqual(0);

        assertTrue(validator.isMinInclusive());

        validator.setAllowDecimals(true);
        element.setValue("0");
        assertTrue(element.isValid());
        element.setValue("0.0");
        assertTrue(element.isValid());
        element.setValue("-1");
        assertFalse(element.isValid());
        element.setValue("-0.01");
        assertFalse(element.isValid());

        element.setValue("4");
        assertTrue(validator.isMaxInclusive());
        assertTrue(element.isValid());
        element.setValue("4.01");
        assertFalse(element.isValid());
    }
}
