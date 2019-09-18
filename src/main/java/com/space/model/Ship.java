package com.space.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String planet;
    @Enumerated(EnumType.STRING)
    private ShipType shipType;
    private Date prodDate;
    private Boolean isUsed;
    private Double speed;
    private Integer crewSize;
    private double rating;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public double getRating() {
        return rating;
    }

    public void calculateRatingAndSet()
    {
        double numerator = 80 * speed * (isUsed ? 0.5 : 1);
        double denominator = 1119 - prodDate.getYear() + 1;
        double preRating = (numerator / denominator) * 100;
        preRating = Math.round(preRating);
        preRating = preRating / 100;
        rating = preRating;
    }

    protected Ship() {}

    public void updateShip(Ship updatingShipEntity) {
        if (updatingShipEntity.getName() != null && !updatingShipEntity.getName().isEmpty()) {
            this.setName(updatingShipEntity.getName());
        }

        if (updatingShipEntity.getPlanet() != null && !updatingShipEntity.getPlanet().isEmpty()) {
            this.setPlanet(updatingShipEntity.getPlanet());
        }

        if (updatingShipEntity.getShipType() != null) {
            this.setShipType(updatingShipEntity.getShipType());
        }

        if (updatingShipEntity.getProdDate() != null && updatingShipEntity.getProdDate().getYear() > 10) {
            this.setProdDate(updatingShipEntity.getProdDate());
        }

        if (updatingShipEntity.isUsed() != null) {
            this.setUsed(updatingShipEntity.isUsed());
        }

        if (updatingShipEntity.getSpeed() != null) {
            this.setSpeed(updatingShipEntity.getSpeed());
        }

        if (updatingShipEntity.getCrewSize() != null) {
            this.setCrewSize(updatingShipEntity.getCrewSize());
        }
    }

}
