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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fields {

        private IssueType issuetype;
        private String summary;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class IssueType {

            private String name;

        }
    }

}