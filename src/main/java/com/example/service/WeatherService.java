package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


public class WeatherService {
	
	
	private static HttpURLConnection connection;
	
	private static String cityName;
	private static String unit;
	private static String pin;
	double temp;
	String pressure;
	String description;

	public static String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public static String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public static String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}

	


	static JSONObject getWeather() {
		BufferedReader reader;
		String st="https://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+"&appid=<your api token>";
		String line;
		StringBuffer rc=new StringBuffer();
		
		try {
			URL url= new URL(st);
			connection=(HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status=connection.getResponseCode();
			if(status>299) {
				reader=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while((line=reader.readLine())!=null) {
					rc.append(line);
				}
				reader.close();
				
			}
			else {
				reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line=reader.readLine())!=null) {
					rc.append(line);
				}
				reader.close();
			}
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		 JSONObject j=new JSONObject(rc.toString());
		 return j;
	}
	
	
	public JSONArray returnWeatherArray() {
		JSONArray weatherArray=getWeather().getJSONArray("weather");
		return weatherArray;
		
	}
	
	
	public JSONObject returnMain() {
		JSONObject m=getWeather().getJSONObject("main");
		temp=m.getDouble("temp");
		pressure=Integer.toString(m.getInt("pressure"));
		return m;
		
	}
	
	public JSONObject returnWind() {
		JSONObject wind=getWeather().getJSONObject("wind");
		return wind;
		
	}
	
	public JSONObject returnSys() {
		JSONObject sys=getWeather().getJSONObject("sys");
		return sys;
		
	}
	
	
	
	
	
	
	
}
