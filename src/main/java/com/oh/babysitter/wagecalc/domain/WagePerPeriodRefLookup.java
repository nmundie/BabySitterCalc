/*
 Put header here
 */
package com.oh.babysitter.wagecalc.domain;

import java.time.LocalDateTime;

public class WagePerPeriodRefLookup {

	public WagePerPeriodRefLookup(LocalDateTime wagePeriodStartTime, LocalDateTime wagePeriodEndTime, Double hourlyWage) {
		this.wagePeriodStartTime = wagePeriodStartTime;
		this.wagePeriodEndTime = wagePeriodEndTime;
		this.hourlyWage = hourlyWage;
	}

	private LocalDateTime wagePeriodStartTime;
	private LocalDateTime wagePeriodEndTime;
	private Double hourlyWage;
	
	public LocalDateTime getWagePeriodStartTime() {
		return wagePeriodStartTime;
	}
	public void setWagePeriodStartTime(LocalDateTime wagePeriodStartTime) {
		this.wagePeriodStartTime = wagePeriodStartTime;
	}
	public LocalDateTime getWagePeriodEndTime() {
		return wagePeriodEndTime;
	}
	public void setWagePeriodEndTime(LocalDateTime wagePeriodEndTime) {
		this.wagePeriodEndTime = wagePeriodEndTime;
	}
	public Double getHourlyWage() {
		return hourlyWage;
	}
	public void setHourlyWage(Double hourlyWage) {
		this.hourlyWage = hourlyWage;
	}

}
