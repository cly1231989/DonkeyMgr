package taiji.org.donkeymgr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import taiji.org.donkeymgr.bean.UserInfo;

/**
 * Created by hose on 2016/4/3.
 */
public class SettingUtils {
    private static boolean isOnline = false;

    public static boolean isOnline() {
        return isOnline;
    }

    public static void setIsOnline(boolean isOnline) {
        SettingUtils.isOnline = isOnline;
    }

    public static void setAutoync(Context context, boolean autoync){
        SharedPreferences settings = context.getSharedPreferences("setting.data", 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putBoolean("autosync", autoync);
        localEditor.commit();
    }

    public static boolean getAutoSync(Context context){
        SharedPreferences settings = context.getSharedPreferences("setting.data", 0);
        return  settings.getBoolean("autosync", true);
    }

    public static void setWifiSync(Context context, boolean autoync){
        SharedPreferences settings = context.getSharedPreferences("setting.data", 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putBoolean("wifisync", autoync);
        localEditor.commit();
    }

    public static boolean getWifiSync(Context context){
        SharedPreferences settings = context.getSharedPreferences("setting.data", 0);
        return settings.getBoolean("wifisync", true);
    }

    public static void saveUserInfo(Context context, UserInfo userInfo){
        SharedPreferences settings = context.getSharedPreferences("config.data", 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putString("user", userInfo.getUsername());
        localEditor.putString("pwd", userInfo.getPwd());
        localEditor.commit();
    }

    public static void getUserInfo(Context context, UserInfo userInfo){
        SharedPreferences settings = context.getSharedPreferences("config.data", 0);
        userInfo.setUsername(settings.getString("user", ""));
        userInfo.setPwd(settings.getString("pwd", ""));
    }

    public static void clearUserInfo(Context context){
        SharedPreferences settings = context.getSharedPreferences("config.data", 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.clear();
        localEditor.commit();
    }

    private static boolean isWifiConnected(Context context){
        ConnectivityManager cm;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

//        int version = android.provider.Settings.System.getInt(context.getContentResolver(),
//                android.provider.Settings.System.SYS_PROP_SETTING_VERSION,
//                3);
//
//        if(version >= 21) {
//            Network[] networks = cm.getAllNetworks();
//            for (Network network : networks) {
//                NetworkInfo networkInfo = cm.getNetworkInfo(network);
//                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                    return (networkInfo.getState() == NetworkInfo.State.CONNECTED);
//                }
//            }
//
//            return false;
//        }else{
        if( cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED )
            return true;
        else
            return true;
//        }
    }

    private static boolean isMobileConnected(Context context){
        ConnectivityManager cm;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

//        int version = android.provider.Settings.System.getInt(context.getContentResolver(),
//                android.provider.Settings.System.SYS_PROP_SETTING_VERSION,
//                3);
//
//        if(version >= 21) {
//            Network[] networks = cm.getAllNetworks();
//            for (Network network : networks) {
//                NetworkInfo networkInfo = cm.getNetworkInfo(network);
//                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                    return (networkInfo.getState() == NetworkInfo.State.CONNECTED);
//                }
//            }
//
//            return false;
//        }else{
        if( cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED )
            return true;
        else
            return false;
//        }
    }

    public static boolean canSync(Context context){
        if ( getWifiSync(context) )
            return  isWifiConnected(context);

        return isWifiConnected(context) || isMobileConnected(context);
    }

    public static boolean canAutoSync(Context context) {
        if ( !getAutoSync(context) )
            return false;

        return canSync(context);
    }

    public static void saveServerAddress(Context context, String serverAddress){
        SharedPreferences settings = context.getSharedPreferences("setting.data", 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putString("server", serverAddress);
        localEditor.commit();
    }

    public static String getServerAddress(Context context){
        SharedPreferences settings = context.getSharedPreferences("setting.data", 0);
        return settings.getString("server", "127.0.0.1:8080");
    }

    public static String makeServerAddress(Context context, String para){
        return "http://" + getServerAddress(context) + "/DonkeyMgrSystem/" + para;
    }
}
