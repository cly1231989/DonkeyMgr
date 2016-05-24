package taiji.org.donkeymgr.bean;

import android.graphics.Bitmap;

/**
 * Created by hose on 2016/4/3.
 */
public class ImageItemInfo {
    public ImageItemInfo(Bitmap image, String remoteImagePath) {
        this.image = image;
        this.remoteImagePath = remoteImagePath;
    }

    public String getRemoteImagePath() {
        return remoteImagePath;
    }

    public void setRemoteImagePath(String remoteImagePath) {
        this.remoteImagePath = remoteImagePath;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    Bitmap image;
    String remoteImagePath;
}
