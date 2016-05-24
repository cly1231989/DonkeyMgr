package taiji.org.donkeymgr.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import taiji.org.donkeymgr.dao.UploadImageInfoDao;

/**
 * Created by hose on 2016/5/12.
 */
public class UploadImageThread extends Thread{

    private static UploadImageThread instance = null;
    private Context context;
    public Handler uploadImageHandler = null;

    private UploadImageThread(Context context) {
        this.context = context;
    }

    public static UploadImageThread getInstance() {
        return instance;
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new UploadImageThread(context);
        }
    }

    @Override
    public void run() {
        Looper.prepare();

        final Context appContext = context.getApplicationContext();
        uploadImageHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if( !SettingUtils.isOnline() )
                    return;
                
                UploadImageInfoDao uploadImageInfoDao = DaoUtils.getUploadImageDao();
                NetworkUtils.uploadImages( appContext, uploadImageInfoDao);
            }
        };

        Looper.loop();
    }

    public void startSync(){
        if (uploadImageHandler != null)
            uploadImageHandler.sendEmptyMessage(0);
    }
}
