package com.example.palakarora_shivangishandilya_comp304_lab5;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Landmarks {
    public final static String
        TYPE_EXTRA = "landmark_type",
        ID_EXTRA = "landmark_id";
    private static int idCount = 0;

    private final static Landmarks[] landmarks = {
        new Landmarks(LandmarkType.ATTRACTION, "CN Tower", "290 Bremner Blvd, Toronto, ON M5V 3L9", 43.642597, -79.387121),
        new Landmarks(LandmarkType.ATTRACTION, "Ripley's Aquarium of Canada", "288 Bremner Blvd, Toronto, ON M5V 3L9", 43.642477, -79.3859608),
        new Landmarks(LandmarkType.ATTRACTION, "Toronto Zoo", "2000 Meadowvale Rd, Toronto, ON M1B 5K7", 43.820801, -79.181464),

        new Landmarks(LandmarkType.STADIUM, "Varsity Centre", "299 Bloor St W, Toronto, ON M5S 1W2", 43.667074, -79.397268),
        new Landmarks(LandmarkType.STADIUM, "Rogers Centre", "1 Blue Jays Way, Toronto, ON M5V 1J1", 43.641896, -79.389090),
        new Landmarks(LandmarkType.STADIUM, "BMO Field", "170 Princes' Blvd, Toronto, ON M6K 3C3", 43.635511, -79.417750),

        new Landmarks(LandmarkType.MUSEUM, "Royal Ontario Museum", "100 Queens Park, Toronto, ON M5S 2C6", 43.667984, -79.394604),
        new Landmarks(LandmarkType.MUSEUM, "Museum of Contemporary Art", "158 Sterling Rd, Toronto, ON M6R2B7", 43.654684, -79.445215),
        new Landmarks(LandmarkType.MUSEUM, "Art Gallery of Ontario", "322 Dundas St W, Toronto, ON M5T 1G5", 43.654526, -79.392194),


        new Landmarks(LandmarkType.OLD_BUILDING, "Fort York", "250 Fort York Blvd, Toronto, ON M5V 3K9", 43.637905, -79.406474),
        new Landmarks(LandmarkType.OLD_BUILDING, "George Brown House", "186 Beverley St, Toronto, ON M5T 1L4", 43.656951, -79.395714),
        new Landmarks(LandmarkType.OLD_BUILDING, "Casa loma", "1 Austin Terrace, Toronto, ON M5R 1X8", 43.678254, -79.409422),
    };


    private int landmarkId;

    private LandmarkType type;

    private String name;

    private double longitude, latitude;

    private String address;

    public Landmarks() {}

    public Landmarks(int landmarkId, LandmarkType type,
                     String name, String address, double lat, double lng) {
        this.landmarkId = landmarkId;
        this.type = type;
        this.name = name;
        this.latitude = lat;
        this.longitude = lng;
        this.address = address;
    }

    private Landmarks(LandmarkType type, String name, String address, double lat, double lng) {
        this(idCount++, type, name, address, lat, lng);
    }

    public int getLandmarkId() { return landmarkId; }
    public void setLandmarkId(int landmarkId) { this.landmarkId = landmarkId; }

    public LandmarkType getType() { return type; }
    public void setType(LandmarkType type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LatLng getLatLng() { return new LatLng(latitude, longitude); }


    public static Landmarks[] getLandmarksByType(LandmarkType type, boolean b) {
        Landmarks[] ofType = new Landmarks[3];
        int x = 0;
        for (Landmarks landmarks : Landmarks.landmarks) {
            if (landmarks.type == type) {
                Log.d("getLandmarksByType", "found: " + landmarks.name);
                ofType[x++] = landmarks;
                if (x == 3) break;
            }
        }

        return ofType;
    }


    public static int getTypeColor(LandmarkType type) {
        return type.getColor();
    }

    public static Landmarks getLandmarkById(int id) {
        for (Landmarks lm : landmarks) {
            if (lm.getLandmarkId() == id) {
                return lm;
            }
        }
        return null;
    }
}
