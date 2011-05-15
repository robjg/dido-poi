package org.oddjob.poi;

public interface SheetData {
	
	public int getCurrentRow();
	
	public int getLastColumn();
	
	public void startAt(int firstRow, int firstColumn);
}