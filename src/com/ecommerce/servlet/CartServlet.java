package com.ecommerce.servlet;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo(); // /add or /update or /remove
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        CartDAO cart = new CartDAO();
        if ("/add".equals(action)) {
            int pid = Integer.parseInt(req.getParameter("productId"));
            int qty = Integer.parseInt(req.getParameter("qty"));
            cart.addToCart(user.getId(), pid, qty);
            resp.sendRedirect(req.getContextPath() + "/cart/view");
        } else if ("/update".equals(action)) {
            int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));
            int qty = Integer.parseInt(req.getParameter("qty"));
            cart.updateQuantity(cartItemId, qty);
            resp.sendRedirect(req.getContextPath() + "/cart/view");
        } else if ("/remove".equals(action)) {
            int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));
            cart.removeItem(cartItemId);
            resp.sendRedirect(req.getContextPath() + "/cart/view");
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        CartDAO cart = new CartDAO();
        if ("/view".equals(action)) {
            req.setAttribute("cartItems", cart.getCartItems(user.getId()));
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        }
    }
}
