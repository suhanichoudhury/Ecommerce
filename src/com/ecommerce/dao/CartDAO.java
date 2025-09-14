package com.ecommerce.dao;

import com.ecommerce.model.Product;
import com.ecommerce.model.CartItem;
import com.ecommerce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    public void addToCart(int userId, int productId, int qty) {
        try (Connection c = DBConnection.getConnection()) {
            String check = "SELECT id,quantity FROM cart_items WHERE user_id=? AND product_id=?";
            try (PreparedStatement ps = c.prepareStatement(check)) {
                ps.setInt(1, userId); ps.setInt(2, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        int existing = rs.getInt("quantity");
                        String upd = "UPDATE cart_items SET quantity=? WHERE id=?";
                        try (PreparedStatement ps2 = c.prepareStatement(upd)) {
                            ps2.setInt(1, existing + qty); ps2.setInt(2, id);
                            ps2.executeUpdate();
                        }
                        return;
                    }
                }
            }
            String sql = "INSERT INTO cart_items (user_id,product_id,quantity) VALUES (?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, userId); ps.setInt(2, productId); ps.setInt(3, qty);
                ps.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT ci.id,ci.quantity,p.id as pid,p.name,p.description,p.price,p.image FROM cart_items ci JOIN products p ON ci.product_id=p.id WHERE ci.user_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("pid"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setImage(rs.getString("image"));
                    CartItem ci = new CartItem();
                    ci.setId(rs.getInt("id"));
                    ci.setProduct(p);
                    ci.setQuantity(rs.getInt("quantity"));
                    list.add(ci);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void updateQuantity(int cartItemId, int qty) {
        String sql = "UPDATE cart_items SET quantity=? WHERE id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, qty); ps.setInt(2, cartItemId); ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void removeItem(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cartItemId); ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void clearCart(int userId) {
        String sql = "DELETE FROM cart_items WHERE user_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId); ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
