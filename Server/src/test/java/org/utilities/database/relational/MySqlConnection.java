package org.utilities.database.relational;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

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

            addRecipeMapping(con, "Curry", "www.allrecipes.com/curry", 4); 
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    private static void addRecipeMapping(Connection con, String recipeName, String recipeUrl, int recipeId) {
        String addRecipe = "INSERT INTO RecipeMapping(RecipeId, Name, Url) VALUES(?, ?, ?);";
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
}
