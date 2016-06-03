/*
 * Copyright 2016 Eduardo Ramos.
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
package net.uniform.html;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.uniform.html.elements.Input;
import net.uniform.html.elements.Multiselect;
import net.uniform.html.elements.Select;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class MultipleElementsWithSameNameTest {

    @Test
    public void testMultipleElementsWithSameName() {
        final HTMLForm form = new HTMLForm();
        final TestBeanSingle beanSingle = new TestBeanSingle();
        final TestBeanList beanList = new TestBeanList();
        final TestBeanSet beanSet = new TestBeanSet();
        final String repeatedName = "testName";

        Input input1 = new Input("i1");
        Input input2 = new Input("i2");
        Input input3 = new Input("i3");

        input1.setValueType(Long.class);
        input2.setValueType(Long.class);
        input3.setValueType(Long.class);
        input1.setProperty("name", repeatedName);
        input2.setProperty("name", repeatedName);
        input3.setProperty("name", repeatedName);

        //First add only one element:
        form.addElement(input1);
        form.populate(new HashMap<String, List<String>>() {
            {
                put(repeatedName, Arrays.asList("1", "2"));
            }
        });

        Assert.assertEquals(
                new HashMap<String, List<String>>() {
            {
                put(repeatedName, Arrays.asList("1"));
            }
        },
                form.getFormDataMultivalue()
        );
        Assert.assertEquals(
                new HashMap<String, Object>() {
            {
                put(repeatedName, "1");
            }
        },
                form.getFormData()
        );
        Assert.assertEquals(
                new HashMap<String, Object>() {
            {
                put(repeatedName, 1l);
            }
        },
                form.getFormDataConvertedToElementValueTypes()
        );

        form.getFormDataIntoBean(beanSingle);
        form.getFormDataIntoBean(beanList);
        form.getFormDataIntoBean(beanSet);
        Assert.assertEquals(1l, (long) beanSingle.testName);
        Assert.assertEquals(Arrays.asList(1l), beanList.testName);
        Assert.assertTrue(beanSet.testName.containsAll(beanList.testName));

        //Then add more:
        form.addElements(input2, input3);

        form.populate(new HashMap<String, List<String>>() {
            {
                put(repeatedName, Arrays.asList(null, "2", "3", "4"));
            }
        });

        Assert.assertEquals(
                new HashMap<String, List<String>>() {
            {
                put(repeatedName, Arrays.asList(null, "2", "3"));
            }
        },
                form.getFormDataMultivalue()
        );
        Assert.assertEquals(
                new HashMap<String, Object>() {
            {
                put(repeatedName, Arrays.asList(null, "2", "3"));
            }
        },
                form.getFormData()
        );
        Assert.assertEquals(
                new HashMap<String, Object>() {
            {
                put(repeatedName, Arrays.asList(null, 2l, 3l));
            }
        },
                form.getFormDataConvertedToElementValueTypes()
        );

        form.getFormDataIntoBean(beanList);
        form.getFormDataIntoBean(beanSet);
        Assert.assertEquals(Arrays.asList(null, 2l, 3l), beanList.testName);
        System.out.println(beanSet.testName);
        Assert.assertTrue(beanSet.testName.containsAll(beanList.testName));

        form.populate(new HashMap<String, List<String>>() {
            {
                put(repeatedName, Arrays.asList("1", ""));
            }
        });

        Assert.assertEquals(
                new HashMap<String, List<String>>() {
            {
                put(repeatedName, Arrays.asList("1", "", null));
            }
        },
                form.getFormDataMultivalue()
        );
        Assert.assertEquals(
                new HashMap<String, Object>() {
            {
                put(repeatedName, Arrays.asList("1", "", null));
            }
        },
                form.getFormData()
        );
        Assert.assertEquals(
                new HashMap<String, Object>() {
            {
                put(repeatedName, Arrays.asList(1l, null, null));
            }
        },
                form.getFormDataConvertedToElementValueTypes()
        );

        form.getFormDataIntoBean(beanList);
        form.getFormDataIntoBean(beanSet);
        Assert.assertEquals(Arrays.asList(1l, null, null), beanList.testName);
        Assert.assertTrue(beanSet.testName.containsAll(beanList.testName));

        form.populate(new HashMap<String, List<String>>() {
            {
                put(repeatedName, Arrays.asList("1", "2", "3"));
            }
        });

        form.removeElement(input2);
        form.getFormDataIntoBean(beanList);
        form.getFormDataIntoBean(beanSet);
        Assert.assertEquals(Arrays.asList(1l, 3l), beanList.testName);
        Assert.assertTrue(beanSet.testName.containsAll(beanList.testName));
    }

    public class TestBeanSingle {

        public Long testName;

        @Override
        public String toString() {
            return "TestBeanSingle{" + "testName=" + testName + '}';
        }
    }

    public class TestBeanList {

        public List<Long> testName;

        @Override
        public String toString() {
            return "TestBeanList{" + "testName=" + testName + '}';
        }
    }

    public class TestBeanSet {

        public Set<Long> testName;

        @Override
        public String toString() {
            return "TestBeanSet{" + "testName=" + testName + '}';
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleElementsWithSameNameMultivaluedNotAllowed() {
        HTMLForm form = new HTMLForm();

        Multiselect m1 = new Multiselect("m1");
        Multiselect m2 = new Multiselect("m2");

        m1.setProperty("name", "mm");
        m2.setProperty("name", "mm");

        form.addElements(m1, m2);

        form.getFormData();//Throws the exception
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleElementsWithSameNameAndDifferentTypeNotAllowed() {
        HTMLForm form = new HTMLForm();

        Select s1 = new Select("m1");
        Select s2 = new Select("m2");

        s1.setProperty("name", "s");
        s2.setProperty("name", "s");

        s1.setValueType(String.class);
        s1.setValueType(Long.class);

        form.addElements(s1, s2);

        form.getFormData();//Throws the exception
    }
}
