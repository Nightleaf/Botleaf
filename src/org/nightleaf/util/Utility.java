package org.nightleaf.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String getTimeStamp() {
	// SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a z");
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
	Date now = new Date();
	return sdf.format(now);
    }

}
