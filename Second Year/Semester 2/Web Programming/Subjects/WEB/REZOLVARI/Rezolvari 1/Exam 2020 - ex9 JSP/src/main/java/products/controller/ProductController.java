package products.controller;

import products.model.Product;
import products.model.User;
import products.service.ProductService;
import products.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProductController extends HttpServlet {

    public ProductController(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("add")!=null) {
            // prints text data to a character stream.
            PrintWriter out = response.getWriter();

            response.setContentType("text/html");

            // get the parameters from the request
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            System.out.println(name + " " + description);
            ProductService.addProduct(name, description);

            response.sendRedirect("success.jsp");
        }
        else if(request.getParameter("dropdownAction") != null){
            System.out.println("Product is: " + request.getParameter("category") + " quantity: " + request.getParameter("quantity"));
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("Username");
            System.out.println("user is: " + user);
            ProductService.addToCart(user, Integer.parseInt(request.getParameter("category")), Integer.parseInt(request.getParameter("quantity")));
            response.sendRedirect("success.jsp");
        }
        else if(request.getParameter("finalize") != null){
            System.out.println("HEEEY");
            ProductService.finalizeCommand();
            response.sendRedirect("success.jsp");
        }
        else if(request.getParameter("cancel") != null){
            System.out.println("HE");
            ProductService.cancelOrder();
            response.sendRedirect("success.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (request.getParameter("filterAction") != null) {
            String filter = request.getParameter("filter");
            System.out.println("filter:  " + filter);
            List<Product> res = ProductService.getProducts(filter);
            //System.out.println(res);


            request.setAttribute("products", res);
            //request.getRequestDispatcher("products.jsp").forward(request, response);
            HttpSession session = request.getSession();
            session.setAttribute("products", res);
            //response.sendRedirect("products.jsp");
            request.getRequestDispatcher("success.jsp").forward(request, response);
            response.sendRedirect("success.jsp");
        }
    }
}
