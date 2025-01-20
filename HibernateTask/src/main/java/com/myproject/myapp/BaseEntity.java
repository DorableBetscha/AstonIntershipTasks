package com.myproject.myapp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
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
}
