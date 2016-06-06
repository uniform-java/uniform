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
package net.uniform.html.decorators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import net.uniform.api.Decorator;
import net.uniform.api.Element;
import net.uniform.api.TranslationEngineContext;
import net.uniform.api.html.SimpleHTMLTag;
import net.uniform.html.elements.Input;
import net.uniform.impl.utils.HTMLRenderingUtils;
import net.uniform.testutils.HTMLTest;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class LabelDecoratorTest {

    @Test
    public void test() {
        Locale spanish = new Locale("es");

        LabelDecorator decorator = new LabelDecorator();
        assertTrue(decorator.isEscape());
        assertTrue(decorator.isPrepend());

        Element element = new Input("test");
        element.setLabel("uniform.test.resource2");
        element.addDecorator(decorator);

        element.setTranslationEnabled(true);

        HTMLTest.assertHTMLEquals("<label class=\"element-label\" for=\"test\">Translation test</label>\n", render(decorator, element));
        element.setTranslationEnabled(false);
        HTMLTest.assertHTMLEquals("<label class=\"element-label\" for=\"test\">uniform.test.resource2</label>\n", render(decorator, element));

        element.setTranslationEnabled();
        TranslationEngineContext.getTranslationEngine().setLocale(spanish);

        HTMLTest.assertHTMLEquals("<label class=\"element-label\" for=\"test\">Translation test</label>\n", render(decorator, element));

        element.setLabel("uniform.test.resource3");
        HTMLTest.assertHTMLEquals("<label class=\"element-label\" for=\"test\">Prueba label</label>\n", render(decorator, element));
    }
    
    @Test
    public void testElementsWithoutIdNoFor() {
        LabelDecorator decorator = new LabelDecorator();
        
        Element element = new Input("test");
        element.removeProperty("id");
        element.setLabel("Element without id");
        element.addDecorator(decorator);
        
        HTMLTest.assertHTMLEquals("<label class=\"element-label\">Element without id</label>\n", render(decorator, element));
    }

    private String render(Decorator decorator, Element element) {
        return HTMLRenderingUtils.render(decorator.render(null, Arrays.asList(element), new ArrayList<SimpleHTMLTag>()));
    }
}
