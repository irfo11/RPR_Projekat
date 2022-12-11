package ba.rpr.dao;

import ba.rpr.domain.Source;

/**
 * Dao interface for Source class
 */
public interface SourceDao extends Dao<Source>{
    /**
     * Returns source object with the same name given as parameter, null if there is no element with the same name
     * @param name - name of the source to be returned
     * @return source object with the same name given as parameter, null it there is no element with the same name
     */
    Source searchByName(String name);
}
