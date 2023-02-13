package ba.rpr.dao;

import ba.rpr.dao.exceptions.DaoException;

import java.sql.*;
import java.util.*;

/**
 * Abstract DAO class that implements CRUD method for every domain class
 * @param <T> Domain class
 */
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

    /**
     * Returns query parts as an Entry object. The key is the column names (e.g. '(col1, col2)'),
     * the value is the question marks to prepare the statement (e.g '(?, ?)')
     * @param row - row from which to prepare insert parts
     * @return query parts
     */
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

    /**
     * Returns query part as String. (e.g. '(col1=?, col2=?)' )
     * @param row - row from which to prepare update part
     * @return query part
     */
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

    /**
     * Executes any query given as parameter and returns a list of domain class objects
     * @param query - query to be executed
     * @param params - parameters of the query
     * @return list of domain class objects
     * @throws DaoException
     */
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

    /**
     * Executes a query that is sure to return a unique domain class object
     * @param query - query to be executed
     * @param params - parameters of the query
     * @return domain class object
     * @throws DaoException
     */
    public T executeQueryUnique(String query, Object[] params) throws DaoException {
        List<T> element = executeQuery(query, params);
        if(element != null && element.size()==1) {
            return element.get(0);
        } else return null;
    }

    /**
     * Executes update given as parameter
     * @param query - update to be executes
     * @param row - row that contains new values for element in database
     * @throws DaoException
     */
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



















