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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import net.uniform.api.Element;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.Validator;
import net.uniform.impl.utils.UniformUtils;

/**
 * Validator for date single-value inputs, with date format.
 * @author Eduardo Ramos
 */
public class DateValidator implements Validator<Element> {

    private SimpleDateFormat dateFormat;

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public DateValidator() {
        this(DEFAULT_DATE_FORMAT);
    }

    public DateValidator(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        this.dateFormat = sdf;
    }
    
    public DateValidator(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public List<String> getValidationErrors(Element element, List<String> value) {
        String dateStr = UniformUtils.firstValue(value);
        if (dateStr != null && !dateStr.isEmpty()) {//May be not required
            dateStr = dateStr.trim();
            try {
                dateFormat.parse(dateStr);
            } catch (ParseException ex) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate("uniform.validators.date.invalid", dateStr, dateFormat.toPattern()));
            }
        }

        return null;
    }

    @Override
    public boolean breakChainOnError() {
        return true;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
    
    public void setDateFormat(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        this.setDateFormat(sdf);
    }
}
