package com.example.studentmanagmentrest.model.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class NameEntity extends BaseEntity {

    @Column
    protected String name;

    public String getName() {
        return name;
    }

    public NameEntity setName(String name) {
        this.name = name;
        return this;
    }
}
