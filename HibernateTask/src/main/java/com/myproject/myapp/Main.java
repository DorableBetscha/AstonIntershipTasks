package com.myproject.myapp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            clearDatabase(session); // очистка таблиц для повторного запуска при тестировании


            Owner owner1 = new Owner();
            owner1.setName("Alice");

            Owner owner2 = new Owner();
            owner2.setName("Bob");

            Veterinarian vet1 = new Veterinarian();
            vet1.setName("Dr. Smith");

            Veterinarian vet2 = new Veterinarian();
            vet2.setName("Dr. Brown");

            Cat cat1 = new Cat();
            cat1.setName("Tom");
            cat1.setOwner(owner1);

            Cat cat2 = new Cat();
            cat2.setName("Whiskers");
            cat2.setOwner(owner1);

            Cat cat3 = new Cat();
            cat3.setName("Shadow");
            cat3.setOwner(owner2);

            // Установка связей между котами и ветеринарами
            owner1.addCat(cat1);
            owner1.addCat(cat2);
            owner2.addCat(cat3);
            cat1.addVeterinarian(vet1);
            cat1.addVeterinarian(vet2);
            cat2.addVeterinarian(vet1);


            // Сохранение объектов в БД
            session.merge(owner1);
            session.merge(owner2);

            session.getTransaction().commit();

            // Полиморфный запрос к BaseEntity
            session.beginTransaction();

            List<BaseEntity> allEntities = session.createQuery("FROM BaseEntity", BaseEntity.class).getResultList();
            System.out.println("All Entities:");
            for (BaseEntity entity : allEntities) {
                String entityType = entity.getClass().getSimpleName();
                switch (entityType) {
                    case "Cat":
                        String catName = ((Cat) entity).getName();
                        if (catName != null) {
                            System.out.printf("Cat: %s%n", catName);
                        }
                        break;
                    case "Owner":
                        String ownerName = ((Owner) entity).getName();
                        if (ownerName != null) {
                            System.out.printf("Owner: %s%n", ownerName);
                        }
                        break;
                    case "Veterinarian":
                        String vetName = ((Veterinarian) entity).getName();
                        if (vetName != null) {
                            System.out.printf("Veterinarian: %s%n", vetName);
                        }
                        break;
                    default:
                        System.out.println("Unknown entity type");
                        break;
                }
            }

            // запрос для проверки связей владельцев и котов
            List<Owner> owners = session.createQuery("FROM Owner", Owner.class).getResultList();
            System.out.println("Owners and their Cats: ");
            for (Owner owner : owners) {
                System.out.println("Owner: " + owner.getName());
                owner.getCats().forEach(cat -> System.out.println("  Cat: " + cat.getName()));
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback(); // откат транзакции в случае ошибки
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static void clearDatabase(Session session) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Очистка таблиц
            session.createNativeQuery("DELETE FROM TABLE cat_veterinarian").executeUpdate();
            session.createNativeQuery("DELETE FROM TABLE Cat").executeUpdate();
            session.createNativeQuery("DELETE FROM TABLE Owner").executeUpdate();
            session.createNativeQuery("DELETE FROM TABLE Veterinarian").executeUpdate();

            transaction.commit(); // завершение транзакции
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // откат транзакции в случае ошибки
            }
            e.printStackTrace();
        }
    }
}
