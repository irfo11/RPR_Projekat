package ba.rpr.business;

import ba.rpr.dao.Dao;

/**
 * Marker interface for manager classes, helps to shorten code
 * @param <T> Domain class
 */
public interface Manager<T> extends Dao<T> {
}
