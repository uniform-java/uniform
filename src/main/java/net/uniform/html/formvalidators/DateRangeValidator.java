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
package net.uniform.html.formvalidators;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.uniform.api.Element;
import net.uniform.api.ElementWithValueConversion;
import net.uniform.api.Form;
import net.uniform.api.FormValidator;
import net.uniform.api.TranslationEngineContext;

/**
 * Form validator for making sure that date ranges are correct.
 * @author Eduardo Ramos
 */
public class DateRangeValidator implements FormValidator {

    private String dateStartId;
    private String dateEndId;
    private boolean allowSameDate = true;

    public DateRangeValidator(String dateStartId, String dateEndId) {
        this.dateStartId = dateStartId;
        this.dateEndId = dateEndId;
    }

    @Override
    public List<String> getValidationErrors(Form form, Map<String, List<String>> formData) {
        ElementWithValueConversion<Date> dateStartElement = findDateElement(form, dateStartId);
        ElementWithValueConversion<Date> dateEndElement = findDateElement(form, dateEndId);
        
        if(dateStartElement.isValid() && dateEndElement.isValid()){
            Date dateStart = dateStartElement.getConvertedValue();
            Date dateEnd = dateEndElement.getConvertedValue();

            if (dateStart == null || dateEnd == null) {
                return null;//Some of the dates might not be required
            }

            if (dateStart.after(dateEnd)) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate(
                        "uniform.formvalidators.daterange.invalid",
                        dateStartElement.getLabelTranslated(),
                        dateEndElement.getLabelTranslated()
                ));
            }

            if (!allowSameDate && dateStart.equals(dateEnd)) {
                return Arrays.asList(TranslationEngineContext.getTranslationEngine().translate(
                        "uniform.formvalidators.daterange.sameNotAllowed",
                        dateStartElement.getLabelTranslated(),
                        dateEndElement.getLabelTranslated()
                ));
            }
        }

        return null;
    }

    private ElementWithValueConversion<Date> findDateElement(Form form, String id) {
        Element element = form.getElement(id);
        if (element == null) {
            throw new IllegalStateException("Element with id '" + id + "' not found");
        }

        if (!Date.class.equals(element.getValueType()) || !(element instanceof ElementWithValueConversion)) {
            throw new IllegalStateException("Element with id '" + id + "' must have Date as its value type and implement <ElementWithValueConversion> interface");
        }

        return (ElementWithValueConversion<Date>) element;
    }

    @Override
    public boolean breakChainOnError() {
        return false;
    }

    public String getDateStartId() {
        return dateStartId;
    }

    public void setDateStartId(String dateStartId) {
        this.dateStartId = dateStartId;
    }

    public String getDateEndId() {
        return dateEndId;
    }

    public void setDateEndId(String dateEndId) {
        this.dateEndId = dateEndId;
    }

    public boolean isAllowSameDate() {
        return allowSameDate;
    }

    public void setAllowSameDate(boolean allowSameDate) {
        this.allowSameDate = allowSameDate;
    }
}
