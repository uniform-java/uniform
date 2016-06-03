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
import net.uniform.api.ElementWithValueConversion;
import net.uniform.api.Renderer;
import net.uniform.api.Validator;
import net.uniform.exceptions.UniformException;
import net.uniform.html.renderers.InputRenderer;
import net.uniform.html.validators.DateValidator;
import net.uniform.impl.AbstractHTMLElement;

/**
 * Element that represents an HTML date picker with the default HTML5 date format.
 * It automatically adds a date validator to the element with the default HTML5 date format.
 * @author Eduardo Ramos
 */
public class DatePicker extends AbstractHTMLElement implements ElementWithValueConversion<Date> {

    private SimpleDateFormat dateFormat;

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public DatePicker(String id) {
        this(id, DEFAULT_DATE_FORMAT);
    }

    public DatePicker(String id, SimpleDateFormat dateFormat) {
        super(id);
        this.setProperty("type", "date");

        this.dateFormat = dateFormat;
        this.addValidator(new DateValidator(this.dateFormat));
    }

    public DatePicker(String id, String dateFormat) {
        super(id);

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        this.setProperty("type", "date");

        this.dateFormat = sdf;
        this.addValidator(new DateValidator(this.dateFormat));
    }

    @Override
    public Class<?> getValueType() {
        return Date.class;
    }

    @Override
    public void setValueType(Class<?> valueType) {
        throw new UniformException("DatePicker does not support value type change");
    }

    /**
     * Sets the value of the input element with a date and automatically converts it to the configured date format.
     *
     * @param date Date value
     * @return This element
     */
    public DatePicker setValue(Date date) {
        setValue(dateFormat.format(date));
        return this;
    }

    /**
     * Returns the value of this element as a Date, automatically converting the value string with the configured date format.
     * If the value is not empty and its format is incorrect, an exception will be thrown.
     * @return Date
     */
    @Override
    public Date getConvertedValue() {
        String dateStr = this.getFirstValue();

        if (dateStr != null && !dateStr.trim().isEmpty()) {
            dateStr = dateStr.trim();
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException ex) {
                throw new UniformException(String.format("Could not parse date %s", dateStr), ex);
            }
        }

        return null;
    }

    /**
     * Sets the date format for this element.
     * It also changes the date format for the date validator, if present.
     * @param dateFormat Date format
     * @return This element
     */
    public DatePicker setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        for (Validator validator : this.getValidators()) {
            if (validator instanceof DateValidator) {
                ((DateValidator) validator).setDateFormat(dateFormat);
            }
        }
        return this;
    }

    /**
     * Sets the date format for this element.
     * It also changes the date format for the date validator, if present.
     * @param dateFormat Date format
     * @return This element
     */
    public DatePicker setDateFormat(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        return setDateFormat(sdf);
    }

    /**
     * Returns the date format for this element.
     *
     * @return Date format
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public Renderer getDefaultRenderer() {
        return new InputRenderer();
    }
}
