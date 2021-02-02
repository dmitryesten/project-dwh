package com.sample.leantech.transfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssueDto implements Serializable {

    private String id;
    private String key;
    private Fields fields;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fields implements Serializable {

        private IssueType issuetype;
        private Project project;
        private Parent epic;
        private Parent parent;
        private String summary;
        private JiraWorklogResponseDto worklog;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class IssueType implements Serializable {

            private String name;

        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Project implements Serializable {

            private String id;

        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Parent implements Serializable {

            private String id;

        }
    }
}