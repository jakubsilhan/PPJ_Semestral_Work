package semestralwork.Models;

import java.time.LocalDateTime;

public class Measurement {
    private int id;
    private LocalDateTime datetime;
    private double temp;
    private int pressure;
    private int humidity;
    private double temp_min;
    private double temp_max;
    private String weather;
    private String weather_desc;
    private double wind_speed;
    private int wind_deg;
    private int city_id;

    public Measurement() {
    }

    public Measurement(int id, LocalDateTime datetime, double temp, int pressure, int humidity, double temp_min, double temp_max, String weather, String weather_desc, double wind_speed, int wind_deg, int city_id) {
        this.id = id;
        this.datetime = datetime;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.weather = weather;
        this.weather_desc = weather_desc;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.city_id = city_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather_desc() {
        return weather_desc;
    }

    public void setWeather_desc(String weather_desc) {
        this.weather_desc = weather_desc;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(int wind_deg) {
        this.wind_deg = wind_deg;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                ", weather='" + weather + '\'' +
                ", weather_desc='" + weather_desc + '\'' +
                ", wind_speed=" + wind_speed +
                ", wind_deg=" + wind_deg +
                ", city_id=" + city_id +
                '}';
    }
}
