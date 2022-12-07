package org.utilities.database.relational;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import scala.Console;

import java.util.ArrayList;
import java.sql.ResultSet;

public class MySqlConnection {
    private static final String sqlUrl = "jdbc:mysql://localhost";
    private static final String sqlUser = "shadi";
    private static final String sqlPassword = "password";
    private static final String useKB = "USE KitchenBuddies";
    public static void main(String args[]) {
        //addCustomer("Marley", "passwordpassword", 1);
        // addToFavRecipes(4, 2);
        // addToFavRecipes(5, 4);
        // addToFavRecipes(6, 1);
        // addToFavRecipes(6, 2);
        // addToFavRecipes(7, 3);
        //findYourRecipes(6);
        //addKitchen(4, 4, 2, 2, 1, 4);
        //getNumBurners(4);
        //addToFavRecipes(6, 1);
        findFriends(4);
    }

    //TODO: RENAME CUSTOMER TO USER
    private static void addCustomer(String username, String password, int skill) {
        //customers are autonumbered
        String addCustomer = "INSERT INTO CustomerInfo(User, Password, Skill) VALUES(?, ?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addCustomer);) {
            
            preprep.executeUpdate();

            //fill in parametrized query
            //customers are autonumbered
            prep.setString(1, username);
            prep.setString(2, password);
            prep.setInt(3, skill);
            prep.executeUpdate();
        }
        catch (SQLIntegrityConstraintViolationException e) {
            Console.print("Duplicate entry was ignored");
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addKitchen(int customerId, int burners, int pans, int pots, int cuttingBoards, int knives) {
        String addKitchen = "INSERT INTO KitchenConstraints(CustomerId, Burners, Pans, Pots, CuttingBoards, Knives) VALUES(?, ?, ?, ?, ?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addKitchen);) {
            
            preprep.executeUpdate();

            //fill in parametrized query
            prep.setInt(1, customerId);
            prep.setInt(2, burners);
            prep.setInt(3, pans);
            prep.setInt(4, pots);
            prep.setInt(5, cuttingBoards);
            prep.setInt(6, knives);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addToAllRecipes(String recipeName, String recipeUrl) {
        String addRecipe = "INSERT INTO AllRecipes(RecipeId, Name, Url) VALUES(?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addRecipe);) {
            
            //fill in parametrized query
            prep.setString(1, recipeName);
            prep.setString(2, recipeUrl);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addToFavRecipes(int customerId, int recipeId) {
        String addFav = "INSERT INTO FavRecipes(CustomerId, RecipeId) VALUES(?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addFav);) {
            
            preprep.executeUpdate();

            //fill in parametrized query
            prep.setInt(1, customerId); //autonumber customers?
            prep.setInt(2, recipeId);
            prep.executeUpdate();
        }
        catch (SQLIntegrityConstraintViolationException e) {
            Console.print("Duplicate entry was ignored");
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //should we avoid duplicates? i.e. if there is a link already between friend 1 and 2, avoid friend 2 and 1?
    //consider performance .... then we need to change findFriend function too
    private static void addToFriendsList(int customerId, int friendId, String friendName) {
        String addFriend = "INSERT INTO FriendsList(CustomerId, FriendId, FriendName) VALUES(?, ?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addFriend);) {
            
            //fill in parametrized query
            prep.setInt(1, customerId);
            prep.setInt(2, friendId);
            prep.setString(3, friendName);
            prep.executeUpdate();
        }
        catch (SQLIntegrityConstraintViolationException e) {
            Console.print("Duplicate entry was ignored");
            return;
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
        String findFriend = "SELECT FriendsList.CustomerId, CustomerInfo.User FROM CustomerInfo INNER JOIN FriendsList on CustomerInfo.CustomerId=FriendsList.FriendId WHERE FriendsList.CustomerId = ?;";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(findFriend);) {
            
            preprep.executeUpdate();
            prep.setInt(1, customerId);
            try (ResultSet rs = prep.executeQuery()) {
                while(rs.next()) {
                    friends.add(rs.getString("User"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Console.print(friends + "\n");
        return friends;
    }

    //call this from front page of app to show the names of your saved recipes
    private static List<String> findYourRecipes(int customerId) {
        String joinRecipeTables = "SELECT FavRecipes.RecipeId, AllRecipes.Name FROM AllRecipes INNER JOIN FavRecipes on AllRecipes.RecipeId=FavRecipes.RecipeId WHERE FavRecipes.CustomerId = ?;";
        List<String> recipeNames = new ArrayList<String>();

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
    }

    //TODO: make a kitchen struct?
    private static Integer getNumBurners(int customerId) {
        int numBurners = -1;

        String joinKitchenTables = "SELECT KitchenConstraints.Burners, CustomerInfo.CustomerId, CustomerInfo.User FROM CustomerInfo INNER JOIN KitchenConstraints on CustomerInfo.CustomerId=KitchenConstraints.CustomerId WHERE KitchenConstraints.CustomerId=?;";

        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(joinKitchenTables);) {
            
            preprep.executeUpdate();
            prep.setInt(1, customerId);
            try (ResultSet rs = prep.executeQuery()) {
                while(rs.next()) {
                    numBurners = rs.getInt("Burners");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Console.print(numBurners + "\n");
        return numBurners; //should do error checking if -1 is received
    }
}
