package org.utilities.database.relational;

import org.junit.jupiter.api.Test;
import org.optimization.Resource;
import org.optimization.User;

import java.util.HashMap;
import java.util.List;

public class test_relational {

    @Test
    public void testRelationDbFunctionGetConstraints() {
        User u = new User("Bob");
        HashMap<String, List<Resource>> constraints = Main.relationDbFunctionGetConstraints(u);

        for (String key: constraints.keySet()) {
            List<Resource> r = constraints.get(key);

            for (int i = 0; i < r.size(); i++) {
                System.out.println(key + " " + r.get(i).getId());
            }
        }

    }
}
