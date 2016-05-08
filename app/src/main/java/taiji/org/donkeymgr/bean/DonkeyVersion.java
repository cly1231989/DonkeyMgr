package taiji.org.donkeymgr.bean;

public class DonkeyVersion {
	public DonkeyVersion() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DonkeyVersion(Long idOnServer, Integer sn, Long version, Long versync, boolean isDelete) {
		super();
		this.idOnServer = idOnServer;
		this.sn = sn;
		this.version = version;
		this.versync= versync;
		this.isDelete = isDelete;
	}

	private Integer sn;
	private Long idOnServer;
	private Long version;
	private Long versync;
	private boolean isDelete;

	public boolean isDelete() {return isDelete;}
	public void setIsDelete(boolean isDelete) {this.isDelete = isDelete;}

	public Long getVersync() {return versync;}
	public void setVersync(Long versync) {this.versync = versync;}

	public Long getIdOnServer() {
		return idOnServer;
	}
	public void setIdOnServer(Long idOnServer) {
		this.idOnServer = idOnServer;
	}

	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}

	public boolean isNewRecord(){return (idOnServer == 0L);};
	public boolean isNotSyncRecord(){return (version > versync);};
}
