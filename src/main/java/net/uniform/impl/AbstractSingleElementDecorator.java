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

import java.util.List;
import net.uniform.api.Element;
import net.uniform.api.Form;
import net.uniform.api.html.SimpleHTMLTag;

/**
 * Abstract decorator implementation that contains all generic logic that any decorator only appliable to a single element should have.
 * @author Eduardo Ramos
 */
public abstract class AbstractSingleElementDecorator extends AbstractDecorator {

    @Override
    public List<SimpleHTMLTag> render(Form form, List<Element> elements, List<SimpleHTMLTag> rendered) {
        if(elements == null || elements.size() != 1){
            throw new IllegalArgumentException("Single element decorator expected only 1 element");
        }
        
        return render(elements.get(0), rendered);
    }
    
    protected abstract List<SimpleHTMLTag> render(Element element, List<SimpleHTMLTag> rendered);
    
}
