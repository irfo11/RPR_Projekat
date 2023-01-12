package ba.rpr.dao;

import ba.rpr.domain.Presence;
import ba.rpr.dao.exceptions.DaoException;

import java.util.List;

/**
 * Dao interface for Presence class
 */
public interface PresenceDao extends Dao<Presence> {
    /**
     * Returns a list of Presence objects that have the given source as a field.
     *
     * @param sourceName - name of the source whose micronutrients are returned
     * @return list of Presence objects that have the given source as a field
     * @throws DaoException - if there are problems with database server
     */
    List<Presence> micronutrientsInSource(String sourceName) throws DaoException;

    /**
     * Returns a list of Presence objects that have the given micronutrient as a field
     *
     * @param micronutrientName - name of the micronutrient whose sources are returned
     * @return list of Presence objects that have the given micronutrient as a field
     * @throws DaoException - if there are problems with the database server
     */
    List<Presence> sourcesOfMicronutrient(String micronutrientName) throws DaoException;

    /**
     * Returns Presence object from database based on given micronutrient name and source name
     *
     * @param micronutrientName - name of the micronutrient in presence element
     * @param sourceName - name of the source to be found in presence element
     * @return presence object with the given micronutrient name and source name, null if element does not exist
     * @throws DaoException - if there are problems with database server
     */
    Presence searchByMicronutrientAndSource(String micronutrientName, String sourceName) throws DaoException;
}
