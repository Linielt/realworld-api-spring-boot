package com.linielt.realworldapispringboot.model;

import jakarta.persistence.*;

@Table(name = "tags")
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String value;

    public Tag(String value) {
        this.value = value;
    }

    protected Tag() {}

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
