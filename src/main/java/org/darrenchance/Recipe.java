package org.darrenchance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * A recipe object
 * <p></p>
 * This class provides simple getters and setter along with methods to import create an Array of Recipe objects based
 * on a {@link Api} the has been implemented by this class.
 */
public class Recipe {
    public enum Api {
        THE_MEAL_DB("https://www.themealdb.com/api.php"),
        RECIPE_DOT_COM("https://www.google.com");
        private final String url;

        Api(String url) {
            this.url = url;
        }
    }

    private String name;
    private String instructions;
    private ArrayList<String> ingredients;
    private String imageUrl;

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", instructions='" + instructions + '\'' +
                ", ingredients=" + ingredients +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    /**
     * Parses JSON to an Array of Recipe objects based on api.
     * <p></p>
     *
     * @param recipesJson a JSON string representing a recipe
     * @param api         the API used to get the recipe
     * @return an ArrayList of Recipe objects
     */
    public static ArrayList<Recipe> importRecipes(String recipesJson, Api api) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        switch (api) {
            case THE_MEAL_DB:
                recipes = getRecipesFromTheMealDB(recipesJson);
                System.out.println("Importing Recipes with API: " + api);
                return recipes;
            case RECIPE_DOT_COM://Holder case, not implemented yet, just here as example.
                System.out.println("Importing Recipes with API: " + api);
                return null;
            default:
                System.out.println("API support for " + api + " not available");
                return null;
        }
    }

    public Recipe(String name, String instructions, ArrayList<String> ingredients) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    /**
     * Parses JSON based on The Meal DB JSON schema
     * <p></p>
     * @param recipesJson string from
     * @return
     */
    private static ArrayList<Recipe> getRecipesFromTheMealDB(String recipesJson) {
        String json = recipesJson;

        JsonNode node = null;
        ObjectMapper objectMapper = new ObjectMapper();
        //Getting node from JSON String
        try {
            node = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
        ArrayList<Recipe> recipes = new ArrayList<>();
        //Parse recipes
        node = node.get("meals");
        //Checking if node is array, if it is iterating through
        // TODO need to catch NullPointerException in case object is null
        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                ArrayList<String> ingredients = new ArrayList<>();
                String strMeal = jsonNode.get("strMeal").asText();
                String strInstructions = jsonNode.get("strInstructions").asText();

                //Getting ingredients, with the meal db, recipes have 20 ingredients with keys of i.e. strIngredient1,
                // strIngredient2, strIngredient3, strIngredient4
                for (int i = 0; i < 20; i++) {
                    String num = String.valueOf(i + 1);
                    String ingredient;
                    //Checking for null ingredient values
                    boolean notNull = !jsonNode.get("strIngredient" + num).asText().isBlank() && !jsonNode.get("strIngredient" + num).isNull();
                    if (notNull) {
                        //Ingredient example string "1) Onion - 1/2"
                        // then adding to ingredients list
                        ingredient = num + ")" + jsonNode.get("strIngredient" + num).asText(null) +
                                " - " + jsonNode.get("strMeasure" + num).asText(null);
                        ingredients.add(ingredient);
                    }
                }
                Recipe recipe;
                recipe = new Recipe(strMeal, strInstructions, ingredients);
                recipes.add(recipe);
            }
        }
        return recipes;
    }
}
