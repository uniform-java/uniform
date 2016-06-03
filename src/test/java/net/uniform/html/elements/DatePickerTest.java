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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.uniform.exceptions.UniformException;
import net.uniform.impl.utils.HTMLRenderingUtils;
import net.uniform.testutils.HTMLTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class DatePickerTest {

    @Test
    public void test() throws ParseException {
        DatePicker datePicker = new DatePicker("my-date");

        datePicker = new DatePicker("my-date", datePicker.getDateFormat());

        datePicker.setValue("2015-03-46");

        assertFalse(datePicker.isValid());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date date = sdf.parse("16/03/2015");
        datePicker.setValue(date);

        assertTrue(datePicker.isValid());
        assertEquals(date, datePicker.getConvertedValue());

        HTMLTest.assertHTMLEquals("<input id=\"my-date\" name=\"my-date\" type=\"date\" value=\"2015-03-16\">", HTMLRenderingUtils.render(datePicker.render()));

        SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
        datePicker.setDateFormat(sdf2);
        datePicker.setDateFormat(datePicker.getDateFormat().toPattern());

        assertFalse(datePicker.isValid());
        datePicker.setValue("01092015");
        assertTrue(datePicker.isValid());
    }

    @Test(expected = UniformException.class)
    public void testBadData() {
        DatePicker datePicker = new DatePicker("my-date");

        datePicker.setValue("2015-13-03");

        datePicker.getConvertedValue();
    }

    @Test(expected = UniformException.class)
    public void testUnsupportedTypeChange() {
        DatePicker datePicker = new DatePicker("date");
        datePicker.setValueType(Integer.class);
    }
}
