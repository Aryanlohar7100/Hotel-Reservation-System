package com.stayease.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "amenities", indexes = {
    @Index(name = "idx_amenities_name", columnList = "name", unique = true)
})
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Amenity name is required")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 60)
    private String icon = "bi-check-circle-fill";

    @Column(columnDefinition = "TEXT")
    private String description;

    public Amenity() {
    }

    public Amenity(String name, String icon, String description) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
