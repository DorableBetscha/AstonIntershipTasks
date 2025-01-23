package com.myproject.myapp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@DiscriminatorValue("Cat")
public class Cat extends BaseEntity {
    @Id //поле является первичным ключом сущности
    @GeneratedValue(strategy = GenerationType.IDENTITY) //будет автоматически генерироваться БД, уникальные значения
    private Long id;
    private String name;

    //многие к одному - несколько котов могут принадлежать одному владельцу
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "owner_id")
    private Owner owner; //owner будет связан с колонкой "owner_id" в БД

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "cat_veterinarian",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "veterinarian_id")
    )
    private Set<Veterinarian> veterinarians = new HashSet<>(); //коллекция ветеринаров без дубликатов

    public Cat() {}

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

    public void addVeterinarian (Veterinarian veterinarian) {
        this.veterinarians.add(veterinarian);
        veterinarian.getCats().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return Objects.equals(id, cat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
