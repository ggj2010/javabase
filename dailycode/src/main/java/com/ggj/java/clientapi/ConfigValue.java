
package com.ggj.java.clientapi;


public class ConfigValue {

    private String value;
    private long timestamp;
    private boolean isLastWorkingConfig = false;

    public ConfigValue(String value) {
        this(value, false);
    }

    public ConfigValue(String value, boolean isLastWorkingConfig) {
        this.value = value;
        this.isLastWorkingConfig = isLastWorkingConfig;
        this.timestamp = System.currentTimeMillis();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public boolean isLastWorkingConfig() {
        return isLastWorkingConfig;
    }

    public void setLastWorkingConfig(boolean isLastWorkingConfig) {
        this.isLastWorkingConfig = isLastWorkingConfig;
    }

}
