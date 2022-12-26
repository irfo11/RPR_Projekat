package ba.rpr.dao;

import ba.rpr.dao.exceptions.ElementNotFoundException;
import ba.rpr.domain.Micronutrient;

/**
 * Dao interface for Micronutrient class
 */
public interface MicronutrientDao extends Dao<Micronutrient> {
    /**
     * Returns micronutrient with the same name given as parameter
     *
     * @param name - name of the micronutrient to be returned
     * @return micronutrient with the same name given as parameter
     * @throws ElementNotFoundException - if element with given name can't be found in database
     */
    Micronutrient searchByName(String name);
}
