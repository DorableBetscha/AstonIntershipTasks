package org.example;
import com.myproject.myapp.BaseEntity;
import com.myproject.myapp.Cat;
import com.myproject.myapp.Owner;
import com.myproject.myapp.Veterinarian;
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

            List<Cat> cats = session.createQuery("FROM Cat", Cat.class).list();
            assertEquals(1, cats.size());
            assertEquals("Pushishka", cats.get(0).getName());
            assertEquals("Mark", cats.get(0).getOwner().getName());
        }
    }

    @Test
    void testUpdateOwner() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Owner owner = new Owner();
            owner.setName("Mark");
            session.persist(owner);

            session.getTransaction().commit();

            session.beginTransaction();
            owner.setName("Alice");
            session.update(owner);
            session.getTransaction().commit();

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

            Cat cat = new Cat();
            cat.setName("Fluffy");

            Veterinarian vet = new Veterinarian();
            vet.setName("Dr.Black");

            cat.getVeterinarians().add(vet);

            session.persist(cat);
            session.persist(vet);

            session.getTransaction().commit();

            List<Cat> cats = session.createQuery("FROM Cat", Cat.class).list();
            assertEquals(1, cats.size());
            assertEquals("Dr.Black", cats.get(0).getVeterinarians().iterator().next().getName());
        }
    }
}
