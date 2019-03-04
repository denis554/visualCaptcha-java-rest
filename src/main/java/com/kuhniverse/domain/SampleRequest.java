package com.kuhniverse.domain;

/**
 * Created by tillkuhn on 27.05.2015.
 */
public class SampleRequest {

    private String name;
    private String summary;
    private CaptchaData captchaData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public CaptchaData getCaptchaData() {
        return captchaData;
    }

    public void setCaptchaData(CaptchaData captchaData) {
        this.captchaData = captchaData;
    }

    @Override
    public String toString() {
        return "SampleResponse{" +
                "name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", captchaData=" + captchaData +
                '}';
    }
}
