package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;

import java.sql.*;
import java.util.*;

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
            if(rs.next())
                return new Source(rs.getInt("id"), rs.getString("name"));
            else
                return null;
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
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName()).append(" WHERE name=?");
        try(PreparedStatement stmt = getConnection().prepareStatement(query.toString())) {
            stmt.setString(1, name);
            return row2object(stmt.executeQuery());
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
