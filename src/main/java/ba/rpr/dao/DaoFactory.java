package ba.rpr.dao;

public class DaoFactory {
    private static final MicronutrientDao micronutrientDao = MicronutrientDaoSQLImpl.getInstance();
    private static final SourceDao sourceDao = SourceDaoSQLImpl.getInstance();
    private static final PresenceDao presenceDao = PresenceDaoSQLImpl.getInstance();
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
