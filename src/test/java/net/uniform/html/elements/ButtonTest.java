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
package net.uniform.html.elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import net.uniform.exceptions.UniformException;
import net.uniform.impl.utils.HTMLRenderingUtils;
import net.uniform.testutils.HTMLTest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class ButtonTest {

    @Test
    public void test() throws ParseException {
        Button button = new Button("btn", Button.DEFAULT_BUTTON_TYPE);
        button.setLabel("My button");
        
        button.setValue("ignored");
        assertNull(button.getValue());
        button.reset();//Ignored
        button.setValue(Arrays.asList("ignored"));
        assertNull(button.getValue());
        button.populate(Arrays.asList("ignored"));
        assertNull(button.getValue());
        
        assertTrue(button.isValid());
        HTMLTest.assertHTMLEquals("<button id=\"btn\" type=\"button\">My button</button>", HTMLRenderingUtils.render(button.render()));
        
        button.setProperty("type", Button.BUTTON_TYPE_RESET);
        HTMLTest.assertHTMLEquals("<button id=\"btn\" type=\"reset\">My button</button>", HTMLRenderingUtils.render(button.render()));
        
        button.setProperty("type", Button.BUTTON_TYPE_SUBMIT);
        HTMLTest.assertHTMLEquals("<button id=\"btn\" type=\"submit\">My button</button>", HTMLRenderingUtils.render(button.render()));
    }
    
    @Test(expected = UniformException.class)
    public void testUnsupportedTypeChange() {
        Button button = new Button("btn");
        button.setValueType(Integer.class);
    }
}
