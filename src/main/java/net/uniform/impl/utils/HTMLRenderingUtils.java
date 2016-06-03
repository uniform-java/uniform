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
package net.uniform.impl.utils;

import java.util.ArrayList;
import java.util.List;
import net.uniform.api.html.SimpleHTMLTag;

/**
 * Class with static methods that simplifies the use of <code>HTMLRenderer</code> class
 *
 * @author Eduardo Ramos
 */
public class HTMLRenderingUtils {

    /**
     * Renders a <code>SimpleHTMLTag</code> as HTML
     *
     * @param tag Single tag
     * @return HTML
     */
    public static String render(SimpleHTMLTag tag) {
        List<SimpleHTMLTag> tags = new ArrayList<>();
        tags.add(tag);

        HTMLRenderer renderer = new HTMLRenderer(tags);

        return renderer.render();
    }

    /**
     * Renders a list of <code>SimpleHTMLTag</code> as HTML
     *
     * @param tags List of tags
     * @return HTML
     */
    public static String render(List<SimpleHTMLTag> tags) {
        HTMLRenderer renderer = new HTMLRenderer(tags);

        return renderer.render();
    }
}
