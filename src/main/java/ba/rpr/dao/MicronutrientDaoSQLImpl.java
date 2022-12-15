package ba.rpr.dao;

import ba.rpr.domain.Micronutrient;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

public class MicronutrientDaoSQLImpl implements MicronutrientDao{

    private Connection conn;

    public MicronutrientDaoSQLImpl() {
        Properties databaseProperties = new Properties();
        try{
            databaseProperties.load(MicronutrientDaoSQLImpl.class.getResourceAsStream("/ba/rpr/dao/db.properties"));
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
     * Returns entity from database based on given id
     *
     * @param id - the id of the entity
     * @return entity that has the same id
     * @throws ElementNotFoundException - if element with given id can't be found in database
     */
    @Override
    public Micronutrient getById(int id) {
        Micronutrient micronutrient = null;
        try(Statement stmt = conn.createStatement()) {
            ResultSet result = stmt.executeQuery("SELECT * FROM micronutrients WHERE id=" + id);
            if(result.next()){
                micronutrient = new Micronutrient(result.getInt("id"),
                                                  result.getString("name"),
                                                  result.getString("role"),
                                                  result.getBoolean("is_vitamin"));
            } else {
                throw new ElementNotFoundException("Micronutrient with id=" + id + " does not exist");
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return micronutrient;
    }

    /**
     * Adds entity to database
     *
     * @param item - entity to be added to database
     * @throws ElementAlreadyExistsException - if element already exist, based on micronutrient name
     */
    @Override
    public void add(Micronutrient item) {
        try(PreparedStatement stmt =
                    conn.prepareStatement("INSERT INTO sources (name, role, is_vitamin) VALUES (?, ?, ?")) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getRole());
            stmt.setBoolean(3, item.isVitamin());
            stmt.executeUpdate();
        } catch(SQLException e) {
            //sql is not case-sensitive, when searching if there is column with same name
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
            if(stmt.executeUpdate("DELETE FROM micronutrient WHERE id=" + id) == 0) {
                //this means that 0 rows were affected
                throw new ElementNotFoundException("Micronutrient with id=" + id + " does not exist");
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
     * @throws ElementAlreadyExistsException - if micronutrient that we are updating changes its name
     * to a value that already exists
     */
    @Override
    public void update(int id, Micronutrient item) {
        try(PreparedStatement stmt =
                    conn.prepareStatement("UPDATE micronutrient SET name=?, role=?, is_vitamin=? WHERE id=" + id)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getRole());
            stmt.setBoolean(3, item.isVitamin());
            if(stmt.executeUpdate() == 0){
                throw new ElementNotFoundException("Micronutrient with id=" + id + " does not exist");
            }
        } catch(SQLException e) {
            if(e.getErrorCode() == 1062) throw new ElementAlreadyExistsException("Cannot update because, " + item.getName() +
                    " already exists");
            System.out.println("Problem with database");
            e.printStackTrace();
        }
    }

    /**
     * Returns micronutrient with the same name given as parameter
     *
     * @param name - name of the micronutrient to be returned
     * @return micronutrient with the same name given as parameter
     * @throws ElementNotFoundException - if element with given name can't be found in database
     */
    @Override
    public Micronutrient searchByName(String name) {
        Micronutrient micronutrient = null;
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM micronutrients WHERE name=?")) {
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                micronutrient = new Micronutrient(result.getInt("id"),
                                                  result.getString("name"),
                                                  result.getString("role"),
                                                  result.getBoolean("is_vitamin"));
            } else {
                throw new ElementNotFoundException(name + " does not exist");
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return micronutrient;
    }

    /**
     * Returns all micronutrients in the database sorted by name
     *
     * @return all micronutrients sorted by name
     */
    @Override
    public SortedSet<Micronutrient> getAll() {
        SortedSet<Micronutrient> micronutrients =
                new TreeSet<>((Micronutrient m1, Micronutrient m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
        try(Statement stmt = conn.createStatement()) {
            ResultSet results = stmt.executeQuery("SELECT * FROM micronutrients");
            while(results.next()) {
                micronutrients.add(new Micronutrient(results.getInt("id"),
                                                     results.getString("name"),
                                                     results.getString("role"),
                                                     results.getBoolean("is_vitamin")));
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return micronutrients;
    }
}
