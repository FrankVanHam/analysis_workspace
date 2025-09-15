package rwan.geotools.shape;

import java.util.Objects;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_point"))
public class ShPoint {

	private final ShCoordinate _coord;

	public ShPoint(ShCoordinate coord) {
		_coord = coord;
	}
	
	@MagikMethod("get_coordinate()")
	public final ShCoordinate getCoordinate() {
		return _coord;
	}
	@ExemplarInstance
	public static ShPoint initialiseExemplar() {
		return new ShPoint(null);
	}
	
	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static ShPoint _new(Object self, Object coord) throws Exception {
		return new ShPoint((ShCoordinate) coord);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_coord);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShPoint other = (ShPoint) obj;
		return _coord.equals(other.getCoordinate());
	}
}
	
