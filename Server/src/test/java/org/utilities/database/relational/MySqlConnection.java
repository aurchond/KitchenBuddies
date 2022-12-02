package org.utilities.database.relational;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlConnection {
    private static final String url = "jdbc:mysql://localhost";
    private static final String user = "shadi";
    private static final String password = "password";
    public static void main(String args[]) {
        try (Connection con = DriverManager.getConnection(url, user, password); )
        {
            String useKB = "USE KitchenBuddies;";

            try (PreparedStatement prep = con.prepareStatement(useKB);) {
                prep.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            int customer = 1;
            String username = "Shadi";
            String password = "abc123";
            int skill = 0;
            int recipe = 1;

            //addCustomer(con, customer, username, password, skill);
            //addToAllRecipes(con, "Curry", "www.allrecipes.com/curry", recipe); 
            addToFavRecipes(con, customer, recipe);
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    private static void addCustomer(Connection con, int customerId, String username, String password, int skill) {
        String addCustomer = "INSERT INTO CustomerInfo(CustomerId, User, Password, Skill) VALUES(?, ?, ?, ?);";
        try (PreparedStatement prep = con.prepareStatement(addCustomer);) {
            
            //fill in parametrized query
            prep.setInt(1, customerId); //autonumber customers?
            prep.setString(2, username);
            prep.setString(3, password);
            prep.setInt(4, skill);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addToAllRecipes(Connection con, String recipeName, String recipeUrl, int recipeId) {
        String addRecipe = "INSERT INTO AllRecipes(RecipeId, Name, Url) VALUES(?, ?, ?);";
        try (PreparedStatement prep = con.prepareStatement(addRecipe);) {
            
            //fill in parametrized query
            prep.setInt(1, recipeId);
            prep.setString(2, recipeName);
            prep.setString(3, recipeUrl);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addToFavRecipes(Connection con, int customerId, int recipeId) {
        String addFav = "INSERT INTO FavRecipes(CustomerId, RecipeId) VALUES(?, ?);";
        try (PreparedStatement prep = con.prepareStatement(addFav);) {
            
            //fill in parametrized query
            prep.setInt(1, customerId); //autonumber customers?
            prep.setInt(2, recipeId);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
