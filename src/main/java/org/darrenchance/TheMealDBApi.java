package org.darrenchance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Darren Chance<br>
 * June 27, 2024<br>
 * <p>
 * <p>
 * The class {@code TheMealDBApi} is a java interface for getting recipe information from the
 * <a href="https://www.themealdb.com/api.php">The Meal DB</a>
 * allowing users to access information from the websites API programmatically in java.
 */
public class TheMealDBApi {
    /**
     * The Meal DB supported endpoints.
     */
    public enum Endpoint {
        MEAL_BY_NAME("https://www.themealdb.com/api/json/v1/1/search.php?s="),
        MEALS_BY_FIRST_LETTER("https://www.themealdb.com/api/json/v1/1/search.php?f="),
        MEAL_BY_ID("https://www.themealdb.com/api/json/v1/1/lookup.php?i="),
        MEAL_BY_RANDOM("https://www.themealdb.com/api/json/v1/1/random.php"),
        MEALS_BY_MAIN_INGREDIENT("https://www.themealdb.com/api/json/v1/1/filter.php?i="),
        MEALS_BY_CATEGORY("https://www.themealdb.com/api/json/v1/1/filter.php?c=");

        private final String url;

        Endpoint(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public enum Area{
        AMERICAN("American"),
        BRITISH("British"),
        CANADIAN("Canadian"),
        CHINESE("Chinese"),
        CROATIAN("Croatian"),
        DUTCH("Dutch"),
        EGYPTIAN("Egyptian"),
        FILIPINO("Filipino"),
        FRENCH("French"),
        GREEK("Greek"),
        INDIAN("Indian"),
        IRISH("Irish"),
        ITALIAN("Italian"),
        JAMAICAN("Jamaican"),
        JAPANESE("Japanese"),
        KENYAN("Kenyan"),
        MALAYSIAN("Malaysian"),
        MEXICAN("Mexican"),
        MOROCCAN("Moroccan"),
        POLISH("Polish"),
        PORTUGUESE("Portuguese"),
        RUSSIAN("Russian"),
        SPANISH("Spanish"),
        THAI("Thai"),
        TUNISIAN("Tunisian"),
        TURKISH("Turkish"),
        UKRAINIAN("Ukrainian"),
        UNKNOWN("Unknown"),
        VIETNAMESE("Vietnamese");

        private final String label;

        Area(String label) {
            this.label = label;
        }
        public String getLabel() {
            return label;
        }
    }


    /**
     * Search for recipes by main ingredient.
     * <p></p>
     *
     * @param search the main ingredient you are searching for.
     * @return one or more meals based represented as JSON String.
     */
    public String getMealsByMainIngredient(String search) {
        String url = Endpoint.MEALS_BY_MAIN_INGREDIENT.getUrl() + search;
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        JsonNode node;
        JsonNode node2;
        String json;
        json = response.body();
        //Getting node from JSON String
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            node = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        node = node.get("meals");
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"meals\":[");
        // Getting list of id's to ues later when getting each recipe
        for (JsonNode nd : node) {
            String recipeId = nd.get("idMeal").asText();
            try {
                node2 = objectMapper.readTree(getMealsById(recipeId));
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            if (i < (node.size())) {
                //System.out.println("Item# " + i);
                stringBuilder.append(node2.get("meals").get(0).toString());
                stringBuilder.append(",");
            } else {
                stringBuilder.append(node2.get("meals").get(0).toString());
                stringBuilder.append("]}");
                //System.out.println("Last item reached, item# "+ i);
                //System.out.println(stringBuilder);
            }
            i++;
        }
        return stringBuilder.toString();
    }

    /**
     * Search for recipes by category.
     * <p></p>
     *
     * @param search the category you are searching for.
     * @return one or more meals based represented as JSON String.
     */
    public String getMealsByMainCategory(String search) {
        String url = Endpoint.MEALS_BY_CATEGORY.getUrl() + search;
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        JsonNode node;
        JsonNode node2;
        String json = response.body();
        //Getting node from JSON String
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            node = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        node = node.get("meals");
        if (node.isNull()) {
            return null;
        }
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"meals\":[");
        for (JsonNode nd : node) {
            String recipeId = nd.get("idMeal").asText();
            try {
                node2 = objectMapper.readTree(getMealsById(recipeId));
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            if (i < (node.size())) {
                stringBuilder.append(node2.get("meals").get(0).toString());
                stringBuilder.append(",");
            } else {
                stringBuilder.append(node2.get("meals").get(0).toString());
                stringBuilder.append("]}");
            }
            i++;
        }
        return stringBuilder.toString();
    }

    /**
     * Searches for meals by recipe id.
     * <p></p>
     *
     * @param id the ID of the meal you are searching for.
     * @return a recipe represented as JSON string.
     */
    public String getMealsById(String id) {
        String url = Endpoint.MEAL_BY_ID.getUrl() + id;
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        return response.body();
    }


    /**
     * Searches for meals by name
     *
     * @param search the meal you are searching for.
     * @return a recipe json string.
     */
    public String getMealsByName(String search) {
        String url = Endpoint.MEAL_BY_NAME.getUrl() + search;
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        return response.body();
    }

    /**
     * Get a single random recipe in JSON from endpoint associated {@link org.darrenchance.TheMealDBApi.Endpoint}
     * <p></p>
     *
     * @return a JSON string representing a single random recipe
     */
    public String getMealsByRandom() {
        String url = Endpoint.MEAL_BY_RANDOM.getUrl();
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        return response.body();
    }

    /**
     * Helper class that returns an {@link HttpResponse}
     * <p></p>
     *
     * @param url a url
     * @return the HttpResponse
     */
    private static HttpResponse<String> getStringHttpResponse(String url) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

}
