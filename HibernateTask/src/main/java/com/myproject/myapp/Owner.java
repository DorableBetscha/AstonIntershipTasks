package com.myproject.myapp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Owner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Cat> cats = new HashSet();

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
