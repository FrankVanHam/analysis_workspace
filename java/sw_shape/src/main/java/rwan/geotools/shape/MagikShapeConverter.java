package rwan.geotools.shape;

import java.util.Date;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class MagikShapeConverter {
	
	private final GeometryFactory _geometryFactory;
	
	public MagikShapeConverter(GeometryFactory _geometryFactory) {
		this._geometryFactory = _geometryFactory;
	}

	Object fromMagikToShape(AttributeDef def, java.lang.Object obj) throws Exception {
		if (obj == null) return null;
		
		var type = def.get_type();
		var length = def.get_length();
		switch (type) {
		case "String":
			var str = MagikJavaConverter.fromMagikString(obj);
			if (str.length() > length) str = str.substring(0, length);
			return str;
		case "Integer":
			return MagikJavaConverter.fromMagikInteger(obj);
		case "Double":
			return MagikJavaConverter.fromMagikDouble(obj);
		case "Boolean":
			return MagikJavaConverter.fromMagikBoolean(obj);
		case "Date":
			return MagikJavaConverter.fromMagikDate(obj);
		case "Point":
			return this.fromMagikPoint((ShPoint) obj);
		case "LineString":
			return this.fromMagikLineString((ShLineString) obj);
		case "Polygon":
			return this.fromMagikPolygon((ShPolygon) obj);
		case "MultiPolygon":
			return this.fromMagikMultiPolygon((ShMultiPolygon) obj);
		default:
			throw new Exception(String.format("Unknown type %s", type));
		}
	}
	
	Object fromShapeToMagik(AttributeDef def, java.lang.Object obj) throws Exception {
		var type = def.get_type();
		switch (type) {
		case "String":
			return MagikJavaConverter.toMagikString((String) obj);
		case "Integer":
			return MagikJavaConverter.toMagikInteger((Integer) obj);
		case "Long":
			return MagikJavaConverter.toMagikInteger((Long) obj);
		case "Double":
			return MagikJavaConverter.toMagikDouble((Double) obj);
		case "Boolean":
			return MagikJavaConverter.toMagikBoolean((Boolean) obj);
		case "Date":
			return MagikJavaConverter.toMagikDate((Date) obj);
		case "Point":
			return this.toMagikPoint((Point) obj);
		case "MultiPoint":
			return this.toMagikMultiPoint((MultiPoint) obj);
		case "MultiLineString":
			return this.toMagikMultiLineString((MultiLineString) obj);
		case "MultiPolygon":
			return this.toMagikMultiPolygon((MultiPolygon) obj);
		default:
			throw new Exception(String.format("Unknown type %s", type));
		}
	}

	Point fromMagikPoint(ShPoint point) throws Exception{
		var shCoord = point.getCoordinate();
		var coord = new Coordinate(shCoord.getX(), shCoord.getY());
		return _geometryFactory.createPoint(coord);
	}
	
	LineString fromMagikLineString(ShLineString lineString) throws Exception{
		var coords = ShCoordinate.listOfJtsCoords(lineString.getCoordinates());
		return _geometryFactory.createLineString(coords);
	}
	
	Polygon fromMagikPolygon(ShPolygon polygon) throws Exception{
		// area with holes
		var outer = ShCoordinate.listOfJtsCoords(polygon.getOuterCoordinates());
		var ring = _geometryFactory.createLinearRing(outer);
		
		var pHoles = polygon.getHoleCoordinates();
		var holes = new LinearRing[pHoles.length];
		for (int i = 0; i < pHoles.length; i++) {
			var holeCoords = ShCoordinate.listOfJtsCoords(pHoles[i]);
			holes[i] = _geometryFactory.createLinearRing(holeCoords);
		};
		if (holes.length > 0) {
			return _geometryFactory.createPolygon(ring, holes);
		} else {
			return _geometryFactory.createPolygon(ring);
		}
	}
	
	MultiPolygon fromMagikMultiPolygon(ShMultiPolygon mPoly) throws Exception{
		var polygons = new Polygon[mPoly.size()];
		for (int i = 0; i < mPoly.size(); i++) {
			polygons[i] = this.fromMagikPolygon(mPoly.get(i));
		};
		return _geometryFactory.createMultiPolygon(polygons);					
	}
	
	ShPoint toMagikPoint(Point point) {
		var coord = point.getCoordinate();
		return new ShPoint(new ShCoordinate(coord.getX(), coord.getY()));
	}
	
	ShMultiPoint toMagikMultiPoint(MultiPoint mPoint) {
		var nrPoints = mPoint.getNumGeometries();
		var pointList = new ShPoint[nrPoints];
		for (int i = 0; i < nrPoints; i++) {
			var point = (Point) mPoint.getGeometryN(i);
			pointList[i] = this.toMagikPoint(point);
		}
		return new ShMultiPoint(pointList);
	}	
	
	ShLineString toMagikLineString(LineString lineString) {
		return new ShLineString(lineString.getCoordinates());
	}
	
	ShMultiLineString toMagikMultiLineString(MultiLineString mLineString) {
		var nrLines = mLineString.getNumGeometries();
		var lineList = new ShLineString[nrLines];
		for (int i = 0; i < nrLines; i++) {
			var line = (LineString) mLineString.getGeometryN(i);
			lineList[i] = this.toMagikLineString(line);
		}
		return new ShMultiLineString(lineList);
	}
	
	ShPolygon toMagikPolygon(Polygon polygon) {
		var nrInners = polygon.getNumInteriorRing();
		var innerList = new ShCoordinate[nrInners][];
		
		var outer = polygon.getExteriorRing();
		var outerCoords = ShCoordinate.listOfCoords(outer.getCoordinates());
		
		for (int i = 0; i < nrInners; i++) {
			var inner = polygon.getInteriorRingN(i);
			innerList[i] = ShCoordinate.listOfCoords(inner.getCoordinates());
		}
		return new ShPolygon(outerCoords, innerList);
	}
	
	ShMultiPolygon toMagikMultiPolygon(MultiPolygon mPolygon) {
		var nrPolys = mPolygon.getNumGeometries();
		var polyList = new ShPolygon[nrPolys];
		for (int i = 0; i < nrPolys; i++) {
			var poly = (Polygon) mPolygon.getGeometryN(i);
			polyList[i] = this.toMagikPolygon(poly);
		}
		return new ShMultiPolygon(polyList);
	}
}
