package lab.storm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Column implements Comparable<Column> {
	private String columnName;
	private ColumnDataType columnDataType;
	private int columnIndex;
	public Column(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public ColumnDataType getColumnDataType() {
		return columnDataType;
	}
	public void setColumnDataType(ColumnDataType columnDataType) {
		this.columnDataType = columnDataType;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	@Override
	public int compareTo(Column column) {
		if(column.getColumnIndex()==this.getColumnIndex()) {
			this.setColumnIndex(column.getColumnIndex()+1);
		}
		return this.getColumnIndex() - column.getColumnIndex();
	}
	public static void main(String[] args) {
		Column column1 = new Column("1");
		Column column2 = new Column("2");
		Column column3 = new Column("3");
		column1.setColumnIndex(0);
		column2.setColumnIndex(1);
		column3.setColumnIndex(0);
		
		List<Column> columns = new ArrayList<>();
		columns.add(column1);
		columns.add(column2);
		columns.add(column3);
		Collections.sort(columns);
		
		for(Column column:columns) {
			System.out.println(column.getColumnName()+":"+column.getColumnIndex());
		}
	}
}
