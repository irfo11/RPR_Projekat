package ba.rpr.business;

import ba.rpr.dao.Dao;
import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;

import java.util.List;

public class SourceManager {

    public Source getById(int id) throws DaoException{
        try {
            return DaoFactory.sourceDao().getById(id);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element does not exist"))
                throw new DaoException("Source with id=" + id + " does not exist");
            throw e;
        }
    }

    public void add(Source source) throws DaoException {
        if(source == null || source.getName().length() < 3 || source.getName().length() > 45)
            throw new DaoException("Source must have name length between 3 to 45 characters");
        try {
            DaoFactory.sourceDao().add(source);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element already exists"))
                throw new DaoException("Cannot add source, because source with the same name already exists");
            throw e;
        }
    }

    public void delete(int id) throws DaoException{
        try {
            DaoFactory.sourceDao().delete(id);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element does not exist"))
                throw new DaoException("Source with id=" + id + " does not exist");
            throw e;
        }
    }

    public void update(int id, Source source) throws DaoException{
        if(source == null || source.getName().length() < 3 || source.getName().length() > 45)
            throw new DaoException("Source must have name length between 3 to 45 characters");
        try {
            DaoFactory.sourceDao().update(id, source);
        } catch(DaoException e) {
            if(e.getMessage().equals("Element does not exist"))
                throw new DaoException("Source with id=" + id + " does not exist");
            else if(e.getMessage().equals("Element already exists"))
                throw new DaoException("Cannot update source, because source with the same name already exists");
            throw e;
        }
    }

    public List<Source> getAll() throws DaoException {
        return DaoFactory.sourceDao().getAll();
    }


}
