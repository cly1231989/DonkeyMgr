package taiji.org.donkeymgr.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "UPLOAD_IMAGE_INFO".
 */
public class UploadImageInfo {

    private Long id;
    private long donkeyid;
    private long idonserver;
    /** Not-null value. */
    private String localimagepath;
    /** Not-null value. */
    private String url;

    public UploadImageInfo() {
    }

    public UploadImageInfo(Long id) {
        this.id = id;
    }

    public UploadImageInfo(long donkeyid, long idonserver, String localimagepath, String url) {
        this.donkeyid = donkeyid;
        this.idonserver = idonserver;
        this.localimagepath = localimagepath;
        this.url = url;
    }

    public UploadImageInfo(Long id, long donkeyid, long idonserver, String localimagepath, String url) {
        this.id = id;
        this.donkeyid = donkeyid;
        this.idonserver = idonserver;
        this.localimagepath = localimagepath;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDonkeyid() {
        return donkeyid;
    }

    public void setDonkeyid(long donkeyid) {
        this.donkeyid = donkeyid;
    }

    public long getIdonserver() {
        return idonserver;
    }

    public void setIdonserver(long idonserver) {
        this.idonserver = idonserver;
    }

    /** Not-null value. */
    public String getLocalimagepath() {
        return localimagepath;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLocalimagepath(String localimagepath) {
        this.localimagepath = localimagepath;
    }

    /** Not-null value. */
    public String getUrl() {
        return url;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUrl(String url) {
        this.url = url;
    }

}
