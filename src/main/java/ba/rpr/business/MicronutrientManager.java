package ba.rpr.business;

import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;

import java.util.List;

public class MicronutrientManager {
    public Micronutrient getById(int id) throws DaoException {
        try {
            return DaoFactory.micronutrientDao().getById(id);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element does not exist"))
                throw new DaoException("Micronutrient with id=" + id + " does not exist");
            throw e;
        }
    }

    public void add(Micronutrient micronutrient) throws DaoException {
        if(micronutrient == null || micronutrient.getName().length() < 1 ||micronutrient.getName().length() > 45)
            throw new DaoException("Micronutrient must have name length between 3 to 45 characters");
        for(String token: micronutrient.getName().split(" "))
            if(token.equalsIgnoreCase("vitamin"))
                throw new DaoException("Micronutrient does not need 'vitamin' in its name");
        try {
            DaoFactory.micronutrientDao().add(micronutrient);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element already exists"))
                throw new DaoException("Cannot add micronutrient, because micronutrient with the same name already exists");
            throw e;
        }
    }

    public void delete(int id) throws DaoException {
        try {
            DaoFactory.micronutrientDao().delete(id);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element does not exist"))
                throw new DaoException("Micronutrient with id=" + id + " does not exist");
            throw e;
        }
    }

    public void update(int id, Micronutrient micronutrient) throws DaoException {
        if(micronutrient == null || micronutrient.getName().length() < 1 ||micronutrient.getName().length() > 45)
            throw new DaoException("Micronutrient must have name length between 3 to 45 characters");
        for(String token: micronutrient.getName().split(" "))
            if(token.equalsIgnoreCase("vitamin"))
                throw new DaoException("Micronutrient does not need 'vitamin' in its name");
        try {
            DaoFactory.micronutrientDao().update(id, micronutrient);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element does not exist"))
                throw new DaoException("Micronutrient with id=" + id + " does not exist");
            else if(e.getMessage().equals("Element already exists"))
                throw new DaoException("Cannot add micronutrient, because micronutrient with the same name already exists");
            throw e;
        }
    }

    public List<Micronutrient> getAll() throws DaoException{
        return DaoFactory.micronutrientDao().getAll();
    }

    public Micronutrient searchByName(String name) throws DaoException {
        if(name == null || name.length() < 1 || name.length() > 45)
            throw new DaoException("Micronutrient must have name length between 3 to 45 characters");
        for(String token: name.split(" "))
            if(token.equalsIgnoreCase("vitamin"))
                throw new DaoException("Micronutrient does not need 'vitamin' in its name");
        if(name == null || name.length() == 0 || name.split(" ").length > 1)
            throw new DaoException("Micronutrient name must be one word, do not add vitamin before name");
        Micronutrient micronutrient = DaoFactory.micronutrientDao().searchByName(name);
        if(micronutrient == null)
            throw new DaoException("Micronutrient with the name=" + name + " does not exist");
        return micronutrient;
    }

}
