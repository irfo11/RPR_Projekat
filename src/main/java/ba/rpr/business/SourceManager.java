package ba.rpr.business;

import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;

import java.util.List;

/**
 * Manager class for Source, which contains business logic
 */
public class SourceManager implements Manager<Source>{
    private void validateName(String name) throws DaoException{
        if(name == null || name.length() < 3 || name.length() > 45)
            throw new DaoException("Source must have name length between 3 to 45 characters.");
        if(!name.matches("^[a-zA-Z ]*$"))
            throw new DaoException("Source name can only contain letters and whitespaces.");
    }
    public Source getById(int id) throws DaoException{
        return DaoFactory.sourceDao().getById(id);
    }

    public void add(Source source) throws DaoException {
        validateName(source.getName());
        try {
            DaoFactory.sourceDao().add(source);
        } catch(DaoException e) {
            if(e.getMessage().contains("Duplicate entry"))
                throw new DaoException("Cannot add Source, because Source with the same name already exists");
            throw e;
        }
    }

    public void delete(int id) throws DaoException{
        try {
            DaoFactory.sourceDao().delete(id);
        } catch(DaoException e) {
            if(e.getMessage().contains("FOREIGN KEY"))
                throw new DaoException("Cannot delete Source, because it is contained inside a Presence/s. " +
                        "First delete the Presence element/s.");
            throw e;
        }
    }

    public void update(int id, Source source) throws DaoException{
        validateName(source.getName());
        try {
            DaoFactory.sourceDao().update(id, source);
        } catch(DaoException e) {
            if(e.getMessage().contains("Duplicate entry"))
                throw new DaoException("Cannot update Source, because Source with the same name already exists");
            throw e;
        }
    }

    public List<Source> getAll() throws DaoException {
        return DaoFactory.sourceDao().getAll();
    }

    public Source searchByName(String name) throws DaoException {
        return DaoFactory.sourceDao().searchByName(name);
    }
}
