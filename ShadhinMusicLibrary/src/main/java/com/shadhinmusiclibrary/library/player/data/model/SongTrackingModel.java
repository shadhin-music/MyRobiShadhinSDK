package com.shadhinmusiclibrary.library.player.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * CREATED BY MEHEDI on 11/28/2019.
 */
@Keep
public class SongTrackingModel {

    @SerializedName("Status")
    @Expose
    private String status;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}