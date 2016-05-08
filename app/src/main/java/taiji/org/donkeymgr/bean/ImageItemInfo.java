package taiji.org.donkeymgr.bean;

/**
 * Created by hose on 2016/4/3.
 */
public class ImageItemInfo {
    public ImageItemInfo(String localImagePath, String remoteImagePath) {
        this.localImagePath = localImagePath;
        this.remoteImagePath = remoteImagePath;
    }

    public String getRemoteImagePath() {
        return remoteImagePath;
    }

    public void setRemoteImagePath(String remoteImagePath) {
        this.remoteImagePath = remoteImagePath;
    }

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    String localImagePath;
    String remoteImagePath;
}
