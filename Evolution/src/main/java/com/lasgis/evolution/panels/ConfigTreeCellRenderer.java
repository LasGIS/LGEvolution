/**
 * @(#)ConfigTreeCellRenderer.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import com.lasgis.evolution.object.LiveObjectElement;
import com.lasgis.util.Util;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.IOException;

/**
 * В этом классе определяется вид конечных
 * элементов в дереве типовых объектов карты.
 *
 * @author vlaskin
 * @version 1.0
 * @since Apr 26, 2006 : 4:06:11 PM
 */
@Slf4j
public class ConfigTreeCellRenderer extends JLabel implements TreeCellRenderer {

    private static final Icon LEAF_ICON = loadImageIcon("textfile.gif");
    private static final Icon OPEN_ICON = loadImageIcon("openfold.gif");
    private static final Icon CLOSED_ICON = loadImageIcon("folder.gif");
    private static final Image SHOW_ICON = loadImage("approve.gif");
    private static final Image NOT_SHOW_ICON = loadImage("cross.gif");
    private static final Color BACKGROUND_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Color BACKGROUND_SELECTION_COLOR = new Color(0, 0, 220);
    private static final Color TEXT_SELECTION_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_SELECTION_COLOR = new Color(255, 0, 0);
    private static final EmptyBorder LEAF_BORDER = new EmptyBorder(1, 20, 1, 4);
    private static final EmptyBorder OTHER_BORDER = new EmptyBorder(1, 2, 1, 4);

    boolean hasFocused = false;
    boolean selected = false;
    private Boolean isShow = null;

    private static Icon loadImageIcon(final String name) {
        Icon icon = null;
        try {
            icon = Util.loadImageIcon(name);
        } catch (final IOException ex) {
            log.error("ConfigTreeCellRenderer не инициализирован:\n\r\t" + ex.getMessage());
        }
        return icon;
    }

    private static Image loadImage(final String name) {
        Image image = null;
        try {
            image = Util.loadImage(name);
        } catch (final IOException ex) {
            log.error("ConfigTreeCellRenderer не инициализирован:\n\r\t" + ex.getMessage());
        }
        return image;
    }

    /**
     * Конструктор.
     */
    public ConfigTreeCellRenderer() {
        super();
    }

    /**
     *
     */
    public void updateUI() {
        super.updateUI();
        setBorder(OTHER_BORDER);
        //setName("Tree.cellRenderer");
    }

    /**
     * Configures the renderer based on the passed in components.
     * The value is set from messaging the tree with
     * <code>convertValueToText</code>, which ultimately invokes
     * <code>toString</code> on <code>value</code>.
     * The foreground color is set based on the selection and the icon
     * is set based on on leaf and expanded.
     *
     * @param tree общее дерево
     * @param value объект привязанный к листику (DefaultMutableTreeNode)
     * @param sel true - если выбран
     * @param expanded true - если данная ветка раскрыта
     * @param leaf true - если это конечный элемент (листик)
     * @param row номер элемента в дереве
     * @param hasFocus true - если элемент в фокусе
     * @return компонент для вывода
     */
    public Component getTreeCellRendererComponent(
        final JTree tree,
        final Object value,
        final boolean sel,
        final boolean expanded,
        final boolean leaf,
        final int row,
        final boolean hasFocus
    ) {
        final String stringValue = tree.convertValueToText(
            value, sel, expanded, leaf, row, hasFocus
        );

        hasFocused = hasFocus;
        setText(stringValue);
        isShow = null;
        if (sel) {
            setForeground(TEXT_SELECTION_COLOR);
        } else {
            setForeground(TEXT_COLOR);
        }

        final Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
        if (userObject instanceof LiveObjectElement) {
            final LiveObjectElement element = (LiveObjectElement) userObject;
            isShow = element.isShow();
            final Icon leafIcon = element.getIcon();
            setIcon((leafIcon != null) ? leafIcon : LEAF_ICON);
        } else if (expanded) {
            setIcon(OPEN_ICON);
        } else {
            setIcon(CLOSED_ICON);
        }
        selected = sel;
        if (isShow != null) {
            setBorder(LEAF_BORDER);
        } else {
            setBorder(OTHER_BORDER);
        }
        return this;
    }

    @Override
    public void paint(final Graphics gr) {

        Color bColor;
        final Icon currentI = getIcon();

        if (selected) {
            bColor = BACKGROUND_SELECTION_COLOR;
        } else {
            bColor = BACKGROUND_COLOR;
        }
        gr.setColor(bColor);
        if (currentI != null && getText() != null) {
            //final int offset = (currentI.getIconWidth() + getIconTextGap());
            gr.fillRect(0, 0, getWidth(), getHeight());
        } else {
            gr.fillRect(0, 0, getWidth(), getHeight());
        }
        if (hasFocused) {
            gr.setColor(BORDER_SELECTION_COLOR);
            BasicGraphicsUtils.drawDashedRect(gr, 0, 0, getWidth(), getHeight());
        }
        if (isShow != null) {
            gr.drawImage(isShow ? SHOW_ICON : NOT_SHOW_ICON, 1, 1, 14, 14, null);
        }
        super.paint(gr);
    }

}
