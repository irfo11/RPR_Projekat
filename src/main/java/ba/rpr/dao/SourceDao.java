package ba.rpr.dao;

import ba.rpr.domain.Source;

/**
 * Dao interface for Source class
 */
public interface SourceDao extends Dao<Source>{
    /**
     * Return source object with the same name given as parameter
     * @param name - name of the source to be returned
     * @return source object with the same name given as parameter
     */
    Source searchByName(String name);
}
