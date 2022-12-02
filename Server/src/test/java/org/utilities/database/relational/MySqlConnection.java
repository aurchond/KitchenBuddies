package org.utilities.database.relational;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import scala.Console;

import java.util.ArrayList;
import java.sql.ResultSet;

public class MySqlConnection {
    private static final String url = "jdbc:mysql://localhost";
    private static final String user = "shadi";
    private static final String password = "password";
    public static void main(String args[]) {
        //TODO: MOVE CONNECTION INTO EVERY FUNCTION
        try (Connection con = DriverManager.getConnection(url, user, password); )
        {
            String useKB = "USE KitchenBuddies;";

            try (PreparedStatement prep = con.prepareStatement(useKB);) {
                prep.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            int customer = 2;
            String username = "Maya";
            String password = "123abc";
            int skill = 0;
            int recipe = 2;

            int friend = 1;
            String friendName = "Shadi";

            //addCustomer(con, customer, username, password, skill);
            //addToAllRecipes(con, "fish", "www.allrecipes.com/fish", recipe); 
            //addToFavRecipes(con, customer, recipe);
            //addToFriendsList(con, customer, friend, friendName);
            //findFriends(con, 2);
            findYourRecipes(con, 2);
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    //TODO: RENAME CUSTOMER TO USER
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

    //should we avoid duplicates? i.e. if there is a link already between friend 1 and 2, avoid friend 2 and 1?
    //consider performance .... then we need to change findFriend function too
    private static void addToFriendsList(Connection con, int customerId, int friendId, String friendName) {
        String addFriend = "INSERT INTO FriendsList(CustomerId, FriendId, FriendName) VALUES(?, ?, ?);";
        try (PreparedStatement prep = con.prepareStatement(addFriend);) {
            
            //fill in parametrized query
            prep.setInt(1, customerId);
            prep.setInt(2, friendId);
            prep.setString(3, friendName);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        //friends are bidirectional?
    }

    //call this from front page of app to show your friends list
    //TODO: make structs for User?
    private static List<String> findFriends(Connection con, int customerId) {
        List<String> friends = new ArrayList<String>();
        String findFriend = "SELECT FriendName FROM FriendsList WHERE CustomerId= ?";

        try (PreparedStatement prep = con.prepareStatement(findFriend)) {
            prep.setInt(1, customerId);
            try (ResultSet rs = prep.executeQuery()) {
                while(rs.next()) {
                    friends.add(rs.getString("FriendName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Console.print(friends + "\n");
        return friends;
    }

    //call this from front page of app to show your saved recipes
    //TODO: USE NEW RECIPE STRUCT?
    private static List<String> findYourRecipes(Connection con, int customerId) {
        List<Integer> recipeIds = new ArrayList<Integer>();
        String findRecipeIds = "SELECT RecipeId FROM FavRecipes WHERE CustomerId= ?";

        try (PreparedStatement prep = con.prepareStatement(findRecipeIds)) {
            prep.setInt(1, customerId);
            try (ResultSet rs = prep.executeQuery()) {
                while(rs.next()) {
                    recipeIds.add(rs.getInt("RecipeId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //is this longer to process? do we just log recipe name in fav recipes?
        List<String> cookbook = new ArrayList<String>();
        String findRecipeNames = "SELECT Name FROM AllRecipes WHERE RecipeId= ?";

        //using each recipe id, query table to get its name
        for (Integer recipe : recipeIds) {
            try (PreparedStatement prep = con.prepareStatement(findRecipeNames)) {
                prep.setInt(1, customerId);
                try (ResultSet rs = prep.executeQuery()) {
                    while(rs.next()) {
                        cookbook.add(rs.getString("Name"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Console.print(cookbook + "\n");
        return cookbook;
    }
}
