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

import java.text.MessageFormat;
import java.util.Locale;

/**
 * <p>
 * This class is responsible of translating text resources inside any Uniform class.
 * It's necessary for translating validation messages, labels...
 * It is accessed through the {@link TranslationEngineContext} class.
 * </p>
 * 
 * <p>
 * The translation engine tries to find the translation in the current locale, and use the default locale (english) if the resource is missing.
 * </p>
 *
 * <p>
 * Like Spring Framework's {@code LocaleContextHolder}, it holds a {@code Locale} instance with the current thread. The {@code Locale} will be inherited by any child threads spawned by the current thread.
 * </p>
 *
 * <p>
 * Java {@link MessageFormat} formatting should be used in all resources.
 * </p>
 *
 * @author Eduardo Ramos
 */
public interface TranslationEngine {

    /**
     * Returns the current locale (ThreadLocal)
     *
     * @return Current locale
     */
    Locale getLocale();

    /**
     * Sets the current locale (ThreadLocal)
     *
     * @param locale Locale to set
     */
    void setLocale(Locale locale);

    /**
     * Translates a resource with values.
     * Uses the default translation if the resource is missing.
     * @param code Resource code
     * @param defaultTranslation Default translation
     * @param args Values
     * @return Translated text
     */
    String translateWithDefault(String code, String defaultTranslation, Object... args);

    /**
     * Translates a resource.
     * Uses the default translation if the resource is missing.
     * @param code Resource code
     * @param defaultTranslation Default translation
     * @return Translated text
     */
    String translateWithDefault(String code, String defaultTranslation);

    /**
     * Translates a resource with values.
     *
     * @param code Resource code
     * @param args Values
     * @return Translated text or null if resource missing
     */
    String translate(String code, Object... args);

    /**
     * Translates a resource.
     *
     * @param code Resource code
     * @return Translated text or null if resource missing
     */
    String translate(String code);

    /**
     * Returns the translation string, without formatting values, if present.
     *
     * @param code Resource code
     * @return Translation string or null
     */
    String getTranslationString(String code);

    /**
     * Returns the translation string, without formatting values, if present.
     *
     * @param code Resource code
     * @param defaultTranslation Default translation to use if resource is missing
     * @return Translation string or default translation if missing resource
     */
    String getTranslationString(String code, String defaultTranslation);

    /**
     * Translates a resource with values, using an specific locale.
     * Uses the default translation if the resource is missing.
     * @param code Resource code
     * @param defaultTranslation Default translation
     * @param locale Locale to use instead of current locale
     * @param args Values
     * @return Translated text
     */
    String translateWithDefault(String code, String defaultTranslation, Locale locale, Object... args);

    /**
     * Translates a resource, using an specific locale.
     * Uses the default translation if the resource is missing.
     * @param code Resource code
     * @param defaultTranslation Default translation
     * @param locale Locale to use instead of current locale
     * @return Translated text
     */
    String translateWithDefault(String code, String defaultTranslation, Locale locale);

    /**
     * Translates a resource with values, using an specific locale.
     *
     * @param code Resource code
     * @param locale Locale to use instead of current locale
     * @param args Values
     * @return Translated text or null if resource missing
     */
    String translate(String code, Locale locale, Object... args);

    /**
     * Translates a resource, using an specific locale.
     *
     * @param code Resource code
     * @param locale Locale to use instead of current locale
     * @return Translated text or null if resource missing
     */
    String translate(String code, Locale locale);

    /**
     * Returns the translation string, without formatting values, if present, using an specific locale.
     *
     * @param code Resource code
     * @param locale Locale to use instead of current locale
     * @return Translation string or null
     */
    String getTranslationString(String code, Locale locale);

    /**
     * Returns the translation string, without formatting values, if present, using an specific locale.
     *
     * @param code Resource code
     * @param defaultTranslation Default translation to use if resource is missing
     * @param locale Locale to use instead of current locale
     * @return Translation string or default translation if missing resource
     */
    String getTranslationString(String code, String defaultTranslation, Locale locale);
}
