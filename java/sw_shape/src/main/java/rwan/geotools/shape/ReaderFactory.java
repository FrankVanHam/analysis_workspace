package rwan.geotools.shape;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.filter.IsLessThenOrEqualToImpl;
import org.geotools.filter.LiteralExpressionImpl;

public class ReaderFactory {

	public Reader create(String fileName) throws Exception {
        File file = new File(fileName);
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        
        var source = dataStore.getFeatureSource(typeName);
        var attributeDefs = this.getAttributes(source);
        return new Reader(fileName, source, attributeDefs);
	}
	
	private AttributeDef[] getAttributes(SimpleFeatureSource source) {
		var descrs = source.getSchema().getAttributeDescriptors();
		var attrDefs = new AttributeDef[descrs.size()];
		for (int i = 0; i < attrDefs.length; i++) {
			var descr = descrs.get(i);
			var name = descr.getName().toString();
			var size = this.figureMaxSize(descr);
			var type = descr.getType().getName().toString();
			attrDefs[i] = new AttributeDef(name,  size,  type);
		};
		return attrDefs;
	}
	
	private int figureMaxSize(AttributeDescriptor descr) {
		var type = descr.getType();
		if (type == null) return 0;
		var restrictions = type.getRestrictions();
		for (var restr: restrictions) {
			if (restr instanceof IsLessThenOrEqualToImpl) {
				var implRestr = (IsLessThenOrEqualToImpl) restr;
				var expr = implRestr.getExpression2();
				if (expr instanceof LiteralExpressionImpl) {
					var implExpr = (LiteralExpressionImpl) expr;
					var value = implExpr.getValue();
					if (value instanceof Integer) {
						return (Integer) value;
					}
				}
			}
		}
		return 0;
	}
}
