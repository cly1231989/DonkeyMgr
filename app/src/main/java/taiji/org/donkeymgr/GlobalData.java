package taiji.org.donkeymgr;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import taiji.org.donkeymgr.bean.ImageItemInfo;

/**
 * Created by hose on 2016/3/23.
 */
public class GlobalData {

    public static final int COUNT_PER_PAGE = 15;
    public static List<Integer> deletedOnServerSnList;

    public static List<PhotoInfo> getResultList() {
        return resultList;
    }

    public static void setResultList(List<PhotoInfo> resultList) {
        GlobalData.resultList.clear();

        for (PhotoInfo photoInfo:resultList) {
            GlobalData.resultList.add(photoInfo);
        }
    }

    public static List<PhotoInfo> resultList = new ArrayList<>();

    public static List<ImageItemInfo> getItemsInfo() {
        return itemsInfo;
    }

    public static void setItemsInfo(List<ImageItemInfo> itemsInfo) {
        GlobalData.itemsInfo.clear();

        for (ImageItemInfo itemInfo:itemsInfo) {
            GlobalData.itemsInfo.add(itemInfo);
        }

    }

    public static List<ImageItemInfo> itemsInfo = new ArrayList<>();
}
