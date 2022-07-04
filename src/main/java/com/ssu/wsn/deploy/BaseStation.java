package com.ssu.wsn.deploy;

public class BaseStation {
	double location_x, location_y;

	public BaseStation(){
		this.location_x = 50;
		this.location_y = 200;
	}
	public BaseStation(double location_x, double location_y){
		this.location_x = location_x;
		this.location_y = location_y;
	}
	public double getLocation_x() {
		return location_x;
	}

	public void setLocation_x(double location_x) {
		this.location_x = location_x;
	}

	public double getLocation_y() {
		return location_y;
	}

	public void setLocation_y(double location_y) {
		this.location_y = location_y;
	}
}
