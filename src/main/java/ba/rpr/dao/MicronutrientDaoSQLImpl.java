package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * MySQL implementation of Dao
 */
public class MicronutrientDaoSQLImpl extends AbstractDao<Micronutrient> implements MicronutrientDao{
    private static MicronutrientDaoSQLImpl instance = null;
    private MicronutrientDaoSQLImpl() {
        super("micronutrients");
    }

    public static MicronutrientDaoSQLImpl getInstance() {
        if(instance == null)
            instance = new MicronutrientDaoSQLImpl();
        return instance;
    }

    @Override
    public Micronutrient row2object(ResultSet rs) throws DaoException{
        try {
            return new Micronutrient(rs.getInt("id"),
                                         rs.getString("name"),
                                         rs.getString("role"),
                                         rs.getBoolean("isVitamin"));
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> object2row(Micronutrient object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("id", object.getId());
        row.put("name", object.getName());
        row.put("role", object.getRole());
        row.put("isVitamin", object.isVitamin());
        return row;
    }

    @Override
    public Micronutrient searchByName(String name) throws DaoException {
        return executeQueryUnique("SELECT * FROM "+getTableName()+" WHERE name=?", new Object[]{name});
    }
}
