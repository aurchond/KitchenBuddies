package org.utilities.database.relational;

import org.server.KitchenConstraint;
import org.server.PastRecipe;
// public package org.utilities.database.relational;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import scala.Console;

import java.util.ArrayList;
import java.util.Arrays;
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
        //addToFriendsList("shadi@gmail.com", "marley@gmail.com");
        // findFriends("shadi@gmail.com");
        // createSupplyTable();
        // getLastRecipeID();
        // createFoodTable();
    }

    private static Connection startSession() throws SQLException {
        Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);

        PreparedStatement preprep = conn.prepareStatement(useKB);
        preprep.executeUpdate();
        return conn;
    }

    private static void createSupplyTable() {
        try {
            // Establish a connection to the database
            Connection conn = startSession();

            ResultSet result = conn.getMetaData().getTables(null, null, "KitchenSupplies", null);
            if (!result.next()) {
              // If the table does not exist, create it
              Statement createTable = conn.createStatement();
              createTable.executeUpdate("CREATE TABLE KitchenSupplies (Name VARCHAR(255))");
            }
        
            // Prepare a SQL statement to insert data into the table
            String sql = "INSERT INTO KitchenSupplies (Name) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(sql);
        
            // Read the text file containing the list of food
            System.out.println(System.getProperty("user.dir"));
            BufferedReader reader = new BufferedReader(new FileReader("./Server/Py_Text_Processing/data/supplies.txt"));
            String supply;
            while ((supply = reader.readLine()) != null) {
                // Set the value to be inserted into the table
                statement.setString(1, supply);
        
                // Execute the statement
                statement.executeUpdate();
            }
        
            // Close the connection to the database
            conn.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createFoodTable() {
        try {
            // Establish a connection to the database
            Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);

            PreparedStatement preprep = conn.prepareStatement(useKB);
            preprep.executeUpdate();
            ResultSet result = conn.getMetaData().getTables(null, null, "Food", null);
            if (!result.next()) {
              // If the table does not exist, create it
              Statement createTable = conn.createStatement();
              createTable.executeUpdate("CREATE TABLE Food (Name VARCHAR(255))");
            }
        
            // Prepare a SQL statement to insert data into the table
            String sql = "INSERT INTO Food (Name) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(sql);
        
            // Read the text file containing the list of food
            System.out.println(System.getProperty("user.dir"));
            BufferedReader reader = new BufferedReader(new FileReader("./Server/Py_Text_Processing/data/unique_foods.txt"));
            String food;
            while ((food = reader.readLine()) != null) {
                // Set the value to be inserted into the table
                statement.setString(1, food);
        
                // Execute the statement
                statement.executeUpdate();
            }
        
            // Close the connection to the database
            conn.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Boolean addUser(String email, int skill, String username) {
        String addUser = "INSERT INTO UserInfo(Email, Skill, Username) VALUES(?, ?, ?);";
        try {
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(addUser);

            //fill in parametrized query
            //users are autonumbered
            stmt.setString(1, email);
            stmt.setInt(2, skill);
            stmt.setString(3, username);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {return true;} else {return false;}
        }
        catch (SQLIntegrityConstraintViolationException e) {
            Console.print("Duplicate entry was ignored");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static KitchenConstraint getKitchen(String email) {
        String getKitchen = "SELECT Burner, Pan, Pot, Knife, CuttingBoard, Oven, Microwave FROM KitchenConstraints WHERE Email = ?";
        KitchenConstraint userKC = new KitchenConstraint();
        try {
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(getKitchen);

            //fill in parametrized query
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return userKC;
            }

            userKC.burner = rs.getInt("Burner");
            userKC.pan = rs.getInt("Pan");
            userKC.pot = rs.getInt("Pot");
            userKC.knife = rs.getInt("Knife");
            userKC.cuttingBoard = rs.getInt("CuttingBoard");
            userKC.oven = rs.getInt("Oven");
            userKC.microwave = rs.getInt("Microwave");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return userKC;
    }

    public static Boolean addKitchen(String email, int burners, int pans, int pots, int knives, int cuttingBoards, int ovens, int microwaves) {
        String addKitchen = "";
        try {
            Connection conn = startSession();
            // Check if this is the first time adding kitchenConstraints
            String checkUser = "SELECT Email FROM KitchenConstraints WHERE Email = ?";

            PreparedStatement check = conn.prepareStatement(checkUser);
            check.setString(1, email);

            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                // First time adding kitchen constraints
                addKitchen = "INSERT INTO KitchenConstraints(Burner, Pan, Pot, Knife, CuttingBoard, Oven, Microwave, Email) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
            } else {
                addKitchen = "UPDATE KitchenConstraints SET Burner = ?, Pan = ?, Pot = ?, Knife = ?, CuttingBoard = ?, Oven = ?, Microwave = ? WHERE Email = ?;";
            }
            PreparedStatement stmt = conn.prepareStatement(addKitchen);

            //fill in parametrized query
            stmt.setInt(1, burners);
            stmt.setInt(2, pans);
            stmt.setInt(3, pots);
            stmt.setInt(4, knives);
            stmt.setInt(5, cuttingBoards);
            stmt.setInt(6, ovens);
            stmt.setInt(7, microwaves);
            stmt.setString(8, email);
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {return true;} else {return false;}

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long addToAllRecipes(String recipeName, String recipeUrl, String ingrString, int recipeTime) {
        String checkRecipe = "SELECT RecipeId FROM AllRecipes WHERE URL = ?";
        String addRecipe = "INSERT INTO AllRecipes(Name, Url, Ingredients, TotalTime) VALUES(?, ?, ?, ?);";
        try {
            Connection conn = startSession();

            // Check if url already in database
            PreparedStatement prep = conn.prepareStatement(checkRecipe);
            prep.setString(1, recipeUrl);
            ResultSet result = prep.executeQuery();
            if (result.next() && result.getInt(1) > 0) {
                System.out.println("Recipe already exists in relational database");
                return (long) result.getInt(1);
            }   

            prep = conn.prepareStatement(addRecipe);

            //fill in parametrized query
            prep.setString(1, recipeName);
            prep.setString(2, recipeUrl);
            prep.setString(3, ingrString);
            prep.setInt(4, recipeTime);
            int rowsUpdated = prep.executeUpdate();
            if (rowsUpdated == 0) {return -1;}

            PreparedStatement stmt = conn.prepareStatement(checkRecipe);
            stmt.setString(1, recipeUrl);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Result set is empty");
                return -1;
            }
            return (long)rs.getInt(1);
            // return value
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public static int getSkillLevel(String email) {
        try {
            String sql = "SELECT Skill FROM UserInfo WHERE Email = ?;";
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Result set is empty");
                return -1;
            }
            int skill = rs.getInt(1);
            return skill;
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -2;
        }
        
    }

    public static Boolean addSkillLevel(String email, int skillLevel) {
        
        try {
            String updateSkill = "UPDATE UserInfo SET Skill = ? WHERE Email = ?;";
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(updateSkill);
            stmt.setInt(1, skillLevel);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {return true;} else {return false;}
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getFriendsList(String email) {
        List<String> friends = new ArrayList<String>();
        try {
            String sql = "SELECT FriendEmail FROM FriendsList WHERE Email = ?;";
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                friends.add(rs.getString("FriendEmail"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return friends;
    }

    public static Boolean addToFriendsList(String email, String friendEmail) {
        String addFriend = "INSERT INTO FriendsList(Email, FriendEmail) VALUES(?, ?);";
        try {
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(addFriend);

            stmt.setString(1, email);
            stmt.setString(2, friendEmail);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                // Most likely already friends
                return false;
            }

            // Adding in friends in other direction now
            stmt = conn.prepareStatement(addFriend);
            stmt.setString(1, friendEmail);
            stmt.setString(2, email);

            rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {return true;} else {return false;}
            
        }
        catch (SQLIntegrityConstraintViolationException e) {
            Console.print("Duplicate entry was ignored");
            
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean addUserLinkedRecipe(String email, int recipeId) {
        try {
            String sql = "INSERT INTO UserLinkedRecipes(Email, RecipeId) VALUES(?, ?);";
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setInt(2, recipeId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {return true;} else {return false;}
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static Boolean doesRecipeExist(String email, String url) {
        /* 
        1. Check 
            a. URL connected with user
            b. if graph
        2. If a. not satisfied
            a. check if URL exists in AllRecipes
            b. if not, return false
            c. Add URL to User
        3. Either after 1 or 2, check if URL has been parsed and placed in graphDB
        */
        try {
            // Check if url in All_Recipes join on UserLinkedRecipes with ID
            String sql = "SELECT ar.InGraphDB as GraphCheck FROM AllRecipes as ar " +
                         "JOIN UserLinkedRecipes as ulr on ar.RecipeID=ulr.RecipeId WHERE ulr.Email = ? AND ar.Url = ?;";
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, url);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                // Check if URL in AllRecipe and select InGraphDB
                sql = "SELECT RecipeId, InGraphDB FROM AllRecipes as ar WHERE ar.Url = ?;";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, url);
                
                if (!rs.next()) {
                    // URL hasn't been parsed
                    return false;
                }

                // Insert recipeId into UserLinkedRecipe
                int recipeId = rs.getInt("RecipeId");
                Boolean res = addUserLinkedRecipe(email, recipeId);

                if (!res) {
                    System.out.println("Couldn't add recipe to UserLinkedRecipe? Throw an error??");
                }
                
                // Check if Recipe in GraphDB
                Boolean inGraphDB = rs.getBoolean("GraphCheck");
                if (inGraphDB) {return true;} else {return false;}
            } 

            Boolean inGraphDB = rs.getBoolean("GraphCheck");
            if (inGraphDB) {return true;} else {return false;}
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static long getRecipeID() {
        try {
            String sql = "SELECT LAST_INSERT_ID() as last_id FROM AllRecipes;";
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Result set is empty");
                return -1;
            }
            System.out.println(rs.getString("last_id"));
            // TODO: Change value to result
            return 5;
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -2;
        }
        
    }

    //call this from front page of app to show the names of your saved recipes
    public static List<PastRecipe> findUserRecipes(String email) {
        String joinRecipeTables = "SELECT ar.RecipeId, ar.Name, ar.Ingredients, ar.TotalTime, url.LastDateMade FROM AllRecipes as ar INNER JOIN UserLinkedRecipes as url on ar.RecipeId=url.RecipeId WHERE url.Email = ?;";
        List<PastRecipe> recipes = new ArrayList<PastRecipe>();

        try {
            Connection conn = startSession();
            PreparedStatement stmt = conn.prepareStatement(joinRecipeTables);

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Integer recipeId = rs.getInt("ar.RecipeId");
                String name = rs.getString("ar.Name");
                Integer totalTime = rs.getInt("ar.TotalTime");
                
                // Parse ingredients
                String strIngr = rs.getString("ar.Ingredients");
                String[] parts = strIngr.split(",");
                List<String> ingredients = Arrays.asList(parts);

                // LastDate
                Date dateValue = rs.getDate("url.LastDateMade");
                String date = "";

                if (dateValue != null) {
                    date = dateValue.toString();
                }

                PastRecipe pRecipe = new PastRecipe(recipeId, name, ingredients, totalTime, date);
                recipes.add(pRecipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
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
