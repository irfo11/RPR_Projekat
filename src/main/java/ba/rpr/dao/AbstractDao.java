package ba.rpr.dao;

import java.sql.*;
import java.util.*;

import ba.rpr.dao.exceptions.DaoException;

public abstract class AbstractDao<T> implements Dao<T>{

    private static Connection conn;
    private String tableName;
    static {
        Properties dbProperties = new Properties();
        try {
            dbProperties.load(AbstractDao.class.getResourceAsStream("/db.properties"));
            conn = DriverManager.getConnection(dbProperties.getProperty("url"),
                    dbProperties.getProperty("username"),
                    dbProperties.getProperty("password"));
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public AbstractDao(String tableName) {
        this.tableName = tableName;
    }

    public Connection getConnection() {
        return this.conn;
    }

    public String getTableName() {
        return tableName;
    }

    public abstract T row2object(ResultSet rs) throws DaoException;
    public abstract Map<String, Object> object2row(T object);


    @Override
    public T getById(int id) throws DaoException{
        try(Statement stmt = getConnection().createStatement()) {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(tableName).append(" WHERE id=").append(id);
            ResultSet result = stmt.executeQuery(query.toString());
            return row2object(result);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void add(T item) throws DaoException{
        Map<String, Object> row = object2row(item);
        Map.Entry<String, String> columns = prepareInsertParts(row);
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(tableName).append(columns.getKey()).append("VALUES").append(columns.getValue());
        try(PreparedStatement stmt = getConnection().prepareStatement(query.toString())) {
            int i=1;
            for(Map.Entry<String, Object> entry: row.entrySet()) {
                if(entry.getKey().equals("id")) continue;
                stmt.setObject(i++, entry.getValue());
            }
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws DaoException{
        try(Statement stmt = getConnection().createStatement()){
            StringBuilder query = new StringBuilder();
            query.append("DELETE FROM ").append(tableName).append(" WHERE id=").append(id);
            stmt.executeUpdate(query.toString());
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void update(int id, T item) throws DaoException{
        Map<String, Object> row = object2row(item);
        String columns = prepareUpdateParts(row);
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(tableName).append(" SET").append(columns).append("WHERE id=").append(id);
        try(PreparedStatement stmt = getConnection().prepareStatement(query.toString())){
            int i=1;
            for(Map.Entry<String, Object> entry: row.entrySet()) {
                if(entry.getKey().equals("id")) continue;
                stmt.setObject(i++, entry.getValue());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<T> getAll() throws DaoException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getTableName());
        try(Statement stmt = getConnection().createStatement()) {
            List<T> list = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(query.toString());
            for(;;) {
                T object = row2object(rs);
                if(object == null) break;
                list.add(object);
            }
            return list;
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
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

}



















