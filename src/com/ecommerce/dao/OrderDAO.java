package com.ecommerce.dao;

import com.ecommerce.model.CartItem;
import com.ecommerce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class OrderDAO {
    public int placeOrder(int userId, List<CartItem> items, double total) {
        String ordSql = "INSERT INTO orders (user_id,total,status) VALUES (?,?,?)";
        String itemSql = "INSERT INTO order_items (order_id,product_id,quantity,price) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection()) {
            try {
                c.setAutoCommit(false);
                try (PreparedStatement ps = c.prepareStatement(ordSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    ps.setDouble(2, total);
                    ps.setString(3, "PLACED");
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int orderId = rs.getInt(1);
                            try (PreparedStatement pis = c.prepareStatement(itemSql)) {
                                for (CartItem ci : items) {
                                    pis.setInt(1, orderId);
                                    pis.setInt(2, ci.getProduct().getId());
                                    pis.setInt(3, ci.getQuantity());
                                    pis.setDouble(4, ci.getProduct().getPrice().doubleValue());
                                    pis.addBatch();
                                }
                                pis.executeBatch();
                            }
                            c.commit();
                            return orderId;
                        }
                    }
                }
            } catch (Exception e) {
                c.rollback();
                e.printStackTrace();
            } finally {
                c.setAutoCommit(true);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }
}
