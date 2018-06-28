package lab.object.tools.define;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerateRule {
	private Random rand = new Random();
//	private String fieldName;
	
	private Class<?> type;
	/**
	 * 随机最大值，值生成类型设置为（R）生效，且KPI Code尾数为1
	 */
	private Integer randomMax;
	/**
	 * 随机最小值，值生成类型设置为（R）生效，且KPI Code尾数为1
	 */
	private Integer randomMin;
	/**
	 * KPI Value 赋值为随机字符串，值生成类型设置为（R）生效，且KPI Code尾数不为1
	 */
	private Integer randomStringLength;
	/**
	 * KPI Value，值生成规则为（A）生效
	 */
	private Object val;
	/**
	 * 值生成类型，允许填值：A,R,E,C
	 * 	A:指定值
	 * 	R:随机值
	 * 	E:随机枚举值
	 */
	private String valueGenType = "A";
	/**
	 * 随机枚举参考值，值生成类型设置为（E）生效
	 */
	private Object[] enumRef;
	/**
	 * 复合实例表达式，实例生成类型设置为（C）生效
	 */
	private String complexRegx;
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Integer getRandomMax() {
		return randomMax;
	}
	public void setRandomMax(Integer randomMax) {
		this.randomMax = randomMax;
	}
	public Integer getRandomMin() {
		return randomMin;
	}
	public void setRandomMin(Integer randomMin) {
		this.randomMin = randomMin;
	}
	public Integer getRandomStringLength() {
		return randomStringLength;
	}
	public void setRandomStringLength(Integer randomStringLength) {
		this.randomStringLength = randomStringLength;
	}
	public Object getVal() {
		return val;
	}
	public void setVal(Object val) {
		this.val = val;
	}
	public String getValueGenType() {
		return valueGenType;
	}
	public void setValueGenType(String valueGenType) {
		this.valueGenType = valueGenType;
	}
	public Object[] getEnumRef() {
		return enumRef;
	}
	public void setEnumRef(Object[] enumRef) {
		this.enumRef = enumRef;
	}
	public String getComplexRegx() {
		return complexRegx;
	}
	public void setComplexRegx(String complexRegx) {
		this.complexRegx = complexRegx;
	}
	public Object generateValue() {
		Object returnValue = null;
		switch (this.valueGenType) {
		case "A":
			returnValue = val;
			break;
		case "R":
			returnValue = getRandom();
			break;
		case "E":
			int index = getRandomInt(0,enumRef.length-1);
			returnValue = enumRef[index];
			break;
		case "C":
			returnValue = getComplexValue();
			break;
		default:
			returnValue = null;
		}
		return returnValue;
	}
	private Object getRandom() {
		Object randomValue = null;
		if(CharSequence.class.isAssignableFrom(type)) {
			StringBuffer randomString = new StringBuffer();
			for(int i=0;i<randomStringLength;i++) {
				int genType = getRandomInt(1,3);
				switch(genType) {
					case 1:	//生成0-9的数字
						randomString.append(getRandomInt(0, 9));
						break;
					case 2:	//生成A-Z的字母
						char upperCase = (char)getRandomInt(65, 90);
						randomString.append(upperCase);
						break;
					case 3:	//生成a-z的字母
						char lowerCase = (char)getRandomInt(97, 122);
						randomString.append(lowerCase);
						break;
				}
			}
			randomValue = randomString.toString();
		}else if(Number.class.isAssignableFrom(type)) {
			randomValue = getRandomInt(randomMin,randomMax);
			if(Float.class.isAssignableFrom(type)||Double.class.isAssignableFrom(type)) {
				randomValue = Double.valueOf(randomValue.toString())+Math.random();
			}
		}else if(Boolean.class.isAssignableFrom(type)) {
			if(getRandomInt(0,1)==0) {
				randomValue = false;
			}else {
				randomValue = true;
			}
		}
		return randomValue;
	}
	private int getRandomInt(int min,int max) {
		return rand.nextInt(max - min + 1) + min;
	}
	private List<String> getComplexValue(){
		List<String> values = new ArrayList<>();
		String regex = "(\\[[a-z]-[a-z]\\])|(\\[[A-Z]-[A-Z]\\])|(\\[[0-9]*-[0-9]*\\])";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(this.complexRegx);
		List<Map<String,Integer>> rangeValues = new ArrayList<>();
		int instanceCount = 0;
		List<String> rangeRegexs = new ArrayList<>();
		while(matcher.find()) {
			String rangeRegex = matcher.group();
			rangeRegexs.add(rangeRegex);
			String[] ranges = rangeRegex.replace("[", "").replace("]", "").split("-");
			Map<String,Integer> range = new HashMap<>();
			String startVal = ranges[0];
			String endVal = ranges[1];
			int startValue = 0,endValue = 0;
			int valueType = 0;
			if(startVal.matches("\\d*")&&endVal.matches("\\d*")) {
				startValue = Integer.parseInt(startVal);
				endValue=Integer.parseInt(endVal);
				valueType = 1;
			}else if((startVal.matches("[a-z]")&&endVal.matches("[a-z]"))||(startVal.matches("[A-Z]")&&endVal.matches("[A-Z]"))) {
				startValue = (int)startVal.toCharArray()[0];
				endValue=(int)endVal.toCharArray()[0];
			}
			range.put("startValue", startValue);
			range.put("endValue", endValue);
			range.put("valueType", valueType);
			if(instanceCount==0) {
				instanceCount = endValue - startValue+1;
			}else {
				instanceCount *= endValue - startValue+1;
			}
			rangeValues.add(range);
			
		}
		for(int i=0;i<instanceCount;i++) {
			values.add(this.complexRegx);
		}
		for(int i=0;i<rangeValues.size();i++) {
			Map<String,Integer> rangeValue = rangeValues.get(i);
			String rangeRegex = "\\"+rangeRegexs.get(i).substring(0, rangeRegexs.get(i).length()-1)+"\\]";
			int startValue = rangeValue.get("startValue");
			int endValue = rangeValue.get("endValue");
			int valueType = rangeValue.get("valueType");
			int row = 0;
			int temp = startValue;
			while(row<instanceCount) {
				if(valueType==1) {
					values.set(row, values.get(row).replaceFirst(rangeRegex, String.valueOf(temp)));
				}else {
					values.set(row, values.get(row).replaceFirst(rangeRegex, String.valueOf((char)temp)));
				}
				if(++temp>endValue) {
					temp = startValue;
				}
				row++;
			}
			Collections.sort(values);
		}
		return values;
	}
	public static void main(String[] args) throws Exception {
//		Integer i = 0;
//		
//		System.out.println((i instanceof Number));
		System.out.println(Class.forName("java.lang.String"));
	}
	
}
