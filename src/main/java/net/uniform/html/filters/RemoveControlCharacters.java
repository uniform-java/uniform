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
package net.uniform.html.filters;

import net.uniform.api.Filter;

/**
 * Removes normally unwanted control characters such as NUL, SOH...
 * Preserves only carriage returns, line breaks and tabs
 * @author Eduardo Ramos
 */
public class RemoveControlCharacters implements Filter {

    private static final String DEFAULT_REPLACEMENT = "";

    private final String replacement;

    public RemoveControlCharacters() {
        this(DEFAULT_REPLACEMENT);
    }

    public RemoveControlCharacters(String replacement) {
        this.replacement = replacement;
    }

    @Override
    public String filter(String value) {
        if (value != null) {
            return value.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", replacement);//Preserve carriage returns, line breaks and tabs
        } else {
            return value;
        }
    }

}
