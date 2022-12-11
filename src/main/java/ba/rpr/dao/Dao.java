package ba.rpr.dao;

import java.util.List;

/**
 * Root interface for all Dao classes
 */
public interface Dao<T> {
    /**
     * Returns entity from database based on given id, null if there is no element with the same id
     *
     * @param id - the id of the entity
     * @return entity that has the same id, null if there is no element with the same id
     */
    T getById(int id);

    /**
     * Adds entity to database
     *
     * @param item - entity to be added to database
     */
    void add(T item);

    /**
     * Deletes entity from database based on id
     *
     * @param id - id of entity to be deleted
     */
    void delete(int id);

    /**
     * Updates the entity with the same id
     *
     * @param id - id of the entity to be updated
     * @param item - object that contains updates for the entity
     */
    void update(int id, T item);

    /**
     * Returns all entities inside database
     *
     * @return all entities inside database
     */
    List<T> getAll();
}
