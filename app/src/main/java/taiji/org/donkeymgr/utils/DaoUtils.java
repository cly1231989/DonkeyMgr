package taiji.org.donkeymgr.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import taiji.org.donkeymgr.dao.DaoMaster;
import taiji.org.donkeymgr.dao.DaoSession;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;

/**
 * Created by hose on 2016/4/3.
 */
public class DaoUtils {

    public static DonkeyDao getDonkeyDao(Context context){
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "donky.db", null).getWritableDatabase();
        DaoSession daoSession = new DaoMaster(db).newSession();
        return daoSession.getDonkeyDao();
    }

    public static Donkey getDonkeyBySn(DonkeyDao donkeyDao, int sn){
        List<Donkey> donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Sn.eq(sn)).where(DonkeyDao.Properties.Deleteflag.notEq(true)).list();
        if (donkeys.size() == 0)
            return null;

        return donkeys.get(0);
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
}
