package org.solq.fm.common.db.model;

import java.util.List;

public interface IdbDrive {

    public IEntity find(Class<? extends IEntity> type, Object key);

    public void delete(Class<? extends IEntity> type, Object... keys);

    public void createOrUpdate(Class<? extends IEntity> type, Object... objects);

    public <T extends IEntity> List<T> query(Class<T> type, Query query);
}
