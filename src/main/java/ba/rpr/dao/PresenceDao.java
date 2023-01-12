package ba.rpr.dao;

import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;

import java.util.List;

/**
 * Dao interface for Presence class
 */
public interface PresenceDao extends Dao<Presence> {
    /**
     * Returns a list of Presence objects that have the given source as a field.
     *
     * @param source - source whose micronutrients are returned
     * @return list of Presence objects that have the given source as a field
     * @throws DaoException - if there are problems with database server
     */
    List<Presence> micronutrientsInSource(Source source) throws DaoException;

    /**
     * Returns a list of Presence objects that have the given micronutrient as a field
     *
     * @param micronutrient - micronutrient whose sources are returned
     * @return list of Presence objects that have the given micronutrient as a field
     * @throws DaoException - if there are problems with the database server
     */
    List<Presence> sourcesOfMicronutrient(Micronutrient micronutrient) throws DaoException;
}
