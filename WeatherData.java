/*  WeatherData -- Provides you with easy-to-read weather data!
*   Publish Date: January 25, 2018
*   Last Update: January 25, 2018
*	Version: 1.0
*	Notes: Initial release.  Pulls weather data from National Weather Service.  Uses airport codes to provide weather data.  
*/

package src.main.java;

import org.json.*;
import java.io.*;
import com.jayway.jsonpath.JsonPath;
import org.jsoup.*;

public class WeatherData{

	final static String DefaultWeatherURL = "http://w1.weather.gov/xml/current_obs/KDTW.rss"; // Weather URL is set to Coleman A. Young (DET) Airport.
	String BlankWeatherURL = "http://w1.weather.gov/xml/current_obs/";
	String UserDefinedLocation = "";
	final static String BlankWeatherURLTail = ".rss";
	String WeatherURLRawData; // This is the saved location of the raw data pulled in from the DTW WEATHER URL.
 
	String CurrentConditions;
	String Location;
	String RefreshDate;
	String AircraftConditions;
	
	public WeatherData(){
		GetRawWeatherData();
		ParseJSONtoSeparateString();
	}
	
	public WeatherData(String UserSpecifiedLocation){
		this.UserDefinedLocation = UserSpecifiedLocation.toUpperCase();
		if((this.UserDefinedLocation.length() > 4) || (this.UserDefinedLocation.length() < 3)){
			SetErrorResults();
		} else if (this.UserDefinedLocation.length() == 3){
			this.UserDefinedLocation = "K" + this.UserDefinedLocation;
			BlankWeatherURL = BlankWeatherURL + UserDefinedLocation + BlankWeatherURLTail;
			GetRawWeatherData();
			ParseJSONtoSeparateString();
		}else {
			BlankWeatherURL = BlankWeatherURL + UserDefinedLocation + BlankWeatherURLTail;
			GetRawWeatherData();
			ParseJSONtoSeparateString();
		}
	}
	
	private void GetRawWeatherData(){
		try{
			if(UserDefinedLocation.equals("")){
				this.WeatherURLRawData = Jsoup.connect(DefaultWeatherURL).get().html(); // This is the XML page pull. 
			} else{
				this.WeatherURLRawData = Jsoup.connect(BlankWeatherURL).get().html();
			}
			ParseXMLtoJSON();
		}catch(IOException e){ // If the WeatherURLRawData pull goes poorly, this will be triggered, and will throw an error below.  
			//System.err.println(e);
			SetErrorResults();
		}
	}
	
	private void SetErrorResults(){ // This will fill in the following data, if the user attempts to put in a non-existent airport.  
		this.CurrentConditions = "N/A";
		this.Location = "N/A";
		this.RefreshDate = "N/A";
		this.AircraftConditions = "N/A";
	}

	private void ParseXMLtoJSON(){
		int IndentAmount = 4; // Amount of indent used in the RAW_DATA, for easier reading. 
		try {
            JSONObject xmlJSONObj = XML.toJSONObject(this.WeatherURLRawData); //This is the main lifter, that convers the XML from RAW_DATA and converts it to JSON.
			this.WeatherURLRawData = xmlJSONObj.toString(IndentAmount); // This converts the JSON from the line above into a String, and places indents in it, and shoves it back to RAW_DATA.
			//System.out.println(WeatherURLRawData); // For debugging purposes; enable this to view the raw data from the NOAA feed.  
        } catch (Exception e){
            System.err.println(e.toString());
        }
	}

	private void ParseJSONtoSeparateString(){ // Using JSONPath, this pulls the specific data for each field.  
		this.CurrentConditions = JsonPath.read(this.WeatherURLRawData, "$.rss.channel.item.title");
		this.Location = JsonPath.read(this.WeatherURLRawData, "$.rss.channel.title");
		this.RefreshDate = JsonPath.read(this.WeatherURLRawData, "$.rss.channel.item.guid.content");
		this.AircraftConditions = JsonPath.read(this.WeatherURLRawData, "$.rss.channel.item.description");
		FormatAircraftConditions();
		FormatLocation();
		FormatConditions();
	}
	
	private void FormatAircraftConditions(){
		String[] StrippedConditions = this.AircraftConditions.split("<br /> "); //There is some random HTML code attached to the aircraft conditions string, this strips out the first portion.  
		StrippedConditions = StrippedConditions[1].split(" Last");
		this.AircraftConditions = StrippedConditions[0];
	}
	
	private void FormatConditions(){
		String[] CurrentConditionSplitter = this.CurrentConditions.split("at");
		this.CurrentConditions = CurrentConditionSplitter[0];
	}
	
	private void FormatLocation(){
		String[] LocationSplitter = this.Location.split(" - "); // Just like the other split functions above, splits the input phrase by a dash.
		this.Location = LocationSplitter[0];
	}
	
	public String GetCurrentConditions(){
		return this.CurrentConditions;
	}
	
	public String GetAircraftConditions(){
		return this.AircraftConditions;
	}
	
	public String GetRefreshDate(){
		return this.RefreshDate;
	}
	
	public String GetLocation(){
		return this.Location;
	}
	
	public void GetFormattedDisplay(){
		System.out.println("Location: "+ GetLocation());
		System.out.println("Current conditions: " + GetCurrentConditions());
		System.out.println("Aircraft condition: " + GetAircraftConditions());
		System.out.println("Refresh date: " + GetRefreshDate());
	}
	
}
