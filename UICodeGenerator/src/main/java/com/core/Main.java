package com.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Main {

	public static void main(String[] args) {
		String urlStr = "http://google.com";
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		WebDriver driver = new ChromeDriver();
		WebDriver driver2 = new RemoteWebDriver(url, null, null);
		driver.get(urlStr);
		driver2.get(urlStr);
	}
	
	

}
