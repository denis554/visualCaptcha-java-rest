package com.kuhniverse.domain;

/**
 * Created by tillkuhn on 27.05.2015.
 */
public class CaptchaData {

    private boolean valid;
    private String name;
    private String value;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CaptchaData{" +
                "valid=" + valid +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
