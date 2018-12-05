/**
 * @(#)XmlStorage.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.config;

import com.lasgis.evolution.panels.ScaleManager;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Этот класс содержит статические методы для хранения
 * локальной конфигурации в XML формате.
 *
 * @author vlaskin
 * @version 1.0
 * @since 27.10.2008 : 18:19:50
 */
@Slf4j
public class XmlStorage {

    /** Ошибка при записи локальной конфигурации. */
    private static final String LOCALE_SAVE_ERROR =
        "Ошибка при записи локальной конфигурации.";
    /** Ошибка при записи локальной конфигурации. */
    private static final String LOCALE_LOAD_ERROR =
        "Ошибка при чтении локальной конфигурации.";
    /** Пустой конструктор. */
    private XmlStorage() {

    }

    /**
     * Читаем локальную конфигурацию в новом формате.
     * @param locale созданный объект класса ConfigLocale
     * @param file созданный и открытый для чтения старый файл  ConfigLocal
     * @return true если всё нормально
     */
    public static boolean load(ConfigLocale locale, File file) {
        assert locale != null : "Объект класса ConfigLocale должен быть уже"
            + " создан к этому моменту";
        assert file != null : "Файл должен быть открытым";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            Node rootElement = doc.getDocumentElement();
            NodeList nl = rootElement.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node nod = nl.item(i);
                if (nod.getNodeType() == Node.ELEMENT_NODE) {
                    if ("LocalConfig".equalsIgnoreCase(nod.getNodeName())) {
                        parseLocale(locale, nod);
                    } else if (
                        "ProjectConfig".equalsIgnoreCase(nod.getNodeName())
                    ) {
                        parseProject(locale, nod);
                    }
                }
            }

            return true;

        } catch (SAXException exc) {
            log.error(LOCALE_LOAD_ERROR, exc);
        } catch (IOException | ParserConfigurationException exc) {
            log.error(LOCALE_LOAD_ERROR);
        }
        return false;
    }

    /**
     * Прочитать локальную конфигурацию.
     * @param locale созданный объект класса ConfigLocale
     * @param rootElement главный элемент
     */
    public static void parseLocale(ConfigLocale locale, Node rootElement) {
        NodeList nl = rootElement.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nod = nl.item(i);
            if (nod.getNodeType() == Node.ELEMENT_NODE) {
                if ("Coordinate".equalsIgnoreCase(nod.getNodeName())) {
                    NamedNodeMap nnm = nod.getAttributes();
                    locale.setLatitude(Double.valueOf(
                        nnm.getNamedItem("latitude").getNodeValue()
                    ));
                    locale.setLongitude(Double.valueOf(
                        nnm.getNamedItem("longitude").getNodeValue()
                    ));
                    ScaleManager sm = ScaleManager.getScaleManager();
                    sm.setScale(Double.valueOf(
                        nnm.getNamedItem("scale").getNodeValue()
                    ));
                    locale.setDelta(sm.getDelta());
                } else if ("Regime".equalsIgnoreCase(nod.getNodeName())) {
                    NamedNodeMap nnm = nod.getAttributes();
                    locale.setRegime(Integer.valueOf(
                        nnm.getNamedItem("number").getNodeValue()
                    ));
                }
            }
        }
    }

    /**
     * Прочитать проектную конфигурацию.
     * @param locale созданный объект класса ConfigLocale
     * @param rootElement главный элемент
     */
    public static void parseProject(ConfigLocale locale, Node rootElement) {
        // todo
    }

    /**
     * Сохраняем локальную конфигурацию в новом формате.
     * @param locale созданный объект класса ConfigLocale
     * @return true если всё нормально
     */
    public static boolean save(ConfigLocale locale) {
        assert locale != null : "Объект класса ConfigLocale должен быть уже"
            + " создан к этому моменту";

        File file = new File(locale.getFileName());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        // factory.setSchema();Attribute("encoding", "UTF-8");
        try {
/*
            Schema s = SchemaFactory.newInstance(
                XMLConstants.RELAXNG_NS_URI
            ).newSchema();
            factory.setSchema(s);
*/
            DocumentBuilder builder = factory.newDocumentBuilder();
            factory.setIgnoringElementContentWhitespace(true);
            Document doc = builder.newDocument();
            // "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            Text txt = doc.createTextNode("\n");
            Node rootElement = doc.createElement("LasGISConfig");
            doc.appendChild(rootElement);
            //doc.insertBefore(txt, rootElement);

//            DOMImplementation impl = builder.getDOMImplementation();
//            Document doc = impl.createDocument(
//                "http://lasgis.config.locale.XmlStorage",
//                "document",
//                null
//            );
//            Node rootElement = doc.getDocumentElement();
            //doc.setXmlEncoding("Windows-1251");
            Element localConfig = doc.createElement("LocalConfig");

            Element coordinate = doc.createElement("Coordinate");
            coordinate.setAttribute(
                "latitude",
                String.valueOf(locale.getLatitude())
            );
            coordinate.setAttribute(
                "longitude",
                String.valueOf(locale.getLongitude())
            );
            ScaleManager sm = ScaleManager.getScaleManager();
            sm.setDelta(locale.getDelta());
            coordinate.setAttribute(
                "scale",
                String.valueOf(sm.getScale())
            );
            Element regime = doc.createElement("Regime");
            regime.setAttribute(
                "number",
                String.valueOf(locale.getRegime())
            );
            localConfig.appendChild(doc.createTextNode("\n    "));
            localConfig.appendChild(coordinate);
            localConfig.appendChild(doc.createTextNode("\n    "));
            localConfig.appendChild(regime);
            localConfig.appendChild(doc.createTextNode("\n  "));
            rootElement.appendChild(doc.createTextNode("\n  "));
            rootElement.appendChild(localConfig);
            rootElement.appendChild(doc.createTextNode("\n"));
            doc.normalizeDocument();

            // Use a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            OutputStream stream = new FileOutputStream(file);
            StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);

            return true;

        } catch (FileNotFoundException | TransformerException | ParserConfigurationException exc) {
            log.error(LOCALE_SAVE_ERROR, exc);
        }
        return false;
    }

}