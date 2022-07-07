package org.test;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label="Step")
public class Step {
    private Long id;

    private String name;

    @Relationship(type = Connection.TYPE, direction = Relationship.OUTGOING)
    private Set<Connection> connections = new HashSet<>();

    public Step(String name) {
        this.name = name;
    }

    public void addConnection(Step target, int distance) {
        this.connections.add(new Connection(this, target, distance));
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

    // hashCode, equals, toString, getter/setter, no-arg constructor ommitted..
}
