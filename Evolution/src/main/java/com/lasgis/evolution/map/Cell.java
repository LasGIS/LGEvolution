/*
 * Cell.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import com.lasgis.evolution.map.element.IInterval2D;
import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.LiveObjectManager;
import com.lasgis.evolution.object.PlantBehaviour;
import com.lasgis.util.LGFormatter;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.lasgis.evolution.map.MatrixHelper.KEYS_ACCESSIBLE_FOR_SAVE;

/**
 * Ячейка карты. Эта ячейка содержит список различных элементов
 *
 * @author Laskin
 * @version 1.0
 * @since 16.06.2010 21:47:16
 */
@Info(type = InfoType.SAVE)
public class Cell implements IInterval2D {

    private static final Object[][] FORBIDDEN_PLANTS = new Object[][]{
        {EvolutionConstants.BARLEY_PLANT_KEY, 1.0},
        {EvolutionConstants.GRASS_PLANT_KEY, 1.0},
        {EvolutionConstants.CHAMOMILE_LEAF_KEY, 1.1},
        {EvolutionConstants.CHAMOMILE_FLOWER_KEY, 5.0},
        {EvolutionConstants.NECTAR_KEY, 10.0},
    };
    /**
     * индекс широты ячейки 0 - это юг, 100 - это север.
     */
    @Info(type = InfoType.SAVE)
    @Getter
    private final int indX;
    /**
     * индекс долгота ячейки 0 - это запад, 100 - это восток.
     */
    @Info(type = InfoType.SAVE)
    @Getter
    private final int indY;
    /**
     * именованный список элементов.
     */
    @Info(type = InfoType.SAVE)
    private final Map<String, Element> elements = new ConcurrentHashMap<>();
    /**
     * именованный список элементов.
     */
    @Info(type = InfoType.SAVE)
    private CopyOnWriteArrayList<AnimalBehaviour> animals = new CopyOnWriteArrayList<>();

    /**
     * Создание ячейки по нижнему левому углу.
     *
     * @param indexX индекс ячейки по широте (от низа - юг)
     * @param indexY индекс ячейки по долготе (от левого края - запад)
     */
    public Cell(final int indexX, final int indexY) {
        this.indX = indexX;
        this.indY = indexY;
    }

    /**
     * Возвращаем элемент по его коду.
     * Если элемента нет, то добавляем его.
     *
     * @param code код элемента
     * @return элемент
     */
    public final Element element(final String code) {
        Element element = elements.get(code);
        if (element == null) {
            element = new Element();
            elements.put(code, element);
        }
        return element;
    }

    @Override
    public double getNorth() {
        return Matrix.CELL_SIZE * (indX + 1);
    }

    @Override
    public double getSouth() {
        return Matrix.CELL_SIZE * indX;
    }

    @Override
    public double getWest() {
        return Matrix.CELL_SIZE * indY;
    }

    @Override
    public double getEast() {
        return Matrix.CELL_SIZE * (indY + 1);
    }

    /**
     * Рисовать ячейку.
     *
     * @param gr  графический контекст
     * @param rec прямоугольник для заполнения
     */
    public void drawPlant(final Graphics gr, final Rectangle rec) {
        gr.setColor(Color.WHITE);
        gr.fillRect(rec.x, rec.y, rec.width, rec.height);
        for (PlantBehaviour plant : LiveObjectManager.PLANTS) {
            if (plant.isShow()) {
                plant.drawPlant(gr, rec, this);
                for (PlantBehaviour elem : plant.subElements()) {
                    if (elem.isShow()) {
                        elem.drawPlant(gr, rec, this);
                    }
                }
            }
        }
    }

    /**
     * Вернуть массив ячеек, обрамляющих данную ячейку.
     * @param sight острота зрения
     * <pre>
     * Доступные значения для остроты зрения:<br/>
     * первый квадрат     [1, 2]<br/>
     * второй квадрат:    [3, 5, 6]<br/>
     * третий квадрат:    [7, 9, 11]<br/>
     * четвёртый квадрат: [12, 14, 15, 17, 19]<br/>
     * пятый квадрат:     [20, 22, 24, 25, 27, 29, 30]<br/>
     * </pre>
     * @return ячейки
     */
    public final Cell[] near(final int sight) {
        final NearPoint[] nearPoints = CellHelper.getNearPoints(sight, false);
        final Cell[] cells = new Cell[nearPoints.length];
        for (int i = 0; i < nearPoints.length; i++) {
            cells[i] = this.getCell(nearPoints[i]);
        }
        return cells;
    }

    /**
     * Вернуть ячейку, по смещениям относительно данной ячейки.
     *
     * @param delX смещение по x
     * @param delY смещение по y
     * @return ячейка, смещенная от этой
     */
    public final Cell offset(final int delX, final int delY) {
        return CellHelper.getCell(indX + delX, indY + delY);
    }

    /**
     * Вернуть ячейку, по смещениям относительно данной ячейки.
     *
     * @param pnt смещение по оси X (положительное смещение вверх)
     *            и по оси Y (положительное смещение вправо)
     * @return ячейка
     */
    public Cell getCell(final NearPoint pnt) {
        return getCell(pnt.x, pnt.y);
    }

    /**
     * Вернуть ячейку, по смещениям относительно данной ячейки.
     *
     * @param delX смещение по оси X (положительное смещение вверх)
     * @param delY смещение по оси Y (положительное смещение вправо)
     * @return ячейка
     */
    public final Cell getCell(final int delX, final int delY) {
        final int nx = (Matrix.MATRIX_SIZE_X + indX + delX) % Matrix.MATRIX_SIZE_X;
        final int ny = (Matrix.MATRIX_SIZE_Y + indY + delY) % Matrix.MATRIX_SIZE_Y;
        return Matrix.cell(nx, ny);
    }

    /**
     * расстояние от данной ячейки до предлагаемой.
     *
     * @param to предлагаемая ячейка
     * @return расстояние
     */
    public final double distance(final Cell to) {
        double dx = Math.abs(to.indX - indX);
        if (dx > Matrix.MATRIX_SIZE_X / 2.) {
            dx -= Matrix.MATRIX_SIZE_X;
        }
        double dy = Math.abs(to.indY - indY);
        if (dy > Matrix.MATRIX_SIZE_Y / 2.) {
            dy -= Matrix.MATRIX_SIZE_Y;
        }
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Вернуть список элементов с их значениями для этой ячейки.
     *
     * @param sb входной StringBuilder для записи
     * @return список элементов с их значениями
     */
    public StringBuilder elements(final StringBuilder sb) {
        final Set<String> set = elements.keySet();
        final String[] array = set.toArray(new String[set.size()]);
        Arrays.sort(array);
        for (String key : array) {
            final double value = element(key).value();
            if (value > 0.001) {
                sb.append(key).append("\t").append(LGFormatter.formatLog(value)).append('\n');
            }
        }
        return sb;
    }

    /**
     * Вернуть список элементов с их значениями для этой ячейки.
     *
     * @return список элементов с их значениями
     * @throws JSONException JSON Exception
     */
    public JSONObject getJsonElements() throws JSONException {
        final JSONObject json = new JSONObject();
        for (Map.Entry<String, Element> element : elements.entrySet()) {
            final String key = element.getKey();
            final Double value = element.getValue().value();
            if (KEYS_ACCESSIBLE_FOR_SAVE.contains(key) && value != 0.0) {
                json.put(key, value);
            }
        }
        return json;
    }

    /**
     * Вернуть список элементов с их значениями для этой ячейки.
     *
     * @return список элементов с их значениями
     */
    public Map<String, Double> getMapElements() {
        final Map<String, Double> map = new HashMap<>();
        for (Map.Entry<String, Element> element : elements.entrySet()) {
            map.put(element.getKey(), element.getValue().value());
        }
        return map;
    }

    /**
     * Check on empty elements for this cell.
     *
     * @return true if all elements have zero value
     */
    public boolean isEmpty() {
        for (Element value : elements.values()) {
            if (value.value() > 0.00001) {
                return false;
            }
        }
        return true;
    }

    /**
     * возвращаем Уникальный ключ ячейки.
     *
     * @return ключ ячейки
     */
    public String getKey() {
        return "" + getIndX() + "_" + getIndY();
    }

    /**
     * Получить доступ к животным в данной ячейке.
     * Добавлять или удалять животных можно только через Cell.
     *
     * @return список животных в этой ячейке
     */
    public final List<AnimalBehaviour> getAnimals() {
        return animals;
    }

    /**
     * Append the element if not present.
     *
     * @param animal element to be added to this list, if absent
     * @return <tt>true</tt> if the element was added
     */
    public boolean addAnimal(final AnimalBehaviour animal) {
        return animals.addIfAbsent(animal);
    }

    /**
     * @param animal element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean removeAnimal(final AnimalBehaviour animal) {
        return animals.remove(animal);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Cell cell = (Cell) o;

        return indX == cell.indX && indY == cell.indY;
    }

    @Override
    public int hashCode() {
        int result = indX;
        result = 31 * result + indY;
        return result;
    }

    /**
     * Коэффициент трудности перемещения. От 0..1.
     * Если > 1, то животное бежит быстрее паровоза.
     *
     * @return коэффициент трудности перемещения
     */
    public double moveTrouble() {
        double trouble = 1.0;
        final Element stone = elements.get(EvolutionConstants.STONE_KEY);
        final Element road = elements.get(EvolutionConstants.ROAD_KEY);
        if (stone != null) {
            trouble -= stone.value() / 50;
        }
        if (road != null) {
            trouble += road.value() / 20;
        }
        return trouble;
    }

    @Override
    public String toString() {
        return "Cell{X=" + indX + ", Y=" + indY + '}';
    }

    /**
     * Фактор препятствующий росту растений.
     *
     * @param excludePlants исключая эти
     * @return число условных конкурентов
     */
    public double plantForbid(final List<String> excludePlants) {
        double count = 0.0;
        for (Object[] plants : FORBIDDEN_PLANTS) {
            final String name = (String) plants[0];
            if (!excludePlants.contains(name)) {
                final double rating = (Double) plants[1];
                count += element(name).value() * rating;
            }
        }
        return count;
    }

    /**
     * Мусор, препятствующий росту.
     *
     * @return число условного мусора
     */
    public double obstacle() {
        return element(EvolutionConstants.EXCREMENT_KEY).value()
            + element(EvolutionConstants.ROAD_KEY).value() * 5
            + element(EvolutionConstants.STONE_KEY).value() * 5;
    }

    /**
     * Очищаем содержимое.
     */
    public void clear() {
        elements.clear();
    }
}
