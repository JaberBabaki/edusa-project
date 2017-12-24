package ir.edusa.parents.Models;

import java.io.Serializable;


public class GooglePlaceModel implements Serializable {
    private String fullText;
    private String placeId;
    private String primaryText;
    private String secondaryText;

    public GooglePlaceModel(String fullText, String placeId, String primaryText, String secondaryText) {
        this.fullText = fullText;
        this.placeId = placeId;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }
}
