package com.lasgis.evolution.config;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

/**
 * <description>
 *
 * @author VLaskin
 * @since <pre>17.07.2019</pre>
 */
public class XmlStorageTest {

    private final static String fileNameSave = "../Evolution/src/test/resources/EvolutionSave.locale";
    private final static String fileNameLoad = "../Evolution/src/test/resources/EvolutionSave.locale";

    @BeforeMethod
    public void setUp() {
    }

    @Test
    public void testLoad() {
        ConfigLocale config = new ConfigLocale(fileNameLoad);
        final XmlStorage<ConfigLocale> storage = new XmlStorage<>();
        storage.load(config, fileNameLoad);
    }

    @Test
    public void testSave() {
        ConfigLocale config = new ConfigLocale(fileNameSave);
        final XmlStorage<ConfigLocale> storage = new XmlStorage<>();
        storage.save(config, fileNameSave);
    }
}