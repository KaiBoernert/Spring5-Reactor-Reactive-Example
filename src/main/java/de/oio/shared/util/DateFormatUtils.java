package de.oio.shared.util;

import java.text.SimpleDateFormat;

public class DateFormatUtils {

	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}
}
