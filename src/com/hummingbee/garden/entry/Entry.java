package com.hummingbee.garden.entry;

import com.hummingbee.garden.common.DBHelper;
import com.hummingbee.garden.view.main.GardenSplashScreen;

public class Entry {
	public static void main(String[] args) {
		if(args.length != 3){
			System.out.println("Usage: java -jar garden.jar <db_user> <db_password> <db_port>");
			System.exit(0);
		}
		DBHelper.setDbUser(args[0]);
		DBHelper.setDbPasswd(args[1]);
		DBHelper.setDbPort(args[2]);
		if(!DBHelper.initDatabase()){
			System.out.println("Cannot connect to the database");
			System.exit(0);
		}
	    new GardenSplashScreen();
	}
}
