package com.lasgis.evolution.config;

import com.lasgis.evolution.model.SimpleModel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * <description>
 *
 * @author VLaskin
 * @since <pre>17.07.2019</pre>
 */
public class XmlStorageTest {

    private final static String fileNameSave = "../Evolution/src/test/resources/EvolutionSave.locale";
    private final static String fileNameLoad = "../Evolution/src/test/resources/EvolutionSave.locale";

    private final static String simpleModelSave = "../Evolution/src/test/resources/simpleModelSave.locale";
    private final static String simpleModelLoad = "../Evolution/src/test/resources/simpleModelLoad.locale";
    private SimpleModel model = SimpleModel.builder()
        .name("simpleModelSave")
        .intNumber(123)
        .dblNumber(3.1415926)
        .bool(true)
        .array(Arrays.asList(
            SimpleModel.builder().name("first").intNumber(1).bool(true).build(),
            SimpleModel.builder().name("second").intNumber(2).bool(false).build()
        ))
        .build();

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
    public void testSaveConfigLocale() {
        ConfigLocale config = new ConfigLocale(fileNameSave);
        final XmlStorage<ConfigLocale> storage = new XmlStorage<>();
        storage.save(config, fileNameSave);
    }

    @Test
    public void testSaveSimpleModel() {
        final XmlStorage<SimpleModel> storage = new XmlStorage<>();
        storage.save(model, simpleModelSave);
    }
}