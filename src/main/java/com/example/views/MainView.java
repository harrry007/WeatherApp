package com.example.views;

import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.service.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.AbstractEmbedded;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
@SpringUI(path="")

public class MainView extends UI{
	
	private WeatherService ws=new WeatherService();
	VerticalLayout mainLayout;
	private NativeSelect<String> unitSelect;
	private TextField textField;
	private Button searchButton;
	private HorizontalLayout dashboard;
	private Label location;
	private Label currentTemp;
	private HorizontalLayout mainDescription;
	private Label weatherDescription;
	private Label maxWeather;
	private Label minWeather;
	private Label humidity;
	private Label pressure;
	private Label wind;
	private ExternalResource iconImg;
	VerticalLayout pressureLayout;
	VerticalLayout description;
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
		mainLayout();
		setHeader();
		setLogo();
		setForm();
		dashboard();
		dashboardDetails();
		
		searchButton.addClickListener(clickEvent->{
        	if(!textField.getValue().equals("")) {
        		try {
        			updateUI();
        			
        		}catch(JSONException e) {
        			e.printStackTrace();
        		}
        	}
        	else {
        		Notification.show("Please Enter city");
        	}
        });
	}
	
	private void mainLayout() {
		mainLayout=new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setSpacing(true);
		//mainLayout.setMargin(true);
		setContent(mainLayout);	
	}
	
	private void setHeader() {
		HorizontalLayout header=new HorizontalLayout();
		//header.setComponentAlignment(header, com.vaadin.ui.Alignment.MIDDLE_CENTER);
		Label title= new Label("Weather App");
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_BOLD);
		title.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(title);
		header.setComponentAlignment(title, com.vaadin.ui.Alignment.MIDDLE_CENTER);
		mainLayout.addComponent(header);
		mainLayout.setComponentAlignment(header, com.vaadin.ui.Alignment.MIDDLE_CENTER);
		
	}
	
	
	private void setLogo() {
		HorizontalLayout logo=new HorizontalLayout();
		
		Image image=new Image(null,new ClassResource("/static/t.png"));
		image.setWidth("150px");
		image.setHeight("150px");
		logo.addComponent(image);
		logo.setComponentAlignment(image, com.vaadin.ui.Alignment.MIDDLE_CENTER);
		mainLayout.addComponent(logo);
		mainLayout.setComponentAlignment(logo, com.vaadin.ui.Alignment.MIDDLE_CENTER);
	}
	
	private void setForm() {
		VerticalLayout vl=new VerticalLayout();
		Label p=new Label("Enter City");
		p.addStyleName(ValoTheme.LABEL_H2);
		p.addStyleName(ValoTheme.LABEL_BOLD);
		vl.addComponent(p);
		HorizontalLayout form= new HorizontalLayout();
		form.setSpacing(true);
		form.setMargin(true);
		
		unitSelect=new NativeSelect<>();
		ArrayList<String> items=new ArrayList<>();
		items.add("\u00b0"+"C");
		items.add("\u00b0"+"F");
		
		unitSelect.setItems(items);
		unitSelect.setValue(items.get(0));
		form.addComponent(unitSelect);
		
		
		textField=new TextField();
		textField.setWidth("200px");
		form.addComponent(textField);
		
		searchButton=new Button();
		searchButton.setIcon(VaadinIcons.SEARCH);
		form.addComponent(searchButton);
		
		mainLayout.addComponent(p);
		mainLayout.setComponentAlignment(p, com.vaadin.ui.Alignment.MIDDLE_CENTER);
		
		mainLayout.addComponent(form);
		mainLayout.setComponentAlignment(form, com.vaadin.ui.Alignment.MIDDLE_CENTER);
		
	}
	
	
	private void dashboard() {
		
		dashboard=new HorizontalLayout();
		location=new Label();
		location.addStyleName(ValoTheme.LABEL_H1);
		location.addStyleName(ValoTheme.LABEL_LIGHT);
		
		currentTemp=new Label();
		currentTemp.addStyleName(ValoTheme.LABEL_H1);
		currentTemp.addStyleName(ValoTheme.LABEL_LIGHT);
		
		dashboard.addComponent(location);
		dashboard.addComponent(currentTemp);
		
		mainLayout.addComponent(dashboard);
		mainLayout.setComponentAlignment(dashboard,com.vaadin.ui.Alignment.MIDDLE_CENTER);
		
	}
	
	private void dashboardDetails() {
		mainDescription=new HorizontalLayout();
		
		
		description=new VerticalLayout();
		
		weatherDescription=new Label();
		
		description.addComponent(weatherDescription);
		
		maxWeather=new Label();
		
		description.addComponent(maxWeather);
		
		minWeather=new Label();
		
		description.addComponent(minWeather);
		
		pressureLayout=new VerticalLayout();
		
		pressure=new Label();
		
		pressureLayout.addComponent(pressure);
		
	    humidity=new Label();
	    
	    pressureLayout.addComponent(humidity);
		
		wind=new Label();
		
	    pressureLayout.addComponent(wind);
	    
	    mainDescription.addComponent(description);
	    mainDescription.addComponent(pressureLayout);
	    
	    mainLayout.addComponent(mainDescription);
	    mainLayout.setComponentAlignment(mainDescription,com.vaadin.ui.Alignment.MIDDLE_CENTER);
	    
		
	}
	
	
	private void updateUI() {
		String city=textField.getValue();
		String unit;
		ws.setCityName(city);
		if(unitSelect.getValue().equals("F")) {
			ws.setUnit("imperial");
			unitSelect.setValue("F");
			unit="\u00b0"+"F";
		}
		else {
			ws.setUnit("metric");
			unitSelect.setValue("C");
			unit="\u00b0"+"C";
		}
		JSONObject mainObject=ws.returnMain();
		int temp=mainObject.getInt("temp");
		double c=temp;
		currentTemp.setValue(Double.toString(c)+unit);
		
		//getting icon
		String iconCode=null;
		String weather=null;
		JSONArray ja=ws.returnWeatherArray();
		for(int i=0;i<ja.length();i++) {
			JSONObject jo=ja.getJSONObject(i);
			iconCode=jo.getString("icon");
			weather=jo.getString("description");
			
		}
		//iconImg.setReource(new ExternalResource("https://openweathermap.org/img/wn/"+iconCode+"2x.png"));
		
		weatherDescription.setValue("Description : "+weather);
		minWeather.setValue("Min. Temperature : "+Double.toString(ws.returnMain().getDouble("temp_min"))+unit);
		maxWeather.setValue("Max. Temperature : "+Double.toString(ws.returnMain().getDouble("temp_max"))+unit);
		pressure.setValue("Pressure : "+ws.returnMain().getInt("pressure")+" hPa");
		humidity.setValue("Humidity : "+ws.returnMain().getInt("humidity")+"%");
		wind.setValue("Wind Speed : "+ws.returnWind().getInt("speed")+"m/s");
		
		
		location.setValue(city.toUpperCase()+" : ");
		
		
		
		
		
	}
	
	
}
