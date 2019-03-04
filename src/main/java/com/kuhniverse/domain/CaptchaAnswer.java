package com.kuhniverse.domain;

import java.io.Serializable;

/**
 * Based on the work of https://github.com/bdotzour/visualCaptcha-java
 */
public class CaptchaAnswer implements Serializable {
    private String name;
    private String path;
    private String obfuscatedName;

    public CaptchaAnswer() {
    }

    public CaptchaAnswer(String name, String obfuscatedName,String path ) {
        this.name = name;
        this.path = path;
        this.obfuscatedName = obfuscatedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public void setObfuscatedName(String obfuscatedName) {
        this.obfuscatedName = obfuscatedName;
    }


    @Override
    public String toString() {
        return "CaptchaAnswer{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", obfuscatedName='" + obfuscatedName + '\'' +
                '}';
    }

}
