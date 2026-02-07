package com.smlaurindo.risanailstudio.application.domain;

import static java.util.UUID.randomUUID;

public class Service {
    private String id;
    private String name;
    private int durationMinutes;
    private int priceCents;
    private String icon;

    public Service(String id, String name, Integer durationMinutes, Integer priceCents, String icon) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.priceCents = priceCents;
        this.icon = icon;
    }

    public Service(String name, int durationMinutes, int priceCents, String icon) {
        this.id = randomUUID().toString();
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

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public String getIcon() {
        return icon;
    }
}
