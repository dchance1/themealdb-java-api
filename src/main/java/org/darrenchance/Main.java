package org.darrenchance;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {


    public static void main(String[] args) {
        TheMealDBApi api = new TheMealDBApi();
        String json = api.getMealsByMainIngredient("beef");

        ArrayList<Recipe> recipes = Recipe.importRecipes(json, Recipe.Api.THE_MEAL_DB );

        int i = 0;
        for (Recipe recipe : recipes) {
            i++;
            System.out.println("-----------------------------------------------");
            System.out.println("Recipe #" + i + " - " + recipe.getName());
            System.out.println();
            System.out.println("Instructions:\n" + recipe.getInstructions());
            System.out.println();
            System.out.println("Ingredients:");
            ArrayList<String> ingredients = recipe.getIngredients();
            for (String ingredient: ingredients){
                System.out.println(ingredient);
            }

            //System.out.println(json2);
        }
    }
}