package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Presence;

import java.sql.*;

import java.util.*;

public class PresenceDaoSQLImpl extends AbstractDao<Presence> implements PresenceDao {

    public PresenceDaoSQLImpl() {
        super("presence");
    }

    @Override
    public Presence row2object(ResultSet rs) throws DaoException { //return null if not found
        Presence presence = null;
        try {
            if(rs.next()) {
                presence = new Presence(rs.getInt("id"),
                        DaoFactory.micronutrientDao().getById(rs.getInt("micronutrient")),
                        DaoFactory.sourceDao().getById(rs.getInt("source")),
                        rs.getDouble("amount"));
            }
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return presence;
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
    public List<Presence> micronutrientsInSource(String sourceName) throws DaoException { //ordered in descending order
        List<Presence> presences = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName()).append(" WHERE source=").
                append(DaoFactory.sourceDao().searchByName(sourceName).getId()).
                append(" ORDER BY amount DESC");
        Presence presence = null;
        try(Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query.toString());
            for(;;) {
                presence = row2object(rs);
                if(presence == null) break;
                presences.add(presence);
            }
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return presences;
    }

    @Override
    public List<Presence> sourcesOfMicronutrient(String micronutrientName) throws DaoException {//refactorisat kao i onaj gore
        List<Presence> presences = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName()).append(" WHERE micronutrient=").
                append(DaoFactory.micronutrientDao().searchByName(micronutrientName).getId()).
                append(" ORDER BY amount DESC");
        Presence presence = null;
        try(Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query.toString());
            for(;;) {
                presence = row2object(rs);
                if(presence == null) break;
                presences.add(presence);
            }
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return presences;
    }

    @Override
    public Presence searchByMicronutrientAndSource(String micronutrientName, String sourceName) throws DaoException {
        Presence presence = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName()).append(" WHERE micronutrient=").
                append(DaoFactory.micronutrientDao().searchByName(micronutrientName).getId()).
                append(" AND source=").append(DaoFactory.micronutrientDao().searchByName(micronutrientName).getId());
        try(Statement stmt = getConnection().createStatement()) {
             ResultSet rs = stmt.executeQuery(query.toString());
             if(rs.next()) {
                 presence = row2object(rs);
             } else {
                 throw new DaoException("Element does not exist");
             }
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return presence;
    }
}
