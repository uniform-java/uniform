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
public class FormBeanMixed {

    public String inputName;
    public Integer selectName;
    private List<Number> multi;
    private boolean chk;

    public FormBeanMixed(String inputName, Integer selectName, List<Number> multi, boolean chk) {
        this.inputName = inputName;
        this.selectName = selectName;
        this.multi = multi;
        this.chk = chk;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public Integer getSelectName() {
        return selectName;
    }

    public void setSelectName(Integer selectName) {
        this.selectName = selectName;
    }

    public List<Number> getMulti() {
        return multi;
    }

    public void setMulti(List<Number> multi) {
        this.multi = multi;
    }

    public boolean isChk() {
        return chk;
    }

    public void setChk(boolean chk) {
        this.chk = chk;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.inputName);
        hash = 53 * hash + Objects.hashCode(this.selectName);
        hash = 53 * hash + Objects.hashCode(this.multi);
        hash = 53 * hash + (this.chk ? 1 : 0);
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
        final FormBeanMixed other = (FormBeanMixed) obj;
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
        return "FormBeanMixed{" + "inputName=" + inputName + ", selectName=" + selectName + ", multi=" + multi + ", chk=" + chk + '}';
    }
}
