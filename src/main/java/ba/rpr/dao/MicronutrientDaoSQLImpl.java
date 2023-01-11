package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class MicronutrientDaoSQLImpl extends AbstractDao<Micronutrient> implements MicronutrientDao{

    public MicronutrientDaoSQLImpl() {
        super("micronutrients");
    }

    @Override
    public Micronutrient row2object(ResultSet rs) throws DaoException{ //vraca null ako nema nista jer je jednostavnije koristiti
                                                                        //za ostale funkcije
        Micronutrient micronutrient = null;
        try {
            if(rs.next()) {
                micronutrient = new Micronutrient(rs.getInt("id"),
                                                  rs.getString("name"),
                                                  rs.getString("role"),
                                                  rs.getBoolean("isVitamin"));
            }
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return micronutrient;
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
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName()).append(" WHERE name=?");
        Micronutrient micronutrient = null;
        try(PreparedStatement stmt = getConnection().prepareStatement(query.toString())) {
            stmt.setString(1, name);
            micronutrient = row2object(stmt.executeQuery()); //ako nema vraca se null
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return micronutrient;
    }
}
