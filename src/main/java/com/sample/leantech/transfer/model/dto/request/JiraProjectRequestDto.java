package com.sample.leantech.transfer.model.dto.request;

import java.util.Objects;

public class JiraProjectRequestDto {

    private String expand;
    private String self;
    private String id;
    private String key;
    private String name;
    private String projectTypeKey;
    private boolean archived;

    public JiraProjectRequestDto(String expand, String self, String id, String key, String name, String projectTypeKey, boolean archived) {
        this.expand = expand;
        this.self = self;
        this.id = id;
        this.key = key;
        this.name = name;
        this.projectTypeKey = projectTypeKey;
        this.archived = archived;
    }

    public JiraProjectRequestDto() {
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraProjectRequestDto that = (JiraProjectRequestDto) o;
        return archived == that.archived &&
                Objects.equals(expand, that.expand) &&
                Objects.equals(self, that.self) &&
                Objects.equals(id, that.id) &&
                Objects.equals(key, that.key) &&
                Objects.equals(name, that.name) &&
                Objects.equals(projectTypeKey, that.projectTypeKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expand, self, id, key, name, projectTypeKey, archived);
    }
}
