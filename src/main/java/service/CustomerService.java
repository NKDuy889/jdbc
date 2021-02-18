package service;

import model.Customers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService implements ICustomerService {
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:6699/customer",
                    "root",
                    ""
            );
        } catch (ClassNotFoundException e) {
            System.out.println("không có driver");
        } catch (SQLException throwables) {
            System.out.println("Không kết nối được");
        }
        System.out.println("ket noi thanh cong");

        return connection;
    }


    @Override
    public List findAll() {
        List<Customers> customers = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                // int id = resultSet.getInt(1);
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                customers.add(new Customers(id, name, age, address));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customers;
    }

    @Override
    public Customers findById(int id) {
        Customers customer = null;
        Connection connection = getConnection();


        try {

//            PreparedStatement p = connection.prepareStatement("select * from customers where id=?");
//            p.setInt(1, id);
            CallableStatement callableStatement = connection.prepareCall("{call getById(?)}");
            callableStatement.setInt(1,id);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                customer = new Customers(id, name, age, address);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer;
    }

    @Override
    public boolean update(Customers customers) {
        Connection connection = getConnection();
        boolean check = false;
        try {
            PreparedStatement p = connection.prepareStatement("update customers set name= ?, age= ?, address= ? where id =?");
            p.setInt(4, customers.getId());
            p.setString(1, customers.getName());
            p.setInt(2, customers.getAge());
            p.setString(3, customers.getAddress());
            check = p.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return check;
    }

    @Override
    public List<Customers> findByName(String name){
        List<Customers> customer = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement("select * from customers where name like ?");
            p.setString(1,"%"+ name + "%");
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String namee = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                customer.add(new Customers(id, namee, age, address));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer;
    }

    @Override
    public boolean save(Customers customer, int[] p) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtAssignment = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            // Bước 1: Thêm 1 sản phẩm vào bảng product
            pstmt = conn.prepareStatement("INSERT INTO customers  (name, age ,address) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, customer.getName());
            pstmt.setInt(2, customer.getAge());
            pstmt.setString(3, customer.getAddress());
            int rowAffected = pstmt.executeUpdate();
            // Bước 2: Lấy id của sản phẩm đó
            rs = pstmt.getGeneratedKeys();
            int pId = 0;
            if (rs.next())
                pId = rs.getInt(1);
            // Thêm mới vào bảng product_permision
            if (rowAffected == 1) {
                String sqlPivot = "INSERT INTO customers1(customerId,colorId) VALUES(?,?)";
                pstmtAssignment = conn.prepareStatement(sqlPivot);
                //Lấy danh sách id của permision và thêm mới vào bảng product_permision
                for (int permisionId : p) {
                    pstmtAssignment.setInt(1, pId);
                    pstmtAssignment.setInt(2, permisionId);
                    pstmtAssignment.executeUpdate();
                }
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (pstmtAssignment != null) pstmtAssignment.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
}
