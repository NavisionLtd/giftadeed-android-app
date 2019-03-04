
package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESULTFFILLER {

    @SerializedName("USER_ID")
    @Expose
    private String uSERID;
    @SerializedName("First_Name")
    @Expose
    private String firstName;



    @SerializedName("Last_Name")
    @Expose
    private String lastName;
    @SerializedName("Total_Fullfiller_Points")
    @Expose
    private String totalFullfillerPoints;
    @SerializedName("FullFillerRank")
    @Expose
    private String fullFillerRank;
    @SerializedName("Url_fullfillerRank")
    @Expose
    private String urlFullfillerRank;

    public  RESULTFFILLER()
    {

    }

    public RESULTFFILLER(String firstName, String lastName, String totalFullfillerPoints, String fullFillerRank, String urlFullfillerRank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalFullfillerPoints = totalFullfillerPoints;
        this.fullFillerRank = fullFillerRank;
        this.urlFullfillerRank = urlFullfillerRank;
    }



    public String getUSERID() {
        return uSERID;
    }

    public void setUSERID(String uSERID) {
        this.uSERID = uSERID;
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

    public String getTotalFullfillerPoints() {
        return totalFullfillerPoints;
    }

    public void setTotalFullfillerPoints(String totalFullfillerPoints) {
        this.totalFullfillerPoints = totalFullfillerPoints;
    }

    public String getFullFillerRank() {
        return fullFillerRank;
    }

    public void setFullFillerRank(String fullFillerRank) {
        this.fullFillerRank = fullFillerRank;
    }

    public String getUrlFullfillerRank() {
        return urlFullfillerRank;
    }

    public void setUrlFullfillerRank(String urlFullfillerRank) {
        this.urlFullfillerRank = urlFullfillerRank;
    }

}
