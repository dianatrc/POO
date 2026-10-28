package org.example;

public class Location {
    private final String country;

    private final String city;
    private final String address;
    private final Double latitude;
    private final Double longitude;

    // constructor privat apelat doar de Builder
    private Location(Builder builder) {
        this.country = builder.country;
        this.city = builder.city;
        this.address = builder.address;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public String getCountry() { return country;}

    public static class Builder {
        protected final String country;  // camp obligatoriu

        // campuri optionale
        protected String city;
        protected String address;
        protected Double latitude;
        protected Double longitude;

        public Builder(String country) {
            this.country = country;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
