package taiji.org.donkeymgr.utils;

import android.os.Bundle;
import android.os.Message;

import java.util.List;

import taiji.org.donkeymgr.EditImagesActivity;
import taiji.org.donkeymgr.MainActivity;
import taiji.org.donkeymgr.bean.ImageItemInfo;

/**
 * Created by hose on 2016/4/3.
 */
public class HandlerUtils {

    public static MainActivity.AddItemHandler getAddItemHandler() {
        return addItemHandler;
    }

    public static void setAddItemHandler(MainActivity.AddItemHandler addItemHandler) {
        HandlerUtils.addItemHandler = addItemHandler;
    }

    public static void notifyNewRecord(int sn){
        Bundle bundle = new Bundle();
        bundle.putInt("num", sn);
        Message msg = new Message();
        msg.what = MainActivity.AddItemHandler.MSG_ADD_ITEM;
        msg.setData(bundle);
        addItemHandler.sendMessage(msg);
    }

    public static void updateUI(){
        Message msg = new Message();
        msg.what = MainActivity.AddItemHandler.MSG_UPDATE_UI;
        addItemHandler.sendMessage(msg);
    }

    public static void notifyDeleteRecord(){
        Message msg = new Message();
        msg.what = MainActivity.AddItemHandler.MSG_DELETE_ITEM;
        addItemHandler.sendMessage(msg);
    }

    private static MainActivity.AddItemHandler addItemHandler = null;

    public static EditImagesActivity.UploadImagesHandler getUploadImagesHandler() {
        return uploadImagesHandler;
    }

    public static void setUploadImagesHandler(EditImagesActivity.UploadImagesHandler uploadImagesHandler) {
        HandlerUtils.uploadImagesHandler = uploadImagesHandler;
    }

    private static EditImagesActivity.UploadImagesHandler uploadImagesHandler = null;

    public static List<ImageItemInfo> getItemsInfo() {
        return itemsInfo;
    }

    public static void setItemsInfo(List<ImageItemInfo> itemsInfo) {
        HandlerUtils.itemsInfo = itemsInfo;
    }

    private static List<ImageItemInfo> itemsInfo;

    public static void notifyImageUploadFinished(List<ImageItemInfo> itemsInfo){
        HandlerUtils.itemsInfo = itemsInfo;

        Message msg = new Message();
        msg.what = EditImagesActivity.UploadImagesHandler.MSG_UPDATE_UI;
        uploadImagesHandler.sendMessage(msg);
    }
}
