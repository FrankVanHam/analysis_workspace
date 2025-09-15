package rwan.geotools.shape;

import java.util.Arrays;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_multi_line_string"))
public class ShMultiLineString {

	private final ShLineString[] _lineStrings;

	public ShMultiLineString(ShLineString[] lineStrings) {
		_lineStrings = lineStrings;
	}
	
	@MagikMethod("size()")
	public final Object magikSize() {
		return MagikJavaConverter.toMagikInteger(_lineStrings.length);
	}
	
	public int size() {
		return _lineStrings.length;
	}
	
	@MagikMethod("get()")
	public final ShLineString get(int index) {
		return _lineStrings[index];
	}

	@ExemplarInstance
	public static ShMultiLineString initialiseExemplar() {
		return new ShMultiLineString(null);
	}
	
	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static ShMultiLineString _new(Object self, Object[] oLineStrings) throws Exception {
		var lineStrings = new ShLineString[oLineStrings.length];
		for (int i = 0; i < oLineStrings.length; i++) {
			lineStrings[i] = (ShLineString) oLineStrings[i];
		};
		return new ShMultiLineString(lineStrings);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(_lineStrings);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShMultiLineString other = (ShMultiLineString) obj;
		return Arrays.equals(_lineStrings, other._lineStrings);
	}
}
	
