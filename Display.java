package src.main.java;

import src.main.java.WeatherData;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.*;
import javafx.stage.WindowEvent;
import javafx.scene.control.Button;

public class Display extends Application{
	
	Label AircraftConditions = new Label(); 
	Label WeatherConditions = new Label();
	Label CurrentLocation = new Label();
	Label LastUpdate = new Label();
	TextField tf1 = new TextField();
	
	WeatherData wd = new WeatherData();

	public HBox addHeader(){
		HBox header = new HBox();
		header.setSpacing(10);
		
		Button quitButton = new Button();
		quitButton.setText("Quit");
		quitButton.setOnAction(e -> {
			Platform.exit();
			System.exit(0);
		});
		
		Button refreshButton = new Button();
		refreshButton.setText("Refresh");
		refreshButton.setOnAction(e -> {
				String userInput = tf1.getText();
				WeatherData refreshClick = new WeatherData(userInput);
				WeatherConditions.setText("Current Weather Conditions: \n" + refreshClick.GetCurrentConditions());
				AircraftConditions.setText("Current Aircraft Weather Conditions: \n" + refreshClick.GetAircraftConditions()); 
				CurrentLocation.setText("Location: \n" + refreshClick.GetLocation());
				LastUpdate.setText("Last Update: " + refreshClick.GetRefreshDate());
		});
	
		header.getChildren().addAll(quitButton, refreshButton);
		return header;
	}
	
	public HBox addEntryBox(){
		HBox LocationBox = new HBox();
		LocationBox.setSpacing(5);

		tf1.setText("Provide Airport Code");
	
		Button LoadButton = new Button();
		LoadButton.setText("Enter");
		LoadButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent){
				String userInput = tf1.getText();
				WeatherData wd = new WeatherData(userInput);
				WeatherConditions.setText("Current Weather Conditions: \n" + wd.GetCurrentConditions());
				AircraftConditions.setText("Current Aircraft Weather Conditions: \n" + wd.GetAircraftConditions()); 
				CurrentLocation.setText("Location: \n" + wd.GetLocation());
				LastUpdate.setText("Last Update: " + wd.GetRefreshDate());
			}
		});
		
		LocationBox.getChildren().addAll(tf1, LoadButton);
		return LocationBox;
	}
	
	public VBox DataDisplay(){
		VBox WeatherData = new VBox();
		WeatherData.setSpacing(10);
		
		WeatherConditions.setText("Current Weather Conditions: \n" + wd.GetCurrentConditions()); 
		WeatherConditions.setWrapText(true);
		      
		AircraftConditions.setText("Current Aircraft Weather Conditions: \n" + wd.GetAircraftConditions());
		AircraftConditions.setWrapText(true);
		
		CurrentLocation.setText("Location: \n" + wd.GetLocation());
		CurrentLocation.setWrapText(true);		

		LastUpdate.setText("Last Update: " + wd.GetRefreshDate());

		WeatherData.getChildren().addAll(WeatherConditions, AircraftConditions, CurrentLocation, LastUpdate);
		return WeatherData;
	}
	
    @Override
    public void start(Stage stage) throws Exception {

		VBox root = new VBox();
		
		root.setSpacing(10);
		root.getChildren().add(addHeader());
		root.getChildren().add(addEntryBox());
	
		root.getChildren().add(DataDisplay());

		stage.setTitle("Weather Report");
		//stage.getIcons().add(new Image("https://www.shareicon.net/download/128x128//2017/01/24/876021_media_512x512.png")); // This was to set an application icon, but it is broken.
		stage.setScene(new Scene(root, 300, 280)); 
		stage.setResizable(false);
		stage.show(); 
    }
	
	
	public static void main(String[] args) {
        Application.launch(args);
    }
}