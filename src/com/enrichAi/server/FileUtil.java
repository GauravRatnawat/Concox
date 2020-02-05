package com.enrichAi.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

public class FileUtil {
	public static String day, month, year, FileName1 = "";
	public static void datafilecreation(String StrResponse) 
	{
		try 
		{
			Calendar calendar = Calendar.getInstance();
			day = "" + calendar.get(5);
			month = "" + (calendar.get(2) + 1);
			year = "" + calendar.get(1);
			if (day.length() == 1) 
			{
				day = "0" + day;
			}
			if (month.length() == 1) 
			{
				month = "0" + month;
			}
			String currDate = day + month + year;
			File file1 = new File("/Data/" + currDate);
			if (!file1.exists()) 
			{
				file1.mkdirs();
			}
			FileName1 = "ServerData-" + month + day + ".json";
			FileOutputStream DebugFile = new FileOutputStream(file1.getAbsolutePath() + "/" + FileName1, true);

			PrintStream DebugDataStream = new PrintStream(DebugFile);
			if (!StrResponse.equals("")) 
			{
				DebugDataStream.println(StrResponse);
			}
			DebugDataStream.close();
			DebugFile.close();
		} 
		catch (IOException ie) 
		{
			ie.printStackTrace();
		}
	}
}
