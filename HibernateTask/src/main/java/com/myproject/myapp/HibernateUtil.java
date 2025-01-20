package com.myproject.myapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory(); //статическая переменная инициализируемая при загрузке класса

    private static SessionFactory buildSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    public static void shutdown() {
        getSessionFactory().close();
    }

    public static void main(String[] args) {
        // Создание SessionFactory
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        // Открытие сессии
        Session session = sessionFactory.openSession();

        // Начало транзакции
        session.beginTransaction();

        // Пример создания объектов
        Owner owner = new Owner();
        owner.setName("Alice");
        session.save(owner);

        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setName("Dr. Smith");
        session.save(veterinarian);

        Cat cat = new Cat();
        cat.setName("Whiskers");
        cat.setOwner(owner);
        cat.getVeterinarians().add(veterinarian);
        session.save(cat);

        // Завершение транзакции
        session.getTransaction().commit();

        // Полиморфный запрос для получения всех сущностей
        Query<BaseEntity> query = session.createQuery("FROM BaseEntity", BaseEntity.class);
        List<BaseEntity> entities = query.getResultList();

        // Вывод результатов
        for (BaseEntity entity : entities) {
            if (entity instanceof Owner) {
                System.out.println("Owner: " + ((Owner) entity).getName());
            } else if (entity instanceof Veterinarian) {
                System.out.println("Veterinarian: " + ((Veterinarian) entity).getName());
            } else if (entity instanceof Cat) {
                System.out.println("Cat: " + ((Cat) entity).getName());
            }
        }

        // Закрытие сессии
        session.close();
        sessionFactory.close();
    }
}
