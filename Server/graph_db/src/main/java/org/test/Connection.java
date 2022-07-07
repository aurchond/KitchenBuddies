package org.test;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = Connection.TYPE)
public class Connection {
    public static final String TYPE = "LEADS_TO";

    private Long id;

    @StartNode
    private Step start;

    @EndNode
    private Step end;

    private int time;

    public Connection(Step start, Step end, int time) {
        this.start = start;
        this.end = end;
        this.time = time;
    }

    // hashCode, equals, toString, getter/setter, no-arg constructor ommitted..
}
