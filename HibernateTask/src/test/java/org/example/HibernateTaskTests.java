package org.example;

import com.myproject.myapp.*;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) //тестовый класс будет создан один раз для всех тестов
public class HibernateTaskTests {
    private SessionFactory sessionFactory; // экземпляр SessionFactory для создания сессий

    @BeforeAll
    void setup() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(BaseEntity.class)
                .addAnnotatedClass(Cat.class)
                .addAnnotatedClass(Owner.class)
                .addAnnotatedClass(Veterinarian.class)
                .buildSessionFactory();
    } //читает конфигурацию, добавляет туда классы

    @AfterAll
    void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    } // закрывает sessionFactory после выполнения всех тестов

    @BeforeEach
    void clearDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("DELETE FROM cat_veterinarian").executeUpdate();
            session.createNativeQuery("DELETE FROM cat").executeUpdate();
            session.createNativeQuery("DELETE FROM veterinarian").executeUpdate();
            session.createNativeQuery("DELETE FROM owner").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testAddOwner() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Owner owner = new Owner();
            owner.setName("Mark");
            session.persist(owner); //сохранение объекты в БД

            session.getTransaction().commit();

            List<Owner> owners = session.createQuery("FROM Owner", Owner.class).list();
            assertEquals(1, owners.size());
            assertEquals("Mark", owners.get(0).getName());
        }
    }

    @Test
    void testGetCats() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Owner owner = new Owner();
            owner.setName("Mark");
            session.persist(owner);

            Cat cat = new Cat();
            cat.setName("Pushishka");
            cat.setOwner(owner);
            session.persist(cat);

            session.getTransaction().commit(); //коммит транзакции для сохранения в БД

            List<Cat> cats = session.createQuery("From Cat").list();
            assertEquals(1, cats.size());
            assertEquals("Pushishka", cats.get(0).getName());
            assertEquals("Mark", cats.get(0).getOwner().getName());
        }
    }

    @Test
    void testUpdateOwner() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Сохраняем объект
            Owner owner = new Owner();
            owner.setName("Mark");
            session.persist(owner);
            session.getTransaction().commit();

            // Начинаем новую транзакцию
            session.beginTransaction();
            owner.setName("Alice");
            session.merge(owner); // Используем merge вместо update
            session.getTransaction().commit();

            // Проверяем обновление
            Owner updatedOwner = session.createQuery("FROM Owner", Owner.class).uniqueResult();
            assertNotNull(updatedOwner);
            assertEquals("Alice", updatedOwner.getName());
        }
    }

    @Test
    void testDeleteVeterinarian() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Veterinarian vet = new Veterinarian();
            vet.setName("Dr.Black");
            session.persist(vet);

            session.getTransaction().commit();

            session.beginTransaction();
            session.remove(vet);
            session.getTransaction().commit();

            List<Veterinarian> veterinarians = session.createQuery("FROM Veterinarian", Veterinarian.class).list();
            assertTrue(veterinarians.isEmpty());
        }
    }

    @Test
    void testCatVetRelationship() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Owner owner = new Owner();
            owner.setName("Mark");

            Cat cat = new Cat();
            cat.setName("Fluffy");
            cat.setOwner(owner);

            Veterinarian vet = new Veterinarian();
            vet.setName("Dr.Black");

            cat.getVeterinarians().add(vet);

            session.persist(owner);
            session.persist(cat);
            session.getTransaction().commit();

            List<Cat> cats = session.createQuery("FROM Cat", Cat.class).list();
            assertEquals(1, cats.size());
            assertEquals("Fluffy", cats.get(0).getName());
            assertEquals(1, cats.get(0).getVeterinarians().size());
            assertEquals("Dr.Black", cats.get(0).getVeterinarians().iterator().next().getName());
            assertEquals("Mark", cats.get(0).getOwner().getName());
        }
    }

    @Test
    void testLazyInitializationException() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Owner owner = new Owner();
            owner.setName("Mark");

            Cat cat = new Cat();
            cat.setName("Fluffy");
            cat.setOwner(owner);

            owner.getCats().add(cat);

            session.persist(owner);
            session.getTransaction().commit();
        }

         /* ошибка
        Owner owner = sessionFactory.openSession()
                .createQuery("FROM OWNER", Owner.class)
                .setMaxResults(1)
                .uniqueResult();

        */

        try (Session session = sessionFactory.openSession()) {
            Owner owner = session.createQuery(
                            "SELECT o FROM Owner o JOIN FETCH o.cats WHERE o.name = :name", Owner.class)
                    .setParameter("name", "Mark")
                    .uniqueResult();
            System.out.println(owner.getCats().size());
        }
    }

    @Test
    void testNPlusOneProblem() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Owner owner1 = new Owner();
            owner1.setName("John Doe");

            Owner owner2 = new Owner();
            owner2.setName("Jane Doe");

            Cat cat1 = new Cat();
            cat1.setName("Fluffy");
            cat1.setOwner(owner1);

            Cat cat2 = new Cat();
            cat2.setName("Mittens");
            cat2.setOwner(owner1);

            Cat cat3 = new Cat();
            cat3.setName("Shadow");
            cat3.setOwner(owner2);

            session.persist(owner1);
            session.persist(owner2);
            session.persist(cat1);
            session.persist(cat2);
            session.persist(cat3);

            session.getTransaction().commit();
        }

        /* ошибка
        try (Session session = sessionFactory.openSession()) {
            List<Owner> owners = session.createQuery("FROM Owner", Owner.class).list();

            for (Owner owner : owners) {
                System.out.println(owner.getName() + "'s Cats: " + owner.getCats().size());
            }
        }
        */

        try(Session session = sessionFactory.openSession()) {
            List<Owner> owners = session.createQuery("SELECT DISTINCT o FROM Owner o JOIN FETCH o.cats", Owner.class)
                    .list();
            for (Owner owner : owners) {
                System.out.println(owner.getName() + " " + owner.getCats().size());
            }
        }
    }

}
