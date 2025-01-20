package com.myproject.myapp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Inheritance(strategy = InheritanceType.JOINED) //как будет происходить наследование - будут храниться в разных таблицах
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING) //столбец для различения сущностей в таблице (название, тип)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at") //@Column - задать имя столбца в БД
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist //метод будет вызываться до сохранения в БД
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate // метод будет вызываться перед обновлением
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
}
