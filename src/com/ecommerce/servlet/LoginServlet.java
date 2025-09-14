package com.ecommerce.servlet;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserDAO dao = new UserDAO();
        User u = dao.authenticate(email, password);
        if (u != null) {
            req.getSession().setAttribute("user", u);
            resp.sendRedirect(req.getContextPath() + "/products");
        } else {
            req.setAttribute("error", "Invalid credentials");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
