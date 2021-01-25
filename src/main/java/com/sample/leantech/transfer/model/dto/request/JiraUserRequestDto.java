package com.sample.leantech.transfer.model.dto.request;

import java.util.Objects;

public class JiraUserRequestDto {

    private String self;
    private String key;
    private String name;
    private String emailAddress;
    private String displayName;
    private boolean active;
    private boolean deleted;
    private String timeZone;
    private String locale;

    public JiraUserRequestDto(String self, String key, String name, String emailAddress, String displayName, boolean active, boolean deleted, String timeZone, String locale) {
        this.self = self;
        this.key = key;
        this.name = name;
        this.emailAddress = emailAddress;
        this.displayName = displayName;
        this.active = active;
        this.deleted = deleted;
        this.timeZone = timeZone;
        this.locale = locale;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraUserRequestDto that = (JiraUserRequestDto) o;
        return active == that.active &&
                deleted == that.deleted &&
                Objects.equals(self, that.self) &&
                Objects.equals(key, that.key) &&
                Objects.equals(name, that.name) &&
                Objects.equals(emailAddress, that.emailAddress) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(timeZone, that.timeZone) &&
                Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, key, name, emailAddress, displayName, active, deleted, timeZone, locale);
    }

    @Override
    public String toString() {
        return "JiraUserRequestDto{" +
                "self='" + self + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", displayName='" + displayName + '\'' +
                ", active=" + active +
                ", deleted=" + deleted +
                ", timeZone='" + timeZone + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
