package lab.object.tools.define;

public class FieldDefin {
	private String fieldName;
	private String type;
	private GenerateRule generateRule;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public GenerateRule getGenerateRule() {
		return generateRule;
	}
	public void setGenerateRule(GenerateRule generateRule) {
		this.generateRule = generateRule;
	}
}
