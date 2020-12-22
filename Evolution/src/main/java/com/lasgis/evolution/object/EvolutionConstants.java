/*
 * EvolutionConstants.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

/**
 * Эти константы используются для работы со значениями
 * в ячейках матрицы.
 * @author vladimir.laskin
 * @version 1.0
 */
@Info(type = InfoType.STAT)
public interface EvolutionConstants {

    /** Земля даёт жизнь растениям. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String GROUND_KEY = "Земля";
    /** Максимально возможное количество земли. */
    double MAX_GROUND_VALUE = 1000.0;
    /** коэффициент: растения забирают сок земли */
    double GROUND_PLAN_DELIMITER = 1000;
    /** Выделения животных, в том числе труп. Кал пагубно влияет на жизнь растений и может их погубить. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String EXCREMENT_KEY = "Фекалии";
    /** То, что осталось от травы после её умирания. Сено не влияет на рост растений. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String HAY_PLANT_KEY = "Сено";
    /** питательность сена.*/
    double HAY_NUTRITIONAL = 1.5;

    String OBSTACLE_KEY = "Препятствия";
    /** По камню трудно передвигаться и растениям и животным. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String STONE_KEY = "Камень";
    /** свинья пробегая превращает землю в дорогу. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String ROAD_KEY = "Дорога";

    /** Трава, растение. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String GRASS_PLANT_KEY = "Трава";
    /** питательность травы.*/
    double GRASS_NUTRITIONAL = 1.0;
    /** Текущее Время жизни травы на данном участке. Если время увеличивается больше определённого в правилах,
     *  трава умирает и превращается в сено. */
    String GRASS_PLANT_TIME = "время трава";
    /** Овёс, растение. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String BARLEY_PLANT_KEY = "Овёс";
    /** питательность овса.*/
    double BARLEY_NUTRITIONAL = 1.0;
    /** Текущее время жизни овса на данном участке. */
    String BARLEY_PLANT_TIME = "время овёс";
    /** Мясо, остаток от коровы. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String MEAT_PLANT_KEY = "Мясо";
    /** питательность мяса.*/
    double MEAT_NUTRITIONAL = 2.5;

    /** ромашка. */
    String CHAMOMILE_KEY = "Ромашка";
    /** листья ромашки. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String CHAMOMILE_LEAF_KEY = "Ромашка(листья)";
    /** питательность листьев ромашки.*/
    double CHAMOMILE_LEAF_NUTRITIONAL = 1.0;
    /** Цветок ромашки. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String CHAMOMILE_FLOWER_KEY = "Ромашка(цветок)";
    /** питательность цветка ромашки.*/
    double CHAMOMILE_FLOWER_NUTRITIONAL = 1.5;

    /**
     * Нектар сам по себе не очень питательный.
     * Напитком богов он становится превращаясь в мед.
     */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String NECTAR_KEY = "нектар";
    /** питательность нектара.*/
    double NECTAR_NUTRITIONAL = 5.0;
    /** Мишка очень любит мед. */
    @Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    String HONEY_KEY = "мед";
    /** питательность меда.*/
    double HONEY_NUTRITIONAL = 10.;

    /**
     * Запах (животного).
     * Добавляется для обозначения пребывания животного
     * или намерения пребывания животного в данном месте.
     */
    String SMELL_KEY = " запах";
    /** название свинья. */
    String TARGET_KEY = " цель";
    /** название свинья. */
    String PIG_KEY = "свинья";
    /** намерение свиньи,
     * т.е. в это место свинья желает попасть в ближайшее время. */
    String PIG_TARGET_KEY = PIG_KEY + TARGET_KEY;
    /** название кролик. */
    String RABBIT_KEY = "кролик";
    /** намерение кролика. */
    String RABBIT_TARGET_KEY = RABBIT_KEY + TARGET_KEY;
    /** название мышка. */
    String MOUSE_KEY = "мышка";
    /** намерение мышки. */
    String MOUSE_TARGET_KEY = MOUSE_KEY + TARGET_KEY;
    /** название грифа. */
    String VULTURE_KEY = "Гриф";
    /** намерение грифа. */
    String VULTURE_TARGET_KEY = VULTURE_KEY + TARGET_KEY;
    /** название муравей. */
    String ANT_KEY = "муравей";
    /** намерение муравья. */
    String ANT_TARGET_KEY = ANT_KEY + TARGET_KEY;
    //** запах смерти. */
    //String SMELL_OF_DEATH = "запах смерти";
    /** сколько сыплется песка. */
    double SMELL_DEATH_FACTOR = 0.0333;
    /** название мышка. */
    String TEST_ANIMAL_KEY = "лабораторное животное";
}
