/*
 Put header here
 */
package com.oh.babysitter.wagecalc.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.oh.babysitter.wagecalc.Constants;
import com.oh.babysitter.wagecalc.domain.*;

public class WageUtils {

	private LocalDateTime bedDateTime;
	private LocalDateTime jobStartDateTime;
	private LocalDateTime jobEndDateTime;
	List<WagePerPeriodRefLookup> wafePerPeriodRefLookupList;
	private Double dailyWages;

    public WageUtils(LocalDateTime bedDateTime, LocalDateTime jobStartDateTime, LocalDateTime jobEndDateTime) {
		this.bedDateTime = bedDateTime;
		this.jobStartDateTime = jobStartDateTime;
		this.jobEndDateTime = jobEndDateTime;
		this.wafePerPeriodRefLookupList = new ArrayList<>();
		this.dailyWages = 0.0;
	}

	public void buildWageTable() {

		// Create midnight date variable is actually 23:59:59 of current day

		LocalDateTime midnight = jobStartDateTime.toLocalDate().atTime(LocalTime.MAX);

		// Load wage table

		// Process wages by each hour worked

		if (!jobEndDateTime.isBefore(bedDateTime)) {
			wafePerPeriodRefLookupList.add(
					new WagePerPeriodRefLookup(jobStartDateTime, bedDateTime, Constants.START_TIME_THRU_BEDTIME_WAGE));
		} else {
			wafePerPeriodRefLookupList.add(new WagePerPeriodRefLookup(jobStartDateTime, jobEndDateTime,
					Constants.START_TIME_THRU_BEDTIME_WAGE));
			// create no more wage records since we have exhausted total time worked
			return;
		}

		if (!jobEndDateTime.isBefore(bedDateTime.plusMinutes(1))) {
			if (jobEndDateTime.isBefore(midnight)) {
			    wafePerPeriodRefLookupList.add(new WagePerPeriodRefLookup(bedDateTime.plusMinutes(1), jobEndDateTime,
						Constants.BED_TIME_THRU_MIDNIGHT_WAGE));
			} else {
			    wafePerPeriodRefLookupList.add(new WagePerPeriodRefLookup(bedDateTime.plusMinutes(1), midnight.plusNanos(1),
					Constants.BED_TIME_THRU_MIDNIGHT_WAGE));
			}
		} 

		if (!jobEndDateTime.isBefore(midnight.plusNanos(1).plusMinutes(1))) {
			wafePerPeriodRefLookupList.add(new WagePerPeriodRefLookup(midnight.plusNanos(1).plusMinutes(1), jobEndDateTime,
					Constants.MIDNIGHT_THRU_END_TIME_WAGE));
		}

	}

	public Double CalculateDailyWage() {
		
		int minCounter = 0;	
		
		for (; jobStartDateTime.plusMinutes(minCounter).isBefore(jobEndDateTime); ) {

			for (WagePerPeriodRefLookup wagePerPeriodRefLookup: wafePerPeriodRefLookupList) {
							
	    		Duration duration = Duration.between(wagePerPeriodRefLookup.getWagePeriodStartTime(), wagePerPeriodRefLookup.getWagePeriodEndTime());

	    		Long minutesWorkedThisPeriod = duration.getSeconds() / Constants.SECONDS_PER_MIN;
	    		
	    		Long remainingMinsWorkedThisPeriod = minutesWorkedThisPeriod % Constants.MINUTES_PER_HOUR;
	    		
	    		Long minutesToNextFullHour = 0L;
	    		
	    		if (remainingMinsWorkedThisPeriod != 0) {
		    		minutesToNextFullHour = Constants.MINUTES_PER_HOUR - remainingMinsWorkedThisPeriod;	    			
	    		}
	    		
	    		// Calculate wage per minute
	    		
	    		Double wagePerMin = wagePerPeriodRefLookup.getHourlyWage() / Constants.MINUTES_PER_HOUR;
	    		
	    		// Calculate wages for full hours worked this period and add total
	    		
	    		Long minsToCalc = 0L;
	    		
	    		if ((minutesWorkedThisPeriod - remainingMinsWorkedThisPeriod) == 0) {
	    			minsToCalc = minutesWorkedThisPeriod;
	    		} else {
	    			minsToCalc = minutesWorkedThisPeriod - remainingMinsWorkedThisPeriod;
	    		}
	    		
	    		dailyWages += (wagePerMin * (Constants.MINUTES_PER_HOUR * minsToCalc / Constants.MINUTES_PER_HOUR.doubleValue()));
	    		
	    		if (!(jobStartDateTime.plusMinutes(minCounter+minutesWorkedThisPeriod+minutesToNextFullHour).isAfter(jobEndDateTime))) {
	    			
	    			// Calculate wages for remaining partial hour since remainder of hour is available as part of day
		    		dailyWages += wagePerMin * remainingMinsWorkedThisPeriod;
	    			
	    		}
	    		
	    		minCounter += (minutesWorkedThisPeriod + remainingMinsWorkedThisPeriod);
	    	
	        }

		}
		
		return dailyWages;
        
	}
    
}
