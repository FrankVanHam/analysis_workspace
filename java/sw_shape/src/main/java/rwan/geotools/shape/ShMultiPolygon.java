package rwan.geotools.shape;

import java.util.Arrays;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_multi_polygon"))
public class ShMultiPolygon {

	private final ShPolygon[] _polygons;

	public ShMultiPolygon(ShPolygon[] polygons) {
		_polygons = polygons;
	}
	
	@MagikMethod("size()")
	public final Object magikSize() {
		return MagikJavaConverter.toMagikInteger(_polygons.length);
	}
	
	public int size() {
		return _polygons.length;
	}

	@MagikMethod("get()")
	public final ShPolygon get(int index) {
		return _polygons[index];
	}

	@ExemplarInstance
	public static ShMultiPolygon initialiseExemplar() {
		return new ShMultiPolygon(null);
	}
	
	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static ShMultiPolygon _new(Object self, Object[] oPolygons) throws Exception {
		var polygons = new ShPolygon[oPolygons.length];
		for (int i = 0; i < oPolygons.length; i++) {
			polygons[i] = (ShPolygon) oPolygons[i];
		};
		return new ShMultiPolygon(polygons);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(_polygons);
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
		ShMultiPolygon other = (ShMultiPolygon) obj;
		return Arrays.equals(_polygons, other._polygons);
	}
}
	
