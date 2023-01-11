package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * SQL implementation for SourceDao
 */
public class SourceDaoSQLImpl extends AbstractDao<Source> implements SourceDao{

    public SourceDaoSQLImpl() {
        super("sources");
    }

    @Override
    public Source row2object(ResultSet rs) throws DaoException { //return null ako nema elementa
        Source source = null;
        try{
            if(rs.next()) {
                source = new Source(rs.getInt("id"), rs.getString("name"));
            }
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return source;
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
        Source source = null;
        try(PreparedStatement stmt = getConnection().prepareStatement(query.toString())) {
            stmt.setString(1, name);
            source = row2object(stmt.executeQuery());
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return source;
    }
}
