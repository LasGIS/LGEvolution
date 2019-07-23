/**
 * @(#)XmlStorage.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.IntStream;

/**
 * Этот класс содержит статические методы для хранения
 * локальной конфигурации в XML формате.
 *
 * @author vlaskin
 * @version 1.0
 * @since 27.10.2008 : 18:19:50
 */
@Slf4j
public final class XmlStorage<OBJ> {

    /** Ошибка при записи объекта. */
    private static final String SAVE_ERROR = "Ошибка при записи объекта: ";
    /** Ошибка при чтении объекта. */
    private static final String LOAD_ERROR = "Ошибка при чтении объекта: ";

    /** Пустой конструктор.
     XmlStorage() {

    } */

    /**
     * Читаем локальную конфигурацию в новом формате.
     * @param locale созданный объект класса
     * @param fileName имя файла для чтения
     * @return true если всё нормально
     */
    public boolean load(final OBJ locale, final String fileName) {
        assert locale != null : "Объект класса OBJ должен быть уже создан к этому моменту";
        assert fileName != null : "Файл должен быть открытым";
        final File file = new File(fileName);

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            Node rootElement = doc.getDocumentElement();
            NodeList nl = rootElement.getChildNodes();
            IntStream.range(0, nl.getLength()).mapToObj(nl::item)
                .filter(nod -> nod.getNodeType() == Node.ELEMENT_NODE)
                .filter(nod -> "LocalConfig".equalsIgnoreCase(nod.getNodeName()))
                .forEach(nod -> parseLocale(locale, nod));

            return true;

        } catch (SAXException exc) {
            log.error(LOAD_ERROR, exc);
        } catch (IOException | ParserConfigurationException exc) {
            log.error(LOAD_ERROR);
        }
        return false;
    }

    /**
     * Прочитать локальную конфигурацию.
     * @param locale созданный объект класса OBJ
     * @param rootElement главный элемент
     */
    private void parseLocale(final OBJ locale, Node rootElement) {
        NodeList nl = rootElement.getChildNodes();
/*
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
*/
    }

    /**
     * Сохраняем локальную конфигурацию в новом формате.
     * @param obj созданный объект класса OBJ
     * @param fileName имя файла для записи
     * @return true если всё нормально
     */
    public boolean save(OBJ obj, final String fileName) {
        assert obj != null : "Объект класса должен быть уже создан к этому моменту";

        final String className = obj.getClass().getSimpleName();
        final Method[] methods = obj.getClass().getDeclaredMethods();
        final File file = new File(fileName);

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            factory.setIgnoringElementContentWhitespace(true);
            final Document doc = builder.newDocument();
            final Node rootElement = doc.createElement(className);
            doc.appendChild(rootElement);
            rootElement.appendChild(doc.createTextNode("\n"));
            for (final Method method : methods) {
                saveField(obj, method, doc, rootElement, 1);
            }
            doc.normalizeDocument();

            // Use a Transformer for output
            final TransformerFactory tFactory = TransformerFactory.newInstance();
            final Transformer transformer = tFactory.newTransformer();
            final DOMSource source = new DOMSource(doc);
            final OutputStream stream = new FileOutputStream(file);
            final StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);

            return true;

        } catch (FileNotFoundException |
            TransformerException |
            ParserConfigurationException |
            IllegalAccessException |
            InvocationTargetException ex
        ) {
            log.error(SAVE_ERROR + ex.getMessage(), ex);
        }
        return false;
    }

    /**
     * -
     * @param obj -
     * @param doc -
     * @param level -
     * @throws InvocationTargetException -
     * @throws IllegalAccessException -
     */
    private Element saveObject(
        final String fieldName,
        final Object obj,
        final Document doc,
        final int level
    ) throws InvocationTargetException, IllegalAccessException {
        final Method[] methods = obj.getClass().getDeclaredMethods();
        final Element element = doc.createElement(fieldName);
        element.appendChild(doc.createTextNode("\n"));
        for (final Method method : methods) {
            saveField(obj, method, doc, element, level + 1);
        }
        element.appendChild(doc.createTextNode(StringUtils.repeat("   ", level)));
        return element;
    }
    /**
     *  -
     * @param obj      -
     * @param method   -
     * @param doc      -
     * @param parentElement  -
     * @param level    -
     */
    private void saveField(
        final Object obj,
        final Method method,
        final Document doc,
        final Node parentElement,
        final int level
    ) throws IllegalAccessException, InvocationTargetException {
        final String methodName = method.getName();
        final String mtdFieldName = methodName.startsWith("get") ?
            methodName.substring(3) :
            methodName.startsWith("is") ?
                methodName.substring(2) : null;
        if (mtdFieldName == null) return;
        final String fieldName = mtdFieldName.substring(0, 1).toLowerCase() + mtdFieldName.substring(1);
        String value = null;
        switch (method.getReturnType().getName()) {
            case "int":
            case "java.lang.Integer": {
                Integer integer = (Integer) method.invoke(obj);
                if (integer != null) {
                    value = Integer.toString(integer);
                }
            } break;
            case "double":
            case "java.lang.Double": {
                final Double dbl = (Double) method.invoke(obj);
                if (dbl != null) {
                    value = Double.toString(dbl);
                }
            } break;
            case "boolean":
            case "java.lang.Boolean": {
                final Boolean bool = (Boolean) method.invoke(obj);
                if (bool != null) {
                    value = Boolean.toString(bool);
                }
            } break;
            case "java.lang.String" :
                value = (String) method.invoke(obj);
                break;
            default: {
                final Object sub = method.invoke(obj);
                if (sub != null) {
                    final Element element = saveObject(fieldName, sub, doc, level);
                    parentElement.appendChild(doc.createTextNode(StringUtils.repeat("   ", level)));
                    parentElement.appendChild(element);
                    parentElement.appendChild(doc.createTextNode("\n"));
                }
            } break;
        }
        if (value != null) {
            final Element element = doc.createElement(fieldName);
            element.setAttribute("value", value);
            parentElement.appendChild(doc.createTextNode(StringUtils.repeat("   ", level)));
            parentElement.appendChild(element);
            parentElement.appendChild(doc.createTextNode("\n"));
        }
    }

}