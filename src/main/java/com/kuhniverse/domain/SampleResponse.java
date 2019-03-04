package com.kuhniverse.domain;

/**
 * Created by tillkuhn on 27.05.2015.
 */
public class SampleResponse {

    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "SampleResponse{" +
                "requestId='" + requestId + '\'' +
                '}';
    }
}
