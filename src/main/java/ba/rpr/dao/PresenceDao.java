package ba.rpr.dao;

import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;
import java.util.SortedSet;

/**
 * Dao interface for Presence class
 */
public interface PresenceDao extends Dao<Presence> {
    /**
     * Returns a sorted set of Presence objects that have the given source as a field.
     * The first element has the highest presence inside the source.
     * @param sourceName - name of the source whose micronutrients are returned
     * @return sorted set of Presence objects that have the given source as a field
     */
    SortedSet<Presence> micronutrientsInSource(String sourceName);

    /**
     * Returns a sorted set of Presence objects that have the given micronutrient as a field
     * The first element has the highest presence inside the micronutrient.
     * @param micronutrientName - name of the micronutrient whose sources are returned
     * @return sorted set of Presence objects that have the given micronutrient as a field
     */
    SortedSet<Presence> sourcesOfMicronutrient(String micronutrientName);

    /**
     * Returns Presence object from database based on given micronutrient name and source name,
     * null if element does not exist.
     * @param micronutrientName - name of the micronutrient in presence element
     * @param sourceName - name of the source to be found in presence element
     * @return presence object with the given micronutrient name and source name, null if element does not exist
     */
    Presence searchByMicronutrientAndSource(String micronutrientName, String sourceName);
}
