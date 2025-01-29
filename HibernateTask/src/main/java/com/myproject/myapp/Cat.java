package com.myproject.myapp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Cat extends BaseEntity {
    private String name;

    //многие к одному - несколько котов могут принадлежать одному владельцу
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner; //owner будет связан с колонкой "owner_id" в БД

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "cat_veterinarian",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "veterinarian_id")
    )
    private Set<Veterinarian> veterinarians = new HashSet<>(); //коллекция ветеринаров без дубликатов

    public Cat() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Set<Veterinarian> getVeterinarians() {
        return veterinarians;
    }

    public void setVeterinarians(Set<Veterinarian> veterinarians) {
        this.veterinarians = veterinarians;
    }

    public void addVeterinarian (Veterinarian veterinarian) {
        this.veterinarians.add(veterinarian);
        veterinarian.getCats().add(this);
    }
}
