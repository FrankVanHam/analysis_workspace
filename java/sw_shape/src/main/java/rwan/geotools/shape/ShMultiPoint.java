package rwan.geotools.shape;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_multi_point"))
public class ShMultiPoint {

	private final ShPoint[] _points;

	public ShMultiPoint(ShPoint[] points) {
		_points = points;
	}
	
	@MagikMethod("size()")
	public final Object magik_size() {
		return MagikJavaConverter.toMagikInteger(_points.length);
	}
	
	public int size() {
		return _points.length;
	}

	@MagikMethod("get()")
	public final ShPoint get(int index) {
		return _points[index];
	}

	@ExemplarInstance
	public static ShMultiPoint initialiseExemplar() {
		return new ShMultiPoint(null);
	}
	
	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static ShMultiPoint _new(Object self, Object[] oPoints) throws Exception {
		var points = new ShPoint[oPoints.length];
		for (int i = 0; i < oPoints.length; i++) {
			points[i] = (ShPoint) oPoints[i];
		};
		return new ShMultiPoint(points);
	}
}
	
