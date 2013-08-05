package org.oddjob.dido.poi.data;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.oddjob.dido.DataException;
import org.oddjob.dido.DataIn;
import org.oddjob.dido.UnsupportedDataInException;
import org.oddjob.dido.poi.BookIn;
import org.oddjob.dido.poi.SheetIn;

public class PoiBookIn implements BookIn {

	private final Workbook workbook;
	
	private int sheet = 0;
	
	public PoiBookIn(InputStream input) throws InvalidFormatException, IOException {
		if (input == null) {
			throw new NullPointerException();
		}
		
		workbook = WorkbookFactory.create(input);
	}
	
	@Override
	public Sheet getSheet(String sheetName) {
		return workbook.getSheet(sheetName);
	}
	
	@Override
	public Sheet nextSheet() {
		return workbook.getSheetAt(sheet++);
	}	
	
	@Override
	public <T extends DataIn> T provideDataIn(Class<T> type) throws DataException {

		if (type.isInstance(this)) {
			return type.cast(this);
		}
		
		if (type.isAssignableFrom(SheetIn.class)) {
			return type.cast(new PoiSheetIn(nextSheet()));
		}
		
		throw new UnsupportedDataInException(this.getClass(), type);
	}
}
