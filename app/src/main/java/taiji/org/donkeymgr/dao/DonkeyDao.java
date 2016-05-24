package taiji.org.donkeymgr.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import taiji.org.donkeymgr.dao.Donkey;

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
        public final static Property Supplier = new Property(4, String.class, "supplier", false, "SUPPLIER");
        public final static Property Supplyaddress = new Property(5, String.class, "supplyaddress", false, "SUPPLYADDRESS");
        public final static Property Dealtime = new Property(6, String.class, "dealtime", false, "DEALTIME");
        public final static Property Supplytime = new Property(7, String.class, "supplytime", false, "SUPPLYTIME");
        public final static Property Breed = new Property(8, String.class, "breed", false, "BREED");
        public final static Property Sex = new Property(9, String.class, "sex", false, "SEX");
        public final static Property Agewhendeal = new Property(10, String.class, "agewhendeal", false, "AGEWHENDEAL");
        public final static Property Agewhenkill = new Property(11, String.class, "agewhenkill", false, "AGEWHENKILL");
        public final static Property Feedpattern = new Property(12, String.class, "feedpattern", false, "FEEDPATTERN");
        public final static Property Forage = new Property(13, String.class, "forage", false, "FORAGE");
        public final static Property Feedstatus = new Property(14, String.class, "feedstatus", false, "FEEDSTATUS");
        public final static Property Healthstatus = new Property(15, String.class, "healthstatus", false, "HEALTHSTATUS");
        public final static Property Breedstatus = new Property(16, String.class, "breedstatus", false, "BREEDSTATUS");
        public final static Property Killdepartment = new Property(17, String.class, "killdepartment", false, "KILLDEPARTMENT");
        public final static Property Killplace = new Property(18, String.class, "killplace", false, "KILLPLACE");
        public final static Property Killtime = new Property(19, String.class, "killtime", false, "KILLTIME");
        public final static Property Freshkeepmethod = new Property(20, String.class, "freshkeepmethod", false, "FRESHKEEPMETHOD");
        public final static Property Freshkeeptime = new Property(21, String.class, "freshkeeptime", false, "FRESHKEEPTIME");
        public final static Property Splitstatus = new Property(22, String.class, "splitstatus", false, "SPLITSTATUS");
        public final static Property Processstatus = new Property(23, String.class, "processstatus", false, "PROCESSSTATUS");
        public final static Property Qualitystatus = new Property(24, String.class, "qualitystatus", false, "QUALITYSTATUS");
        public final static Property QC = new Property(25, String.class, "QC", false, "QC");
        public final static Property QA = new Property(26, String.class, "QA", false, "QA");
        public final static Property Furquality = new Property(27, String.class, "furquality", false, "FURQUALITY");
        public final static Property Reserved = new Property(28, String.class, "reserved", false, "RESERVED");
        public final static Property Factorytime = new Property(29, String.class, "factorytime", false, "FACTORYTIME");
        public final static Property Version = new Property(30, Long.class, "version", false, "VERSION");
        public final static Property Syncver = new Property(31, Long.class, "syncver", false, "SYNCVER");
        public final static Property Idonserver = new Property(32, Long.class, "idonserver", false, "IDONSERVER");
        public final static Property Syncing = new Property(33, Boolean.class, "syncing", false, "SYNCING");
        public final static Property Deleteflag = new Property(34, Boolean.class, "deleteflag", false, "DELETEFLAG");
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
                "\"SUPPLIER\" TEXT," + // 4: supplier
                "\"SUPPLYADDRESS\" TEXT," + // 5: supplyaddress
                "\"DEALTIME\" TEXT," + // 6: dealtime
                "\"SUPPLYTIME\" TEXT," + // 7: supplytime
                "\"BREED\" TEXT," + // 8: breed
                "\"SEX\" TEXT," + // 9: sex
                "\"AGEWHENDEAL\" TEXT," + // 10: agewhendeal
                "\"AGEWHENKILL\" TEXT," + // 11: agewhenkill
                "\"FEEDPATTERN\" TEXT," + // 12: feedpattern
                "\"FORAGE\" TEXT," + // 13: forage
                "\"FEEDSTATUS\" TEXT," + // 14: feedstatus
                "\"HEALTHSTATUS\" TEXT," + // 15: healthstatus
                "\"BREEDSTATUS\" TEXT," + // 16: breedstatus
                "\"KILLDEPARTMENT\" TEXT," + // 17: killdepartment
                "\"KILLPLACE\" TEXT," + // 18: killplace
                "\"KILLTIME\" TEXT," + // 19: killtime
                "\"FRESHKEEPMETHOD\" TEXT," + // 20: freshkeepmethod
                "\"FRESHKEEPTIME\" TEXT," + // 21: freshkeeptime
                "\"SPLITSTATUS\" TEXT," + // 22: splitstatus
                "\"PROCESSSTATUS\" TEXT," + // 23: processstatus
                "\"QUALITYSTATUS\" TEXT," + // 24: qualitystatus
                "\"QC\" TEXT," + // 25: QC
                "\"QA\" TEXT," + // 26: QA
                "\"FURQUALITY\" TEXT," + // 27: furquality
                "\"RESERVED\" TEXT," + // 28: reserved
                "\"FACTORYTIME\" TEXT," + // 29: factorytime
                "\"VERSION\" INTEGER," + // 30: version
                "\"SYNCVER\" INTEGER," + // 31: syncver
                "\"IDONSERVER\" INTEGER," + // 32: idonserver
                "\"SYNCING\" INTEGER," + // 33: syncing
                "\"DELETEFLAG\" INTEGER);"); // 34: deleteflag
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
 
        String supplier = entity.getSupplier();
        if (supplier != null) {
            stmt.bindString(5, supplier);
        }
 
        String supplyaddress = entity.getSupplyaddress();
        if (supplyaddress != null) {
            stmt.bindString(6, supplyaddress);
        }
 
        String dealtime = entity.getDealtime();
        if (dealtime != null) {
            stmt.bindString(7, dealtime);
        }
 
        String supplytime = entity.getSupplytime();
        if (supplytime != null) {
            stmt.bindString(8, supplytime);
        }
 
        String breed = entity.getBreed();
        if (breed != null) {
            stmt.bindString(9, breed);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(10, sex);
        }
 
        String agewhendeal = entity.getAgewhendeal();
        if (agewhendeal != null) {
            stmt.bindString(11, agewhendeal);
        }
 
        String agewhenkill = entity.getAgewhenkill();
        if (agewhenkill != null) {
            stmt.bindString(12, agewhenkill);
        }
 
        String feedpattern = entity.getFeedpattern();
        if (feedpattern != null) {
            stmt.bindString(13, feedpattern);
        }
 
        String forage = entity.getForage();
        if (forage != null) {
            stmt.bindString(14, forage);
        }
 
        String feedstatus = entity.getFeedstatus();
        if (feedstatus != null) {
            stmt.bindString(15, feedstatus);
        }
 
        String healthstatus = entity.getHealthstatus();
        if (healthstatus != null) {
            stmt.bindString(16, healthstatus);
        }
 
        String breedstatus = entity.getBreedstatus();
        if (breedstatus != null) {
            stmt.bindString(17, breedstatus);
        }
 
        String killdepartment = entity.getKilldepartment();
        if (killdepartment != null) {
            stmt.bindString(18, killdepartment);
        }
 
        String killplace = entity.getKillplace();
        if (killplace != null) {
            stmt.bindString(19, killplace);
        }
 
        String killtime = entity.getKilltime();
        if (killtime != null) {
            stmt.bindString(20, killtime);
        }
 
        String freshkeepmethod = entity.getFreshkeepmethod();
        if (freshkeepmethod != null) {
            stmt.bindString(21, freshkeepmethod);
        }
 
        String freshkeeptime = entity.getFreshkeeptime();
        if (freshkeeptime != null) {
            stmt.bindString(22, freshkeeptime);
        }
 
        String splitstatus = entity.getSplitstatus();
        if (splitstatus != null) {
            stmt.bindString(23, splitstatus);
        }
 
        String processstatus = entity.getProcessstatus();
        if (processstatus != null) {
            stmt.bindString(24, processstatus);
        }
 
        String qualitystatus = entity.getQualitystatus();
        if (qualitystatus != null) {
            stmt.bindString(25, qualitystatus);
        }
 
        String QC = entity.getQC();
        if (QC != null) {
            stmt.bindString(26, QC);
        }
 
        String QA = entity.getQA();
        if (QA != null) {
            stmt.bindString(27, QA);
        }
 
        String furquality = entity.getFurquality();
        if (furquality != null) {
            stmt.bindString(28, furquality);
        }
 
        String reserved = entity.getReserved();
        if (reserved != null) {
            stmt.bindString(29, reserved);
        }
 
        String factorytime = entity.getFactorytime();
        if (factorytime != null) {
            stmt.bindString(30, factorytime);
        }
 
        Long version = entity.getVersion();
        if (version != null) {
            stmt.bindLong(31, version);
        }
 
        Long syncver = entity.getSyncver();
        if (syncver != null) {
            stmt.bindLong(32, syncver);
        }
 
        Long idonserver = entity.getIdonserver();
        if (idonserver != null) {
            stmt.bindLong(33, idonserver);
        }
 
        Boolean syncing = entity.getSyncing();
        if (syncing != null) {
            stmt.bindLong(34, syncing ? 1L: 0L);
        }
 
        Boolean deleteflag = entity.getDeleteflag();
        if (deleteflag != null) {
            stmt.bindLong(35, deleteflag ? 1L: 0L);
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
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // supplier
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // supplyaddress
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // dealtime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // supplytime
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // breed
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // sex
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // agewhendeal
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // agewhenkill
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // feedpattern
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // forage
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // feedstatus
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // healthstatus
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // breedstatus
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // killdepartment
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // killplace
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // killtime
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // freshkeepmethod
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // freshkeeptime
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // splitstatus
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // processstatus
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // qualitystatus
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // QC
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // QA
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // furquality
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // reserved
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // factorytime
            cursor.isNull(offset + 30) ? null : cursor.getLong(offset + 30), // version
            cursor.isNull(offset + 31) ? null : cursor.getLong(offset + 31), // syncver
            cursor.isNull(offset + 32) ? null : cursor.getLong(offset + 32), // idonserver
            cursor.isNull(offset + 33) ? null : cursor.getShort(offset + 33) != 0, // syncing
            cursor.isNull(offset + 34) ? null : cursor.getShort(offset + 34) != 0 // deleteflag
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
        entity.setSupplier(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSupplyaddress(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDealtime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSupplytime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBreed(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSex(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAgewhendeal(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setAgewhenkill(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFeedpattern(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setForage(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setFeedstatus(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setHealthstatus(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setBreedstatus(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setKilldepartment(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setKillplace(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setKilltime(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setFreshkeepmethod(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setFreshkeeptime(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setSplitstatus(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setProcessstatus(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setQualitystatus(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setQC(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setQA(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setFurquality(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setReserved(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setFactorytime(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setVersion(cursor.isNull(offset + 30) ? null : cursor.getLong(offset + 30));
        entity.setSyncver(cursor.isNull(offset + 31) ? null : cursor.getLong(offset + 31));
        entity.setIdonserver(cursor.isNull(offset + 32) ? null : cursor.getLong(offset + 32));
        entity.setSyncing(cursor.isNull(offset + 33) ? null : cursor.getShort(offset + 33) != 0);
        entity.setDeleteflag(cursor.isNull(offset + 34) ? null : cursor.getShort(offset + 34) != 0);
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
