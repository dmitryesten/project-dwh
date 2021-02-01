package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import java.util.Collection;

public interface IRepository {

    Collection<? extends EntityDB> get();
    void save(Collection<? extends EntityDB> entities);

}
