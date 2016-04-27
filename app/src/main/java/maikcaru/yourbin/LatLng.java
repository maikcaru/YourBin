package maikcaru.yourbin;

/**
 * Created by michael.carr on 27/04/16.
 */
public class LatLng {

    double latitude;
    double longitude;

    public LatLng() {

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String toString() {
        return this.latitude + "," + this.longitude;
    }
}
