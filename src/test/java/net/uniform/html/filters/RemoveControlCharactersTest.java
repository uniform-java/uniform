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
package net.uniform.html.filters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author Eduardo Ramos
 */
public class RemoveControlCharactersTest {

    @Test
    public void test() {
        RemoveControlCharacters filter = new RemoveControlCharacters();

        assertNull(filter.filter(null));

        assertEquals("abc", filter.filter("abc"));
        assertEquals("abc", filter.filter("\u0000abc"));
        assertEquals("\r\nabc", filter.filter("\u0000\r\nabc"));
        assertEquals("test", filter.filter("test\u0000"));
        assertEquals("test", filter.filter("test\u0001"));
        assertEquals("test", filter.filter("test\u0002"));
        assertEquals("test", filter.filter("test\u0003"));
        assertEquals("test", filter.filter("test\u0004"));
        assertEquals("test", filter.filter("test\u0005"));
        assertEquals("test", filter.filter("test\u0006"));
        assertEquals("test", filter.filter("test\u0007"));
        assertEquals("test", filter.filter("test\u0008"));
        assertEquals("test\t", filter.filter("test\t"));
        assertEquals("test\n", filter.filter("test\n"));
        assertEquals("test", filter.filter("test\u000b"));
        assertEquals("test", filter.filter("test\u000c"));
        assertEquals("test\r", filter.filter("test\r"));
        assertEquals("test", filter.filter("test\u000e"));
        assertEquals("test", filter.filter("test\u007f"));

        assertEquals("testfg", filter.filter("test\u0066\u0067"));
    }

}
