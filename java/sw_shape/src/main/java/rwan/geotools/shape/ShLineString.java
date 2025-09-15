package rwan.geotools.shape;

import java.util.Objects;

import org.locationtech.jts.geom.Coordinate;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_line_string"))
public class ShLineString {

	private final ShCoordinate[] _coords;

	public ShLineString(ShCoordinate[] coord) {
		_coords = coord;
	}
	
	public ShLineString(Coordinate[] coordinates) {
		_coords = ShCoordinate.listOfCoords(coordinates);
	}
	
	ShCoordinate[] getRawCoords() {
		return _coords;
	}

	@MagikMethod("get_coordinates()")
	public final ShCoordinateList getCoordinates() {
		return new ShCoordinateList(_coords);
	}
	@ExemplarInstance
	public static ShLineString initialiseExemplar() {
		return new ShLineString((ShCoordinate[]) null);
	}
	
	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static ShLineString _new(Object self, Object[] oCoords) throws Exception {
		return new ShLineString(ShCoordinate.castToCoords(oCoords));
	}

	@Override
	public int hashCode() {
		return Objects.hash((Object[]) _coords);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ShLineString other = (ShLineString) obj;
		return ShCoordinate.listEquals(_coords, other.getRawCoords());
	}
}
	
