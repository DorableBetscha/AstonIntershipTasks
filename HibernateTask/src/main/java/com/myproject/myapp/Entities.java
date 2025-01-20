package com.myproject.myapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Entities {
    @Entity //сущность, отображаемая в БД, хибер может управлять
    public class Cat {
        @Id //поле является первичным ключом сущности
        @GeneratedValue(strategy = GenerationType.IDENTITY);
        private long id;
    }
}
