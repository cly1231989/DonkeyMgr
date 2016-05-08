package taiji.org.donkeymgr.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DONKEY".
 */
public class Donkey {

    private Long id;
    private int sn;
    private String farmer;
    private String breedaddress;
    private String dealtime;
    private String breed;
    private String sex;
    private String agewhendeal;
    private String agewhenkill;
    private String feedstatus;
    private String healthstatus;
    private String breedstatus;
    private String killdepartment;
    private String killplace;
    private String killtime;
    private String splitstatus;
    private String processstatus;
    private String qualitystatyus;
    private String factorytime;
    private Long version;
    private Long syncver;
    private Long idonserver;
    private Boolean syncing;
    private Boolean deleteflag;

    public Donkey() {
    }

    public boolean hasSync(){
        return (syncver == version);
    }

    public Donkey(Long id) {
        this.id = id;
    }

    public Donkey(Long id, int sn, String farmer, String breedaddress, String dealtime, String breed, String sex, String agewhendeal, String agewhenkill, String feedstatus, String healthstatus, String breedstatus, String killdepartment, String killplace, String killtime, String splitstatus, String processstatus, String qualitystatyus, String factorytime, Long version, Long syncver, Long idonserver, Boolean syncing, Boolean deleteflag) {
        this.id = id;
        this.sn = sn;
        this.farmer = farmer;
        this.breedaddress = breedaddress;
        this.dealtime = dealtime;
        this.breed = breed;
        this.sex = sex;
        this.agewhendeal = agewhendeal;
        this.agewhenkill = agewhenkill;
        this.feedstatus = feedstatus;
        this.healthstatus = healthstatus;
        this.breedstatus = breedstatus;
        this.killdepartment = killdepartment;
        this.killplace = killplace;
        this.killtime = killtime;
        this.splitstatus = splitstatus;
        this.processstatus = processstatus;
        this.qualitystatyus = qualitystatyus;
        this.factorytime = factorytime;
        this.version = version;
        this.syncver = syncver;
        this.idonserver = idonserver;
        this.syncing = syncing;
        this.deleteflag = deleteflag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getFarmer() {
        return farmer;
    }

    public void setFarmer(String farmer) {
        this.farmer = farmer;
    }

    public String getBreedaddress() {
        return breedaddress;
    }

    public void setBreedaddress(String breedaddress) {
        this.breedaddress = breedaddress;
    }

    public String getDealtime() {
        return dealtime;
    }

    public void setDealtime(String dealtime) {
        this.dealtime = dealtime;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAgewhendeal() {
        return agewhendeal;
    }

    public void setAgewhendeal(String agewhendeal) {
        this.agewhendeal = agewhendeal;
    }

    public String getAgewhenkill() {
        return agewhenkill;
    }

    public void setAgewhenkill(String agewhenkill) {
        this.agewhenkill = agewhenkill;
    }

    public String getFeedstatus() {
        return feedstatus;
    }

    public void setFeedstatus(String feedstatus) {
        this.feedstatus = feedstatus;
    }

    public String getHealthstatus() {
        return healthstatus;
    }

    public void setHealthstatus(String healthstatus) {
        this.healthstatus = healthstatus;
    }

    public String getBreedstatus() {
        return breedstatus;
    }

    public void setBreedstatus(String breedstatus) {
        this.breedstatus = breedstatus;
    }

    public String getKilldepartment() {
        return killdepartment;
    }

    public void setKilldepartment(String killdepartment) {
        this.killdepartment = killdepartment;
    }

    public String getKillplace() {
        return killplace;
    }

    public void setKillplace(String killplace) {
        this.killplace = killplace;
    }

    public String getKilltime() {
        return killtime;
    }

    public void setKilltime(String killtime) {
        this.killtime = killtime;
    }

    public String getSplitstatus() {
        return splitstatus;
    }

    public void setSplitstatus(String splitstatus) {
        this.splitstatus = splitstatus;
    }

    public String getProcessstatus() {
        return processstatus;
    }

    public void setProcessstatus(String processstatus) {
        this.processstatus = processstatus;
    }

    public String getQualitystatyus() {
        return qualitystatyus;
    }

    public void setQualitystatyus(String qualitystatyus) {
        this.qualitystatyus = qualitystatyus;
    }

    public String getFactorytime() {
        return factorytime;
    }

    public void setFactorytime(String factorytime) {
        this.factorytime = factorytime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getSyncver() {
        return syncver;
    }

    public void setSyncver(Long syncver) {
        this.syncver = syncver;
    }

    public Long getIdonserver() {
        return idonserver;
    }

    public void setIdonserver(Long idonserver) {
        this.idonserver = idonserver;
    }

    public Boolean getSyncing() {
        return syncing;
    }

    public void setSyncing(Boolean syncing) {
        this.syncing = syncing;
    }

    public Boolean getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(Boolean deleteflag) {
        this.deleteflag = deleteflag;
    }

}
