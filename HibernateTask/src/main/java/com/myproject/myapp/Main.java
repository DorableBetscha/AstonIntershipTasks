package com.myproject.myapp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(BaseEntity.class)
                .addAnnotatedClass(Cat.class)
                .addAnnotatedClass(Owner.class)
                .addAnnotatedClass(Veterinarian.class)
                .buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Очистка таблиц
            clearDatabase(session);
            session.clear();

            // Создание объектов
            Owner owner1 = new Owner();
            owner1.setName("Alice");

            Owner owner2 = new Owner();
            owner2.setName("Bob");

            Cat cat1 = new Cat();
            cat1.setName("Tom");
            cat1.setOwner(owner1);

            Cat cat2 = new Cat();
            cat2.setName("Whiskers");
            cat2.setOwner(owner1);

            Cat cat3 = new Cat();
            cat3.setName("Shadow");
            cat3.setOwner(owner2);

            Veterinarian vet1 = new Veterinarian();
            vet1.setName("Dr. Smith");

            Veterinarian vet2 = new Veterinarian();
            vet2.setName("Dr. Brown");

            // Установка связей между котами и ветеринарами
            cat1.getVeterinarians().add(vet1);
            cat1.getVeterinarians().add(vet2);
            cat2.getVeterinarians().add(vet1);
            vet1.getCats().add(cat1);
            vet1.getCats().add(cat2);
            vet2.getCats().add(cat1);

            // Добавление котов владельцам
            owner1.getCats().add(cat1);
            owner1.getCats().add(cat2);
            owner2.getCats().add(cat3);

            // Сохранение объектов в БД
            session.save(cat1);
            session.save(cat2);
            session.save(cat3);
            session.save(owner1);
            session.save(owner2);
            session.save(vet1);
            session.save(vet2);

            session.getTransaction().commit();

            // Полиморфный запрос к BaseEntity
            session.beginTransaction();

            List<BaseEntity> allEntities = session.createQuery("FROM BaseEntity", BaseEntity.class).getResultList();
            System.out.println("=== All Entities ===");
            for (BaseEntity entity : allEntities) {
                if (entity instanceof Cat) {
                    System.out.println("Cat: " + ((Cat) entity).getName());
                } else if (entity instanceof Owner) {
                    System.out.println("Owner: " + ((Owner) entity).getName());
                } else if (entity instanceof Veterinarian) {
                    System.out.println("Veterinarian: " + ((Veterinarian) entity).getName());
                }
            }

            // Запрос для проверки связей владельцев и котов
            List<Owner> owners = session.createQuery("FROM Owner", Owner.class).getResultList();
            System.out.println("=== Owners and their Cats ===");
            for (Owner owner : owners) {
                System.out.println("Owner: " + owner.getName());
                owner.getCats().forEach(cat -> System.out.println("  Cat: " + cat.getName()));
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }

    private static void clearDatabase(Session session) {
        session.createNativeQuery("DELETE FROM cat_veterinarian").executeUpdate();
        session.createNativeQuery("DELETE FROM Cat").executeUpdate();
        session.createNativeQuery("DELETE FROM Veterinarian").executeUpdate();
        session.createNativeQuery("DELETE FROM Owner").executeUpdate();
        session.createNativeQuery("ALTER TABLE cat_veterinarian AUTO_INCREMENT = 1").executeUpdate();
        session.getTransaction().commit();
    }
}
