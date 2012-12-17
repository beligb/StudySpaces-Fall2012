package edu.upenn.cis573.test;

import java.util.Date;

import junit.framework.TestCase;
import org.junit.Test;

import edu.upenn.cis573.SearchOptions;

public class SearchOptionsTest extends TestCase {

	SearchOptions obj = new SearchOptions();

	public void testGetStartDate() {
		obj.setStartHour(1);
		obj.setStartMinute(10);
		obj.setDay(1);
		obj.setMonth(2);
		obj.setYear(2012);

		Date startDate = obj.getStartDate();

		assertEquals(2, startDate.getMonth());
		assertEquals(1, startDate.getDate());

	}
	
	public void testGetEndDate(){
		obj.setDay(3);
		obj.setMonth(4);
		obj.setYear(2012);
		obj.setEndHour(3);
		obj.setEndMinute(20);
		
		Date endDate = obj.getEndDate();

		assertEquals(4, endDate.getMonth());
		assertEquals(3, endDate.getDate());
		
		
	}

}
