package lab.storm.plugins;

public interface DataConvertor<RETURN,VALUE> {
	public RETURN transform(VALUE original);
}
