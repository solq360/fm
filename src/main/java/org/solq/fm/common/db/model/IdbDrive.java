package org.solq.fm.common.db.model;

import java.io.Serializable;
import java.util.List;

public interface IdbDrive {

    public <T extends IEntity> T find(Class<T> type, Serializable key);

    public void delete(Class<? extends IEntity> type,Serializable... keys);
    
    public void delete(IEntity... objects);
    public void saveOrUpdate(IEntity... objects);

    public <T extends IEntity> List<T> query(Class<T> type, HQuery query);
}
