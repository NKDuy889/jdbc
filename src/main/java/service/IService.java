package service;

import java.util.List;

public interface IService<T> {
    List<T> findAll();
    T findById(int id);
    boolean update(T t);
    boolean save(T t, int[] p);
}
