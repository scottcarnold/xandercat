package org.xandercat.ofeapps.teslamodels;

import java.util.Properties;

import org.xandercat.ofe.searchutility.stat.AbstractStatCollectorSource;
import org.xandercat.ofe.stat.AverageStatCollector;
import org.xandercat.ofe.stat.CountDistinctStatCollector;

public class CPOStatCollectorSource extends AbstractStatCollectorSource {

	@Override
	public void initialize(Properties properties) throws Exception {
		addStatCollector("price", new AverageStatCollector<Integer>(Integer.class));
		addStatCollector("trim", new CountDistinctStatCollector<String>(String.class));
	}


}
