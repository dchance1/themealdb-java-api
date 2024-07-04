package org.darrenchance;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TheMealDBApi api = new TheMealDBApi();
        String json = api.getMealsByRandom();

        ArrayList<Recipe> recipes = Recipe.importRecipes(json, Recipe.Api.THE_MEAL_DB);
        if (recipes == null) {
            System.exit(0);
        }

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
            for (String ingredient : ingredients) {
                System.out.println(ingredient);
            }
        }
    }
}