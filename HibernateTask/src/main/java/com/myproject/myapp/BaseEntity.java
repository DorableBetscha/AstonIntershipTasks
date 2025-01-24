package com.myproject.myapp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_gen")
    @TableGenerator(name = "id_gen",
            table = "id_generator",
            pkColumnName = "pk_name", // Название столбца для имени ключа
            valueColumnName = "pk_value", // Название столбца для значения ключа
            pkColumnValue = "entity_id")
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
