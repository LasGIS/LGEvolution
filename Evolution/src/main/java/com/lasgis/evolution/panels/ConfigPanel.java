/**
 * @(#)ConfigPanel.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import com.lasgis.evolution.object.AnimalManagerBehaviour;
import com.lasgis.evolution.object.LiveObjectElement;
import com.lasgis.evolution.object.LiveObjectManager;
import com.lasgis.evolution.object.PlantBehaviour;
import com.lasgis.util.SettingMenuItem;
import com.lasgis.util.Util;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Панель конфигурации.
 * @author VLaskin
 * @version 1.0 Date: 13.01.2005 16:38:05
 */
@Slf4j
public class ConfigPanel extends JPanel implements
    TreeSelectionListener, TreeExpansionListener, MouseWheelListener, MouseListener {

    /** корневой узел дерева. */
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    /** дерево конфигурации. */
    private JTree tree;
    /** панель для прокрутки дерева. */
    private JScrollPane treeScroll;
    /** панель для информации об ячейках. */
    private final JTextArea plantInfo = new JTextArea();
    /** панель для информации о животных. */
    private final JTextArea animalInfo = new JTextArea();

    /** Настройка выпадающего меню. */
    private SettingMenuItem[] setPopUpMenu = {
        new SettingMenuItem(
            "Visible", null, "Сделать выделенные слои видимыми/невидимыми", this::jMenuSetUnVisibleAction,
            null
        ),
        new SettingMenuItem(
                "File", "openFile.gif", "", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "Open", "openFile.gif", "Открываем приложение", null, null
                ),
                new SettingMenuItem(
                    "Exit", "closeFile.gif", "Закрываем приложение", this::jMenuSetUnVisibleAction, null
                )
            }
        )
    };

    /**
     * Сделать выделенные слои видимыми/невидимыми.
     * @param e  {@link java.awt.event.ActionEvent}
     */
    public void jMenuSetUnVisibleAction(final ActionEvent e) {
        final String comm = e.getActionCommand();
    }

    /**
     * Called whenever the value of the selection changes.
     * @param event {@link javax.swing.event.TreeSelectionEvent}
     */
    public void valueChanged(final TreeSelectionEvent event) {
        final TreePath path = event.getNewLeadSelectionPath();
        final boolean isAdded = event.isAddedPath();
    }

    /**
     * Called whenever an item in the tree has been expanded.
     * @param event TreeExpansionEvent
     */
    public void treeExpanded(final TreeExpansionEvent event) {
        final TreePath path = event.getPath();
    }

    /**
     * Called whenever an item in the tree has been collapsed.
     * @param event TreeExpansionEvent
     */
    public void treeCollapsed(final TreeExpansionEvent event) {
        final TreePath path = event.getPath();
    }

    /**
     * Invoked when the mouse wheel is rotated.
     * @param event MouseWheelEvent
     */
    public void mouseWheelMoved(final MouseWheelEvent event) {
        final int modif = event.getModifiersEx();
        final int rotation = event.getWheelRotation();
        /*if ((modif & InputEvent.CTRL_DOWN_MASK) != 0) {
            //
        } else*/
        if ((modif & InputEvent.SHIFT_DOWN_MASK) != 0) {
            final JScrollBar sb = treeScroll.getHorizontalScrollBar();
            final int val = sb.getValue();
            sb.setValue(val + rotation * 15);
        } else {
            final JScrollBar sb = treeScroll.getVerticalScrollBar();
            final int val = sb.getValue();
            sb.setValue(val + rotation * 15);
        }
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     * @param event MouseEvent
     */
    public void mouseClicked(final MouseEvent event) {
        final Point pnt = event.getPoint();
        final int button = event.getButton();
        if (button == MouseEvent.BUTTON1) {
            final JTree eventTree = (JTree) event.getSource();
            final int row = eventTree.getLeadSelectionRow();
            if (row >= 0) {
                final Object object = eventTree.getLastSelectedPathComponent();
                if (object instanceof DefaultMutableTreeNode) {
                    final Object userObject = ((DefaultMutableTreeNode) object).getUserObject();
                    if (userObject instanceof LiveObjectElement) {
                        final LiveObjectElement element = (LiveObjectElement) userObject;
                        final TreePath path = eventTree.getSelectionPath();
                        final Rectangle rect = eventTree.getPathBounds(path);
                        if (rect != null) {
                            rect.width = 16;
                            if (rect.contains(pnt)) {
                                element.setShow(!element.isShow());
                                eventTree.repaint();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param event MouseEvent
     */
    public void mousePressed(final MouseEvent event) {
        final Point pnt = event.getPoint();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * @param event MouseEvent
     */
    public void mouseReleased(final MouseEvent event) {
        final Point pnt = event.getPoint();
    }

    /**
     * Method for implements MouseListener.
     * @param event MouseEvent
     */
    public void mouseEntered(final MouseEvent event) {
        final Point pnt = event.getPoint();
    }

    /**
     * Invoked when the mouse exits a component.
     * @param event MouseEvent
     */
    public void mouseExited(final MouseEvent event) {
        final Point pnt = event.getPoint();
    }

    /**
     * Конструктор.
     */
    public ConfigPanel() {
        super();
        tree = new JTree(root);
        tree.setBackground(MapPanel.PANEL_GRAY_COLOR);
        treeScroll = new JScrollPane(tree);
        treeScroll.setViewportView(tree);

        final Font font = new Font("Arial", Font.PLAIN, 12);

        /** панель для информации об ячейках. */
        plantInfo.setFont(font);
        plantInfo.setTabSize(16);
        plantInfo.setColumns(2);
        final JScrollPane plantInfoScroll = new JScrollPane(plantInfo);
        plantInfoScroll.setViewportView(plantInfo);

        /** панель для информации о животных. */
        animalInfo.setFont(font);
        animalInfo.setTabSize(16);
        animalInfo.setColumns(2);
        final JScrollPane animalInfoScroll = new JScrollPane(animalInfo);
        animalInfoScroll.setViewportView(animalInfo);

        /* разделительная панелька */
        final JSplitPane infoSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        infoSplitPane.setContinuousLayout(true);
        infoSplitPane.add(plantInfoScroll, JSplitPane.TOP);
        infoSplitPane.add(animalInfoScroll, JSplitPane.BOTTOM);
        infoSplitPane.setDividerLocation(100);
        infoSplitPane.setLastDividerLocation(100);
        infoSplitPane.setResizeWeight(0.0);

        /* разделительная панелька */
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.add(treeScroll, JSplitPane.TOP);
        splitPane.add(infoSplitPane, JSplitPane.BOTTOM);
        splitPane.setDividerLocation(100);
        splitPane.setLastDividerLocation(100);
        splitPane.setResizeWeight(0.0);

        setLayout(new BorderLayout());
        /* панель режима. */
        final RegimePanel regime = new RegimePanel();
        add(regime, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        createPopupMenu();

        tree.addTreeSelectionListener(this);
        tree.addTreeExpansionListener(this);
        tree.addMouseWheelListener(this);
        tree.addMouseListener(this);
        final DefaultMutableTreeNode rootRaster = new DefaultMutableTreeNode("Растения");
        final DefaultMutableTreeNode rootVector = new DefaultMutableTreeNode("Животные");
        root.add(rootRaster);
        root.add(rootVector);
        tree.expandRow(0);
        final ConfigTreeCellRenderer renderer = new ConfigTreeCellRenderer();
        tree.setCellRenderer(renderer);

        //super.setCellRenderer();
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setScrollsOnExpand(true);

        loadConfig();
    }

    /**
     * Загружаем файл конфигурации и создаём дерево объектов карты.
     */
    public void loadConfig() {

        // добавляем в растения PlantBehaviour
        final DefaultMutableTreeNode rootPlant = (DefaultMutableTreeNode) root.getFirstChild();
        rootPlant.removeAllChildren();
        for (PlantBehaviour plant : LiveObjectManager.PLANTS) {
            final DefaultMutableTreeNode node = new DefaultMutableTreeNode(plant);
            rootPlant.add(node);
            for (PlantBehaviour elem : plant.subElements()) {
                node.add(new DefaultMutableTreeNode(elem));
            }
            plant.init();
        }

        // добавляем в животные AnimalManagerBehaviour
        final DefaultMutableTreeNode rootAnimal = (DefaultMutableTreeNode) root.getChildAfter(rootPlant);
        rootAnimal.removeAllChildren();
        for (AnimalManagerBehaviour animal : LiveObjectManager.ANIMALS) {
            rootAnimal.add(new DefaultMutableTreeNode(animal));
            animal.init();
        }
        tree.updateUI();
    }

    /**
     * .
     * @return ,
     */
    public List<LiveObjectElement> getSelectedElements() {
        final ArrayList<LiveObjectElement> elements = new ArrayList<>();
        final TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            for (TreePath path : paths) {
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                final Object obj = node.getUserObject();
                if (obj instanceof LiveObjectElement) {
                    elements.add((LiveObjectElement) obj);
                }
            }
        }
        return elements;
    }

    /**
     * Создаём и загружаем выпадающее меню для дерева слоёв.
     */
    public void createPopupMenu() {

        /* создаём и настраиваем выпадающее меню */
        final JPopupMenu popup = new JPopupMenu();
        for (SettingMenuItem aSetMenu : setPopUpMenu) {
            popup.add(Util.createImageMenuItem(aSetMenu));
        }

        //Add listener to the text area so the popup menu can come up.
        final MouseListener popupListener = new PopupListener(popup);
        tree.addMouseListener(popupListener);
    }

    public JTextArea getAnimalInfo() {
        return animalInfo;
    }

    public JTextArea getPlantInfo() {
        return plantInfo;
    }
}
