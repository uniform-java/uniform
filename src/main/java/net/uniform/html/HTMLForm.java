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
package net.uniform.html;

import java.util.ArrayList;
import java.util.List;
import net.uniform.api.Element;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.html.decorators.ElementErrorsDecorator;
import net.uniform.html.decorators.FormErrorsDecorator;
import net.uniform.html.decorators.LabelDecorator;
import net.uniform.impl.AbstractForm;

/**
 * Default HTML form implementation.
 * 
 * Adds a few details to the {@link AbstractForm}:
 * <ul>
 * <li>Adds {@link LabelDecorator} as a default decorator for all elements of any class</li>
 * <li>Adds {@link ElementErrorsDecorator} as a default decorator for all elements of any class</li>
 * <li>Adds {@link FormErrorsDecorator} to the top of the form</li>
 * <li>Encloses rendered elements in the HTML {@code <form>} tag and sets its attributes</li>
 * </ul>
 * @author Eduardo Ramos
 */
public class HTMLForm extends AbstractForm {
    
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    public HTMLForm() {
        super();
        this.setProperty("method", METHOD_POST);
    }
    
    @Override
    protected void setupDefaultRenderers(){
    }
    
    @Override
    protected void setupDefaultDecorators(){
        this.addDefaultDecoratorForElementClass(Element.class, new LabelDecorator());
        this.addDefaultDecoratorForElementClass(Element.class, new ElementErrorsDecorator());
    }
    
    @Override
    protected void setupTopFormDecorators(){
        this.startDecorator("form-level-errors", new FormErrorsDecorator());
        this.endDecorator();
    }
    
    @Override
    public List<SimpleHTMLTag> render() {
        List<SimpleHTMLTag> innerTags = super.render();
        SimpleHTMLTag formTag = new SimpleHTMLTag("form");
        
        formTag.setProperties(this.getProperties());
        for (SimpleHTMLTag innerTag : innerTags) {
            formTag.addSubTag(innerTag);
        }
        
        List<SimpleHTMLTag> result = new ArrayList<>();
        result.add(formTag);
        
        return result;
    }
    
    /**
     * Adds the given class to the {@code class} property of this form.
     * @param clazz HTML class to add
     * @return This form
     */
    public HTMLForm addClass(String clazz){
        String currentClass = this.getProperty("class");
        if(currentClass != null){
            currentClass += " " + clazz;
        }else{
            currentClass = clazz;
        }
        this.setProperty("class", currentClass);
        
        return this;
    }
}
