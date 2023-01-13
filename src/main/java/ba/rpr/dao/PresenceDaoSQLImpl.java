package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;

import java.sql.*;

import java.util.*;

public class PresenceDaoSQLImpl extends AbstractDao<Presence> implements PresenceDao {
    private static PresenceDaoSQLImpl instance = null;
    private PresenceDaoSQLImpl() {
        super("presence");
    }
    public static PresenceDaoSQLImpl getInstance() {
        if(instance == null)
            instance = new PresenceDaoSQLImpl();
        return instance;
    }

    @Override
    public Presence row2object(ResultSet rs) throws DaoException {
        try {
            return new Presence(rs.getInt("id"),
                        DaoFactory.micronutrientDao().getById(rs.getInt("micronutrient")),
                        DaoFactory.sourceDao().getById(rs.getInt("source")),
                        rs.getDouble("amount"));
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> object2row(Presence object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("id", object.getId());
        row.put("micronutrient", object.getMicronutrient().getId());
        row.put("source", object.getSource().getId());
        row.put("amount", object.getAmount());
        return row;
    }

    @Override
    public List<Presence> micronutrientsInSource(Source source) throws DaoException {
        List<Presence> presences = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName()).append(" WHERE source=").
                append(source.getId());
        try(Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query.toString());
            while(rs.next()) presences.add(row2object(rs));
            return presences;
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Presence> sourcesOfMicronutrient(Micronutrient micronutrient) throws DaoException {
        List<Presence> presences = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName()).append(" WHERE micronutrient=").
                append(micronutrient.getId());
        try(Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query.toString());
            while(rs.next()) presences.add(row2object(rs));
            return presences;
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
