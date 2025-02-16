package com.pms.util;

import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

public class FileUpload {
	public static String filepath() {
		String os_Name = System.getProperty("os.name");
		String path = null;
		try {
			path = FileUpload.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			if (os_Name.contains("Windows") || os_Name.contains("Window"))
				path = path.substring(1, path.indexOf("WEB-INF")).replace("/", "\\");
			else
				path = path.substring(0, path.indexOf("WEB-INF"));
		} catch (URISyntaxException e) {
			// e.printStackTrace();
			MessageLog.printError(e);
		}
		return path;

	}
	
	public static JSONObject convertRequetToJson(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload fileUpload = new ServletFileUpload(factory);
				List<FileItem> items = fileUpload.parseRequest(request);
				if (items != null) {
					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						final FileItem item = iter.next();
						final String fieldName = item.getFieldName();
						final String fieldValue = item.getString();
						if (item.isFormField()) {
							json.put(fieldName, fieldValue);
						} else {
							json.put(fieldName, item);
						}
					}
				}
			} else {
				Enumeration paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String name = (String) paramNames.nextElement();
					String[] values = request.getParameterValues(name);
					if (values.length == 1) {
						String value = values[0].trim();
						json.put(name, value);
					} else {
						String trimmedValues[] = new String[values.length];
						for (int i = 0; i < values.length; i++)
							trimmedValues[i] = values[i].trim();
						json.put(name, trimmedValues);
					}
				}
			}
		} catch (Exception e) {
			MessageLog.printError(e);
		}
		return json;
	}
}
