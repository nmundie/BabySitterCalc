/*
 Put header here
 */
package com.oh.babysitter.wagecalc;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class HourlyEmployee {

	public HourlyEmployee(LocalDateTime empStartDateTime, LocalDateTime empEndDateTime) {
		this.empStartDateTime = empStartDateTime;
		this.empEndDateTime = empEndDateTime;
		adjustStartAndEndTimes();
		validateStartandEndJobTimes();
	}

	protected LocalDateTime empStartDateTime;
	protected LocalDateTime empEndDateTime;

	private void validateStartandEndJobTimes() {
		if (this.empStartDateTime.isAfter(this.empEndDateTime)) {
			throw new RuntimeException("Job Start Time is after job End Time");
		}
		
		Duration duration = Duration.between(empStartDateTime, empEndDateTime);
		
		if ((duration.getSeconds()/Constants.SECONDS_PER_MIN) < Constants.MIN_PAY_PERIOD_MINUTES) {
			throw new RuntimeException("No Wages produced since period less than " + Constants.MIN_PAY_PERIOD_MINUTES + " minutes");
		}
	}

	private void adjustStartAndEndTimes() {
		// Retrieve Date only to construct full required start date time
		LocalDate empStartDate = this.empStartDateTime.toLocalDate();

		// Construct time component with required start time
		LocalTime requiredStartTime = LocalTime.parse(Constants.EARLIEST_START_TIME,
				DateTimeFormatter.ofPattern("hh:mm a", Locale.US));

		// Fully assemble required LocalDateTime field representing required start date
		// and time
		LocalDateTime requiredStartDateTime = LocalDateTime.of(empStartDate, requiredStartTime);

		// Adjust employee start date and time to required minimum if original start date time is before
		// required earliest start date and time
		if (this.empStartDateTime.isBefore(requiredStartDateTime)) {
			this.empStartDateTime = requiredStartDateTime;
		}

		// Retrieve Date only to construct full required end date time
		LocalDate empEndDate = this.empEndDateTime.toLocalDate();

		// Construct time component with required start time
		LocalTime requiredEndTime = LocalTime.parse(Constants.LATEST_END_TIME,
				DateTimeFormatter.ofPattern("hh:mm a", Locale.US));

		// Fully assemble required LocalDateTime field representing required end date
		// and time
		
		LocalDateTime requiredEndDateTime = null;
		
		if (empStartDate.atStartOfDay().equals(empEndDate.atStartOfDay())) {
			requiredEndDateTime = LocalDateTime.of(empEndDate.plusDays(1), requiredEndTime);
		} else {			
			requiredEndDateTime = LocalDateTime.of(empEndDate, requiredEndTime);
		}


		// Adjust employee end date and time to required maximum end date and time if original end date time is after
		// required latest end date and time
		if (this.empEndDateTime.isAfter(requiredEndDateTime)) {
			this.empEndDateTime = requiredEndDateTime;
		}
	}

	private void showEmployeeStartAndEndTimes() {
		System.out.printf("Employee start date/time: {} end date/time: {}", this.empStartDateTime, this.empEndDateTime);
	}

	public LocalDateTime getEmpStartDateTime() {
		return empStartDateTime;
	}

	public LocalDateTime getEmpEndDateTime() {
		return empEndDateTime;
	}

}
