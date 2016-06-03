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

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.custommonkey.xmlunit.HTMLDocumentBuilder;
import org.custommonkey.xmlunit.TolerantSaxDocumentBuilder;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Eduardo Ramos
 */
public class HTMLTest {

    public static void assertHTMLEquals(String html1, String html2) {
        try {
            TolerantSaxDocumentBuilder tolerantSaxDocumentBuilder = new TolerantSaxDocumentBuilder(XMLUnit.getTestParser());
            HTMLDocumentBuilder htmlDocumentBuilder = new HTMLDocumentBuilder(tolerantSaxDocumentBuilder);

            Document xml1 = htmlDocumentBuilder.parse(html1);
            Document xml2 = htmlDocumentBuilder.parse(html2);

            assertXMLEqual(xml1, xml2);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            throw new IllegalArgumentException("Invalid XML produced", ex);
        }
    }
}
