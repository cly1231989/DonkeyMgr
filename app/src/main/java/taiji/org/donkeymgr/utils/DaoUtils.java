package taiji.org.donkeymgr.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import taiji.org.donkeymgr.dao.DaoMaster;
import taiji.org.donkeymgr.dao.DaoSession;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;
import taiji.org.donkeymgr.dao.UploadImageInfoDao;

/**
 * Created by hose on 2016/4/3.
 */
public class DaoUtils {
    private static DaoSession daoSession = null;

    public static void init(Context context){
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "donky.db", null).getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DonkeyDao getDonkeyDao(){
//        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "donky.db", null).getWritableDatabase();
//        DaoSession daoSession = new DaoMaster(db).newSession();
        return daoSession.getDonkeyDao();
    }

    public static Donkey getDonkeyByID(DonkeyDao donkeyDao, Long id){
        List<Donkey> donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Id.eq(id)).where(DonkeyDao.Properties.Deleteflag.notEq(true)).list();
        if (donkeys.size() == 0)
            return null;

        return donkeys.get(0);
    }

    public static Donkey getDonkeyBySn(DonkeyDao donkeyDao, int sn){
        List<Donkey> donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Sn.eq(sn)).where(DonkeyDao.Properties.Deleteflag.notEq(true)).list();
        if (donkeys.size() == 0)
            return null;

        return donkeys.get(0);
    }

    public static List<Donkey> getDonkeyByFarmer(DonkeyDao donkeyDao, String farmer){
        String query = "%"+farmer+"%";
        return donkeyDao.queryBuilder().where(DonkeyDao.Properties.Farmer.like(query)).where(DonkeyDao.Properties.Deleteflag.notEq(true)).list();
    }

    public static Donkey getDonkeyByIdOnServer(DonkeyDao donkeyDao, Long idOnServer){
        List<Donkey> donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Idonserver.eq(idOnServer)).where(DonkeyDao.Properties.Deleteflag.notEq(true)).list();
        if (donkeys.size() == 0)
            return null;

        return donkeys.get(0);
    }

    public static Donkey getDeletedDonkeyByIdOnServer(DonkeyDao donkeyDao, Long idOnServer){
        List<Donkey> donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Idonserver.eq(idOnServer)).where(DonkeyDao.Properties.Deleteflag.eq(true)).list();
        if (donkeys.size() == 0)
            return null;

        return donkeys.get(0);
    }

    public static UploadImageInfoDao getUploadImageDao(){
//        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "donky.db", null).getWritableDatabase();
//        DaoSession daoSession = new DaoMaster(db).newSession();
        return daoSession.getUploadImageInfoDao();
    }

    public static void deleteUploadImageInfo(Context context, Long donkeyid){
        getUploadImageDao().queryBuilder()
                .where(UploadImageInfoDao.Properties.Donkeyid.eq(donkeyid))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
