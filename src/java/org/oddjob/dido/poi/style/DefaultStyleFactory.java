package org.oddjob.dido.poi.style;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class DefaultStyleFactory implements StyleProviderFactory {

	public static String HEADING_STYLE = "heading";
	
	public static String DATE_STYLE = "date";
	
	public static String BEANCMPR_DIFF_STYLE = "beancmpr-difference";
	
	private final StyleProviderFactory factory;
	
	public DefaultStyleFactory() {
		
		Map<String, StyleBean> styles = new HashMap<String, StyleBean>();
		
		StyleBean heading = new StyleBean();
		heading.setBold(true);
		
		StyleBean date = new StyleBean();
		date.setFormat("d/m/yyyy");
		
		StyleBean beanCmprDiff = new StyleBean();
		beanCmprDiff.setColour(IndexedColors.RED);
		
		styles.put(HEADING_STYLE, heading);
		styles.put(DATE_STYLE, date);
		styles.put(BEANCMPR_DIFF_STYLE, beanCmprDiff);
		
		factory = new BeanStyleFactory(styles);
	}
	
	@Override
	public StyleProvider providerFor(Workbook workbook) {
		return factory.providerFor(workbook);
	}
}
