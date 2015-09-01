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
package net.uniform.testutils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.uniform.impl.utils.UniformUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class BeanPropertiesTest {
    public class BeanType {
        public String a;
        public Integer b;
        private List<Long> c;
        public short d;

        public List<Long> getC() {
            return c;
        }

        public void setC(List<Long> c) {
            this.c = c;
        }
    }
    
    @Test
    public void test(){
        BeanType bean = new BeanType();
        bean.a = "1";
        bean.b = 2;
        bean.setC(Arrays.asList(3L, 4L));
        bean.d = 5;
        
        assertEquals(
            new HashMap<String, Object>(){{
                put("a", "1");
                put("b", 2);
                put("c", Arrays.asList(3L, 4L));
                put("d", (short) 5);
            }},
            UniformUtils.getBeanProperties(bean)
        );
        
        Map<String, Object> newProps = new HashMap<String, Object>(){{
            put("a", 3.5);
            put("b", 1);
            put("c", Arrays.asList(2L));
            put("d", null);//Ignored because of null and primitive
        }};
        UniformUtils.fillBeanProperties(bean, newProps);
        
        newProps.put("a", "3.5");
        newProps.put("d", (short) 5);
        assertEquals(newProps, UniformUtils.getBeanProperties(bean));
        
        
        Map<String, Object> newProps2 = new HashMap<String, Object>(){{
            put("a", new Date());//Ignored because of wrong type
            put("b", 1);
            put("c", Arrays.asList(2L));
            put("d", null);//Ignored because of null and primitive
        }};
        UniformUtils.fillBeanProperties(bean, newProps2);
        
        newProps2.put("a", "3.5");
        newProps2.put("d", (short) 5);
        assertEquals(newProps2, UniformUtils.getBeanProperties(bean));
    }
}
