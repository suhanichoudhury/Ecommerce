package com.ecommerce.servlet;

import com.ecommerce.dao.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/product")
public class ProductDetailsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null) {
            ProductDAO dao = new ProductDAO();
            req.setAttribute("product", dao.getById(Integer.parseInt(id)));
            req.getRequestDispatcher("productDetails.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
