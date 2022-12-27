package ba.rpr.dao;

import ba.rpr.domain.Micronutrient;
import ba.rpr.dao.exceptions.DaoException;

/**
 * Dao interface for Micronutrient class
 */
public interface MicronutrientDao extends Dao<Micronutrient> {
    /**
     * Returns micronutrient with the same name given as parameter
     *
     * @param name - name of the micronutrient to be returned
     * @return micronutrient with the same name given as parameter
     * @throws DaoException - if element with given name can't be found in database
     */
    Micronutrient searchByName(String name) throws DaoException;
}
