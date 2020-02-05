package com.enrichAi.server;

public class Data {

	private String latitude;
	private String longitude;
	private String deviceId;
	private String speed;
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	@Override
	public String toString() {
		return "{latitude"+":" + latitude + ", longitude"+":" + longitude + ", deviceId"+":" + deviceId + ", speed"+":" + speed
				+ "}";
	}
	
}
