package ba.rpr.business;

import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;

import java.util.List;

/**
 * Manager class for Micronutrient, which contains business logic
 */
public class MicronutrientManager implements Manager<Micronutrient>{
    private void validateName(String name) throws DaoException {
        if(name == null || name.length() < 1 || name.length() > 45)
            throw new DaoException("Micronutrient must have name length between 1 to 45 characters");
        if(name.toLowerCase().contains("vitamin"))
            throw new DaoException("Micronutrient does not need to have 'vitamin' in its name");
    }

    public Micronutrient getById(int id) throws DaoException {
        return DaoFactory.micronutrientDao().getById(id);
    }

    public void add(Micronutrient micronutrient) throws DaoException {
        validateName(micronutrient.getName());
        try {
            DaoFactory.micronutrientDao().add(micronutrient);
        } catch(DaoException e) {
            if(e.getMessage().contains("Duplicate entry"))
                throw new DaoException("Cannot add micronutrient, because Micronutrient with the same name already exists");
            throw e;
        }
    }

    public void delete(int id) throws DaoException {
        try {
            DaoFactory.micronutrientDao().delete(id);
        } catch(DaoException e) {
            if(e.getMessage().contains("FOREIGN KEY"))
                throw new DaoException("Cannot delete Micronutrient, because it is contained inside a Presence/s. " +
                        "First delete the Presence element/s.");
            throw e;
        }
    }

    public void update(int id, Micronutrient micronutrient) throws DaoException {
        validateName(micronutrient.getName());
        try {
            DaoFactory.micronutrientDao().update(id, micronutrient);
        } catch(DaoException e) {
            if(e.getMessage().contains("Duplicate entry"))
                throw new DaoException("Cannot update Micronutrient, because Micronutrient with the same name already exists");
            throw e;
        }
    }

    public List<Micronutrient> getAll() throws DaoException{
        return DaoFactory.micronutrientDao().getAll();
    }

    public Micronutrient searchByName(String name) throws DaoException {
        return DaoFactory.micronutrientDao().searchByName(name);
    }

}
