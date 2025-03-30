package semestralwork.Models;

import java.util.Objects;

public class City {
    private int id;
    private String name;
    private double longitude;
    private double latitude;
    private int country_id;

    public City(){

    }

    public City(int id, String name, double longitude, double latitude, int country_id) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.country_id = country_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id && Double.compare(longitude, city.longitude) == 0 && Double.compare(latitude, city.latitude) == 0 && Objects.equals(name, city.name) && Objects.equals(country_id, city.country_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, longitude, latitude, country_id);
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", country=" + country_id +
                '}';
    }
}
