/**
 * @(#)AnimationXMLHelper.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation;

import com.lasgis.animation.map.Animation;
import com.lasgis.animation.map.object.AniPoint;
import com.lasgis.animation.map.object.AniTimeLine;
import com.lasgis.animation.map.object.AniTimePoint;
import com.lasgis.animation.map.object.AniTimePolygon;
import com.lasgis.util.xml.ListChildNodes;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * The Class AnimationXMLHelper.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 01.03.2011 : 13:12:01
 */
@Slf4j
public class AnimationXMLHelper {

    /**  */
    private static final String SAVE_ERROR = "Ошибка при записи файла \"{0}\":\n";
    /**  */
    private static final String LOAD_ERROR = "Ошибка при чтении файла \"{0}\":\n";
    /**
     * Format for coordinate.
     */
    private static DecimalFormat fmt = new DecimalFormat(
        "####.#######", new DecimalFormatSymbols(Locale.ENGLISH)
    );

    /**
     * Для предотвращения создания экземпляра статического класса.
     */
    private AnimationXMLHelper() {
    }

    /**
     * Чтение мультика из файла.
     * @param fileName имя файла с мультиком
     * @return открытый класс Animation как мультик
     */
    public static Animation load(String fileName) {
        Animation anime = new Animation();
        anime.setFileName(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(fileName));
            for (Node nod : new ListChildNodes(doc.getDocumentElement())) {
                if ("TimeLines".equalsIgnoreCase(nod.getNodeName())) {
                    for (Node nodLine : new ListChildNodes(nod)) {
                        anime.add(parseTimeLine(nodLine));
                    }
                } else if ("TimePolygons".equalsIgnoreCase(nod.getNodeName())) {
                    for (Node nodPolygon : new ListChildNodes(nod)) {
                        anime.add(parsePolygon(nodPolygon));
                    }
                }
            }
            return anime;
        } catch (SAXException exc) {
            log.error(
                MessageFormat.format(LOAD_ERROR + exc.getMessage(), fileName), exc
            );
        } catch (IOException | ParserConfigurationException exc) {
            log.error(
                MessageFormat.format(LOAD_ERROR + exc.getMessage(), fileName)
            );
        }
        return null;
    }

    /**
     * Разбираем тег
<pre>
    &lt;TimeLine Color="FFFFFF" Thick="3"&gt;
        &lt;TimePoint&gt;...&lt;/TimePoint&gt;
            . . .
        &lt;TimePoint&gt;...&lt;/TimePoint&gt;
    &lt;/TimeLine&gt;
</pre>
     * и создаём на его основе класс AniTimeLine.
     * @param node тег &lt;TimeLine/&gt;
     * @return открытый класс AniTimeLine
     */
    private static AniTimeLine parseTimeLine(Node node) {
        AniTimeLine line = new AniTimeLine();
        NamedNodeMap nnm = node.getAttributes();
        String value = nnm.getNamedItem("LineColor").getNodeValue();
        line.setLineColor(getColor(value));
        value = nnm.getNamedItem("Thick").getNodeValue();
        line.setThick(Integer.parseInt(value));
        for (Node nodPoint : new ListChildNodes(node)) {
            line.add(parseAniTimePoint(nodPoint));
        }
        return line;
    }

    /**
     * Разбираем тег
<pre>
    &lt;TimePolygon LineColor="ff0000" FillColor="ffff00" Thick="7"&gt;
        &lt;TimePoint&gt;...&lt;/TimePoint&gt;
            . . .
        &lt;TimePoint&gt;...&lt;/TimePoint&gt;
    &lt;/TimePolygon&gt;
</pre>
     * и создаём на его основе класс AniTimePolygon.
     * @param node тег &lt;TimePolygon/&gt;
     * @return открытый класс AniTimePolygon
     */
    private static AniTimePolygon parsePolygon(Node node) {
        AniTimePolygon polygon = new AniTimePolygon();
        NamedNodeMap nnm = node.getAttributes();
        String value = nnm.getNamedItem("LineColor").getNodeValue();
        polygon.setLineColor(getColor(value));
        value = nnm.getNamedItem("FillColor").getNodeValue();
        polygon.setFillColor(getColor(value));
        value = nnm.getNamedItem("Thick").getNodeValue();
        polygon.setThick(Integer.parseInt(value));
        for (Node nodPoint : new ListChildNodes(node)) {
            polygon.add(parseAniTimePoint(nodPoint));
        }
        return polygon;
    }

    /**
     * получить Color из строки типа "FFFFFFFF".
     * @param value входная строка
     * @return Color
     */
    private static Color getColor(String value) {
        return new Color((int) (Long.parseLong(value, 16) & 0xFFFFFF));
    }

    /**
     * Разбираем тег
<pre>
    &lt;TimePoint&gt;
        &lt;Point Time="0.0" X="100" Y="100"/&gt;
        &lt;Point Time="10.0" X="90" Y="120"/&gt;
        &lt;Point Time="20.0" X="80" Y="130"/&gt;
        &lt;Point Time="30.0" X="50" Y="160"/&gt;
    &lt;/TimePoint&gt;
</pre>
     * и создаём на его основе класс AniTimePoint.
     * @param node тег &lt;TimePoint/&gt;
     * @return открытый класс AniTimePoint
     */
    private static AniTimePoint parseAniTimePoint(Node node) {
        AniTimePoint timePoint = new AniTimePoint();
        for (Node pntNode : new ListChildNodes(node)) {
            AniPoint pnt = new AniPoint();
            NamedNodeMap nnm = pntNode.getAttributes();
            String value = nnm.getNamedItem("Time").getNodeValue();
            pnt.setTime(Double.parseDouble(value));
            value = nnm.getNamedItem("X").getNodeValue();
            pnt.setX(Double.parseDouble(value));
            value = nnm.getNamedItem("Y").getNodeValue();
            pnt.setY(Double.parseDouble(value));
            timePoint.add(pnt);
        }
        return timePoint;
    }

    /**
     * Сохраняем мультик в файл.
     * @param anime мультик
     * @param fileName имя файла для сохранения в формате GPX
     * @return true if all right
     */
    public static boolean save(Animation anime, String fileName) {

        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();

        // Generate the XML
        XMLStreamWriter xmlw;
        try {
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(fileName));
            // Write the default XML declaration
            xmlw.writeStartDocument();
            xmlw.writeCharacters("\n");
            xmlw.writeDTD("<!DOCTYPE anime SYSTEM \"LGEv_anime.dtd\">");
            xmlw.writeCharacters("\n");
            xmlw.writeStartElement("anime");
            xmlw.writeAttribute("creator", "lasgis.Evolution");
            xmlw.writeAttribute("version", "1.0");
            xmlw.writeCharacters("\n    ");
            xmlw.writeStartElement("TimeLines");
            for (AniTimeLine line : anime.getLines()) {
                write(xmlw, line);
            }
            // End the "TimeLines" element
            xmlw.writeCharacters("\n    ");
            xmlw.writeEndElement();

            xmlw.writeCharacters("\n    ");
            xmlw.writeStartElement("TimePolygons");
            for (AniTimePolygon polygon : anime.getPolygons()) {
                write(xmlw, polygon);
            }
            // End the "TimePolygons" element
            xmlw.writeCharacters("\n    ");
            xmlw.writeEndElement();

            close(xmlw);
            anime.setFileName(fileName);
            return true;

        } catch (XMLStreamException exc) {
            log.error(
                MessageFormat.format(SAVE_ERROR + exc.getMessage(), fileName),
                exc
            );
        } catch (FileNotFoundException exc) {
            log.error(
                MessageFormat.format(SAVE_ERROR + exc.getMessage(), fileName),
                exc
            );
        }
        return false;
    }

    /**
     *
     * @param xmlw XMLStreamWriter
     * @throws XMLStreamException The base exception for unexpected processing errors.
     */
    public static void close(XMLStreamWriter xmlw) throws XMLStreamException {
        // End the "gpx" element
        xmlw.writeCharacters("\n");
        xmlw.writeEndElement();
        // End the XML document
        xmlw.writeEndDocument();
        // Close the XMLStreamWriter to free up resources
        xmlw.flush();
        xmlw.close();
    }

    /**
     * Сохраняем локальную конфигурацию в новом формате.
     * @param xmlw XMLStreamWriter
     * @param line созданный объект класса ConfigLocale
     * @throws XMLStreamException The base exception for unexpected processing errors.
     */
    public static void write(
        XMLStreamWriter xmlw,
        AniTimeLine line
    ) throws XMLStreamException {
        // Write a comment
        xmlw.writeCharacters("\n        ");

        // Write the element "TimeLine"
        xmlw.writeStartElement("TimeLine");
        xmlw.writeAttribute("LineColor", Integer.toHexString(line.getLineColor().getRGB() & 0xFFFFFF));
        xmlw.writeAttribute("Thick", Integer.toString(line.getThick()));

        for (AniTimePoint pnt : line) {
            write(xmlw, pnt);
        }

        // End the "TimeLine" element
        xmlw.writeCharacters("\n        ");
        xmlw.writeEndElement();
    }

    /**
     * Сохраняем локальную конфигурацию в новом формате.
     * @param xmlw XMLStreamWriter
     * @param polygon созданный объект класса ConfigLocale
     * @throws XMLStreamException The base exception for unexpected processing errors.
     */
    public static void write(
        XMLStreamWriter xmlw,
        AniTimePolygon polygon
    ) throws XMLStreamException {
        // Write a comment
        xmlw.writeCharacters("\n        ");

        // Write the element "TimePolygon"
        xmlw.writeStartElement("TimePolygon");
        xmlw.writeAttribute(
            "LineColor", Integer.toHexString(
                polygon.getLineColor().getRGB() & 0xFFFFFF
            )
        );
        xmlw.writeAttribute(
            "FillColor", Integer.toHexString(
                polygon.getFillColor().getRGB() & 0xFFFFFF
            )
        );
        xmlw.writeAttribute("Thick", Integer.toString(polygon.getThick()));

        for (AniTimePoint pnt : polygon) {
            write(xmlw, pnt);
        }

        // End the "TimePolygon" element
        xmlw.writeCharacters("\n        ");
        xmlw.writeEndElement();
    }

    /**
     * Сохраняем локальную конфигурацию в новом формате.
     * @param xmlw XMLStreamWriter
     * @param point созданный объект класса ConfigLocale
     * @throws XMLStreamException The base exception for unexpected processing errors.
     */
    public static void write(XMLStreamWriter xmlw, AniTimePoint point) throws XMLStreamException {
        xmlw.writeCharacters("\n            ");
        xmlw.writeStartElement("TimePoint");

        for (AniPoint pnt : point) {
            write(xmlw, pnt);
        }

        xmlw.writeCharacters("\n            ");
        xmlw.writeEndElement();
    }

    /**
     * @param xmlw XMLStreamWriter
     * @param point созданный объект класса ConfigLocale
     * @throws XMLStreamException The base exception for unexpected processing errors.
     */
    public static void write(XMLStreamWriter xmlw, AniPoint point) throws XMLStreamException {
        xmlw.writeCharacters("\n                ");
        xmlw.writeEmptyElement("Point");
        xmlw.writeAttribute("Time", Double.toString(point.getTime()));
        xmlw.writeAttribute("X", fmt.format(point.getX()));
        xmlw.writeAttribute("Y", fmt.format(point.getY()));
    }

}
