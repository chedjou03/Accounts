package com.account.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Properties {

    private String msg;
    private String buildVersion;
    private Map<String, String> mailDetails;
    private List<String> activeBranches;

    public Properties(String msg, String buildVersion, List<String> activeBranches, Map<String, String> mailDetails) {
        this.msg = msg;
        this.buildVersion = buildVersion;
        this.activeBranches = activeBranches;
        this.mailDetails = mailDetails;
    }
}