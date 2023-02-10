package ba.rpr.dao;

import java.sql.*;
import java.util.*;

import ba.rpr.dao.exceptions.DaoException;

public abstract class AbstractDao<T> implements Dao<T>{

    private static Connection conn = null;
    private String tableName;
    public AbstractDao(String tableName) {
        this.tableName = tableName;
    }

    public Connection getConnection() throws DaoException{
        if(conn == null) {
            Properties dbProperties = new Properties();
            try {
                dbProperties.load(AbstractDao.class.getResourceAsStream("/db.properties"));
                conn = DriverManager.getConnection(dbProperties.getProperty("url"),
                        dbProperties.getProperty("username"),
                        dbProperties.getProperty("password"));
            } catch(Exception e) {
                throw new DaoException(e.getMessage());
            }
        }
        return this.conn;
    }

    public String getTableName() {
        return tableName;
    }

    public abstract T row2object(ResultSet rs) throws DaoException;
    public abstract Map<String, Object> object2row(T object);


    @Override
    public T getById(int id) throws DaoException{
        return executeQueryUnique("SELECT * FROM "+tableName+" WHERE id=?", new Object[]{id});
    }

    @Override
    public void add(T item) throws DaoException{
        Map<String, Object> row = object2row(item);
        Map.Entry<String, String> columns = prepareInsertParts(row);
        executeUpdate("INSERT INTO "+tableName+columns.getKey()+"VALUES"+columns.getValue(), row);
    }

    @Override
    public void delete(int id) throws DaoException{
        executeUpdate("DELETE FROM "+tableName+" WHERE id="+id, null);
    }

    @Override
    public void update(int id, T item) throws DaoException{
        Map<String, Object> row = object2row(item);
        String columns = prepareUpdateParts(row);
        executeUpdate("UPDATE "+getTableName()+" SET"+columns+"WHERE id="+id, row);
    }

    @Override
    public List<T> getAll() throws DaoException {
        return executeQuery("SELECT * FROM "+getTableName(), null);
    }

    private Map.Entry<String, String> prepareInsertParts(Map<String, Object> row) {
        StringBuilder columnNames = new StringBuilder();
        StringBuilder questionMarks = new StringBuilder();
        columnNames.append(" (");
        questionMarks.append(" (");
        for(Map.Entry<String, Object> entry: row.entrySet()) {
            if(entry.getKey().equals("id")) continue;
            columnNames.append(entry.getKey()).append(",");
            questionMarks.append("?,");
        }
        columnNames.deleteCharAt(columnNames.length()-1);
        questionMarks.deleteCharAt(questionMarks.length()-1);
        columnNames.append(") ");
        questionMarks.append(") ");
        return new AbstractMap.SimpleEntry<String, String>(columnNames.toString(), questionMarks.toString());
    }

    private String prepareUpdateParts(Map<String, Object> row) {
        StringBuilder updatePart = new StringBuilder();
        updatePart.append(" ");
        for(Map.Entry<String, Object> entry: row.entrySet()) {
            if(entry.getKey().equals("id")) continue;
            updatePart.append(entry.getKey()).append("=?,");
        }
        updatePart.deleteCharAt(updatePart.length()-1);
        updatePart.append(" ");
        return updatePart.toString();
    }

    public List<T> executeQuery(String query, Object[] params) throws DaoException{
        try(PreparedStatement stmt = getConnection().prepareStatement(query)) {
            if(params != null) {
                for (int i = 1; i <= params.length; i++) {
                    stmt.setObject(i, params[i - 1]);
                }
            }
            List<T> results = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                results.add(row2object(rs));
            }
            return results;
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public T executeQueryUnique(String query, Object[] params) throws DaoException {
        List<T> element = executeQuery(query, params);
        if(element != null && element.size()==1) {
            return element.get(0);
        } else return null;
    }

    public void executeUpdate(String query, Map<String, Object> row) throws DaoException{
        try(PreparedStatement stmt = getConnection().prepareStatement(query)) {
            if(row != null) {
                int i = 1;
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    if (entry.getKey().equals("id")) continue;
                    stmt.setObject(i++, entry.getValue());
                }
            }
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

}



















