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
     * Returns a sorted set of micronutrient objects that are found inside the given source.
     * The first element has the highest presence inside the source.
     * @param sourceName - name of the source whose micronutrients are returned
     * @return sorted set of micronutrient objects that are found inside the given source
     */
    SortedSet<Micronutrient> micronutrientsInSource(String sourceName);

    /**
     * Returns a sorted set of source objects that are a source of the given micronutrient.
     * The first element has the highest presence inside the micronutrient.
     * @param micronutrientName - name of the micronutrient whose sources are returned
     * @return sorted set of source objects that are a source of the given micronutrient
     */
    SortedSet<Source> sourcesOfMicronutrient(String micronutrientName);

}
