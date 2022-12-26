package ba.rpr.dao;

import ba.rpr.dao.exceptions.ElementAlreadyExistsException;
import ba.rpr.dao.exceptions.ElementNotFoundException;

import java.util.SortedSet;

/**
 * Root interface for all Dao classes
 */

public interface Dao<T> {
    /**
     * Returns entity from database based on given id
     *
     * @param id - the id of the entity
     * @return entity that has the same id
     * @throws ElementNotFoundException - if element with given id can't be found in database
     */
    T getById(int id);

    /**
     * Adds entity to database
     *
     * @param item - entity to be added to database
     * @throws ElementAlreadyExistsException - if element already exist, based on user defined property
     */
    void add(T item);

    /**
     * Deletes entity from database based on id
     *
     * @param id - id of entity to be deleted
     * @throws ElementNotFoundException - if element with given id can't be found in database
     */
    void delete(int id);

    /**
     * Updates the entity with the same id
     *
     * @param id - id of the entity to be updated
     * @param item - object that contains updates for the entity
     * @throws ElementNotFoundException - if element with given id can't be found in database
     * @throws ElementAlreadyExistsException - if element that we are updating changes one of its unique columns
     * to a value that already exist
     */
    void update(int id, T item);

    /**
     * Returns all elements in table sorted by user defined comparison
     *
     * @return all elements in table sorted
     */
    SortedSet<T> getAll();

}
