package taiji.org.donkeymgr.msgs;

/**
 * Created by hose on 2016/5/20.
 */
public class QRCodeScanResult {
    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    private int sn;

    public QRCodeScanResult(int sn) {
        this.sn = sn;
    }
}
