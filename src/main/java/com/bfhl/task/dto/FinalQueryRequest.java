package com.bfhl.task.dto;

public class FinalQueryRequest {

    private String finalQuery;

    public FinalQueryRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }
}
