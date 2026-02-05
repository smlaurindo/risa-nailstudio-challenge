package com.smlaurindo.risanailstudio.service.application.domain;

public class Service {
    private String id;
    private String name;
    private String durationMinutes;
    private Integer priceCents;
    private String icon;

    public Service(String id, String name, String durationMinutes, Integer priceCents, String icon) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.priceCents = priceCents;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getPriceCents() {
        return priceCents;
    }

    public String getIcon() {
        return icon;
    }
}
