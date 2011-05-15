package org.oddjob.poi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.oddjob.arooa.ArooaBeanDescriptor;
import org.oddjob.arooa.ArooaDescriptor;
import org.oddjob.arooa.ConfiguredHow;
import org.oddjob.arooa.deploy.ClassPathDescriptorFactory;
import org.oddjob.arooa.life.SimpleArooaClass;
import org.oddjob.arooa.reflect.BeanOverview;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.dido.DataException;
import org.oddjob.dido.DataPlanType;

public class NumericFormulaCellTest extends TestCase {

	public void testReadWrite() throws DataException {
		
		Workbook workbook = new HSSFWorkbook();
		
		Sheet sheet = workbook.createSheet();
		
		NumericFormulaCell test1 = new NumericFormulaCell();
		test1.setArooaSession(new StandardArooaSession());
		test1.setFormula("2 + 2");
		
		SheetOut out = new PoiSheetOut(sheet);
		out.nextRow();
		
		test1.begin(out);
		test1.out(out);
		test1.end(out);
		
		assertEquals(0, test1.getColumn());
		
		NumericFormulaCell test2 = new NumericFormulaCell();
		test2.setArooaSession(new StandardArooaSession());

		SheetIn in = new PoiSheetIn(sheet);
		assertTrue(in.nextRow());
		
		test2.begin(in);
		test2.in(in);
		test2.end(in);
		
		assertEquals(new Double(4), test2.getValue());
	}
	
	/**
	 * This was tracking down a weird feature where the 
	 * {@link FormulaCell} wasn't public and so the formula property 
	 * couldn't be seen. However this had worked on my laptop. 
	 * Different version of java maybe? 
	 */
	public void testFormulaType() {
		
		ClassPathDescriptorFactory descriptorFactory = 
			new ClassPathDescriptorFactory();
		descriptorFactory.setResource(DataPlanType.DIDO_DESCRIPTOR_RESOURCE);
		
		ArooaDescriptor descriptor = 
			descriptorFactory.createDescriptor(getClass().getClassLoader());
		
		StandardArooaSession session = new StandardArooaSession(descriptor);

		PropertyAccessor propertyAccessor = 
			session.getTools().getPropertyAccessor(); 
		
		BeanOverview overview = propertyAccessor.getBeanOverview(
				NumericFormulaCell.class);
		
		Set<String> properties = new HashSet<String>(
				Arrays.asList(overview.getProperties()));
		
		assertTrue(properties.contains("formula"));
		
		assertTrue(overview.hasWriteableProperty("formula"));
		
		ArooaBeanDescriptor beanDescriptor = 
			session.getArooaDescriptor().getBeanDescriptor(
				new SimpleArooaClass(NumericFormulaCell.class), 
				propertyAccessor);
		
		assertEquals(ConfiguredHow.HIDDEN, 
				beanDescriptor.getConfiguredHow("arooaSession"));
		assertEquals(ConfiguredHow.ATTRIBUTE, 
				beanDescriptor.getConfiguredHow("name"));
		assertEquals(ConfiguredHow.ATTRIBUTE, 
				beanDescriptor.getConfiguredHow("title"));
		assertEquals(ConfiguredHow.ATTRIBUTE, 
				beanDescriptor.getConfiguredHow("value"));
		assertEquals(ConfiguredHow.ATTRIBUTE, 
				beanDescriptor.getConfiguredHow("style"));
		assertEquals(ConfiguredHow.ATTRIBUTE, 
				beanDescriptor.getConfiguredHow("formula"));
	}
}