package taiji.org.donkeymgr.msgs;

/**
 * Created by hose on 2016/5/20.
 */
public class LoginResultMsgEvent {
    public LoginResultMsgEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private boolean success;
}
