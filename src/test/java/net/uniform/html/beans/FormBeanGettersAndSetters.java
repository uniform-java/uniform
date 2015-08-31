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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Eduardo Ramos
 */
public class FormBeanGettersAndSetters {

    private String inputName;
    private String selectName;
    private List<Long> multi;
    private Boolean chk;

    public FormBeanGettersAndSetters(String inputName, String selectName, List<Long> multi, Boolean chk) {
        this.inputName = inputName;
        this.selectName = selectName;
        this.multi = multi;
        this.chk = chk;
    }

    public String getInputName() {
        return inputName;
    }

    public String getSelectName() {
        return selectName;
    }

    public List<Long> getMulti() {
        return multi;
    }

    public Boolean getChk() {
        return chk;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

    public void setMulti(List<Long> multi) {
        this.multi = multi;
    }

    public void setChk(Boolean chk) {
        this.chk = chk;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.inputName);
        hash = 67 * hash + Objects.hashCode(this.selectName);
        hash = 67 * hash + Objects.hashCode(this.multi);
        hash = 67 * hash + Objects.hashCode(this.chk);
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
        final FormBeanGettersAndSetters other = (FormBeanGettersAndSetters) obj;
        if (!Objects.equals(this.inputName, other.inputName)) {
            return false;
        }
        if (!Objects.equals(this.selectName, other.selectName)) {
            return false;
        }
        if (!Objects.equals(this.multi, other.multi)) {
            return false;
        }
        if (!Objects.equals(this.chk, other.chk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "FormBeanGettersAndSetters{" + "inputName=" + inputName + ", selectName=" + selectName + ", multi=" + multi + ", chk=" + chk + '}';
    }
}
