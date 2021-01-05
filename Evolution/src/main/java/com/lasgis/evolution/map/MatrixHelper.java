/*
 * MatrixHelper.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2021 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.AnimalManagerBehaviour;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.LiveObjectManager;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Class MatrixHelper.
 *
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public class MatrixHelper {

    public static final Set<String> KEYS_ACCESSIBLE_FOR_SAVE = getKeysAccessibleForSave();

    public static final Map<String, AnimalManagerBehaviour> NAME_TO_ANIMAL_MANAGER_MAP = getNameAnimal2ManagerMap();

    private MatrixHelper() {
    }

    private static Set<String> getKeysAccessibleForSave() {
        final Field[] fields = EvolutionConstants.class.getDeclaredFields();
        return Arrays.stream(fields).filter(field -> {
            final Info info = field.getAnnotation(Info.class);
            if (info != null && field.getType().getName().equals("java.lang.String")) {
                for (InfoType infoType : info.type()) {
                    if (infoType == InfoType.SAVE) {
                        return true;
                    }
                }
            }
            return false;
        }).map(field -> {
            try {
                return (String) field.get(null);
            } catch (IllegalAccessException ex) {
                log.error(ex.getMessage(), ex);
            }
            return null;
        }).collect(Collectors.toSet());
    }

    private static Map<String, AnimalManagerBehaviour> getNameAnimal2ManagerMap() {
        final Map<String, AnimalManagerBehaviour> map = new HashMap<>();
        for (AnimalManagerBehaviour animal : LiveObjectManager.ANIMALS) {
            map.put(animal.getName(), animal);
        }
        return map;
    }

    /**
     * Сохраняем текущий контекст в файл.
     *
     * @param fileName имя файла
     * @throws IOException   IO Exception
     * @throws JSONException JSON Exception
     */
    public static void matrixContextSave(final String fileName) throws IOException, JSONException {
        final FileWriter fw = new FileWriter(fileName);
        fw.write("{");
        saveMatrixElements(fw);
        fw.write(",\n");
        saveAnimals(fw);
        fw.write("}");
        fw.flush();
        fw.close();
    }

    /**
     * Сохраняем Элементы матрицы.
     *
     * @param fw FileWriter
     * @throws IOException   IO Exception
     * @throws JSONException JSON Exception
     */
    public static void saveMatrixElements(final FileWriter fw) throws IOException, JSONException {
        fw.write("\"matrix\":[");
        boolean isFirst = true;
        for (int y = 0; y < Matrix.MATRIX_SIZE_Y; y++) {
            for (int x = 0; x < Matrix.MATRIX_SIZE_X; x++) {
                final Cell cell = Matrix.cell(x, y);
                assert cell != null;
                if (!cell.isEmpty()) {
                    if (isFirst) {
                        fw.write("{");
                        isFirst = false;
                    } else {
                        fw.write(",{");
                    }
                    fw.write("\"x\":");
                    fw.write(Double.toString(cell.getIndX()));
                    fw.write(",\"y\":");
                    fw.write(Double.toString(cell.getIndY()));
                    fw.write(",\"elements\":");
                    JSONObject json = cell.getJsonElements();
                    fw.write(json.toString());
                    fw.write("}\n");
                }
            }
        }
        fw.write("]");
    }

    /**
     * Сохраняем Животных.
     *
     * @param fw FileWriter
     * @throws IOException   IO Exception
     * @throws JSONException JSON Exception
     */
    public static void saveAnimals(final FileWriter fw) throws IOException, JSONException {
        fw.write("\"animals\":[");
        boolean isFirst = true;
        final List<AnimalBehaviour> animals = new ArrayList<>();
        LiveObjectManager.manipulationAnimals(animals::add);
        for (AnimalBehaviour animal : animals) {
            if (isFirst) {
                isFirst = false;
            } else {
                fw.write(",\n");
            }
            fw.write(animal.getJsonObject().toString());
        }
        fw.write("]");
    }

    /**
     * Загружаем контекст из ранее сохранённого файла.
     *
     * @param fileName имя файла
     * @throws FileNotFoundException File Not Found Exception
     * @throws JSONException         JSON Exception
     */
    public static void loadMatrixContext(final String fileName) throws FileNotFoundException, JSONException {
        final FileReader reader = new FileReader(fileName);
        final JSONObject json = new JSONObject(new JSONTokener(reader));
        loadMatrixElements(json.getJSONArray("matrix"));
        loadAnimals(json.getJSONArray("animals"));
    }

    /**
     * Загружаем элементы матрицы.
     *
     * @param array массив элементов мартицы как JSONArray объект
     * @throws JSONException JSON Exception
     */
    public static void loadMatrixElements(final JSONArray array) throws JSONException {
        Matrix.clear();
        for (int i = 0; i < array.length(); i++) {
            final JSONObject json = array.getJSONObject(i);
            final int x = json.getInt("x");
            final int y = json.getInt("y");
            final JSONObject elements = json.getJSONObject("elements");
            final Cell cell = Matrix.cell(x, y);
            if (cell != null) {
                final Iterator itr = elements.keys();
                while (itr.hasNext()) {
                    final String key = (String) itr.next();
                    final int value = elements.getInt(key);
                    cell.element(key).setValue(value);
                }
            }
        }
    }

    /**
     * Загружаем всех сохраненных ранее животных.
     *
     * @param array массив сохраненных ранее животных как JSONArray объект
     * @throws JSONException JSON Exception
     */
    public static void loadAnimals(final JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            final JSONObject json = array.getJSONObject(i);
            final AnimalManagerBehaviour manager = NAME_TO_ANIMAL_MANAGER_MAP.get(json.getString("name"));
            final double latitude = json.getDouble("latitude");
            final double longitude = json.getDouble("longitude");
            manager.createSavedAnimal(latitude, longitude, json);
        }
    }
}
