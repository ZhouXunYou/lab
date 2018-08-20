package lab.storm.model;

import java.io.Serializable;

public class AlarmRule implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String kpiCode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKpiCode() {
		return kpiCode;
	}
	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}
}
