package rwan.geotools.shape;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_writer"))
public class Writer {
	private final AttributeDef[] _attributeDefs;
	private final SimpleFeatureType _builder;
	private final GeometryFactory _geometryFactory;
	private final SimpleFeatureBuilder _featureBuilder;
	private List<SimpleFeature> _features;
	private final MagikShapeConverter _shapeConverter;

	public Writer() {
		_attributeDefs = null; _builder = null; _featureBuilder = null; _geometryFactory = null; _shapeConverter = null;
	}

	public Writer(String geomName, String geomType, AttributeDef[] attributeDefs){
		_attributeDefs = this.appendGeomToAttribute(geomName, geomType, attributeDefs);
		_builder = this.createBuilder();
		_featureBuilder = new SimpleFeatureBuilder(_builder);
		_geometryFactory = JTSFactoryFinder.getGeometryFactory();
		_shapeConverter = new MagikShapeConverter(_geometryFactory);
		_features = new ArrayList<>();
	}

	private AttributeDef[] appendGeomToAttribute(String geomName, String geomType, AttributeDef[] attributeDefs) {
		var attrs = new AttributeDef[attributeDefs.length + 1];
		System.arraycopy(attributeDefs, 0, attrs, 1, attributeDefs.length);
		attrs[0] = new AttributeDef(geomName, 0, geomType);
		return attrs;
	}

	private SimpleFeatureType createBuilder() {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("Location");
		// add attributes in order 
		for (var def : _attributeDefs) {
			this.addToBuilder(def, builder);
		};
		final SimpleFeatureType LOCATION = builder.buildFeatureType();
		return LOCATION;
	}
	
	public void addToBuilder(AttributeDef def, SimpleFeatureTypeBuilder builder) {
		var type = def.get_type();
		var name = def.get_name();
		switch (type) {
		case "String":
			var length = def.get_length();
			builder.length(length).add(name, String.class);
			break;
		case "Integer":
			builder.add(name, Integer.class);
			break;
		case "Double":
			builder.add(name, Double.class);
			break;
		case "Boolean":
			builder.add(name, Boolean.class);
			break;
		case "Date":
			builder.add(name, Date.class);
			break;
		case "Point":
			builder.add(name, Point.class);
			break;
		case "LineString":
			builder.add(name, LineString.class);
			break;
		case "Polygon":
			builder.add(name, Polygon.class);
			break;
		case "MultiPolygon":
			builder.add(name, MultiPolygon.class);
			break;
		default:
			throw new RuntimeException(String.format("Unknown type %s", type));
		}
	}

	
	@ExemplarInstance
	public static Writer initialiseExemplar() {
		return new Writer();
	}

	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static Writer _new(Object self, Object oGeomName, Object oGeomType, Object[] oAttributeDefs)
			throws Exception {
		var factory = new WriterFactory();
		var geomName = MagikJavaConverter.fromMagikString(oGeomName);
		var geomType = MagikJavaConverter.fromMagikString(oGeomType);
		var attributeDefs = fromMagikAttributeDefs(oAttributeDefs);
		return factory.create(geomName, geomType, attributeDefs);
	}
	

	@MagikMethod("add_data()")
	public final String addData(java.lang.Object geom, java.lang.Object[] aList) throws Exception{
		var size = _attributeDefs.length - 1;
		if (size != aList.length)
			throw new Exception(String.format("The list size should be %d", size));
		
		var value = _shapeConverter.fromMagikToShape(_attributeDefs[0], geom);
		_featureBuilder.add(value);
		for (int i = 0; i < aList.length; i++) {
			value = _shapeConverter.fromMagikToShape(_attributeDefs[i+1], aList[i]);
			_featureBuilder.add(value);
		}
		SimpleFeature feature = _featureBuilder.buildFeature(null);
		_features.add(feature);
		var id = feature.getID();
		return id;
	}

	@MagikMethod("save_to_file()")
	public final boolean saveToFile(Object oFileName) throws Exception{
		var file = this.ensureFileName(oFileName);

		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", file.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        newDataStore.createSchema(_builder);
        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        		
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
        SimpleFeatureCollection collection = new ListFeatureCollection(_builder, _features);
        featureStore.setTransaction(transaction);
        var ok = true;
        try {
            featureStore.addFeatures(collection);
            transaction.commit();
        } catch (Exception problem) {
            problem.printStackTrace();
            transaction.rollback();
            ok = false;
        } finally {
            transaction.close();
        };
        newDataStore.dispose();
        return ok;
	}
	
	private static AttributeDef[] fromMagikAttributeDefs(Object[] oAttributeDefs) {
		var defs = new AttributeDef[oAttributeDefs.length];
		for (int i = 0; i < defs.length; i++) {
			var oDef = (Object[]) oAttributeDefs[i];
			var name = MagikJavaConverter.fromMagikString(oDef[0]);
			var length = MagikJavaConverter.fromMagikInteger(oDef[1]);
			if (length == null) length = 0;
			var type = MagikJavaConverter.fromMagikString(oDef[2]);
			defs[i] = new AttributeDef(name, length, type);
		}
		return defs;
	}

	private File ensureFileName(java.lang.Object oFileName) throws Exception {
		String fileName = MagikJavaConverter.fromMagikString(oFileName);
		return new File(fileName);
	}
}
