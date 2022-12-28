package ba.rpr.dao;

import ba.rpr.domain.Presence;
import ba.rpr.dao.exceptions.DaoException;

import java.util.List;

/**
 * Dao interface for Presence class
 */
public interface PresenceDao extends Dao<Presence> {
    /**
     * Returns a sorted list of Presence objects that have the given source as a field.
     * The first element has the highest presence inside the source.
     *
     * @param sourceName - name of the source whose micronutrients are returned
     * @return sorted list of Presence objects that have the given source as a field
     * @throws DaoException - if source with the given sourceName can't be found in database
     */
    List<Presence> micronutrientsInSource(String sourceName) throws DaoException; //sorting done by sql

    /**
     * Returns a sorted list of Presence objects that have the given micronutrient as a field
     * The first element has the highest presence inside the micronutrient.
     *
     * @param micronutrientName - name of the micronutrient whose sources are returned
     * @return sorted list of Presence objects that have the given micronutrient as a field
     * @throws DaoException - if micronutrient with given micronutrientName can't be found in database
     */
    List<Presence> sourcesOfMicronutrient(String micronutrientName) throws DaoException; //sorting done by sql

    /**
     * Returns Presence object from database based on given micronutrient name and source name
     *
     * @param micronutrientName - name of the micronutrient in presence element
     * @param sourceName - name of the source to be found in presence element
     * @return presence object with the given micronutrient name and source name, null if element does not exist
     * @throws DaoException - if micronutrient with given micronutrientName or source with given sourceName
     * or presence element with given names can't be found in database
     */
    Presence searchByMicronutrientAndSource(String micronutrientName, String sourceName) throws DaoException;
}
