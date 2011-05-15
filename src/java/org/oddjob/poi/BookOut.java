package org.oddjob.poi;

import org.apache.poi.ss.usermodel.Sheet;
import org.oddjob.dido.DataOut;

public interface BookOut extends DataOut, StyleProvider {
	
	public Sheet createSheet(String name);
}