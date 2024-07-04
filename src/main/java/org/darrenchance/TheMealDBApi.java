package org.darrenchance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Darren Chance<br>
 * June 27, 2024<br>
 * <p>
 * <p>
 * The class {@code TheMealDBApi} is a java interface for getting recipe information from the https://www.themealdb.com/api.php
 * allowing user to access information from the website programmatically.
 */
public class TheMealDBApi {

    public enum Endpoint {
        /*

         */
        MEAL_BY_NAME("https://www.themealdb.com/api/json/v1/1/search.php?s="), MEALS_BY_FIRST_LETTER("https://www.themealdb.com/api/json/v1/1/search.php?f="), MEAL_BY_ID("https://www.themealdb.com/api/json/v1/1/lookup.php?i="), MEAL_BY_RANDOM("https://www.themealdb.com/api/json/v1/1/random.php"), MEAL_BY_MAIN_INGREDIENT("https://www.themealdb.com/api/json/v1/1/filter.php?i=");

        private final String url;

        Endpoint(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    // TODO work in progress, only returns meal id's, need to work it to be able to use
    //  id's to get recipes from MEAL_BY_ID Endpoint
    public String getMealsByMainIngredient(String search) {
        String url = Endpoint.MEAL_BY_MAIN_INGREDIENT.getUrl() + search;
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        JsonNode node = null;
        JsonNode node2 = null;
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
        ArrayList<Integer> mealIds = new ArrayList<>();
        ArrayList<String> testerStrings = new ArrayList<>();
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

    public String getMealsById(String id) {
        String url = Endpoint.MEAL_BY_ID.getUrl() + id;
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        return response.body();
    }

    /**
     * Searches for meals by name
     *
     * @param search the meal you are searching for
     * @return a recipe json string
     */
    public String getMealsByName(String search) {
        String url = Endpoint.MEAL_BY_NAME.getUrl() + search;
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        return response.body();
    }

    public String getMealsByRandom() {
        String url = Endpoint.MEAL_BY_RANDOM.getUrl();
        //Getting JSON string from website API
        HttpResponse<String> response = getStringHttpResponse(url);
        return response.body();
    }

    /**
     * Helper class that returns an {@link HttpResponse}
     * <p>yo</p>
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
