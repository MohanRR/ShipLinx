package com.meritconinc.shiplinx.model;

public class Service {

	private Long id;
	private String name;
	private Carrier carrier;
	private String description;
	private String code;
	private String transitCode;
	private Integer serviceTimeMins;
	private Long carrierId;
	private Long masterCarrierId;
	private Integer mode;
	private Carrier masterCarrier;
	private int serviceType;
	private Long zoneStructureId;
	
	//web only
	private long businessId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Carrier getCarrier() {
		return carrier;
	}
	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getServiceTimeMins() {
		return serviceTimeMins;
	}
	public void setServiceTimeMins(Integer serviceTimeMins) {
		this.serviceTimeMins = serviceTimeMins;
	}
	public Long getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public String getTransitCode() {
		return transitCode;
	}
	public void setTransitCode(String transitCode) {
		this.transitCode = transitCode;
	}
	
	public long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}
	public Carrier getMasterCarrier() {
		return masterCarrier;
	}
	public void setMasterCarrier(Carrier masterCarrier) {
		this.masterCarrier = masterCarrier;
	}
	public Long getMasterCarrierId() {
		return masterCarrierId;
	}
	public void setMasterCarrierId(Long masterCarrierId) {
		this.masterCarrierId = masterCarrierId;
	}
	public int getServiceType() {
		return serviceType;
	}
	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	public Long getZoneStructureId() {
		return zoneStructureId;
	}
	public void setZoneStructureId(Long zoneStructureId) {
		this.zoneStructureId = zoneStructureId;
	}
}
