package sw_shape_test;

import rwan.geotools.shape.GetResult;
import rwan.geotools.shape.ReaderFactory;
import rwan.geotools.shape.ShCoordinate;
import rwan.geotools.shape.ShLineString;
import rwan.geotools.shape.ShMultiLineString;
import rwan.geotools.shape.ShMultiPolygon;
import rwan.geotools.shape.ShPoint;
import rwan.geotools.shape.ShPolygon;
import rwan.geotools.shape.Writer;
import rwan.geotools.shape.WriterFactory;

import com.gesmallworld.magik.interop.MagikInteropUtils;

import java.io.File;

import com.gesmallworld.magik.commons.runtime.objects.Char16Vector;

public class run {
	public static void main(String[] args) throws Exception {	
		testUrban();
		/*
		testAFile();
		testPoint();
		testLine();
		testPolygon();
		testPolygonWithHoles();
		testMultiPolygonWithHoles();
		testAttribute();
		*/
	}
	
	public static void testAFile() throws Exception {
		var dir = new File("D:\\50m_cultural").listFiles();
		for (int i = 0; i < dir.length; i++) {
			File file = dir[i];
			if (file.getName().matches(".*[.]shp")) {
				System.out.println(file.getPath());
				var reader =new ReaderFactory().create(file.getPath());
				try {
					var result = reader.get();
					
				} finally {
					reader.dispose();
				}
			};
		}
	}
	
	public static void testUrban() throws Exception {
		var fileName = "D:\\50m_cultural\\ne_50m_urban_areas.shp";
		var reader =new ReaderFactory().create(fileName);
		try {
			var result = reader.get();
			
		} finally {
			reader.dispose();
		}
	}
	
	public static void testPoint() throws Exception {
		System.out.println("--testPoint()--");
		Object[] []atts = {{s("name"), 20, s("String")}};
		var writer = Writer._new(null, s("the_geom"), s("Point"), atts);
		
		var geom = new ShPoint(new ShCoordinate(0d,0d));
		Object[] data = {s("Frank")};
		var id = writer.addData(geom, data);
		
		var file = "C:\\Users\\frnkv\\Documents\\test_point.shp";
		writer.saveToFile(s(file));
		
		var results = testReadFile(file);
		var resultGeom = results.getGeometry();
		
		if (!geom.equals(resultGeom)) System.out.println("coordinates not matching");
		if (!"Frank".equals(MagikInteropUtils.fromMagikString(results.getAttributeValues()[0]))) System.out.println("Name not matching");
	}
	
	public static void testLine() throws Exception {
		System.out.println("--testLine()--");
		Object[][] atts = {{s("name"), 20, s("String")}};
		var writer = Writer._new(null, s("the_geom"), s("LineString"), atts);
		
		var coords = new ShCoordinate[] {new ShCoordinate(0d ,0d), new ShCoordinate(10d, 0d)};
		var geom = new ShLineString(coords);
		Object[] data = {s("Frank")};
		writer.addData(geom, data);
		var file = "C:\\Users\\frnkv\\Documents\\test_line.shp";
		writer.saveToFile(s(file));
		
		var results = testReadFile(file);

		var resultGeoms = (ShMultiLineString) results.getGeometry();
		var resultGeom = resultGeoms.get(0);
		
		if (!geom.equals(resultGeom)) System.out.println("coordinates not matching");
		if (!"Frank".equals(MagikInteropUtils.fromMagikString(results.getAttributeValues()[0]))) System.out.println("Name not matching");
	}
	
	public static void testPolygon() throws Exception {
		System.out.println("--testPolygon()--");
		var factory = new WriterFactory();
		Object[][] atts = {{s("name"), 20, s("String")}};
		var writer = Writer._new(null, s("the_geom"), s("Polygon"), atts);
		
		var outer = ShCoordinate.listOfCoords(new Double[]{0d, 0d, 0d, 10d, 10d, 10d, 10d, 0d, 0d, 0d});
		var holes = new ShCoordinate[0][];
		var geom = new ShPolygon(outer, holes);
		Object[] data = { s("Frank")};
		writer.addData(geom, data);
		var file = "C:\\Users\\frnkv\\Documents\\test_polygon.shp";
		writer.saveToFile(s(file));
		
		var results = testReadFile(file);
		var resultGeoms = (ShMultiPolygon) results.getGeometry();
		var resultGeom = (ShPolygon) resultGeoms.get(0);
		
		if (!geom.equals(resultGeom)) System.out.println("coordinates not matching");
		if (!"Frank".equals(MagikInteropUtils.fromMagikString(results.getAttributeValues()[0]))) System.out.println("Name not matching");
	}
	
	public static void testPolygonWithHoles() throws Exception {
		System.out.println("--testPolygonWithHoles()--");
		var factory = new WriterFactory();
		Object[][] atts = {{s("name"), 20, s("String")}};
		var writer = Writer._new(null, s("the_geom"), s("Polygon"), atts);
		
		var outer = ShCoordinate.listOfCoords(new Double[]{0d,0d,0d,100d,100d,100d,100d,0d,0d,0d});
		var holes = new ShCoordinate[][] {
				ShCoordinate.listOfCoords(new Double[]{10d,10d,20d,10d,20d,20d,10d,20d,10d,10d}),
				ShCoordinate.listOfCoords(new Double[]{50d,50d,60d,50d,60d,60d,50d,60d,50d,50d}),
		};
		var geom = new ShPolygon(outer, holes);
		Object[] data = {s("Frank")};
		writer.addData(geom, data);
		var file = "C:\\Users\\frnkv\\Documents\\test_polygon_with_holes.shp";
		writer.saveToFile(s(file));
		
		var results = testReadFile(file);
		var resultGeoms = (ShMultiPolygon) results.getGeometry();
		var resultGeom = (ShPolygon) resultGeoms.get(0);
		
		if (!geom.equals(resultGeom)) System.out.println("coordinates not matching");
		if (!"Frank".equals(MagikInteropUtils.fromMagikString(results.getAttributeValues()[0]))) System.out.println("Name not matching");
	}
	
	public static void testMultiPolygonWithHoles() throws Exception {
		System.out.println("--testMultiPolygonWithHoles()--");
		var factory = new WriterFactory();
		Object[][] atts = {{s("name"), 20, s("String")}};
		var writer = Writer._new(null, s("the_geom"), s("MultiPolygon"), atts);
				
		var outer = ShCoordinate.listOfCoords(new Double[]{0d,0d,0d,100d,100d,100d,100d,0d,0d,0d});
		var holes = new ShCoordinate[][] {
				ShCoordinate.listOfCoords(new Double[]{10d,10d,20d,10d,20d,20d,10d,20d,10d,10d}),
		};
		var geom1 = new ShPolygon(outer, holes);
		outer = ShCoordinate.listOfCoords(new Double[]{150d,150d,150d,160d,160d,160d,160d,150d,150d,150d});
		var geom2 = new ShPolygon(outer, null);
		
		var geom = new ShMultiPolygon(new ShPolygon[] {geom1, geom2});
		Object[] data = {s("Frank")};
		writer.addData(geom, data);
		var file = "C:\\Users\\frnkv\\Documents\\test_multipolygon_with_holes.shp";
		writer.saveToFile(s(file));
		
		var results = testReadFile(file);
		var resultGeom = (ShMultiPolygon) results.getGeometry();
		
		if (!geom.equals(resultGeom)) System.out.println("coordinates not matching");
		if (!"Frank".equals(MagikInteropUtils.fromMagikString(results.getAttributeValues()[0]))) System.out.println("Name not matching");
	}
	
	public static void testAttribute() throws Exception {
		System.out.println("--testAttributes()--");
		var factory = new WriterFactory();
		//"string", "integer", "double", "boolean", "date"
		Object[][] atts = {{s("aString"),   20,   s("String")},
				{s("anInteger"), null, s("Integer")},
				{s("aDouble"),   null, s("Double")},
				{s("aBoolean"),  null, s("Boolean")},
				{s("aDate"),     null, s("Date")}};
		var writer = Writer._new(null, s("the_geom"), s("Point"), atts);
		
		var geom = new ShPoint(new ShCoordinate(0d,0d));
		Object[] data = {s("Frank"), 20, 1.13, MagikInteropUtils.toMagikBoolean(true), s("2025-12-28")};
		writer.addData(geom, data);
		var file = "C:\\Users\\frnkv\\Documents\\test_attrs.shp";
		writer.saveToFile(s(file));
		
		var results = testReadFile(file);
		var resultAttributes = results.getAttributeValues();
		if (!"Frank".equals(MagikInteropUtils.fromMagikString(resultAttributes[0]))) System.out.println("aString not matching");
		if (20 != MagikInteropUtils.fromMagikInteger(resultAttributes[1])) System.out.println("anInteger not matching");
		if (1.13 != MagikInteropUtils.fromMagikDouble(resultAttributes[2])) System.out.println("aDouble not matching");
		if (true != MagikInteropUtils.fromMagikBoolean(resultAttributes[3])) System.out.println("aBoolean not matching");
		if (!"2025-12-28".equals(MagikInteropUtils.fromMagikString(resultAttributes[4]))) System.out.println("aDate not matching");
	}
	
	public static GetResult testReadFile(String fileName) throws Exception {
		var reader =new ReaderFactory().create(fileName);
		var type = reader.geometryType().toString();
		var defs = reader.attributeDefs();
		
		var result = reader.get();
		var finalResult = reader.get();
		if (finalResult != null) throw new Exception("should only be one record");
		reader.dispose();
		return result;
	}
	
	private static Char16Vector s(String str) {
		return new Char16Vector(str);
		
	}
	
	private static boolean coordsCompare(Object[] javaCoords, Object[] magikCoords) {
		if (javaCoords.length != magikCoords.length) return false;
		for (int i = 0; i < magikCoords.length; i++) {
			if ((javaCoords[i] instanceof Object[]) & (magikCoords[i] instanceof Object[])) {
				var same = coordsCompare((Object[])javaCoords[i], (Object[])magikCoords[i]);
				if (!same) return false;
			} else {
				var javaD = (Double) javaCoords[i];
				var magikD = MagikInteropUtils.fromMagikDouble(magikCoords[i]);
				if (javaD != magikD) return false;
			}
		}
		return true;
	}
}
