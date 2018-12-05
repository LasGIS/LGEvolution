/**
 * @(#)MatrixHelper.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * The Class MatrixHelper.
 * @author vladimir.laskin
 * @version 1.0
 */
public class MatrixHelper {

    private MatrixHelper() {
    }

    /**
     * Сохраняем текущий контекст в файл.
     * @param fileName имя файла
     * @throws IOException IO Exception
     * @throws JSONException JSON Exception
     */
    public static void matrixContextSave(String fileName) throws IOException, JSONException {
        FileWriter fw = new FileWriter(fileName);
        fw.write("{\"matrix\":[");
        boolean isFirst = true;
        for (int y = 0; y < Matrix.MATRIX_SIZE_Y; y++) {
            for (int x = 0; x < Matrix.MATRIX_SIZE_X; x++) {
                Cell cell = Matrix.getMatrix().getCell(x, y);
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
        fw.write("]}");
        fw.flush();
        fw.close();
    }

    /**
     * Загружаем контекст из ранее сохранённого файла.
     * @param fileName имя файла
     * @throws FileNotFoundException File Not Found Exception
     * @throws JSONException JSON Exception
     */
    public static void matrixContextLoad(final String fileName) throws FileNotFoundException, JSONException {
        final FileReader reader = new FileReader(fileName);
        JSONObject json = new JSONObject(new JSONTokener(reader));
        final JSONArray array = json.getJSONArray("matrix");
        for (int i = 0; i < array.length(); i++) {
            json = array.getJSONObject(i);
            final int x = json.getInt("x");
            final int y = json.getInt("y");
            final JSONObject elements = json.getJSONObject("elements");
            final Cell cell = Matrix.getMatrix().getCell(x, y);
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

}
