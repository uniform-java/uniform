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

import java.util.List;
import net.uniform.api.html.SimpleHTMLTag;

/**
 * A Renderer is responsible of generating the HTML tags that represent a single {@link Element}.
 *
 * @author Eduardo Ramos
 * @param <T> Element type to render
 */
public interface Renderer<T extends Element> {

    /**
     * Returns the list of HTML tags for an element.
     *
     * @param element Element to render
     * @return List of tags for the element and renderer
     */
    List<SimpleHTMLTag> render(T element);
}
