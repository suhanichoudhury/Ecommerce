package com.ecommerce.servlet;

import com.ecommerce.dao.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        req.setAttribute("products", dao.listAll());
        req.getRequestDispatcher("products.jsp").forward(req, resp);
    }
}
