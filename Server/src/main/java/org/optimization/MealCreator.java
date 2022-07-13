package org.optimization;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;
import org.utilities.database.graph.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

import org.utilities.database.graph.*;
import org.utilities.database.relational.Main.*;

import static org.utilities.database.graph.Main.createAsparagus;
import static org.utilities.database.graph.Main.createChicken;

public class MealCreator {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Meal m = new Meal();
        m.createMeal();

        String uri = "neo4j+s://db42e3f1.databases.neo4j.io";
        Configuration configuration = new Configuration.Builder()
                .uri(uri)
                .credentials("neo4j", "9hCaQ7nmAyf5AAkUwjrk5lY8ejC61PYa2-4-zLBc6hg")
                .build();
        SessionFactory sessionFactory = new SessionFactory(configuration, "org.utilities.database.graph");
        final Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        // These recipes are hard coded for testing purposes
        createAsparagus(session);
        createChicken(session);
        System.out.println("Hello world!");

        tx.commit();
        tx.close();
    }

}