/**
 * @(#)ListChildNodesTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.util.xml;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * The Class ListChildNodesTest.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 04.03.2011 : 15:40:08
 */
public class ListChildNodesTest {

    private final String context = (
          "<?xml version=\"1.0\" ?>\n"
        + "<TimePoint><!--Comment-->\n"
        + "  <Point count=\"0\"/>\n"
        + "<!--Comment-->\n"
        + "  <Point count=\"1\"/>\n"
        + "<!--Comment-->\n"
        + "  <Point count=\"2\"/>\n"
        + "<!--Comment-->\n"
        + "  <Point count=\"3\"/>\n"
        + "</TimePoint>\n"
    );

    @BeforeMethod
    public void setUp() throws Exception {
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoad() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(context.getBytes()));
        Node rootElement = doc.getDocumentElement();
        int count = 0;
        ListChildNodes list = new ListChildNodes(rootElement);
        for (Node nod : list) {
            assertEquals(Node.ELEMENT_NODE, nod.getNodeType());
            assertEquals("Point", nod.getNodeName());
            NamedNodeMap attributes = nod.getAttributes();
            Node atr = attributes.getNamedItem("count");
            assertNotNull(atr);
            assertEquals(Integer.toString(count), atr.getNodeValue());
            System.out.println(atr.getNodeValue());
            count++;
        }
        assertFalse(list.hasNext(), "проверка на hasNext == False");
        assertFalse(list.hasNext(), "проверка на hasNext == False");
    }

    @Test
    public void testIterator() throws Exception {
    }
}
