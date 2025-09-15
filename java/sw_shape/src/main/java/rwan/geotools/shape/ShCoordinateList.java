package rwan.geotools.shape;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_coordinate_list"))
public class ShCoordinateList {

	private final ShCoordinate[] _coords;

	public ShCoordinateList(ShCoordinate[] coords) {
		_coords = coords;
	}

	@MagikMethod("size()")
	public final Object magikSize() {
		return MagikJavaConverter.toMagikInteger(_coords.length);
	}
	
	public int size() {
		return _coords.length;
	}	
	@MagikMethod("get()")
	public final ShCoordinate get(int index) {
		return _coords[index];
	}	

	@ExemplarInstance
	public static ShCoordinateList initialiseExemplar() {
		return new ShCoordinateList(null);
	}
}
	
