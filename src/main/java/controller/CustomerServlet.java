package controller;

import model.Customers;
import service.CustomerService;
import service.ICustomerService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    private ICustomerService customerService = new CustomerService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";

        }
        switch (action) {
            case "":
                showAllCustomer(request, response);
                break;
            case "edit":
                editCustomer(request, response);
                break;

        }
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String address = request.getParameter("address");
        int id = Integer.parseInt(request.getParameter("id"));
        Customers c = new Customers(id, name, age, address);
        this.customerService.update(c);
        response.sendRedirect("/customer");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null){
            action = "";

        }
        switch (action){
            case "":
                showAllCustomer(request, response);
                break;
            case "edit":
                showFormEdit(request, response);
                break;
            case "search":
                searchByName(request,response);
                break;
        }
    }

    private void showFormEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customers customer = this.customerService.findById(id);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("edit.jsp");
        request.setAttribute("customer", customer);
        requestDispatcher.forward(request, response);
    }

    private void showAllCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
        List<Customers> customerList = customerService.findAll();
        request.setAttribute("list", customerList);
        requestDispatcher.forward(request, response);
    }

    private void searchByName(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("name");
        List<Customers> customerList = customerService.findByName(name);
        request.setAttribute("list",customerList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");
        try {
            dispatcher.forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
