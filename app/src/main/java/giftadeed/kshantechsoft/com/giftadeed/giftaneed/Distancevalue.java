
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.giftaneed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distancevalue {

    @SerializedName("distance_name")
    @Expose
    private String distanceName;
    @SerializedName("distance_value")
    @Expose
    private String distanceValue;
    @SerializedName("distance_unit")
    @Expose
    private String distanceUnit;

    public String getDistanceName() {
        return distanceName;
    }

    public void setDistanceName(String distanceName) {
        this.distanceName = distanceName;
    }

    public String getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(String distanceValue) {
        this.distanceValue = distanceValue;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

}
