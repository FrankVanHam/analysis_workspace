package rwan.geotools.shape;

import java.util.Arrays;
import java.util.HashSet;

public class WriterFactory {
	private static final HashSet<String> ALLOWED_GEOM_TYPES = new HashSet<>(Arrays.asList("Point", "LineString", "Polygon", "MultiPolygon"));
	private static final HashSet<String> ALLOWED_ATTR_TYPES = new HashSet<>(Arrays.asList("String", "Integer", "Double", "Boolean", "Date", "Point", "LineString", "Polygon", "MultiPolygon"));

	public Writer create(String geomType, AttributeDef[] attributeDefs)
			throws Exception {
		this.validateAttributeDefs(attributeDefs);
		this.validateGeom(geomType);
		return new Writer(geomType, attributeDefs);
	}
	
	private void validateGeom(String geomType) throws Exception {
		if (!ALLOWED_GEOM_TYPES.contains(geomType)) throw new Exception(String.format("Unknow geomentry type %s", geomType));
	}
	
	private void validateAttributeDefs(AttributeDef[] attributeDefs) throws Exception {
		for (AttributeDef def : attributeDefs) {
			var name = def.get_name();
			var nameLen = name.length();
			var ok = (nameLen > 0 && nameLen <= 10);
			if (!ok) throw new Exception(String.format("Attribute name '%s' length should be between 0 and 10", name));
		
			var length = def.get_length();
			ok = length >= 0 && length < 255;
			if (!ok) throw new Exception(String.format("attribute length %d is should be [0,255]", length));
			
			var type = def.get_type();
			ok = ALLOWED_ATTR_TYPES.contains(type);
			if (!ok) throw new Exception(String.format("attribute type %s is not allowed", type));
		};
	}

}
