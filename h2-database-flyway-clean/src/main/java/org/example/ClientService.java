package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final Connection connection;

    public ClientService(Connection connection) {
        this.connection = connection;
    }

    public long create(String name) throws Exception {
        validateName(name);
        String sql = "INSERT INTO client (name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new Exception("Не вдалося отримати ID нового клієнта");
                }
            }
        }
    }

    public String getById(long id) throws Exception {
        String sql = "SELECT name FROM client WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                } else {
                    throw new Exception("Клієнта з таким ID не знайдено");
                }
            }
        }
    }

    public void setName(long id, String name) throws Exception {
        validateName(name);
        String sql = "UPDATE client SET name = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setLong(2, id);
            if (ps.executeUpdate() == 0) {
                throw new Exception("Клієнта з таким ID не знайдено");
            }
        }
    }

    public void deleteById(long id) throws Exception {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            if (ps.executeUpdate() == 0) {
                throw new Exception("Клієнта з таким ID не знайдено");
            }
        }
    }

    public List<Client> listAll() throws SQLException {
        String sql = "SELECT id, name FROM client";
        List<Client> clients = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clients.add(new Client(rs.getLong("id"), rs.getString("name")));
            }
        }
        return clients;
    }

    private void validateName(String name) throws Exception {
        if (name == null || name.trim().length() < 3 || name.length() > 100) {
            throw new Exception("Ім'я має бути від 3 до 100 символів");
        }
    }
}
