package rwan.geotools.shape;

public class AttributeDef {
	private final String _name;
	private final int _length;
	private final String _type;
	
	public AttributeDef(String _name, int _length, String _type) {
		this._name = _name;
		this._length = _length;
		this._type = _type;
	}

	public String get_name() {
		return _name;
	}

	public int get_length() {
		return _length;
	}

	public String get_type() {
		return _type;
	}
}
