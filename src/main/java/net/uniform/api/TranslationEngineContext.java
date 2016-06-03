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

import net.uniform.impl.translation.SimpleTranslationEngine;

/**
 * This class is responsible of holding the translation engine used in all Uniform classes.
 * The translation engine can be changed so Uniform uses any other message resource locator, such as Spring Framework's {@code MessageResource}.
 * @author Eduardo Ramos
 */
public class TranslationEngineContext {

    /**
     * Default translation engine
     */
    private static TranslationEngine engine = new SimpleTranslationEngine();

    private TranslationEngineContext() {
    }

    /**
     * Returns the current translation engine instance.
     *
     * @return Translation engine
     */
    public static TranslationEngine getTranslationEngine() {
        return engine;
    }

    /**
     * Sets the translation engine to be used by Uniform.
     *
     * @param engine New engine, not null
     */
    public static void setTranslationEngine(TranslationEngine engine) {
        TranslationEngineContext.engine = engine;
    }
}
