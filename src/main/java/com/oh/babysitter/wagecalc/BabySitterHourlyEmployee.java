/*
 Put header here
 */
package com.oh.babysitter.wagecalc;

import java.lang.RuntimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.oh.babysitter.wagecalc.utils.WageUtils;

public class BabySitterHourlyEmployee extends HourlyEmployee {
	
    public static void main( String[] args )
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T19:00");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T19:01");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        System.out.println(babySitterHourlyEmployee.calcDayWages(bedDateTime, startDateTime, endDateTime) + " dollars today");
    }

	public BabySitterHourlyEmployee(LocalDateTime bedDateTime, 
			                        LocalDateTime empStartDateTime, 
			                        LocalDateTime empEndDateTime) {
		super(empStartDateTime, empEndDateTime);
		this.bedDateTime = bedDateTime;
		validateBedTime();
	}

	private LocalDateTime bedDateTime;

	public Double calcDayWages(LocalDateTime bedDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		WageUtils wageUtils = new WageUtils(bedDateTime, startDateTime, endDateTime);
		wageUtils.buildWageTable();
		return wageUtils.CalculateDailyWage();
	}

	private void validateBedTime() {
		if (bedDateTime.isBefore(this.empStartDateTime) || bedDateTime.isAfter(this.empEndDateTime)) {
			throw new RuntimeException("Bed Date/Time not in range of start and end times");
		}
	}
	
	public void showEmployeeStartAndEndTimes() {
		System.out.printf("Employee start date/time: {} end date/time: {}", this.empStartDateTime, this.empEndDateTime);
	}

}