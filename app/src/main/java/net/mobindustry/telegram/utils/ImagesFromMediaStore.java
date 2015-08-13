package net.mobindustry.telegram.utils;

public class ImagesFromMediaStore {

    private String data;
    private String size;
    private String displayName;
    private String title;
    private String mimType;
    private String bucketDisplayName;
    private String isPrivate;
    private Long id;
    private String thumbData;

    public String getThumbData() {
        return thumbData;
    }

    public void setThumbData(String thumbData) {
        this.thumbData = thumbData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImagesFromMediaStore() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimType() {
        return mimType;
    }

    public void setMimType(String mimType) {
        this.mimType = mimType;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }
}
