package it.unical.scalab.parsoda.common.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.json.JSONObject;

import it.unical.scalab.parsoda.common.Metadata;

public class DataUtils {
	
	public static List<String> splitDataByTime(Iterable<Text> items, String key, String splitType, int timeStep){
		
		LocalDateTime oldTimestamp = new LocalDateTime(0);
		LocalDateTime currTimestamp = null;
		String oldLocation = null;
		String currentLocation = null;
		JSONObject item = null;
		List<String> ret = new LinkedList<String>();
		List<String> s = new LinkedList<String>();
		for (Text value : items) {
			item = new JSONObject(value.toString());
			currTimestamp = new LocalDateTime(item.getJSONObject(Metadata.BASIC_PAYLOAD).get(Metadata.DATETIME));
			int interval = 0;
			switch (splitType) {
			case "day":
				interval = Days.daysBetween(oldTimestamp, currTimestamp).getDays();
				break;
			case "week":
				interval = Weeks.weeksBetween(oldTimestamp, currTimestamp).getWeeks();
				break;
			case "year":
				interval = Years.yearsBetween(oldTimestamp, currTimestamp).getYears();
				break;				
			case "hour":
				interval = Hours.hoursBetween(oldTimestamp, currTimestamp).getHours();
				break;
			case "minute":
				interval = Minutes.minutesBetween(oldTimestamp, currTimestamp).getMinutes();
				break;	
			case "second":
				interval = Seconds.secondsBetween(oldTimestamp, currTimestamp).getSeconds();
				break;	
			default:
				break;
			}
			
			if (interval > timeStep) {
				if (s.size() > 0) {
					ret.add(String.join(" ", s));
				}
				s.clear();
				oldLocation = null;
				oldTimestamp = currTimestamp;
			}
			
			currentLocation = item.getString(Metadata.LOCATION_NAME).replace(" ", "_").replace("'", "");
			if (!currentLocation.equals(oldLocation)) {
				s.add(currentLocation);
				oldLocation = currentLocation;
			}
		}
		if (s.size()>0) {
			ret.add(String.join(" ", s));
		}
		return ret;
	}
	
	public static Set<String> splitDataByTimeSet(Iterable<Text> items, String key, String splitType, int timeStep){
		
		LocalDateTime oldTimestamp = new LocalDateTime(0);
		LocalDateTime currTimestamp = null;
		String oldLocation = null;
		String currentLocation = null;
		JSONObject item = null;
		Set<String> ret = new HashSet<String>();
		List<String> s = new LinkedList<String>();
		for (Text value : items) {
			item = new JSONObject(value.toString());
			currTimestamp = new LocalDateTime(item.getJSONObject(Metadata.BASIC_PAYLOAD).get(Metadata.DATETIME));
			int interval = 0;
			switch (splitType) {
			case "day":
				interval = Days.daysBetween(oldTimestamp, currTimestamp).getDays();
				break;
			case "week":
				interval = Weeks.weeksBetween(oldTimestamp, currTimestamp).getWeeks();
				break;
			case "year":
				interval = Years.yearsBetween(oldTimestamp, currTimestamp).getYears();
				break;				
			case "hour":
				interval = Hours.hoursBetween(oldTimestamp, currTimestamp).getHours();
				break;
			case "minute":
				interval = Minutes.minutesBetween(oldTimestamp, currTimestamp).getMinutes();
				break;	
			case "second":
				interval = Seconds.secondsBetween(oldTimestamp, currTimestamp).getSeconds();
				break;	
			default:
				break;
			}
			
			if (interval > timeStep) {
				if (s.size() > 0) {
					ret.add(String.join(" ", s));
				}
				s.clear();
				oldLocation = null;
				oldTimestamp = currTimestamp;
			}
			
			currentLocation = item.getString(Metadata.LOCATION_NAME).replace(" ", "_").replace("'", "");
			if (!currentLocation.equals(oldLocation)) {
				s.add(currentLocation);
				oldLocation = currentLocation;
			}
		}
		if (s.size()>0) {
			ret.add(String.join(" ", s));
		}
		return ret;
	}
}
