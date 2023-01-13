package ba.rpr.business;

import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;

import java.util.List;

/**
 * Manager class for Presence, which contains business logic
 */
public class PresenceManager {
    public Presence getById(int id) throws DaoException{
        return DaoFactory.presenceDao().getById(id);
    }

    public void add(Presence presence) throws DaoException {
        try {
            DaoFactory.presenceDao().add(presence);
        } catch(DaoException e) {
            if(e.getMessage().contains("Duplicate entry"))
                throw new DaoException("Cannot add Presence, because Presence with same Micronutrient and Source already exists");
            throw e;
        }
    }

    public void delete(int id) throws DaoException {
        DaoFactory.presenceDao().delete(id);
    }

    public void update(int id, Presence presence) throws DaoException {
        try {
            DaoFactory.presenceDao().update(id, presence);
        } catch(DaoException e) {
            if(e.getMessage().contains("Duplicate entry"))
                throw new DaoException("Cannot update Presence, because Presence with same Micronutrient and Source already exists");
            throw e;
        }
    }

    public List<Presence> micronutrientsInSource(Source source) throws DaoException {
        return DaoFactory.presenceDao().micronutrientsInSource(source);
    }

    public List<Presence> sourcesOfMicronutrient(Micronutrient micronutrient) throws DaoException {
        return DaoFactory.presenceDao().sourcesOfMicronutrient(micronutrient);
    }
}
