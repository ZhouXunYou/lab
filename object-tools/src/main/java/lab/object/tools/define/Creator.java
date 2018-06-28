package lab.object.tools.define;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Creator {
	private static final ClassPool classPool = ClassPool.getDefault();
	private CtClass ctClass;
	private Map<String,GenerateRule> fieldTypeMapper = new HashMap<>();
	private static final Map<String,Class<?>> typeClass = new HashMap<>();
	private String className;
	static {
		typeClass.put("int", Integer.class);
		typeClass.put("integer", Integer.class);
		typeClass.put("java.lang.integer", Integer.class);
		
		typeClass.put("long", Long.class);
		typeClass.put("java.lang.long", Long.class);
		
		typeClass.put("float", Float.class);
		typeClass.put("java.lang.Float", Float.class);
		
		typeClass.put("double", Double.class);
		typeClass.put("java.lang.double", Double.class);
		
		typeClass.put("string", String.class);
		typeClass.put("java.lang.String", String.class);
		
		typeClass.put("boolean", Boolean.class);
		typeClass.put("java.lang.boolean", Boolean.class);
		
		
	}
	
	public Creator(String className) {
		this.className = className;
		try {
			ctClass = classPool.get(className);
		} catch (NotFoundException e) {
			System.out.println(String.format("class %s not found,make", className));
			ctClass = classPool.makeClass(className);
		}
	}
	public Creator(String className,Class<?> superClass) throws NotFoundException, CannotCompileException {
		this(className);
		ctClass.setSuperclass(classPool.get(superClass.getName()));
	}
	public Creator(String className,Class<?> superClass,Class<?>[] interfaces) throws NotFoundException, CannotCompileException {
		this(className,superClass);
		addInterfaces(interfaces);
	}
	public Creator(String className,Class<?>[] interfaces) throws NotFoundException, CannotCompileException {
		this(className);
		addInterfaces(interfaces);
	}
	private void addInterfaces(Class<?>[] interfaces) {
		try {
			for(Class<?> interfaceClass:interfaces) {
				this.ctClass.addInterface(classPool.get(interfaceClass.getName()));
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
	public void addField(String name,String typeName, GenerateRule generateRule) throws CannotCompileException {
		if(typeName==null||"".equals(typeName)) {
			typeName = Object.class.getName();
		}
		Class<?> typeClass = getTypeClass(typeName);
		if(!this.ctClass.isFrozen()) {
			CtField field = null;
			
			try {
				field = ctClass.getDeclaredField(name);
			} catch (NotFoundException e) {
				field = CtField.make(String.format("private %s %s;", typeClass.getName(),name), this.ctClass);
			}
			if(field!=null) {
				ctClass.addField(field);
				addGetMethod(name,typeClass);
				addSetMethod(name,typeClass);
			}
		}
		generateRule.setType(typeClass);
		fieldTypeMapper.put(name, generateRule);
	}
	private Class<?> getTypeClass(String type){
		
		if(typeClass.get(type.toLowerCase())!=null) {
			return typeClass.get(type.toLowerCase());
		}else {
			try {
				return Class.forName(type);
			} catch (ClassNotFoundException e) {
				System.out.println("class not found,object type as Object.class");
			}
		}
		return Object.class;
	}
	private void addGetMethod(String name,Class<?> type) {
		String typeName = type.getName();
		String methodName = "set"+name.substring(0, 1).toUpperCase() + name.substring(1);
		String src = String.format("public void %s(%s %s){this.%s=%s;}",methodName,typeName,name,name,name);
		addMethod(src);
	}
	private void addSetMethod(String name,Class<?> type) {
		String typeName = type.getName();
		String methodName = "get"+name.substring(0, 1).toUpperCase() + name.substring(1);
		String src = String.format("public %s %s(){return this.%s;}",typeName,methodName,name);
		addMethod(src);
	}
	public void addMethod(String src) {
		try {
			this.ctClass.addMethod(CtMethod.make(src, this.ctClass));
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}
	public Class<?> getDynamicClass(){
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			try {
				return this.ctClass.toClass();
			} catch (CannotCompileException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	public Object createObject() {
		Class<?> clazz = getDynamicClass();
		try {
			Object object = clazz.newInstance();
			fieldTypeMapper.forEach((field,generateRule)->{
				String setMethodName = "set"+field.substring(0, 1).toUpperCase() + field.substring(1);
				try {
					Method method = clazz.getDeclaredMethod(setMethodName,generateRule.getType());
					Object value = generateRule.generateValue();
					method.invoke(object, value);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			});
			return object;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
