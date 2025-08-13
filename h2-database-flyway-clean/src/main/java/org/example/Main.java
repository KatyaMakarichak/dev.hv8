package org.example;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) throws Exception {
        // Flyway міграції
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:./test", "sa", "")
                .load();
        flyway.migrate();

        try (Connection connection = DriverManager.getConnection("jdbc:h2:./test", "sa", "")) {
            ClientService service = new ClientService(connection);

            long id = service.create("David");
            System.out.println("Новий клієнт ID: " + id);

            System.out.println("Отримати по ID: " + service.getById(id));

            service.setName(id, "Dave");
            System.out.println("Після оновлення: " + service.getById(id));

            System.out.println("Всі клієнти: " + service.listAll());

            service.deleteById(id);
            System.out.println("Видалено. Всі клієнти: " + service.listAll());
        }
    }
}
