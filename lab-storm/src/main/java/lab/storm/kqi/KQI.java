package lab.storm.kqi;

import java.io.Serializable;

public class KQI implements Serializable{
	private static final long serialVersionUID = 1L;
	private String kqiCode;
	private String script;
	private Integer version = 0;
	public String getKqiCode() {
		return kqiCode;
	}
	public void setKqiCode(String kqiCode) {
		this.kqiCode = kqiCode;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}