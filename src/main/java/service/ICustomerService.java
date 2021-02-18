package service;

import model.Customers;

import java.util.List;

public interface ICustomerService extends IService<Customers> {
    List<Customers> findByName(String name);
}
