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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.uniform.api.TranslationEngine;

/**
 * Default implementation of the translation engine that is able to read the Uniform base implementation message resources, and hold a ThreadLocal {@link Locale}.
 * 
 * If no {@code Locale} is set, {@link #DEFAULT_LOCALE} will be used.
 * 
 * @author Eduardo Ramos
 */
public class SimpleTranslationEngine implements TranslationEngine {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    
    private static final String DEFAULT_RESOURCE_BASENAME = "net.uniform.impl.translation.messages";
    
    protected ThreadLocal<Locale> localeHolder = new InheritableThreadLocal<>();
    protected Map<Locale, ResourceBundle> bundles = new HashMap<>();
    
    protected String resourceBaseName;

    public SimpleTranslationEngine() {
        this(DEFAULT_RESOURCE_BASENAME);
    }
    
    public SimpleTranslationEngine(String resourceBaseName) {
        this.resourceBaseName = resourceBaseName;
    }

    @Override
    public Locale getLocale() {
        Locale locale = localeHolder.get();
        
        if(locale == null){
            locale = DEFAULT_LOCALE;
        }
        
        return locale;
    }
    
    private ResourceBundle getBundle(Locale locale){
        if(!bundles.containsKey(locale)){
            try {
                bundles.put(locale, ResourceBundle.getBundle(resourceBaseName, locale));
            } catch (MissingResourceException e) {
                bundles.put(locale, null);
            }
        }
        
        return bundles.get(locale);
    }

    @Override
    public void setLocale(Locale locale) {
        if(locale == null){
            throw new IllegalArgumentException("Locale cannot be null");
        }
        this.localeHolder.set(locale);
    }

    @Override
    public String translateWithDefault(String code, String defaultTranslation, Locale locale, Object... args) {
        String translationString = getTranslationString(code, defaultTranslation, locale);
        if(translationString == null){
            return null;
        }
        
        return MessageFormat.format(translationString, args);
    }

    
    @Override
    public String translateWithDefault(String code, String defaultTranslation, Object... args) {
        return this.translateWithDefault(code, defaultTranslation, getLocale(), args);
    }

    @Override
    public String translateWithDefault(String code, String defaultTranslation) {
        return this.translateWithDefault(code, defaultTranslation, getLocale(), (Object[]) null);
    }

    @Override
    public String translate(String code, Object... args) {
        return this.translateWithDefault(code, null, getLocale(), args);
    }

    @Override
    public String translate(String code) {
        return this.translateWithDefault(code, null, getLocale(), (Object[]) null);
    }

    @Override
    public String translateWithDefault(String code, String defaultTranslation, Locale locale) {
        return this.translateWithDefault(code, defaultTranslation, locale, (Object[]) null);
    }

    @Override
    public String translate(String code, Locale locale, Object... args) {
        return this.translateWithDefault(code, null, locale, args);
    }

    @Override
    public String translate(String code, Locale locale) {
        return this.translateWithDefault(code, null, locale, (Object[]) null);
    }

    @Override
    public String getTranslationString(String code) {
        return this.getTranslationString(code, null, getLocale());
    }

    @Override
    public String getTranslationString(String code, String defaultTranslation) {
        return this.getTranslationString(code, defaultTranslation, getLocale());
    }

    @Override
    public String getTranslationString(String code, Locale locale) {
        return this.getTranslationString(code, null, locale);
    }

    @Override
    public String getTranslationString(String code, String defaultTranslation, Locale locale) {
        ResourceBundle localeBundle = getBundle(locale);
        
        if(localeBundle != null && localeBundle.containsKey(code)){
            return localeBundle.getString(code);
        }else{
            ResourceBundle configuredLocaleBundle = getBundle(getLocale());
            if(configuredLocaleBundle != null && configuredLocaleBundle.containsKey(code)){
                return configuredLocaleBundle.getString(code);
            }else{
                ResourceBundle defaultLocaleBundle = getBundle(DEFAULT_LOCALE);
                if(defaultLocaleBundle != null && defaultLocaleBundle.containsKey(code)){
                    return defaultLocaleBundle.getString(code);
                }else{
                    return defaultTranslation;
                }
            }
        }
    }
    
}
