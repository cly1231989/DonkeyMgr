package taiji.org.donkeymgr.bean;

/**
 * Created by hose on 2016/3/19.
 */
public class OperaResult {
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess(){
        return ( result.compareToIgnoreCase("success") == 0 );
    }

    private String result;
}
