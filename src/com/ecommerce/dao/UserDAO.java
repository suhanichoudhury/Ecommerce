package com.ecommerce.dao;

import com.ecommerce.model.User;
import com.ecommerce.util.DBConnection;
import com.ecommerce.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public boolean register(String name, String email, String password, String phone) {
        String sql = "INSERT INTO users (name,email,password_hash,phone) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.hash(password));
            ps.setString(4, phone);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authenticate(String email, String password) {
        String sql = "SELECT id,name,email,phone,password_hash FROM users WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("password_hash");
                    if (stored.equals(PasswordUtil.hash(password))) {
                        User u = new User();
                        u.setId(rs.getInt("id"));
                        u.setName(rs.getString("name"));
                        u.setEmail(rs.getString("email"));
                        u.setPhone(rs.getString("phone"));
                        return u;
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
