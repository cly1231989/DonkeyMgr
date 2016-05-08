package taiji.org.donkeymgr.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "DONKEY".
*/
public class DonkeyDao extends AbstractDao<Donkey, Long> {

    public static final String TABLENAME = "DONKEY";

    /**
     * Properties of entity Donkey.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Sn = new Property(1, int.class, "sn", false, "SN");
        public final static Property Farmer = new Property(2, String.class, "farmer", false, "FARMER");
        public final static Property Breedaddress = new Property(3, String.class, "breedaddress", false, "BREEDADDRESS");
        public final static Property Dealtime = new Property(4, String.class, "dealtime", false, "DEALTIME");
        public final static Property Breed = new Property(5, String.class, "breed", false, "BREED");
        public final static Property Sex = new Property(6, String.class, "sex", false, "SEX");
        public final static Property Agewhendeal = new Property(7, String.class, "agewhendeal", false, "AGEWHENDEAL");
        public final static Property Agewhenkill = new Property(8, String.class, "agewhenkill", false, "AGEWHENKILL");
        public final static Property Feedstatus = new Property(9, String.class, "feedstatus", false, "FEEDSTATUS");
        public final static Property Healthstatus = new Property(10, String.class, "healthstatus", false, "HEALTHSTATUS");
        public final static Property Breedstatus = new Property(11, String.class, "breedstatus", false, "BREEDSTATUS");
        public final static Property Killdepartment = new Property(12, String.class, "killdepartment", false, "KILLDEPARTMENT");
        public final static Property Killplace = new Property(13, String.class, "killplace", false, "KILLPLACE");
        public final static Property Killtime = new Property(14, String.class, "killtime", false, "KILLTIME");
        public final static Property Splitstatus = new Property(15, String.class, "splitstatus", false, "SPLITSTATUS");
        public final static Property Processstatus = new Property(16, String.class, "processstatus", false, "PROCESSSTATUS");
        public final static Property Qualitystatyus = new Property(17, String.class, "qualitystatyus", false, "QUALITYSTATYUS");
        public final static Property Factorytime = new Property(18, String.class, "factorytime", false, "FACTORYTIME");
        public final static Property Version = new Property(19, Long.class, "version", false, "VERSION");
        public final static Property Syncver = new Property(20, Long.class, "syncver", false, "SYNCVER");
        public final static Property Idonserver = new Property(21, Long.class, "idonserver", false, "IDONSERVER");
        public final static Property Syncing = new Property(22, Boolean.class, "syncing", false, "SYNCING");
        public final static Property Deleteflag = new Property(23, Boolean.class, "deleteflag", false, "DELETEFLAG");
    };


    public DonkeyDao(DaoConfig config) {
        super(config);
    }
    
    public DonkeyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DONKEY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SN\" INTEGER NOT NULL ," + // 1: sn
                "\"FARMER\" TEXT," + // 2: farmer
                "\"BREEDADDRESS\" TEXT," + // 3: breedaddress
                "\"DEALTIME\" TEXT," + // 4: dealtime
                "\"BREED\" TEXT," + // 5: breed
                "\"SEX\" TEXT," + // 6: sex
                "\"AGEWHENDEAL\" TEXT," + // 7: agewhendeal
                "\"AGEWHENKILL\" TEXT," + // 8: agewhenkill
                "\"FEEDSTATUS\" TEXT," + // 9: feedstatus
                "\"HEALTHSTATUS\" TEXT," + // 10: healthstatus
                "\"BREEDSTATUS\" TEXT," + // 11: breedstatus
                "\"KILLDEPARTMENT\" TEXT," + // 12: killdepartment
                "\"KILLPLACE\" TEXT," + // 13: killplace
                "\"KILLTIME\" TEXT," + // 14: killtime
                "\"SPLITSTATUS\" TEXT," + // 15: splitstatus
                "\"PROCESSSTATUS\" TEXT," + // 16: processstatus
                "\"QUALITYSTATYUS\" TEXT," + // 17: qualitystatyus
                "\"FACTORYTIME\" TEXT," + // 18: factorytime
                "\"VERSION\" INTEGER," + // 19: version
                "\"SYNCVER\" INTEGER," + // 20: syncver
                "\"IDONSERVER\" INTEGER," + // 21: idonserver
                "\"SYNCING\" INTEGER," + // 22: syncing
                "\"DELETEFLAG\" INTEGER);"); // 23: deleteflag
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_DONKEY_SN ON DONKEY" +
                " (\"SN\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DONKEY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Donkey entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSn());
 
        String farmer = entity.getFarmer();
        if (farmer != null) {
            stmt.bindString(3, farmer);
        }
 
        String breedaddress = entity.getBreedaddress();
        if (breedaddress != null) {
            stmt.bindString(4, breedaddress);
        }
 
        String dealtime = entity.getDealtime();
        if (dealtime != null) {
            stmt.bindString(5, dealtime);
        }
 
        String breed = entity.getBreed();
        if (breed != null) {
            stmt.bindString(6, breed);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(7, sex);
        }
 
        String agewhendeal = entity.getAgewhendeal();
        if (agewhendeal != null) {
            stmt.bindString(8, agewhendeal);
        }
 
        String agewhenkill = entity.getAgewhenkill();
        if (agewhenkill != null) {
            stmt.bindString(9, agewhenkill);
        }
 
        String feedstatus = entity.getFeedstatus();
        if (feedstatus != null) {
            stmt.bindString(10, feedstatus);
        }
 
        String healthstatus = entity.getHealthstatus();
        if (healthstatus != null) {
            stmt.bindString(11, healthstatus);
        }
 
        String breedstatus = entity.getBreedstatus();
        if (breedstatus != null) {
            stmt.bindString(12, breedstatus);
        }
 
        String killdepartment = entity.getKilldepartment();
        if (killdepartment != null) {
            stmt.bindString(13, killdepartment);
        }
 
        String killplace = entity.getKillplace();
        if (killplace != null) {
            stmt.bindString(14, killplace);
        }
 
        String killtime = entity.getKilltime();
        if (killtime != null) {
            stmt.bindString(15, killtime);
        }
 
        String splitstatus = entity.getSplitstatus();
        if (splitstatus != null) {
            stmt.bindString(16, splitstatus);
        }
 
        String processstatus = entity.getProcessstatus();
        if (processstatus != null) {
            stmt.bindString(17, processstatus);
        }
 
        String qualitystatyus = entity.getQualitystatyus();
        if (qualitystatyus != null) {
            stmt.bindString(18, qualitystatyus);
        }
 
        String factorytime = entity.getFactorytime();
        if (factorytime != null) {
            stmt.bindString(19, factorytime);
        }
 
        Long version = entity.getVersion();
        if (version != null) {
            stmt.bindLong(20, version);
        }
 
        Long syncver = entity.getSyncver();
        if (syncver != null) {
            stmt.bindLong(21, syncver);
        }
 
        Long idonserver = entity.getIdonserver();
        if (idonserver != null) {
            stmt.bindLong(22, idonserver);
        }
 
        Boolean syncing = entity.getSyncing();
        if (syncing != null) {
            stmt.bindLong(23, syncing ? 1L: 0L);
        }
 
        Boolean deleteflag = entity.getDeleteflag();
        if (deleteflag != null) {
            stmt.bindLong(24, deleteflag ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Donkey readEntity(Cursor cursor, int offset) {
        Donkey entity = new Donkey( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // sn
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // farmer
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // breedaddress
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // dealtime
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // breed
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // sex
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // agewhendeal
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // agewhenkill
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // feedstatus
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // healthstatus
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // breedstatus
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // killdepartment
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // killplace
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // killtime
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // splitstatus
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // processstatus
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // qualitystatyus
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // factorytime
            cursor.isNull(offset + 19) ? null : cursor.getLong(offset + 19), // version
            cursor.isNull(offset + 20) ? null : cursor.getLong(offset + 20), // syncver
            cursor.isNull(offset + 21) ? null : cursor.getLong(offset + 21), // idonserver
            cursor.isNull(offset + 22) ? null : cursor.getShort(offset + 22) != 0, // syncing
            cursor.isNull(offset + 23) ? null : cursor.getShort(offset + 23) != 0 // deleteflag
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Donkey entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSn(cursor.getInt(offset + 1));
        entity.setFarmer(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBreedaddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDealtime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBreed(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSex(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAgewhendeal(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAgewhenkill(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFeedstatus(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setHealthstatus(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setBreedstatus(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setKilldepartment(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setKillplace(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setKilltime(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setSplitstatus(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setProcessstatus(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setQualitystatyus(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setFactorytime(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setVersion(cursor.isNull(offset + 19) ? null : cursor.getLong(offset + 19));
        entity.setSyncver(cursor.isNull(offset + 20) ? null : cursor.getLong(offset + 20));
        entity.setIdonserver(cursor.isNull(offset + 21) ? null : cursor.getLong(offset + 21));
        entity.setSyncing(cursor.isNull(offset + 22) ? null : cursor.getShort(offset + 22) != 0);
        entity.setDeleteflag(cursor.isNull(offset + 23) ? null : cursor.getShort(offset + 23) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Donkey entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Donkey entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
