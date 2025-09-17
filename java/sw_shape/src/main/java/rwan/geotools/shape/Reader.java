package rwan.geotools.shape;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import com.gesmallworld.magik.commons.interop.annotations.ExemplarInstance;
import com.gesmallworld.magik.commons.interop.annotations.MagikExemplar;
import com.gesmallworld.magik.commons.interop.annotations.MagikMethod;
import com.gesmallworld.magik.commons.interop.annotations.Name;

@MagikExemplar(@Name("geotools_shape_reader"))
public class Reader {
	private final String _fileName;
	private FeatureSource<SimpleFeatureType, SimpleFeature> _source;
	private FeatureIterator<SimpleFeature> _iterator;
	private final AttributeDef[] _attributeDefs;
	private final String _geomType;
	private final String _geomName;
	private final MagikShapeConverter _shapeConverter;

	public Reader() {
		_fileName = null; _source = null; _attributeDefs = null; _iterator = null;
		_geomType = null; _shapeConverter = null; _geomName = null;
	}
	
	public Reader(String fileName, SimpleFeatureSource source, AttributeDef[] attributeDefs) throws IOException {
		_fileName = fileName;
        _source = source;
        _attributeDefs = attributeDefs;
        _shapeConverter = new MagikShapeConverter(null);
        _geomName = _source.getSchema().getGeometryDescriptor().getName().toString();
        _geomType = _source.getSchema().getGeometryDescriptor().getType().getName().toString();
        _iterator = null;
	}
	
	@MagikMethod("geometry_type()")
	public final Object geometryType() throws Exception {
		return MagikJavaConverter.toMagikString(_geomType);
	}
	
	@MagikMethod("geometry_name()")
	public final Object geometryName() throws Exception {
		return MagikJavaConverter.toMagikString(_geomName);
	}
	
	@MagikMethod("file_name()")
	public final Object fileName() throws Exception {
		return MagikJavaConverter.toMagikString(_fileName);
	}
	
	@MagikMethod("attribute_defs()")
	public final Object[] attributeDefs() throws Exception {
		var list = new Object[_attributeDefs.length - 1];
		for (int i = 0; i < list.length; i++) {
			var def = _attributeDefs[i+1];	
			var magikName = MagikJavaConverter.toMagikString(def.get_name());
			var magikLength = MagikJavaConverter.toMagikInteger(def.get_length());
			var magikType = MagikJavaConverter.toMagikString(def.get_type());
			list[i] = new Object[] {magikName, magikLength, magikType};
		};
		return list;
	}
	
	@MagikMethod("get()")
	public final GetResult get() throws Exception {
		if (_source == null) return null;
		if (_iterator == null) _iterator = this.initIterator();
		
		if (_iterator.hasNext()) {
			SimpleFeature feature = _iterator.next();
			var id = feature.getID();
			var attrValues = feature.getAttributes();
			return this.constructMagikResult(id, attrValues);
		} else {
			return null;
		}
	}
	
	@MagikMethod("dispose()")
	public final void dispose() {
		if (_iterator != null) _iterator.close();
		if (_source != null) _source.getDataStore().dispose();
		_source = null;
	}

	
	private GetResult constructMagikResult(String id, List<Object> oAttrValues) throws Exception {
		var mAttrValues = this.convertToMagikAttrValues(oAttrValues);
		var geom = mAttrValues[0];
		var resultValues = Arrays.copyOfRange(mAttrValues, 1, mAttrValues.length);
		return new GetResult(id, geom, resultValues);
	}
	
	private Object[] convertToMagikAttrValues(List<Object> attrValues) throws Exception {
		var list = new Object[attrValues.size()];
		for (int i = 0; i < list.length; i++) {
			var def = _attributeDefs[i];
			list[i] = _shapeConverter.fromShapeToMagik(def, attrValues.get(i));
		}
		return list;
	}
	
	private FeatureIterator<SimpleFeature> initIterator() throws IOException {
		Filter filter = Filter.INCLUDE;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = _source.getFeatures(filter);
        return collection.features();
	}	

	@ExemplarInstance
	public static Reader initialiseExemplar() {
		return new Reader();
	}

	// This method returns a new instance of the exemplar
	@MagikMethod("new()")
	public static Reader _new(Object self, Object oFileName) throws Exception {
		var fileName = MagikJavaConverter.fromMagikString(oFileName);
		var factory = new ReaderFactory();
		return factory.create(fileName);
	}
}
