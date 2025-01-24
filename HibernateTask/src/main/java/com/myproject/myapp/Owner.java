package com.myproject.myapp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Owner extends BaseEntity {
    private String name;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true) //fetch = FetchType.EAGER - решится проблема с ленивой инициализацией
    private Set<Cat> cats = new HashSet();

    public Owner() {
    }
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
    public void addCat(Cat cat) {
        this.cats.add(cat);
        cat.setOwner(this);
    }
}
