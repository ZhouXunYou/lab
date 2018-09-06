package lab.storm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table {
	private String tableName;
	private List<Column> columns = new ArrayList<>();
	public Table(String tableName) {
		this.tableName = tableName;
	}
	public Table addColumn(String columnName) {
		return addColumn(columnName,columns.size());
	}
	public Table addColumn(String columnName,int index) {
		Column column = new Column(columnName);
		if(index<columns.size()) {
			columns.add(index, column);
		}else {
			columns.add(column);
		}
		Collections.sort(columns);
		return this;
	}
	private void resetColumnIndex() {
		for(int i=0;i<columns.size();i++) {
			
		}
	}
	
}
