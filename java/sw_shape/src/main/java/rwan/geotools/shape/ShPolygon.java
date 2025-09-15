package rwan.geotools.shape;

import java.util.Objects;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_polygon"))
public class ShPolygon {

	private final ShCoordinate[] _outerCoords;
	private final ShCoordinate[][] _holes;

	public ShPolygon() {
		_outerCoords = new ShCoordinate[0]; 
		_holes = new ShCoordinate[0][];
	}
	
	public ShPolygon(ShCoordinate[] coords, ShCoordinate[][] holes) {
		_outerCoords = coords;
		if (holes == null) {
			_holes = new ShCoordinate[0][];
		} else {
			_holes = holes;
		}
	}

	ShCoordinate[] getOuterCoordinates() {
		return _outerCoords;
	}
	
	ShCoordinate[][] getHoleCoordinates() {
		return _holes;
	}
	
	@MagikMethod("get_outer_coordinates()")
	public final ShCoordinateList getMagikOuterCoordinates() {
		return new ShCoordinateList(_outerCoords);
	}
	
	@MagikMethod("hole_size()")
	public final Object magikHoleSize() {
		return MagikJavaConverter.toMagikInteger(_holes.length);
	}
	
	@MagikMethod("hole_get()")
	public final ShCoordinateList magikHoleGet(int index) {
		return new ShCoordinateList(_holes[index]);
	}
		
	@ExemplarInstance
	public static Object initialiseExemplar() {
		return new ShPolygon();
	}
	
	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static ShPolygon _new(Object self, Object[] oCoords, Object[] oHoles) throws Exception {
		var coords = ShCoordinate.castToCoords(oCoords);
		var holes = (oHoles != null) ? new ShCoordinate[oHoles.length][] : new ShCoordinate[0][];
		for (int i = 0; i < holes.length; i++) {
			holes[i] = ShCoordinate.castToCoords((Object[]) oHoles[i]);
		}
		return new ShPolygon(coords, holes);
	}

	@Override
	public int hashCode() {
		return Objects.hash((Object[]) _outerCoords, (Object[][]) _holes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ShPolygon other = (ShPolygon) obj;
		var coords = other.getOuterCoordinates();
		if (!ShCoordinate.listEquals(_outerCoords, coords)) return false;
		var otherHoles =  other.getHoleCoordinates();
		if (_holes.length != otherHoles.length) return false;
		for (int i = 0; i < otherHoles.length; i++) {
			if (!ShCoordinate.listEquals(_holes[i], otherHoles[i])) return false;
		}
		return true;
	}
}
	
