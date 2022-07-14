package org.utilities.database.graph;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.RelationshipEntity;

@RelationshipEntity(type = Connection.TYPE)
public class Connection {
    public static final String TYPE = "LEADS_TO";

    private Long id;

    @StartNode
    private Step start;
    @EndNode
    private Step end;
    private Integer time;

    ////CONSTRUCTORS////
    //most used constructor
    public Connection(Step start, Step end, Integer time) {
        this.start = start;
        this.end = end;
        this.time = time;
    }

    //empty default constructor
    public Connection() {

    }

    ////GETTER FUNCTIONS////
    public Long getId() {
        return id;
    }
    public Step getStartNode() {
        return this.start;
    }
    public Step getEndNode() {
        return this.end;
    }
    public Integer getConnectionTime() {
        return this.time;
    }

    // hashCode, equals, toString omitted..
}
