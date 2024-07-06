package org.darrenchance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

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
     * @param recipesJson a JSON string representing a recipe
     * @param api   the API used to get the recipe
     * @return an ArrayList of Recipe objects
     */
    public static ArrayList<Recipe> importRecipes(String recipesJson, Api api) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        switch (api){
            case THE_MEAL_DB:
                recipes = getRecipesFromTheMealDB(recipesJson);
                System.out.println("Importing Recipes with API: " + api);
                return recipes;
            case RECIPE_DOT_COM://Holder case, not implemented yet, just here as example.
                System.out.println("Importing Recipes with API: " + api);
                return null;
            default:
                System.out.println("API support for " + api+ " not available");
                return null;
        }
    }



    public Recipe(String name, String instructions, ArrayList<String> ingredients) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    private static ArrayList<Recipe> getRecipesFromTheMealDB(String recipesJson){
        ArrayList<Recipe> recipes = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        String json = recipesJson;
        if (json == null){
            System.out.println("JSON was blank/null");
            return null;
        }else if(json.isBlank()){
            System.out.println("JSON was blank/null, contained text: "+ json);
            return null;
        }
        //Getting node from JSON String
        try {
            node = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {

            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        //Par recipes
        node = node.get("meals");
        //Checking if node is array, if it is iterating through
        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                ArrayList<String> ingredients = new ArrayList<>();
                String strMeal = jsonNode.get("strMeal").asText();
                for (int i = 0; i < 20; i++) {

                    String num = String.valueOf(i + 1);

                    String ingredient;
                    //Checking for null ingredient values
                    boolean notNull = !jsonNode.get("strIngredient" + num).asText().isBlank() && !jsonNode.get("strIngredient" + num).isNull();
                    if (notNull) {
                        ingredient = num + ")" + jsonNode.get("strIngredient" + num).asText(null) + " - " + jsonNode.get("strMeasure" + num).asText(null);
                        ingredients.add(ingredient);
                    }
                }
                String inst = "strIngredient";
                String num = String.valueOf(1);

                String strInstructions = jsonNode.get("strInstructions").asText();

                Recipe recipe;
                recipe = new Recipe(strMeal, strInstructions, ingredients);
                recipes.add(recipe);
            }
        }
        return recipes;

    }


}
