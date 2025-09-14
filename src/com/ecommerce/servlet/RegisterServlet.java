package com.ecommerce.servlet;

import com.ecommerce.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");

        UserDAO dao = new UserDAO();
        boolean ok = dao.register(name, email, password, phone);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?registered=1");
        } else {
            req.setAttribute("error", "Registration failed or email already exists.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
