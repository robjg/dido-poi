package org.oddjob.dido.poi.data;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.oddjob.dido.DataException;
import org.oddjob.dido.DataIn;
import org.oddjob.dido.UnsupportedDataInException;
import org.oddjob.dido.poi.RowsIn;
import org.oddjob.dido.poi.SheetIn;
import org.oddjob.dido.poi.TupleIn;

/**
 * Implementation of {@link RowsIn}.
 * 
 * @author rob
 *
 */
public class PoiRowsIn implements RowsIn {

	private final Sheet sheet;
	
	private SimpleHeadings headings;
	
	/** The offset from the first column. Used to calculate cell position
	 * for columns. */
	private final int columnOffset; 
	
	/** The 1 based index of the last row written. */
	private int lastRowNum;
	
	/** The 1 based index of the last column. */
	private int lastColumnNum;
		
	
	
	private Row row;
	
	/**
	 * Create an instance.
	 * 
	 * @param sheetIn
	 * @param firstRow
	 * @param firstColumn
	 */
	public PoiRowsIn(SheetIn sheetIn, int firstRow, int firstColumn) {
		this.sheet = sheetIn.getTheSheet();
		
		if (firstRow < 1) {
			firstRow = 1;
		}
		if (firstColumn < 1) {
			firstColumn = 1;
		}
		
		this.lastRowNum = firstRow - 1;
		this.lastColumnNum = firstColumn -1;
		
		this.columnOffset = lastColumnNum;
	}
	
	@Override
	public boolean headerRow() {
		
		Row row = sheet.getRow(lastRowNum);
		
		if (row == null) {
			return false;
		}
		else {
			this.headings = new SimpleHeadings(row, columnOffset);
			++lastRowNum;
			return true;
		}
	}
	
	@Override
	public boolean nextRow() {
		
		row = sheet.getRow(lastRowNum);
		if (row == null) {
			return false;
		}
		else {
			++lastRowNum;
			return true;
		}
	}

	@Override
	public int getLastRow() {
		return lastRowNum;
	}
	
	@Override
	public int getLastColumn() {
		return lastColumnNum;
	}
	
	@Override
	public <T extends DataIn> T provideDataIn(Class<T> type) throws DataException {

		if (type.isInstance(this)) {
			return type.cast(this);
		}
		
		if (type.isAssignableFrom(TupleIn.class)) {
			return type.cast(new PoiTupleIn());
		}
		
		throw new UnsupportedDataInException(this.getClass(), type);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + sheet.getSheetName();
	}
	
	
	/**
	 * Implementation of {@link TupleIn}.
	 */
	class PoiTupleIn implements TupleIn {
		
		@Override
		public int indexForHeading(String title) {
			
			if (headings != null && title != null) {
				Integer column = headings.position(title);
				if (column == null) {
					return 0; 
				}
				else {
					int columnInt = column.intValue();
					if (columnInt > lastColumnNum - columnOffset) {
						lastColumnNum = columnInt + columnOffset;
					}
					return columnInt;
				}
			}
			else {
				return ++lastColumnNum - columnOffset;
			}
		}	
		
		@Override
		public Cell getCell(int column) {
			
			if (column < 1) {
				throw new IndexOutOfBoundsException(
						"Column " + column + " is invalid.");
			}
			
			return row.getCell(columnOffset + column - 1);
		}
		
		@Override
		public <T extends DataIn> T provideDataIn(Class<T> type) throws DataException {
			
			if (type.isInstance(this)) {
				return type.cast(this);
			}
			
			throw new UnsupportedDataInException(this.getClass(), type);
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + " row [" + 
					row.getRowNum() + "]";
		}
		
	}
}