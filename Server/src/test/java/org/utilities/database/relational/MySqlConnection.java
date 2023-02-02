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
        //addUser("shadi@gmail.com", 1);
        //addToAllRecipes("chicken noodle soup", "recipes.com/chicken%20noodle%20soup");
        //addToFavRecipes("marley@gmail.com", 2); //should we add based on username?
        //addToFavRecipes("shadi@gmail.com", 1);
        //findYourRecipes("shadi@gmail.com");
        //addKitchen("shadi@gmail.com", 4, 2, 2, 1, 4);
        //getNumBurners("shadi@gmail.com");
        addToFriendsList("shadi@gmail.com", "marley@gmail.com");
        findFriends("shadi@gmail.com");
    }

    private static void addUser(String email, int skill) {
        //users are autonumbered
        String addUser = "INSERT INTO UserInfo(Email, Skill) VALUES(?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addUser);) {
            
            preprep.executeUpdate();

            //fill in parametrized query
            //users are autonumbered
            prep.setString(1, email);
            prep.setInt(2, skill);
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

    private static void addKitchen(String email, int burners, int pans, int pots, int cuttingBoards, int knives) {
        String addKitchen = "INSERT INTO KitchenConstraints(Email, Burners, Pans, Pots, CuttingBoards, Knives) VALUES(?, ?, ?, ?, ?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addKitchen);) {
            
            preprep.executeUpdate();

            //fill in parametrized query
            prep.setString(1, email);
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
        String addRecipe = "INSERT INTO AllRecipes(Name, Url) VALUES(?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addRecipe);) {
            
            preprep.executeUpdate();

            //fill in parametrized query
            prep.setString(1, recipeName);
            prep.setString(2, recipeUrl);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addToFavRecipes(String email, int recipeId) {
        String addFav = "INSERT INTO FavRecipes(Email, RecipeId) VALUES(?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addFav);) {
            
            preprep.executeUpdate();

            //fill in parametrized query
            prep.setString(1, email); //autonumber users?
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
    private static void addToFriendsList(String email, String friendEmail) {
        String addFriend = "INSERT INTO FriendsList(Email, FriendEmail) VALUES(?, ?);";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(addFriend);) {

            preprep.executeUpdate();
            
            //fill in parametrized query
            prep.setString(1, email);
            prep.setString(2, friendEmail);
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
    private static List<String> findFriends(String email) {
        List<String> friends = new ArrayList<String>();
        String findFriend = "SELECT FriendsList.Email, UserInfo.Email FROM UserInfo INNER JOIN FriendsList on UserInfo.Email=FriendsList.FriendEmail WHERE FriendsList.Email = ?;";
        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(findFriend);) {
            
            preprep.executeUpdate();
            prep.setString(1, email);
            try (ResultSet rs = prep.executeQuery()) {
                while(rs.next()) {
                    friends.add(rs.getString("Email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Console.print(friends + "\n");
        return friends;
    }

    //call this from front page of app to show the names of your saved recipes
    private static List<String> findYourRecipes(String email) {
        String joinRecipeTables = "SELECT FavRecipes.RecipeId, AllRecipes.Name FROM AllRecipes INNER JOIN FavRecipes on AllRecipes.RecipeId=FavRecipes.RecipeId WHERE FavRecipes.Email = ?;";
        List<String> recipeNames = new ArrayList<String>();

        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(joinRecipeTables);) {
            
            preprep.executeUpdate();
            prep.setString(1, email);
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
    private static Integer getNumBurners(String email) {
        int numBurners = -1;

        String joinKitchenTables = "SELECT KitchenConstraints.Burners, UserInfo.Email FROM UserInfo INNER JOIN KitchenConstraints on UserInfo.Email=KitchenConstraints.Email WHERE KitchenConstraints.Email=?;";

        try (Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement preprep = con.prepareStatement(useKB);
            PreparedStatement prep = con.prepareStatement(joinKitchenTables);) {
            
            preprep.executeUpdate();
            prep.setString(1, email);
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
