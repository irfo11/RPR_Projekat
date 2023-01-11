package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;

import java.sql.SQLException;
import java.util.List;

/**
 * Root interface for all Dao classes
 */

public interface Dao<T> {
    /**
     * Returns entity from database based on given id, null if element with given id can't be found in database
     *
     * @param id - the id of the entity
     * @return entity that has the same id
     * @throws DaoException - if there are problems with database server
     */
    T getById(int id) throws DaoException;

    /**
     * Adds entity to database
     *
     * @param item - entity to be added to database
     * @throws DaoException - if there are problems with database server
     */
    void add(T item) throws DaoException;

    /**
     * Deletes entity from database based on id
     *
     * @param id - id of entity to be deleted
     * @throws DaoException - if there are problems with database server
     */
    void delete(int id) throws DaoException;

    /**
     * Updates the entity with the same id
     *
     * @param id - id of the entity to be updated
     * @param item - object that contains updates for the entity
     * @throws DaoException - if there are problems with database server
     */
    void update(int id, T item) throws DaoException;

    /**
     * Returns all elements in table
     *
     * @return all elements in table
     * @throws DaoException - if there are problems with database server
     */
    List<T> getAll() throws DaoException;

}
