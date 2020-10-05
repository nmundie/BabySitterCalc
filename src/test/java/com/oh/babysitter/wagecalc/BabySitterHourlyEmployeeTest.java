package com.oh.babysitter.wagecalc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.RuntimeException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class BabySitterHourlyEmployeeTest
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
	
	@Test
    public void testJobEndDateTimeBeforeJobStartDateTimeTime()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T19:00");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T16:59");
		Exception exception = assertThrows(RuntimeException.class,
				                          () -> new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime));
		assertEquals(exception.getMessage(), "Job Start Time is after job End Time");
    }

	@Test
    public void testJobTimeElapsedLessThan60Minutes()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T19:00");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T17:00");
		Exception exception = assertThrows(RuntimeException.class,
				                          () -> new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime));
		assertEquals(exception.getMessage(), "No Wages produced since period less than 60 minutes");
    }

	@Test
    public void testJobBedDateTimeIsBeforeJobStartDateTime()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T16:59");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T18:00");
		Exception exception = assertThrows(RuntimeException.class,
				                          () -> new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime));
		assertEquals(exception.getMessage(), "Bed Date/Time not in range of start and end times");
    }

	@Test
    public void testJobBedDateTimeIsAfterJobEndDateTime()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T18:01");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T18:00");
        //BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        //Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, startDateTime, endDateTime);
		Exception exception = assertThrows(RuntimeException.class,
				                          () -> new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime));
		assertEquals(exception.getMessage(), "Bed Date/Time not in range of start and end times");
    }

	@Test
    public void testJobStartDateTimeAndJobStartEndTimesAdjusted()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T18:00");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T16:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-04T18:00");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        assertEquals(babySitterHourlyEmployee.getEmpStartDateTime(), LocalDateTime.parse("2020-10-03T17:00"));
        assertEquals(babySitterHourlyEmployee.getEmpEndDateTime(), LocalDateTime.parse("2020-10-04T04:00"));
    }

	@Test
    public void testCalcWagesSpanningEntireDay()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T22:30");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-04T18:00");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, babySitterHourlyEmployee.getEmpStartDateTime(), babySitterHourlyEmployee.getEmpEndDateTime());
		assertEquals(String.format("%10.2f", dailyWages), String.format("%10.2f", 125.87));
    }

	@Test
    public void testCalcWagesSpanningEntireDayLastHourNotComplete()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T22:30");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-04T03:59");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, babySitterHourlyEmployee.getEmpStartDateTime(), babySitterHourlyEmployee.getEmpEndDateTime());
		assertEquals(String.format("%10.2f", dailyWages), String.format("%10.2f", 125.87));
    }

	@Test
    public void testCalcWagesSpanningEntireDayJobStartTimeNotOnTheHour()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T22:30");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:15");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-04T04:00");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, babySitterHourlyEmployee.getEmpStartDateTime(), babySitterHourlyEmployee.getEmpEndDateTime());
		assertEquals(String.format("%10.2f", dailyWages), String.format("%10.2f", 122.87));
    }

	@Test
    public void testCalcWagesJobStartTimeAndJobEndTimeNotOnTheHour()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T22:30");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:15");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-04T03:30");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, babySitterHourlyEmployee.getEmpStartDateTime(), babySitterHourlyEmployee.getEmpEndDateTime());
		assertEquals(String.format("%10.2f", dailyWages), String.format("%10.2f", 122.87));
    }

	@Test
    public void testCalcWagesOnlyUntilBedTime()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T22:30");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T22:30");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, babySitterHourlyEmployee.getEmpStartDateTime(), babySitterHourlyEmployee.getEmpEndDateTime());
		assertEquals(String.format("%10.2f", dailyWages), String.format("%10.2f", 60.0));
    }

	@Test
    public void testCalcWagesOnlyUntilBedTimePlusPartialHour()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T22:30");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T22:45");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, babySitterHourlyEmployee.getEmpStartDateTime(), babySitterHourlyEmployee.getEmpEndDateTime());
		assertEquals(String.format("%10.2f", dailyWages), String.format("%10.2f", 61.87));
    }

	@Test
    public void testCalcWagesOnlyUntilBedTimePlusHourOrMore()
    {
        LocalDateTime bedDateTime = LocalDateTime.parse("2020-10-03T22:30");
        LocalDateTime startDateTime = LocalDateTime.parse("2020-10-03T17:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2020-10-03T23:01");
        BabySitterHourlyEmployee babySitterHourlyEmployee = new BabySitterHourlyEmployee(bedDateTime, startDateTime, endDateTime);
        Double dailyWages = babySitterHourlyEmployee.calcDayWages(bedDateTime, babySitterHourlyEmployee.getEmpStartDateTime(), babySitterHourlyEmployee.getEmpEndDateTime());
		assertEquals(String.format("%10.2f", dailyWages), String.format("%10.2f", 70.0));
    }

}
