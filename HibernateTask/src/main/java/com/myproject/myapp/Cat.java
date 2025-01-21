package com.myproject.myapp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@DiscriminatorValue("Cat")
public class Cat extends BaseEntity {
    @Id //поле является первичным ключом сущности
    @GeneratedValue(strategy = GenerationType.IDENTITY) //будет автоматически генерироваться БД, уникальные значения
    private Long id;
    private String name;

    //многие к одному - несколько котов могут принадлежать одному владельцу
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Owner owner; //owner будет связан с колонкой "owner_id" в БД

    @ManyToMany
    @JoinTable(
            name = "cat_veterinarian",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "veterinarian_id")
    )
    private Set<Veterinarian> veterinarians = new HashSet<>(); //коллекция ветеринаров без дубликатов

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

}
