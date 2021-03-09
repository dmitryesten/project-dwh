package com.sample.leantech.transfer.model.dto.request;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.List;

@Data
public class JiraIssueDto {

    private String id;
    private String key;
    private Fields fields;

    @Data
    public static class Fields {

        private IssueType issuetype;
        private Parent project;
        private Parent epic;
        private Parent parent;
        private String summary;
        private JiraWorklogResponseDto worklog;

        @JsonProperty("customfield_10300")
        private Customfield customfield;

        @JsonProperty("components")
        private List<Component> components;
        @JsonIgnore
        private Component component;
        @Data
        public static class IssueType {

            private String name;

        }

        @Data
        public static class Parent {

            private String id;

        }

        @Data
        public static class Customfield {
            private String id;
            private String value;
        }

        @Data
        public static class Component {
            private String id;
            @JsonProperty("name")
            private String value;
            private String issueId;
        }

    }
}