package org.solq.fm.common.db.model;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface IDbOperation {

    public <T extends IEntity> T find(Class<T> type, Serializable key);

    public void delete(Class<? extends IEntity> type, Serializable... keys);

    public void delete(IEntity... objects);

    public void saveOrUpdate(IEntity... objects);

    public <T extends IEntity> List<T> query(Class<T> type, HQuery hQuery);

    public <T extends IEntity> List<T> query(Class<T> type, HCriteria hCriteria);

    public <T extends IEntity> void forEach(Class<T> type, HQuery hQuery, Consumer<T> action);

    public <T extends IEntity> void forEach(Class<T> type, HCriteria hCriteria, Consumer<T> action);

    public int count(Class<? extends IEntity> type, HCriteria hCriteria);
}
