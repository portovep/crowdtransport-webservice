package com.coctelmental.server.helpers;

import java.util.List;

import com.coctelmental.server.dao.LineDAO;
import com.coctelmental.server.dao.LineDAOImpl;

public class LineHelper {
	
	private LineDAO lineDAO;

	public LineHelper() {
		lineDAO = new LineDAOImpl();
	}
	
	public String[] getLines(String targetCity) {
		List<String> linesAvailable = lineDAO.getLineNames(targetCity);
		String[] lines = new String[linesAvailable.size()];
		System.arraycopy(linesAvailable.toArray(), 0, lines, 0, linesAvailable.size());
		return lines;
	}
}
