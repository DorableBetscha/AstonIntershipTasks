package com.myproject.myapp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Veterinarian extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "veterinarians")
    private Set<Cat> cats = new HashSet<>();

    public Veterinarian() {}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<Cat> getCats() {
        return cats;
    }
    public void setCats(Set<Cat> cats) {
        this.cats = cats;
    }
}
