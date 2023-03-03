package org.input_processing;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestWebscrape {
    @Test
    public void testWebscraper() {
        List<String> urls = Arrays.asList("https://cookieandkate.com/classic-minestrone-soup-recipe/", 
                                          "https://www.allrecipes.com/recipe/166160/juicy-thanksgiving-turkey/",
                                          "https://www.thewholesomedish.com/the-best-classic-chili/",
                                          "https://tasty.co/recipe/easy-chicken-alfredo-penne",
                                          "https://www.recipetineats.com/oven-baked-chicken-and-rice/");

        for (String url: urls) {
            Webscrape scraper = new Webscrape(url);
            InputRecipe inRecipe = scraper.extractRecipe();
        }
    }
}
