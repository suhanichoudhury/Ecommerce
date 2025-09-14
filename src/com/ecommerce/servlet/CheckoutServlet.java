package com.ecommerce.servlet;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.OrderDAO;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        CartDAO cartDAO = new CartDAO();
        List<CartItem> items = cartDAO.getCartItems(user.getId());
        double total = items.stream().mapToDouble(i -> i.getProduct().getPrice().doubleValue() * i.getQuantity()).sum();
        OrderDAO orderDAO = new OrderDAO();
        int orderId = orderDAO.placeOrder(user.getId(), items, total);
        if (orderId > 0) {
            cartDAO.clearCart(user.getId());
            req.setAttribute("message", "Order placed successfully: Order ID " + orderId);
            req.getRequestDispatcher("orderSuccess.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Failed to place order. Please try again.");
            req.getRequestDispatcher("cart.jsp").forward(req, resp);
        }
    }
}
