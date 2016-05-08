package taiji.org.donkeymgr.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import taiji.org.donkeymgr.GlobalData;
import taiji.org.donkeymgr.bean.DonkeyVersion;
import taiji.org.donkeymgr.bean.ImageItemInfo;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;

/**
 * Created by cundong on 2015/10/9.
 * <p/>
 * 网络使用工具类
 */
public class NetworkUtils {

    public static List<Long> downloadIdList = new ArrayList<>();
    public static List<Integer> deletedOnServerSnList = new ArrayList<>();
    public static List<Long> uploadIdList = new ArrayList<>();
    public static List<Long> deleteIdList = new ArrayList<>();
    public static List<Integer> newSnList = new ArrayList<>();

    public static List<ImageItemInfo> uploadImages(Context context, Long idOnServer, int sn, boolean isMakeThumbImage){
        String uploadImageUrl = SettingUtils.makeServerAddress(context, "donkey/images/upload");
        Response response;

        String thumbImageDir = FileUtils.getThumbImageDirPath(context, sn);
        List<ImageItemInfo> itemsInfo = new LinkedList<>();
        String path = FileUtils.getImageDirPath(context, sn);
        File[] files = new File(path).listFiles();
        if(files != null && files.length != 0){
            for(File file:files){
                try {
                    response = OkHttpUtils
                            .post()
                            .addFile("image", "", file)
                            .url(uploadImageUrl)
                            .addParams("donkeyid", Long.toString(idOnServer))
                            .tag(context)
                            .build()
                            .execute();
                }catch (IOException e){
                    return null;
                }

                if ( !response.isSuccessful() )
                    return null;

                Map<String, String> result;
                try {
                    result = JSON.parseObject(response.body().string(), new HashMap<String, String>().getClass());
                }catch (IOException e){
                    return null;
                }

                if( result.get("result").compareToIgnoreCase("success") != 0 )
                    return null;

                if (isMakeThumbImage) {
                    FileUtils.makeThumbImage(file.getAbsolutePath(), thumbImageDir + file.getName());
                    itemsInfo.add(new ImageItemInfo(thumbImageDir + file.getName(), result.get("url")));
                }
                else{
                    itemsInfo.add(new ImageItemInfo(file.getAbsolutePath(), result.get("url")) );
                }

                file.delete();
            }
        }
        return itemsInfo;
    }

    public static boolean downloadDonkeys(Context context, DonkeyDao donkeyDao){

        if (deletedOnServerSnList.size() > 0) {
            GlobalData.deletedOnServerSnList = deletedOnServerSnList;
            HandlerUtils.notifyDeleteRecord();

            try {
                Thread.currentThread().sleep(3000);
            }
            catch (InterruptedException e){

            }

            for (Integer sn:deletedOnServerSnList){
                donkeyDao.delete(DaoUtils.getDonkeyBySn(donkeyDao, sn));
            }
        }

        for (Long idOnServer: downloadIdList){
            Response response;

            String downloadBaseInfoUrl = SettingUtils.makeServerAddress(context, "donkey/json/id/");
            downloadBaseInfoUrl += idOnServer;

            try {
                response = OkHttpUtils
                        .post()
                        .url(downloadBaseInfoUrl)
                        .tag(context)
                        .build()
                        .execute();
            }catch (IOException e){
                return false;
            }

            if ( !response.isSuccessful() )
                return false;

            Donkey donkey;
            try {
                donkey = JSON.parseObject(response.body().string(), Donkey.class);
            }catch (IOException e){
                return false;
            }catch (JSONException e){
                return false;
            }

            boolean isNew = false;
            Donkey donkeyLocal = DaoUtils.getDonkeyBySn(donkeyDao, donkey.getSn());
            if (donkeyLocal == null) {
                isNew = true;
                donkeyLocal = new Donkey();
            }

            donkeyLocal.setSn(donkey.getSn());
            donkeyLocal.setDeleteflag(false);
            donkeyLocal.setSyncing(false);
            donkeyLocal.setIdonserver(idOnServer);
            donkeyLocal.setVersion(donkey.getVersion());
            donkeyLocal.setSyncver(donkey.getVersion());
            donkeyLocal.setAgewhendeal(donkey.getAgewhendeal());
            donkeyLocal.setAgewhenkill(donkey.getAgewhenkill());
            donkeyLocal.setBreed(donkey.getBreed());
            donkeyLocal.setBreedaddress(donkey.getBreedaddress());
            donkeyLocal.setBreedstatus(donkey.getBreedstatus());
            donkeyLocal.setFactorytime(donkey.getFactorytime());
            donkeyLocal.setFarmer(donkey.getFarmer());
            donkeyLocal.setFeedstatus(donkey.getFeedstatus());
            donkeyLocal.setHealthstatus(donkey.getHealthstatus());
            donkeyLocal.setKilldepartment(donkey.getKilldepartment());
            donkeyLocal.setKillplace(donkey.getKillplace());
            donkeyLocal.setKilltime(donkey.getKilltime());
            donkeyLocal.setProcessstatus(donkey.getProcessstatus());
            donkeyLocal.setQualitystatyus(donkey.getQualitystatyus());
            donkeyLocal.setSex(donkey.getSex());
            donkeyLocal.setSplitstatus(donkey.getSplitstatus());
            donkeyLocal.setDealtime(donkey.getDealtime());

            if (isNew) {
                donkeyDao.insert(donkeyLocal);
                HandlerUtils.notifyNewRecord(donkeyLocal.getSn());
            }
            else {
                donkeyDao.update(donkeyLocal);
            }
        }

        return true;
    }

    public static Map<String, String> makeParas(DonkeyDao donkeyDao, String oper, Long id){
        if (oper.compareToIgnoreCase("del") == 0){
            Map<String, String> paras = new HashMap<>();
            paras.put("oper", oper);
            paras.put("id", Long.toString(id));

            return paras;
        }else{
            Donkey donkey;
            if (oper.compareToIgnoreCase("add") == 0)
                donkey = DaoUtils.getDonkeyBySn(donkeyDao, id.intValue());
            else
                donkey = DaoUtils.getDonkeyByIdOnServer(donkeyDao, id);

            //Map<String, String> paras = (Map<String, String>)JSON.parse( JSON.toJSONString(donkey) );
            Map<String, String> paras = new HashMap<>();
            paras.put("sn",             Integer.toString(donkey.getSn()));
            paras.put("agewhendeal",    donkey.getAgewhendeal());
            paras.put("agewhenkill",    donkey.getAgewhenkill());
            paras.put("breed",          donkey.getBreed());
            paras.put("breedaddress",   donkey.getBreedaddress());
            paras.put("breedstatus",    donkey.getBreedstatus());
            paras.put("dealtime",       donkey.getDealtime());
            paras.put("factorytime",    donkey.getFactorytime());
            paras.put("farmer",         donkey.getFarmer());
            paras.put("feedstatus",     donkey.getFeedstatus());
            paras.put("healthstatus",   donkey.getHealthstatus());
            paras.put("killdepartment", donkey.getKilldepartment());
            paras.put("killplace",      donkey.getKillplace());
            paras.put("killtime",       donkey.getKilltime());
            paras.put("processstatus",  donkey.getProcessstatus());
            paras.put("qualitystatyus", donkey.getQualitystatyus());
            paras.put("sex",            donkey.getSex());
            paras.put("splitstatus",    donkey.getSplitstatus());
            paras.put("version",        Long.toString(donkey.getVersion()));
            paras.put("id",             Long.toString(id));
            paras.put("oper",           oper);

            return paras;
        }
    }

    //0:失败; 其他：服务器返回的id
    public static Long uploadDonkeys(Context context, DonkeyDao donkeyDao, String oper, Long id){
        Response response;
        String uploadBaseInfoUrl = SettingUtils.makeServerAddress(context, "donkey/save");

        Map<String, String> params = makeParas(donkeyDao, oper, id);
        try {
            response = OkHttpUtils
                    .post()
                    .url(uploadBaseInfoUrl)
                    .params(params)
                    .tag(context)
                    .build()
                    .execute();
        }catch (IOException e){
            return 0L;
        }

        if ( !response.isSuccessful() )
            return 0L;

        if (oper.compareToIgnoreCase("del") == 0) {
            return 1L;
        }else{
            Map<String, String> result;
            try {
                result = JSON.parseObject(response.body().string(), new HashMap<String, String>().getClass());
            }catch (IOException e){
                return 0L;
            }
            return Long.parseLong(result.get("id"));
        }

    }

    public static boolean uploadModifiedDonkeys(Context context, DonkeyDao donkeyDao){
        for (Long id:uploadIdList) {
            Long result = uploadDonkeys(context, donkeyDao, "edit", id);
            if( 0 == result )
                return false;

            Donkey donkey = DaoUtils.getDonkeyByIdOnServer(donkeyDao, id);
            if( null == NetworkUtils.uploadImages(context, id, donkey.getSn(), false) )
                return false;

            donkey.setSyncver(donkey.getVersion());
            donkeyDao.update(donkey);
            HandlerUtils.updateUI();
        }

        return true;
    }

    public static boolean uploadNewDonkeys(Context context, DonkeyDao donkeyDao){
        for (Integer sn:newSnList) {
            Long result = uploadDonkeys(context, donkeyDao, "add", sn.longValue());
            if( 0 == result )
                return false;

            Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, sn);
            if( null == NetworkUtils.uploadImages(context, result, sn, false) )
                return false;

            donkey.setSyncver(donkey.getVersion());
            donkey.setIdonserver(result);
            donkeyDao.update(donkey);

            HandlerUtils.updateUI();
        }

        return true;
    }

    public static boolean deleteDonkeys(Context context, DonkeyDao donkeyDao){
        for (Long id:deleteIdList){

            FileUtils.deleteDirById(context, donkeyDao, id);
            if( 0 == uploadDonkeys(context, donkeyDao, "del", id) )
                return false;

            donkeyDao.delete( DaoUtils.getDeletedDonkeyByIdOnServer(donkeyDao, id) );
        }

        return true;
    }

    public static boolean uploadDonkeys(Context context, DonkeyDao donkeyDao){
        if ( !deleteDonkeys(context, donkeyDao) )
            return false;
        if ( !uploadNewDonkeys(context, donkeyDao) )
            return false;

        return uploadModifiedDonkeys(context, donkeyDao);
    }

    public static boolean downloadIdList(Context context, DonkeyDao donkeyDao){
        downloadIdList.clear();
        uploadIdList.clear();
        newSnList.clear();
        deleteIdList.clear();
        deletedOnServerSnList.clear();
        Response response;
        String downloadDonkeyListUrl = SettingUtils.makeServerAddress(context, "donkey/getdonkeys");

        try {
            response = OkHttpUtils
                    .post()
                    .url(downloadDonkeyListUrl)
                    .tag(context)
                    .build()
                    .execute();
        }catch (IOException e){
            return false;
        }

        if ( !response.isSuccessful() )
            return false;

        Map<String, Integer> donkeyVersions;
        try {
            donkeyVersions = JSON.parseObject(response.body().string(), new HashMap<String, Integer>().getClass());
        }catch (IOException e){
            return false;
        }

        List<Donkey> donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Deleteflag.eq(true)).list();
        for (Donkey donkey: donkeys) {
            if(donkey.getIdonserver() == 0) {
                donkeyDao.delete(donkey);
            }
            else {
                deleteIdList.add(donkey.getIdonserver());

                String idOnServer = Long.toString(donkey.getIdonserver());
                if (donkeyVersions.containsKey(idOnServer))
                    donkeyVersions.remove(idOnServer);
            }
        }

        int totalCount = (int)donkeyDao.count();
        int countPerLoop = totalCount/10;
        int countLeft = totalCount - countPerLoop*10;
        int loop = 0;
        List<DonkeyVersion> donkeyVersionsLocal = new ArrayList<DonkeyVersion>();
        for (; loop < 10; loop++) {
            donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Deleteflag.notEq(true)).offset(loop*countPerLoop).limit(countPerLoop).list();
            for (Donkey donkey: donkeys) {
                donkeyVersionsLocal.add(new DonkeyVersion(donkey.getIdonserver(), donkey.getSn(), donkey.getVersion(), donkey.getSyncver(), donkey.getDeleteflag()));
            }
        }

        donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Deleteflag.notEq(true)).offset(loop*countPerLoop).limit(countLeft).list();
        for (Donkey donkey: donkeys) {
            donkeyVersionsLocal.add(new DonkeyVersion(donkey.getIdonserver(), donkey.getSn(), donkey.getVersion(), donkey.getSyncver(), donkey.getDeleteflag()));
        }

        for ( Map.Entry<String, Integer> entry : donkeyVersions.entrySet() ) {
            Long idonserver = Long.parseLong(entry.getKey());
            Donkey donkey = DaoUtils.getDonkeyByIdOnServer(donkeyDao, idonserver);
            Integer ver = entry.getValue();
            if (donkey == null || entry.getValue() > donkey.getVersion()) {
                downloadIdList.add(idonserver);
            }
        }

        for (DonkeyVersion donkeyVersion:donkeyVersionsLocal) {
            if (donkeyVersion.isNewRecord()) {
                newSnList.add(donkeyVersion.getSn());
            }else if ( !donkeyVersions.containsKey(Long.toString(donkeyVersion.getIdOnServer())) ){
                deletedOnServerSnList.add(donkeyVersion.getSn());
            }else if(donkeyVersion.isNotSyncRecord()) {
                uploadIdList.add(donkeyVersion.getIdOnServer());
            }
        }

        return true;
    }

}