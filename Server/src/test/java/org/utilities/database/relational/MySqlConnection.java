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
    private static final String sqlUrl = "jdbc:mysql://localhost";
    private static final String sqlUser = "shadi";
    private static final String sqlPassword = "password";
    public static void main(String args[]) {
        //TODO: MOVE CONNECTION INTO EVERY FUNCTION
        int customer = 3;
        String username = "Caleb";
        String password = "passwd";
        int skill = 1;
        int recipe = 2;

        int friend = 1;
        String friendName = "Shadi";

        //addCustomer(customer, username, password, skill);
        //addToAllRecipes(con, "fish", "www.allrecipes.com/fish", recipe); 
        //addToFavRecipes(con, customer, recipe);
        //addToFriendsList(con, customer, friend, friendName);
        //findFriends(con, 2);
        findYourRecipes(2);
    }

    //TODO: RENAME CUSTOMER TO USER
    private static void addCustomer(int customerId, String username, String password, int skill) {
        String addCustomer = "INSERT INTO CustomerInfo(CustomerId, User, Password, Skill) VALUES(?, ?, ?, ?);";
        String useKB = "USE KitchenBuddies";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addCustomer);) {
            
            preprep.executeUpdate();

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

    private static void addToAllRecipes(String recipeName, String recipeUrl, int recipeId) {
        String addRecipe = "INSERT INTO AllRecipes(RecipeId, Name, Url) VALUES(?, ?, ?);";
        String useKB = "USE KitchenBuddies";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addRecipe);) {
            
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

    private static void addToFavRecipes(int customerId, int recipeId) {
        String addFav = "INSERT INTO FavRecipes(CustomerId, RecipeId) VALUES(?, ?);";
        String useKB = "USE KitchenBuddies";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addFav);) {
            
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
    private static void addToFriendsList(int customerId, int friendId, String friendName) {
        String addFriend = "INSERT INTO FriendsList(CustomerId, FriendId, FriendName) VALUES(?, ?, ?);";
        String useKB = "USE KitchenBuddies";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addFriend);) {
            
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
    private static List<String> findFriends(int customerId) {
        List<String> friends = new ArrayList<String>();
        String findFriend = "SELECT FriendName FROM FriendsList WHERE CustomerId= ?";
        String useKB = "USE KitchenBuddies";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(findFriend);) {

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
    private static List<String> findYourRecipes(int customerId) {
        String joinRecipeTables = "SELECT FavRecipes.RecipeId, AllRecipes.Name FROM AllRecipes INNER JOIN FavRecipes on AllRecipes.RecipeID=FavRecipes.RecipeId WHERE FavRecipes.CustomerId = ?;";
        List<String> recipeNames = new ArrayList<String>();

        String useKB = "USE KitchenBuddies";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(joinRecipeTables);) {
            
            preprep.executeUpdate();
            prep.setInt(1, customerId);
            try (ResultSet rs = prep.executeQuery()) {
                while(rs.next()) {
                    recipeNames.add(rs.getString("Name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Console.print(recipeNames + "\n");
        return recipeNames;
        // String findRecipeIds = "SELECT RecipeId FROM FavRecipes WHERE CustomerId= ?";
        // String useKB = "USE KitchenBuddies";
        // try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
        //     PreparedStatement preprep = con.prepareStatement(useKB);
        //     PreparedStatement prep = con.prepareStatement(findRecipeIds)) {
        //     prep.setInt(1, customerId);
        //     try (ResultSet rs = prep.executeQuery()) {
        //         while(rs.next()) {
        //             recipeIds.add(rs.getInt("RecipeId"));
        //         }
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }

        // //is this longer to process? do we just log recipe name in fav recipes?
        // List<String> cookbook = new ArrayList<String>();
        // String findRecipeNames = "SELECT Name FROM AllRecipes WHERE RecipeId= ?";

        // //using each recipe id, query table to get its name
        // for (Integer recipe : recipeIds) {
        //     try (PreparedStatement prep = con.prepareStatement(findRecipeNames)) {
        //         prep.setInt(1, customerId);
        //         try (ResultSet rs = prep.executeQuery()) {
        //             while(rs.next()) {
        //                 cookbook.add(rs.getString("Name"));
        //             }
        //         }
        //     } catch (SQLException e) {
        //         e.printStackTrace();
        //     }
        // }

        // Console.print(cookbook + "\n");
        // return cookbook;
    }
}
