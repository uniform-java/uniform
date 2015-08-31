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
package net.uniform.html.beans;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Eduardo Ramos
 */
public class FormBeanPublic3 {

    public String inputName;
    public Number selectName;
    public List<Long> multi;
    public boolean chk;

    public FormBeanPublic3(String inputName, Number selectName, List<Long> multi, boolean chk) {
        this.inputName = inputName;
        this.selectName = selectName;
        this.multi = multi;
        this.chk = chk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.inputName);
        hash = 17 * hash + Objects.hashCode(this.selectName);
        hash = 17 * hash + Objects.hashCode(this.multi);
        hash = 17 * hash + (this.chk ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FormBeanPublic3 other = (FormBeanPublic3) obj;
        if (!Objects.equals(this.inputName, other.inputName)) {
            return false;
        }
        if (!Objects.equals(this.selectName, other.selectName)) {
            return false;
        }
        if (!Objects.equals(this.multi, other.multi)) {
            return false;
        }
        if (this.chk != other.chk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FormBeanPublic3{" + "inputName=" + inputName + ", selectName=" + selectName + ", multi=" + multi + ", chk=" + chk + '}';
    }
}
