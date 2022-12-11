package ba.rpr.dao;

import ba.rpr.domain.Source;
import com.sun.source.tree.Tree;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedReader;
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
     */
    @Override
    public void add(Source item) {
        try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO sources name VALUES ?")){
            //prepared statement is used because name can be set into a sql query
            stmt.setString(1, item.getName());
            stmt.executeUpdate();
        } catch(SQLException e){
            System.out.println("Problem with database");
            e.printStackTrace();
        }
    }

    /**
     * Deletes entity from database based on id
     *
     * @param id - id of entity to be deleted
     */
    @Override
    public void delete(int id) {
        try(Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM sources WHERE id=" + id);
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
     */
    @Override
    public void update(int id, Source item) {
        try(PreparedStatement stmt =
                    conn.prepareStatement("UPDATE sources SET name=? WHERE id=" + id)){
            stmt.setString(1, item.getName());
            stmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
    }

    /**
     * Return source object with the same name given as parameter, null if there is no element with the same name
     *
     * @param name - name of the source to be returned
     * @return source object with the same name given as parameter, null if there is no element with same name
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
