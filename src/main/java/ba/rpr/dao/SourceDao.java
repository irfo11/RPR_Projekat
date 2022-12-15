package ba.rpr.dao;

import ba.rpr.dao.exceptions.ElementNotFoundException;
import ba.rpr.domain.Source;
import java.util.SortedSet;

/**
 * Dao interface for Source class
 */
public interface SourceDao extends Dao<Source>{
    /**
     * Returns source object with the same name given as parameter, null if there is no element with the same name
     *
     * @param name - name of the source to be returned
     * @return source object with the same name given as parameter, null it there is no element with the same name
     * @throws ElementNotFoundException - if element with given name can't be found in database
     */
    Source searchByName(String name);

    /**
     * Returns all the sources in database sorted by name
     *
     * @return all sources sorted by name
     */
    SortedSet<Source> getAll();
}
