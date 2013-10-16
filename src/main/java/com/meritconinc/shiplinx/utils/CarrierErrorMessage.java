package com.meritconinc.shiplinx.utils;

public class CarrierErrorMessage {

	private long carrierId;
	private String message;
	
	public CarrierErrorMessage(long id, String msg){
		this.carrierId = id;
		this.message = msg;
	}
	
	public CarrierErrorMessage(String msg){
		this.message = msg;
	}

	/**
	 * @return Returns the carrierId.
	 */
	public long getCarrierId() {
		return carrierId;
	}
	/**
	 * @param carrierId The carrierId to set.
	 */
	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}