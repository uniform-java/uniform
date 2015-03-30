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
package net.uniform.impl.translation;

import net.uniform.impl.translation.SimpleTranslationEngine;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class SimpleTranslationEngineTest {

    private final SimpleTranslationEngine eng;

    public SimpleTranslationEngineTest() {
        eng = new SimpleTranslationEngine();
    }

    @Test
    public void testTranslation() {
        Locale spanish = new Locale("es");
        eng.setLocale(SimpleTranslationEngine.DEFAULT_LOCALE);
        
        assertEquals("Translation test 1 - 2", eng.translate("uniform.test.resource", 1, 2));
        assertEquals("Translation test", eng.translate("uniform.test.resource2"));

        assertEquals("Test traducción 1 - 2", eng.translate("uniform.test.resource", spanish, 1, 2));
        assertEquals("Translation test", eng.translate("uniform.test.resource2", spanish));//Spanish translation for this message is missing

        assertEquals("Translation test", eng.getTranslationString("uniform.test.resource2"));
        assertEquals("Test traducción {0} - {1}", eng.getTranslationString("uniform.test.resource", spanish));
        assertEquals("Translation test", eng.getTranslationString("uniform.test.resource2", spanish));//Spanish translation for this message is missing

        //Missing resources in base language:
        assertEquals("Default", eng.translateWithDefault("uniform.test.missing", "Default"));
        assertEquals("Default arg", eng.translateWithDefault("uniform.test.missing", "Default {0}", "arg"));

        assertNull(eng.getTranslationString("uniform.test.missing"));
        assertEquals("Default", eng.getTranslationString("uniform.test.missing", "Default"));
    }
}
