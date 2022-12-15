package ba.rpr.dao;

import ba.rpr.dao.exceptions.ElementAlreadyExistsException;
import ba.rpr.dao.exceptions.ElementNotFoundException;

/**
 * Root interface for all Dao classes
 */
/*
 should format name for micronutrient and sources when using add method, could do that by changing setters and constructor
 in domain classes so first letter is capitalized and rest small letters (or in javaFX, but then terminal app would not be able to use it).
 So sql can see if it unique. Presence is a little
 harder now I have to always check before adding if there is a already a link before adding, sql can't take care of that.
 Create function that builds object from resultSet. Or find a way how to use getObject()
 Gotta add throws in javadoc for the main Dao interface aswell even if its an interface

 u add mozes dodat id i da baza vrati error ako objekat sa istim id-em postoji
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

}
