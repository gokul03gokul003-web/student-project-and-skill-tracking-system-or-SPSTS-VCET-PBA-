package com.vcet.tracking.dao;

import java.util.List;
import com.vcet.tracking.exception.DatabaseException;

/**
 * Generic Data Access Object Interface.
 * Demonstrates Unit 2: Interfaces and Unit 5: Generic Programming.
 *
 * @param <T> The entity type
 * @param <K> The primary key type
 */
public interface GenericDAO<T, K> {
    boolean insert(T entity) throws DatabaseException;
    boolean update(T entity) throws DatabaseException;
    boolean delete(K id) throws DatabaseException;
    T findById(K id) throws DatabaseException;
    List<T> findAll() throws DatabaseException;
}
