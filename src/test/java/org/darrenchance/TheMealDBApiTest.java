package org.darrenchance;

import static org.junit.jupiter.api.Assertions.*;

class TheMealDBApiTest {

    @org.junit.jupiter.api.Test
    void getMealsByMainIngredient() {
        TheMealDBApi api = new TheMealDBApi();
        String json = api.getMealsByMainIngredient("chicken");
        assertInstanceOf(String.class, json);
        assertAll(
                () -> assertTrue(json.length() > 1),
                () -> assertEquals("{\"meals\":[",api.getMealsByMainIngredient("123"))
        );
    }

    @org.junit.jupiter.api.Test
    void getMealsByMainCategory() {
        TheMealDBApi api = new TheMealDBApi();
        assertAll(
                () -> assertEquals("",api.getMealsByMainCategory("Fish"))
        );
    }

    @org.junit.jupiter.api.Test
    void getMealsById() {
    }

    @org.junit.jupiter.api.Test
    void getMealsByName() {
    }

    @org.junit.jupiter.api.Test
    void getMealsByRandom() {
    }
}