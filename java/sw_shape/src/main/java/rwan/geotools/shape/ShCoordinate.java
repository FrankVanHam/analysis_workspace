package rwan.geotools.shape;

import java.util.Objects;

import org.locationtech.jts.geom.Coordinate;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_coordinate"))
public class ShCoordinate {

	private final double _x;
	private final double _y;

	public ShCoordinate(double x, double y) {
		_x = x;
		_y = y;
	}
	
	public ShCoordinate(Coordinate coordinate) {
		_x = coordinate.getX();
		_y = coordinate.getY();
	}

	@MagikMethod("get_x()")
	public final Object getMagikX() {
		return MagikJavaConverter.toMagikDouble(_x);
	}
	@MagikMethod("get_y()")
	public final Object getMagikY() {
		return MagikJavaConverter.toMagikDouble(_y);
	}
	
	public final double getX() {
		return _x;
	}
	
	public final double getY() {
		return _y;
	}

	@ExemplarInstance
	public static ShCoordinate initialiseExemplar() {
		return new ShCoordinate(0d, 0d);
	}
	
	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static ShCoordinate _new(Object self, Double x, Double y) throws Exception {
		return new ShCoordinate(x, y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_x, _y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShCoordinate other = (ShCoordinate) obj;
		return Double.doubleToLongBits(_x) == Double.doubleToLongBits(other._x)
				&& Double.doubleToLongBits(_y) == Double.doubleToLongBits(other._y);
	}

	public static ShCoordinate[] listOfCoords(Double[] coords) {
		var result = new ShCoordinate[coords.length/2];
		for (int i = 0; i < coords.length/2; i++) {
			result[i] = new ShCoordinate(coords[i*2], coords[1 + (i*2)]);
		}
		return result;
	}
	public static ShCoordinate[] listOfCoords(Coordinate[] coords) {
		var result = new ShCoordinate[coords.length];
		for (int i = 0; i < coords.length; i++) {
			result[i] = new ShCoordinate(coords[i]);
		}
		return result;
	}
	
	public static Coordinate[] listOfJtsCoords( ShCoordinateList coords) {
		var result = new Coordinate[coords.size()];
		for (int i = 0; i < coords.size(); i++) {
			var coord = coords.get(i);
			result[i] = new Coordinate(coord.getX(), coord.getY());
		}
		return result;
	}
	
	public static Coordinate[] listOfJtsCoords( ShCoordinate[] coords) {
		var result = new Coordinate[coords.length];
		for (int i = 0; i < coords.length; i++) {
			var coord = coords[i];
			result[i] = new Coordinate(coord.getX(), coord.getY());
		}
		return result;
	}

	public static boolean listEquals(ShCoordinate[] coords1, ShCoordinate[] coords2) {
		if (coords1.length != coords2.length) return false;
		for (int i = 0; i < coords1.length; i++) {
			if (!coords1[i].equals(coords2[i])) return false;
		}
		return true;
	}
	
	public static ShCoordinate[] castToCoords(Object[] objs) {
		if (objs == null) return new ShCoordinate[0];
		var coords = new ShCoordinate[objs.length];
		for (int i = 0; i < objs.length; i++) {
			coords[i] = (ShCoordinate)objs[i];
		};
		return coords;
	}
	
}
	
