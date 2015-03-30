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

import java.util.HashMap;
import java.util.Map;
import net.uniform.api.Decorator;

/**
 * Abstract decorator implementation that contains all generic logic that any decorator should have.
 * @author Eduardo Ramos
 */
public abstract class AbstractDecorator implements Decorator {

    protected Map<String, Object> properties;

    public AbstractDecorator() {
        this.properties = new HashMap<>();
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public boolean hasProperty(String key) {
        return this.properties.containsKey(key);
    }

    @Override
    public Decorator setProperty(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public Decorator removeProperty(String key) {
        properties.remove(key);
        return this;
    }

    @Override
    public Decorator setProperties(Map<String, Object> properties) {
        this.properties.clear();
        if(properties != null){
            this.properties.putAll(properties);
        }

        return this;
    }

    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public Decorator clearProperties() {
        properties.clear();
        return this;
    }

    public String getStringProperty(String key) {
        Object value = this.getProperty(key);

        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    public boolean getBooleanProperty(String key) {
        Object value = this.getProperty(key);

        if (value != null) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            return Boolean.valueOf(value.toString().toLowerCase());
        } else {
            return false;//False by default
        }
    }

    public Integer getIntegerProperty(String key) {
        Object value = this.getProperty(key);

        if (value != null) {
            try {
                if (value instanceof Integer) {
                    return (Integer) value;
                }
                Integer integer = Integer.parseInt(value.toString());
                return integer;
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
