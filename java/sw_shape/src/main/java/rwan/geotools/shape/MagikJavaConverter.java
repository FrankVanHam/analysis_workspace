package rwan.geotools.shape;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.gesmallworld.magik.interop.MagikInteropUtils;

public class MagikJavaConverter {

	static String fromMagikString(java.lang.Object obj) {
		if (obj != null) {
			return MagikInteropUtils.fromMagikString(obj);
		} else {
			return null;		
		}
	}
	
	static Object toMagikString(String str) {
		return MagikInteropUtils.toMagikString(str);
	}

	static Integer fromMagikInteger(java.lang.Object obj) {
		if (obj != null) {
			return MagikInteropUtils.fromMagikInteger(obj);
		} else {
			return null;
		}
	}
	
	static Object toMagikInteger(Integer it) {
		return MagikInteropUtils.toMagikInteger(it);
	}

	static Double fromMagikDouble(java.lang.Object obj) {
		if (obj != null) {
			return MagikInteropUtils.fromMagikDouble(obj);
		} else {
			return null;
		}
	}
	
	static Object toMagikDouble(Double dbl) {
		return MagikInteropUtils.toMagikDouble(dbl);
	}
	
	static Date fromMagikDate(java.lang.Object obj) {
		if (obj != null) {
			var str = fromMagikString(obj);
			try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(str);
			} catch (ParseException e) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	static Object toMagikDate(Date dt) {
		if (dt != null) {
			var str = new SimpleDateFormat("yyyy-MM-dd").format(dt);
			return toMagikString(str);
		} else {
			return null;
		}
	}
	
	static Boolean fromMagikBoolean(java.lang.Object obj) {
		if (obj != null) {
			return MagikInteropUtils.fromMagikBoolean(obj);
		} else {
			return null;
		}
	}
	
	static Object toMagikBoolean(boolean bool) {
		return MagikInteropUtils.toMagikBoolean(bool);
	}
}
