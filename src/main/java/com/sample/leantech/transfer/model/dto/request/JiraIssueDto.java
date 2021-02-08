package com.sample.leantech.transfer.model.dto.request;

import lombok.Data;

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

        @Data
        public static class IssueType {

            private String name;

        }

        @Data
        public static class Parent {

            private String id;

        }
    }
}