package com.sample.leantech.transfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssueDto {

    private String id;
    private String key;
    private Fields fields;

    /**private String id;
     private String expand;
     private String self;
     private String key;
     private String created;
     private String summary;
     private String description;
     private int timespent;
     **/

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fields {

        private IssueType issuetype;
        private Project project;
        private Parent epic;
        private Parent parent;
        private String summary;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class IssueType {

            private String name;

        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Project {

            private String id;

        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Parent {

            private String id;

        }
    }
