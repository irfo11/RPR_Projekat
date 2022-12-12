package ba.rpr.dao;

import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;

import java.io.IOException;
import java.sql.*;

import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

public class PresenceDaoSQLImpl implements PresenceDao {

    private Connection conn;

    public PresenceDaoSQLImpl() {
        Properties databaseProperties = new Properties();
        try {
            databaseProperties.load(PresenceDaoSQLImpl.class.getResourceAsStream("/ba/rpr/dao/db.properties"));
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
    public Presence getById(int id) {
        Presence presence = null;
        try(Statement stmt = conn.createStatement()) {
            ResultSet result = stmt.executeQuery("SELECT * FROM presence WHERE id=" + id);
            if(result.next()) {
                //mislim da je ovo sporo, i ruzno, pogledat kako napraviti sa getObject
                MicronutrientDao micronutrientDao = new MicronutrientDaoSQLImpl();
                SourceDao sourceDao = new SourceDaoSQLImpl();
                presence = new Presence(result.getInt("id"),
                                        micronutrientDao.getById(result.getInt("micronutrient_id")),
                                        sourceDao.getById(result.getInt("source_id")),
                                        result.getDouble("amount"));
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        if(presence == null) throw new ElementNotFoundException("Id does not exist");
        return presence;
    }

    /**
     * Adds entity to database
     *
     * @param item - entity to be added to database
     */
    @Override
    public void add(Presence item) {
        try(Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO presence (micronutrient_id, source_id, amount) VALUES (" +
                    item.getMicronutrient().getId() + ", " + item.getSource().getId() + ", " + item.getAmount() + ")");
        } catch(SQLException e) {
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
            stmt.executeUpdate("DELETE FROM presence WHERE id=" + id);
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
    public void update(int id, Presence item) {
        try(Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE presence SET (micronutrient_id=" + item.getMicronutrient().getId() +
                ", source_id=" + item.getSource().getId() + ", amount=" + item.getAmount() + ") " +
                    "WHERE id=" + id);
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
    }

    /**
     * Returns a sorted set of Presence objects that have the given source as a field.
     * The first element has the highest presence inside the source.
     *
     * @param sourceName - name of the source whose micronutrients are returned
     * @return sorted set of Presence objects that have the given source as a field
     * @throws ElementNotFoundException - if source with given sourceName can't be found in database
     */
    @Override
    public SortedSet<Presence> micronutrientsInSource(String sourceName) {
        SortedSet<Presence> presences =
                new TreeSet<>((Presence p1, Presence p2) -> Double.compare(p2.getAmount(), p1.getAmount()));
                //changed orientation so it is sorted from highest to lowest
        try(Statement stmt = conn.createStatement()) {
            SourceDao sourceDao = new SourceDaoSQLImpl();
            MicronutrientDao micronutrientDao = new MicronutrientDaoSQLImpl();
            //it would be faster if there was a method getIdFromName
            ResultSet results = stmt.executeQuery("SELECT * FROM presence WHERE source_id=" +
                    sourceDao.searchByName(sourceName).getId());
            /*this source gets created over and over in while loop, so if we can find some way to
            create once up there and there and use it in while loop. Or we could use Pair class to
            just store <Micronutrient, double>
             */
            while(results.next()) {
                presences.add(new Presence(results.getInt("id"),
                                           micronutrientDao.getById(results.getInt("micronutrient_id")),
                                           sourceDao.getById(results.getInt("source_id")),
                                           results.getDouble("amount")));
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return presences;
    }

    /**
     * Returns a sorted set of Presence objects that have the given micronutrient as a field
     * The first element has the highest presence inside the micronutrient.
     *
     * @param micronutrientName - name of the micronutrient whose sources are returned
     * @return sorted set of Presence objects that have the given micronutrient as a field
     * @throws ElementNotFoundException - if micronutrient with given micronutrientName can't be found in database
     */
    @Override
    public SortedSet<Presence> sourcesOfMicronutrient(String micronutrientName) {
        SortedSet<Presence> presences =
                new TreeSet<>((Presence p1, Presence p2) -> Double.compare(p2.getAmount(), p1.getAmount()));
        try(Statement stmt = conn.createStatement()) {
            MicronutrientDao micronutrientDao = new MicronutrientDaoSQLImpl();
            SourceDao sourceDao = new SourceDaoSQLImpl();
            ResultSet results = stmt.executeQuery("SELECT * FROM presence WHERE micronutrient_id=" +
                    micronutrientDao.searchByName(micronutrientName).getId());
            while(results.next()) {
                presences.add(new Presence(results.getInt("id"),
                                           micronutrientDao.getById(results.getInt("micronutrient_id")),
                                           sourceDao.getById(results.getInt("source_id")),
                                           results.getDouble("amount")));
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        return presences;
    }

    /**
     * Returns Presence object from database based on given micronutrient name and source name,
     * null if element does not exist.
     *
     * @param micronutrientName - name of the micronutrient in presence element
     * @param sourceName        - name of the source to be found in presence element
     * @return presence object with the given micronutrient name and source name, null if element does not exist
     * @throws ElementNotFoundException - if element with given micronutrientName and sourceName can't be found in database
     */
    @Override
    public Presence searchByMicronutrientAndSource(String micronutrientName, String sourceName) {
        Presence presence = null;
        try(Statement stmt = conn.createStatement()) {
            MicronutrientDao micronutrientDao = new MicronutrientDaoSQLImpl();
            SourceDao sourceDao = new SourceDaoSQLImpl();
            //again it would be faster if dao had getIdFromName, so we don't create unnecessary objects
            ResultSet result = stmt.executeQuery("SELECT * FROM presence WHERE micronutrient_id=" +
                    micronutrientDao.searchByName(micronutrientName).getId() + " AND source_id=" +
                    sourceDao.searchByName(sourceName).getId());
            if(result.next()) {
                presence = new Presence(result.getInt("id"),
                                        micronutrientDao.getById(result.getInt("micronutrient_id")),
                                        sourceDao.getById(result.getInt("source_id")),
                                        result.getDouble("amount"));
            }
        } catch(SQLException e) {
            System.out.println("Problem with database");
            e.printStackTrace();
        }
        if(presence == null) throw new ElementNotFoundException("micronutrient name and source name can't be found");
        return presence;
    }
}
