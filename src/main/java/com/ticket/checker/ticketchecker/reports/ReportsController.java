package com.ticket.checker.ticketchecker.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.checker.ticketchecker.users.UserUtil;

@RestController
public class ReportsController {
	
	@Value("${reports.folder}")
	private String reportsFolderName;
	
	@Autowired
	private UserUtil userUtil;
	
	public static final String BASE_REPORT_FILE_NAME = "CrashLog";

	@PostMapping(path="/report")
	public void createReports(@RequestHeader("Authorization") String authorization, @RequestBody String crashReportJSON) {
		String path = reportsFolderName;
		if(reportsFolderName == null || reportsFolderName.equals("")) {
			path = "reports";
		}
		File reportsDirectory = new File(path);
		if(!reportsDirectory.exists()) {
			reportsDirectory.mkdirs();
		}
		
		String username = userUtil.getUsernameFromAuthorization(authorization);
	
		try {
			File reportFile = createReportFile(path, username);
			PrintWriter writer = new PrintWriter(reportFile);
			writer.println(crashReportJSON);
			writer.close();
			
		} catch (FileNotFoundException  e) {}
	}
	
	private static File createReportFile(String logsFolderPath, String username) {
		File logFile;
		Date currentDate = new Date();
		String baseFileName = BASE_REPORT_FILE_NAME + "-" + username + "-" + new SimpleDateFormat("yyyyMMdd").format(currentDate);
		int count=0;
		do {
			count++;
			String fileName = baseFileName + "_" + count;
			logFile = new File(logsFolderPath + "\\" + fileName + ".report");
		}while(logFile.isFile());
		return logFile;
	}
}
