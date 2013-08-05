package org.oddjob.dido.poi.beancmpr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import org.oddjob.OurDirs;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.arooa.types.ArooaObject;
import org.oddjob.dido.DataException;
import org.oddjob.dido.DataWriter;
import org.oddjob.dido.poi.data.PoiWorkbook;
import org.oddjob.dido.poi.layouts.DataBook;
import org.oddjob.dido.poi.layouts.DataRows;

public class BeanCmprResultBindingTest extends TestCase {

	File workDir;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		workDir = new OurDirs().relative("work");
	}
	
	public void testsimple() throws DataException, FileNotFoundException {
		
		FakeResultsBean.Builder builder = 
				new FakeResultsBean.Builder(3);
		
		builder.addKey("id", new Integer(10));
		builder.addKey("region", "UK");

		builder.addComparison("quantity", new Integer(15), 
				new Integer(16), -1);
		
		builder.addComparison("price", new Double(22.4), 
				new Float(22.4), 0);

		builder.addComparison("colour", null, 
				null, 0);
		
		ArooaSession session = new StandardArooaSession();
		
		DataBook book = new DataBook();
		
		DataRows rows = new DataRows();
		book.setOf(0, rows);
		
		BeanCmprResultBinding test = new BeanCmprResultBinding();
		test.setArooaSession(session);
		
		rows.bind(test);

		PoiWorkbook workbook = new PoiWorkbook();
		workbook.setArooaSession(session);
		workbook.setOutput(new ArooaObject(new FileOutputStream(
				new File(workDir, "BeanCmprResultsTest.xlsx"))));
		
		DataWriter writer = book.writerFor(workbook);
		
		writer.write(builder.build()); 
		
		writer.close();
	}
	
}
