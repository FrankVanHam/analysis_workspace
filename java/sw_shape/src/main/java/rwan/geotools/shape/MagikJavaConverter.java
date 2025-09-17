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
		if (str != null) {
			return MagikInteropUtils.toMagikString(str);
		} else {
			return null;
		}
	}

	static Integer fromMagikInteger(java.lang.Object obj) {
		if (obj != null) {
			return MagikInteropUtils.fromMagikInteger(obj);
		} else {
			return null;
		}
	}
	
	static Object toMagikInteger(Integer it) {
		if (it != null) {
			return MagikInteropUtils.toMagikInteger(it);
		} else {
			return null;
		}
	}
	
	static Object toMagikInteger(Long lng) {
		if (lng != null) {
			return MagikInteropUtils.toMagikInteger(lng);
		} else {
			return null;
		}
	}
	static Double fromMagikDouble(java.lang.Object obj) {
		if (obj != null) {
			return MagikInteropUtils.fromMagikDouble(obj);
		} else {
			return null;
		}
	}
	
	static Object toMagikDouble(Double dbl) {
		if (dbl != null) {
			return MagikInteropUtils.toMagikDouble(dbl);
		} else {
			return null;
		}
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
	
	static Object toMagikBoolean(Boolean bool) {
		if (bool != null) {
			return MagikInteropUtils.toMagikBoolean(bool);
		} else {
			return null;
		}
	}
}
