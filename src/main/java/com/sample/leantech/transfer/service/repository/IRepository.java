package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Project;

import java.util.List;

public interface IRepository  {

    List<Project> getProjects();

    void save(List<Project> project);

}
