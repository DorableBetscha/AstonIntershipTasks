package com.myproject.myapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HibernateUtil {
    public static void main(String[] args) {
        // Создание конфигурации Hibernate
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml"); // Убедитесь, что файл конфигурации находится в classpath

        // Создание SessionFactory
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        // Создание сессии
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // Начало транзакции
            transaction = session.beginTransaction();

            // Создание владельцев
            Owner owner1 = new Owner();
            owner1.setName("Иван Иванов");

            Owner owner2 = new Owner();
            owner2.setName("Петр Петров");

            // Создание ветеринаров
            Veterinarian vet1 = new Veterinarian();
            vet1.setName("Доктор Смирнов");

            Veterinarian vet2 = new Veterinarian();
            vet2.setName("Доктор Кузнецов");

            // Создание котов
            Cat cat1 = new Cat();
            cat1.setName("Мурзик");
            cat1.setOwner(owner1);

            Cat cat2 = new Cat();
            cat2.setName("Барсик");
            cat2.setOwner(owner1);

            Cat cat3 = new Cat();
            cat3.setName("Снежок");
            cat3.setOwner(owner2);

            // Установка связи многие-ко-многим
            Set<Veterinarian> vetsForCat1 = new HashSet<>();
            vetsForCat1.add(vet1);
            vetsForCat1.add(vet2);
            cat1.setVeterinarians(vetsForCat1);

            Set<Veterinarian> vetsForCat2 = new HashSet<>();
            vetsForCat2.add(vet1);
            cat2.setVeterinarians(vetsForCat2);

            // Сохранение объектов
            session.save(owner1);
            session.save(owner2);
            session.save(vet1);
            session.save(vet2);
            session.save(cat1);
            session.save(cat2);
            session.save(cat3);

            // Завершение транзакции
            transaction.commit();

            // Проверка: Извлечение всех котов из базы данных
            System.out.println("Список котов:");
            List<Cat> cats = session.createQuery("FROM Cat", Cat.class).getResultList();
            for (Cat cat : cats) {
                System.out.println("Кот: " + cat.getName() + ", Владелец: " + cat.getOwner().getName());
                System.out.println("Ветеринары:");
                for (Veterinarian vet : cat.getVeterinarians()) {
                    System.out.println(" - " + vet.getName());
                }
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
