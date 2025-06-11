package com.example.shoestore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String brand;
    @Column(length = 1000)
    private String description;
    private String imageUrl;

    // Constructors
    public Product() {
    }

    public Product(String name, Double price, String brand, String description) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.description = description;
    }

    // Getters & Setters
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() { return  imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
