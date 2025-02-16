package com.pms.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MessageLog {
	static Logger l = Logger.getLogger(MessageLog.class.getName());
	static {
		try {
			String sep = System.getProperty("file.separator");
			String dynamicLog = new FileUpload().filepath() + "logs"; // log directory chosen...
			System.out.println("dynamic log:" + dynamicLog);
			l.error("dynamic log:" + dynamicLog);
			Properties p = new Properties();
			InputStream in = MessageLog.class.getResourceAsStream("/log4j.properties");
			p.load(in);
			p.put("log.dir", dynamicLog); // overwrite "log.dir"
			PropertyConfigurator.configure(p);
			in.close();
			p.clear();
			p.clone();
		} catch (Exception e) {
			MessageLog.printError(e);
			e.printStackTrace();
		}
	}

	public static void debug(String msg) {
		l.debug(msg);
	}

	public static void info(String msg) {
		l.info(msg);
	}

	public static void warn(String msg) {
		l.warn(msg);
	}

	public static void error(String msg) {
		l.error(msg);
	}

	public static void fatal(String msg) {
		l.fatal(msg);
	}

	public static void printError(Exception printStackTrace) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace.printStackTrace(pw);
		String sStackTrace = sw.toString();
		String s = sStackTrace;
		error(s);
	}

	public static void printError(Throwable printStackTrace) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace.printStackTrace(pw);
		String sStackTrace = sw.toString();
		String s = sStackTrace;
		error(s);
	}
}
