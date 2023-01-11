package ba.rpr.dao;

import ba.rpr.domain.Micronutrient;
import ba.rpr.dao.exceptions.DaoException;

/**
 * Dao interface for Micronutrient class
 */
public interface MicronutrientDao extends Dao<Micronutrient> {
    /**
     * Returns micronutrient with the same name given as parameter, null if there is no element with the same name
     *
     * @param name - name of the micronutrient to be returned
     * @return micronutrient with the same name given as parameter, null if there is no element with the same name
     * @throws DaoException - if there are problems with database server
     */
    Micronutrient searchByName(String name) throws DaoException;
}
