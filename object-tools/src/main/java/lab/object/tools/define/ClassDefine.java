package lab.object.tools.define;

import java.util.List;

public class ClassDefine {
	private String className;
	private Integer cycle;
	private Integer count;
	private String topic;
	private List<FieldDefin> fields;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public List<FieldDefin> getFields() {
		return fields;
	}
	public void setFields(List<FieldDefin> fields) {
		this.fields = fields;
	}
}
