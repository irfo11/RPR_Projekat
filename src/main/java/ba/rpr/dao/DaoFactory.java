package ba.rpr.dao;

/* vjerovatno ce trebati promijeniti pitaj*/

public class DaoFactory {
    private static MicronutrientDao micronutrientDao = new MicronutrientDaoSQLImpl();
    private static SourceDao sourceDao = new SourceDaoSQLImpl();
    private static PresenceDao presenceDao = new PresenceDaoSQLImpl();
    private DaoFactory() {}
    public static MicronutrientDao micronutrientDao() {
        return micronutrientDao;
    }
    public static SourceDao sourceDao() {
        return sourceDao;
    }
    public static PresenceDao presenceDao() {
        return presenceDao;
    }
}
