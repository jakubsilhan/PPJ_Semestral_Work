package semestralwork.DTOs;

public class MeasurementAggregate {
    private double avg_temp;
    private double avg_pressure;
    private double avg_humidity;
    private double avg_temp_min;
    private double avg_temp_max;
    private double avg_wind_speed;

    public MeasurementAggregate() {
    }

    public MeasurementAggregate(double avg_temp, double avg_pressure, double avg_humidity, double avg_temp_min, double avg_temp_max, double avg_wind_speed) {
        this.avg_temp = avg_temp;
        this.avg_pressure = avg_pressure;
        this.avg_humidity = avg_humidity;
        this.avg_temp_min = avg_temp_min;
        this.avg_temp_max = avg_temp_max;
        this.avg_wind_speed = avg_wind_speed;
    }

    public double getAvg_temp() {
        return avg_temp;
    }

    public void setAvg_temp(double avg_temp) {
        this.avg_temp = avg_temp;
    }

    public double getAvg_pressure() {
        return avg_pressure;
    }

    public void setAvg_pressure(double avg_pressure) {
        this.avg_pressure = avg_pressure;
    }

    public double getAvg_humidity() {
        return avg_humidity;
    }

    public void setAvg_humidity(double avg_humidity) {
        this.avg_humidity = avg_humidity;
    }

    public double getAvg_temp_min() {
        return avg_temp_min;
    }

    public void setAvg_temp_min(double avg_temp_min) {
        this.avg_temp_min = avg_temp_min;
    }

    public double getAvg_temp_max() {
        return avg_temp_max;
    }

    public void setAvg_temp_max(double avg_temp_max) {
        this.avg_temp_max = avg_temp_max;
    }

    public double getAvg_wind_speed() {
        return avg_wind_speed;
    }

    public void setAvg_wind_speed(double avg_wind_speed) {
        this.avg_wind_speed = avg_wind_speed;
    }

    @Override
    public String toString() {
        return "MeasurementAggregate{" +
                "avg_temp=" + avg_temp +
                ", avg_pressure=" + avg_pressure +
                ", avg_humidity=" + avg_humidity +
                ", avg_temp_min=" + avg_temp_min +
                ", avg_temp_max=" + avg_temp_max +
                ", avg_wind_speed=" + avg_wind_speed +
                '}';
    }
}
