package taiji.org.donkeymgr.bean;

/**
 * Created by hose on 2016/3/27.
 */
public class UserInfo {
    public UserInfo(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public UserInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private String pwd;
}
