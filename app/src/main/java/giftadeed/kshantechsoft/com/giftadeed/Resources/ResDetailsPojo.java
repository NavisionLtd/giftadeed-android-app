/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResDetailsPojo {
    @SerializedName("group_id")
    @Expose
    private String group_id;
    @SerializedName("group_name")
    @Expose
    private String group_name;
    @SerializedName("creator_id")
    @Expose
    private String creatorId;
    @SerializedName("resource_name")
    @Expose
    private String resName;
    @SerializedName("sub_type")
    @Expose
    private List<SubType> subTypes = null;
    @SerializedName("geopoint")
    @Expose
    private String geopoint;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("resource_audience_all_groups")
    @Expose
    private String resource_audience_all_groups;
    @SerializedName("resource_audience_group_ids")
    @Expose
    private String resource_audience_group_ids;
    @SerializedName("resource_audience_group_names")
    @Expose
    private String resource_audience_group_names;
    @SerializedName("resource_main_categories")
    @Expose
    private String resource_main_categories;
    @SerializedName("resource_group_categories")
    @Expose
    private String resource_group_categories;
    @SerializedName("resource_group_category_names")
    @Expose
    private String resource_group_category_names;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public List<SubType> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<SubType> subTypes) {
        this.subTypes = subTypes;
    }

    public String getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(String geopoint) {
        this.geopoint = geopoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource_audience_all_groups() {
        return resource_audience_all_groups;
    }

    public void setResource_audience_all_groups(String resource_audience_all_groups) {
        this.resource_audience_all_groups = resource_audience_all_groups;
    }

    public String getResource_audience_group_ids() {
        return resource_audience_group_ids;
    }

    public void setResource_audience_group_ids(String resource_audience_group_ids) {
        this.resource_audience_group_ids = resource_audience_group_ids;
    }

    public String getResource_audience_group_names() {
        return resource_audience_group_names;
    }

    public void setResource_audience_group_names(String resource_audience_group_names) {
        this.resource_audience_group_names = resource_audience_group_names;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getResource_main_categories() {
        return resource_main_categories;
    }

    public void setResource_main_categories(String resource_main_categories) {
        this.resource_main_categories = resource_main_categories;
    }

    public String getResource_group_categories() {
        return resource_group_categories;
    }

    public void setResource_group_categories(String resource_group_categories) {
        this.resource_group_categories = resource_group_categories;
    }

    public String getResource_group_category_names() {
        return resource_group_category_names;
    }

    public void setResource_group_category_names(String resource_group_category_names) {
        this.resource_group_category_names = resource_group_category_names;
    }
}
