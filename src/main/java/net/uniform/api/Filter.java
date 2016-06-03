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
package net.uniform.api;

/**
 * A Filter is responsible of pre-processing any value set or populated to a element.
 * It is useful for apply generic logic like trimming values or removing special characters.
 * @author Eduardo Ramos
 * @see Element
 */
public interface Filter {

    /**
     * Filter a single string value.
     *
     * @param value Input value, may be null
     * @return Filtered value
     */
    String filter(String value);
}
