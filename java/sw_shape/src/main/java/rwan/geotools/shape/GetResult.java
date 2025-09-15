package rwan.geotools.shape;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_reader_result"))
public class GetResult {

	private final String _id;
	private final Object _geometry;
	private final Object[] _attrValues;

	public GetResult() {
		_id = null; _geometry = null; _attrValues = null;
	}

	public GetResult(String id, Object geometry, Object[] attrValues) {
		_id = id;
		if (attrValues != null) {
			_geometry = geometry;
			_attrValues = attrValues;
		} else {
			_geometry = null;
			_attrValues = null;
		}
	}
	
	@MagikMethod("get_id()")
	public final Object getId() {
		return MagikJavaConverter.toMagikString(_id);
	}
	
	@MagikMethod("get_geometry()")
	public final Object getGeometry() {
		return _geometry;
	}
	
	@MagikMethod("get_attribute_values()")
	public final Object[] getAttributeValues() {
		return _attrValues;
	}

	@ExemplarInstance
	public static GetResult initialiseExemplar() {
		return new GetResult();
	}
}
