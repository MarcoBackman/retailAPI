package org.example.simpleapi.dao;

import org.apache.logging.log4j.Marker;

/**
 * @param <T> Domain type to map from Entity
 */
public interface RecordDAO<T> {
    T getRecordById(Marker mk, int id);
    long addRecord(Marker mk, T customer);
    int updateRecord(Marker mk, T customer);
}
