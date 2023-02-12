package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * SQL implementation for SourceDao
 */
public class SourceDaoSQLImpl extends AbstractDao<Source> implements SourceDao{
    private static SourceDaoSQLImpl instance = null;
    private SourceDaoSQLImpl() {
        super("sources");
    }

    public static SourceDaoSQLImpl getInstance() {
        if(instance == null)
            instance = new SourceDaoSQLImpl();
        return instance;
    }
    @Override
    public Source row2object(ResultSet rs) throws DaoException {
        try{
            return new Source(rs.getInt("id"), rs.getString("name"));
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> object2row(Source object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("id", object.getId());
        row.put("name", object.getName());
        return row;
    }

    @Override
    public Source searchByName(String name) throws DaoException {
        return executeQueryUnique("SELECT * FROM "+getTableName()+" WHERE name=?", new Object[]{name});
    }
}
