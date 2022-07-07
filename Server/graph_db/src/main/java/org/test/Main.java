package org.test;

import java.util.Collections;

import org.neo4j.graphdb.Path;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;
import org.neo4j.ogm.config.Configuration;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        String uri = "neo4j+s://db42e3f1.databases.neo4j.io";
        Configuration configuration = new Configuration.Builder()
                .uri(uri)
                .credentials("neo4j", "9hCaQ7nmAyf5AAkUwjrk5lY8ejC61PYa2-4-zLBc6hg")
                .build();
        //"com.javahelps.ogmhelloworld.entities"
        SessionFactory sessionFactory = new SessionFactory(configuration, "org.test");
        final Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Step s1 = new Step("Step 1");
        Step s2 = new Step("Step 2");
        Step s3 = new Step("Step 3");

        s1.addConnection(s2, 52);
        s1.addConnection(s3, 49);
        s2.addConnection(s3, 20);
        s2.addConnection(s1, 95);

        session.save(s1);
        session.save(s2);
        session.save(s3);
        tx.commit();

        System.out.println(session.countEntitiesOfType(Step.class) + " stations saved");
//        getRoute("Step 1", "Step 3", session);

        System.out.println(session.load(Step.class, s1.getId()).getName());

        tx.close();
    }

    private static void getRoute(final String from, final String destination, final Session session) {
        System.out.printf("searching for the shortest route from %s to %s..\n", from, destination);

        String cypherQuery = String.format(
                "MATCH (from:TrainStation {name:'%s'}), (to:TrainStation {name:'%s'}), paths=allShortestPaths((from)-[:LEADS_TO*]->(to)) " +
                        "WITH REDUCE(dist = 0, rel in rels(paths) | dist + rel.distance) AS distance, paths " +
                        "RETURN paths, distance " +
                        "ORDER BY distance " +
                        "LIMIT 1",
                from, destination);
        final Result result = session.query(cypherQuery, Collections.emptyMap());
        System.out.printf("shortest way from %s to %s via\n", from, destination);
        result.queryResults().forEach(entry -> {
            Long distance = (Long) entry.get("distance");
            Path path = (Path) entry.get("paths");
            System.out.printf("distance: %s\n", distance);
            path.nodes().forEach(node -> {
                System.out.printf("- %s\n", node.getProperty("name"));
            });
        });
    }
}