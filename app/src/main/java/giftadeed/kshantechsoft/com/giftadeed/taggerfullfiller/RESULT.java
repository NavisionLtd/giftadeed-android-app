
/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESULT {

    @SerializedName("First_Name")
    @Expose
    private String firstName;


    @SerializedName("Last_Name")
    @Expose
    private String lastName;
    @SerializedName("Total_Credit_Point")
    @Expose
    private String totalCreditPoint;

    public RESULT(String firstName, String lastName, String totalCreditPoint) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalCreditPoint = totalCreditPoint;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTotalCreditPoint() {
        return totalCreditPoint;
    }

    public void setTotalCreditPoint(String totalCreditPoint) {
        this.totalCreditPoint = totalCreditPoint;
    }

}
