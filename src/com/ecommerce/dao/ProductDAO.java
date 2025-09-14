package com.ecommerce.dao;

import com.ecommerce.model.Product;
import com.ecommerce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> listAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id,name,description,price,stock,image FROM products ORDER BY created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setStock(rs.getInt("stock"));
                p.setImage(rs.getString("image"));
                list.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public Product getById(int id) {
        String sql = "SELECT id,name,description,price,stock,image FROM products WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStock(rs.getInt("stock"));
                    p.setImage(rs.getString("image"));
                    return p;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
