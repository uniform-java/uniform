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
package net.uniform.impl;

import net.uniform.html.elements.Multicheckbox;
import net.uniform.html.elements.Radio;

/**
 * <p>
 * Implementation of an abstract element with multiple options based on HTML {@code <input>} tags such as {@link Multicheckbox} and {@link Radio}.
 * It allows to define how and where each option label is rendered (position and escaping).
 * </p>
 *
 * <p>
 * It also allows to define the separator to put between each option input
 * </p>
 *
 * @author Eduardo Ramos
 */
public abstract class MultioptionInputElement extends ElementWithOptions {

    public static final String DEFAULT_SEPARATOR = " ";
    public static final boolean DEFAULT_PREPEND_OPTION_LABELS = false;
    public static final boolean DEFAULT_ESCAPE_OPTION_LABELS = true;

    protected boolean escapeOptionLabels = DEFAULT_ESCAPE_OPTION_LABELS;
    protected boolean prependOptionLabels = DEFAULT_PREPEND_OPTION_LABELS;
    protected String separator = DEFAULT_SEPARATOR;

    public MultioptionInputElement(String id) {
        super(id);
    }

    public final void setEscapeOptionLabels(boolean escape) {
        this.escapeOptionLabels = escape;
    }

    public final boolean isEscapeOptionLabels() {
        return escapeOptionLabels;
    }

    public final void setPrependOptionLabels(boolean prepend) {
        this.prependOptionLabels = prepend;
    }

    public final boolean isPrependOptionLabels() {
        return prependOptionLabels;
    }

    public final void setSeparator(String separator) {
        this.separator = separator;
    }

    public final String getSeparator() {
        return separator;
    }
}
