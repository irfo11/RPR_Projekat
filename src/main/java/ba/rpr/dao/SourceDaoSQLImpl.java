package ba.rpr.dao;

import ba.rpr.dao.exceptions.ElementAlreadyExistsException;
import ba.rpr.dao.exceptions.ElementNotFoundException;
import ba.rpr.domain.Source;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * SQL implementation for SourceDao
 */
public class SourceDaoSQLImpl implements SourceDao{

    private Connection conn;

    /**
     * Creates connection to database with information provided in properties file
     */
    public SourceDaoSQLImpl() {
        Properties databaseProperties = new Properties();
        try {
            //getResourceAsStream returns null if file does not exist
            databaseProperties.load(SourceDaoSQLImpl.class.getResourceAsStream("/ba/rpr/dao/db.properties"));
            conn = DriverManager.getConnection(databaseProperties.getProperty("url"),
                                               databaseProperties.getProperty("username"),
                                               databaseProperties.getProperty("password"));
        } catch(NullPointerException e) {
            System.out.println("File does not exist");
            e.printStackTrace();
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        } catch(IOException e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }
    }

    /**
     * Returns entity from database based on given id, null if there is no element with the same id
     *
     * @param id - the id of the entity
     * @return entity that has the same id, null if there is no element with the same id
     * @throws ElementNotFoundException - if element with given id can't be found in database
     */
    @Override
    public Source getById(int id) {
        Source source = null;
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sources WHERE id=?")) {
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                source = new Source(result.getInt("id"),
                                    result.getString("name"));
            } else {
                throw new ElementNotFoundException("Id does not exist");
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return source;
    }

    /**
     * Adds entity to database
     *
     * @param item - entity to be added to database
     * @throws ElementAlreadyExistsException - if source with the same name already exists
     */
    @Override
    public void add(Source item) {
        try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO sources name VALUES ?")){
            //prepared statement is used because name can be set into a sql query
            stmt.setString(1, item.getName());
            stmt.executeUpdate();
        } catch(SQLException e){
            if(e.getErrorCode() == 1062) throw new ElementAlreadyExistsException(item.getName() + " already exists");
            System.out.println("Problem with database");
            e.printStackTrace();
        }
    }

    /**
     * Deletes entity from database based on id
     *
     * @param id - id of entity to be deleted
     * @throws ElementNotFoundException - if element with given id can't be found in database
     */
    @Override
    public void delete(int id) {
        try(Statement stmt = conn.createStatement()) {
            if(stmt.executeUpdate("DELETE FROM sources WHERE id=" + id) == 0) {
                throw new ElementNotFoundException("Source with id=" + id + " does not exist");
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
    }

    /**
     * Updates the entity with the same id
     *
     * @param id   - id of the entity to be updated
     * @param item - object that contains updates for the entity
     * @throws ElementNotFoundException - if element with given id can't be found in database
     * @throws ElementAlreadyExistsException - if source that we are trying to update changes its name
     * to a value that already exists
     */
    @Override
    public void update(int id, Source item) {
        try(PreparedStatement stmt =
                    conn.prepareStatement("UPDATE sources SET name=? WHERE id=" + id)){
            stmt.setString(1, item.getName());
            if(stmt.executeUpdate() == 0) {
                throw new ElementNotFoundException("Source with id=" + id + " does not exist");
            }
        } catch(SQLException e) {
            if(e.getErrorCode() == 1062) throw new ElementAlreadyExistsException("Cannot update because, " +
                    item.getName() + " already exists");
            System.out.println("Problem with database");
            e.printStackTrace();
        }
    }

    /**
     * Return source object with the same name given as parameter, null if there is no element with the same name
     *
     * @param name - name of the source to be returned
     * @return source object with the same name given as parameter, null if there is no element with same name
     * @throws ElementNotFoundException - if element with given name can't be found in database
     */
    @Override
    public Source searchByName(String name) {
        Source source = null;
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sources WHERE name=?")) {
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                source = new Source(result.getInt("id"),
                                    result.getString("name"));
            } else {
                throw new ElementNotFoundException(name + " does not exist");
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return source;
    }

    /**
     * Returns all the sources in database sorted by name
     *
     * @return all sources sorted by name
     */
    @Override
    public SortedSet<Source> getAll() {
        SortedSet<Source> sources =
                new TreeSet<>((Source s1, Source s2) -> s1.getName().compareToIgnoreCase(s2.getName())); // custom comparator
        try(Statement stmt = conn.createStatement()){
            ResultSet results = stmt.executeQuery("SELECT * FROM sources");
            while(results.next()){
                sources.add(new Source(results.getInt("id"),
                                       results.getString("name")));
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return sources;
    }
}
