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
package net.uniform.html.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.uniform.api.Renderer;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.html.elements.Textarea;

/**
 * Default renderer for {@link Textarea} element.
 *
 * @author Eduardo Ramos
 */
public class TextareaRenderer implements Renderer<Textarea> {

    @Override
    public List<SimpleHTMLTag> render(Textarea textarea) {
        Map<String, String> finalProps = new HashMap<>();
        finalProps.putAll(textarea.getProperties());

        if (textarea.isRequired()) {
            finalProps.put("required", "required");
        }

        SimpleHTMLTag tag = new SimpleHTMLTag("textarea", finalProps);

        String value = textarea.getFirstValue();
        if (value == null) {
            value = "";//Important so textarea is never a self-closed element
        }
        tag.setContent(value);
        tag.setEscapeContent(false);

        List<SimpleHTMLTag> result = new ArrayList<>();
        result.add(tag);

        return result;
    }

}
