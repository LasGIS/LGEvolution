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
            for (final Method method : methods) {
                saveField(obj, method, doc, rootElement, 1);
            }
/*
            final Element localConfig = doc.createElement("LocalConfig");
            final Element coordinate = doc.createElement("Coordinate");
            coordinate.setAttribute(
                "latitude",
                String.valueOf(obj.getLatitude())
            );
            coordinate.setAttribute(
                "longitude",
                String.valueOf(obj.getLongitude())
            );
            final ScaleManager sm = ScaleManager.getScaleManager();
            sm.setDelta(obj.getDelta());
            coordinate.setAttribute(
                "scale",
                String.valueOf(sm.getScale())
            );
            final Element regime = doc.createElement("Regime");
            regime.setAttribute(
                "number",
                String.valueOf(obj.getRegime())
            );
            localConfig.appendChild(doc.createTextNode("\n    "));
            localConfig.appendChild(coordinate);
            localConfig.appendChild(doc.createTextNode("\n    "));
            localConfig.appendChild(regime);
            localConfig.appendChild(doc.createTextNode("\n  "));
            rootElement.appendChild(doc.createTextNode("\n  "));
            rootElement.appendChild(localConfig);
            rootElement.appendChild(doc.createTextNode("\n"));
*/
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
        final Element element = doc.createElement(fieldName);
        final String value;
        switch (method.getReturnType().getName()) {
            case "int":
                value = Integer.toString((Integer) method.invoke(obj));
                break;
            case "double" :
                value = Double.toString((Double) method.invoke(obj));
                break;
            case "java.lang.String" :
                value = (String) method.invoke(obj);
                break;
            default:
                value = "";
                break;
/*
            default:
                final Object fieldObj = field.get(obj);
                if (fieldObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    final Map<Object, Object> map = (Map<Object, Object>) fieldObj;
                    for (Map.Entry<Object, Object> entry : map.entrySet()) {
                        final String key = entry.getKey().toString();
                        final String valStr;
                        final Object val = entry.getValue();
                        if (val instanceof Double) {
                            valStr = LGFormatter.format((Double) val * info.rate());
                        } else if (val instanceof Integer) {
                            valStr = Integer.toString((Integer) val);
                        } else {
                            valStr = val.toString();
                        }
                        sb.append(name).append('.').append(key).append(" \t").append(valStr).append('\n');
                    }
                } else if (fieldObj instanceof AnimalState) {
                    sb.append(name).append(" \t").append(((AnimalState) fieldObj).name).append('\n');
                } else {
                    sb.append("--- ").append(name).append(" --- \n");
                    getInfo(sb, fieldObj, fieldObj.getClass());
                }
                break;
*/
        }
        element.setAttribute("value", value);
        parentElement.appendChild(doc.createTextNode(StringUtils.repeat("   ", level)));
        parentElement.appendChild(element);
        parentElement.appendChild(doc.createTextNode("\n"));
    }

}