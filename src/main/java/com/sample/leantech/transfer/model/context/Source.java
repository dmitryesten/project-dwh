package com.sample.leantech.transfer.model.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Source {
    JIRA(1);

    @Getter
    private final Integer value;
}