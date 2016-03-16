package org.xandercat.ofeapps.teslamodels;

import java.text.NumberFormat;
import java.util.Properties;

import org.xandercat.ofe.filter.AggregateFilter;
import org.xandercat.ofe.filter.IntegerFilter;
import org.xandercat.ofe.filter.NumericMatchStyle;
import org.xandercat.ofe.searchutility.stat.AbstractStatCollectorSource;
import org.xandercat.ofe.stat.AverageStatCollector;
import org.xandercat.ofe.stat.CountDistinctStatCollector;
import org.xandercat.ofe.stat.MatchCountStatCollector;
import org.xandercat.ofe.stat.MinMaxStatCollector;

public class CPOStatCollectorSource extends AbstractStatCollectorSource {

	@Override
	public void initialize(Properties properties) throws Exception {
		addStatCollector("price", new AverageStatCollector<Integer>(Integer.class, NumberFormat.getCurrencyInstance()));
		addStatCollector("price", new MinMaxStatCollector<Integer>(Integer.class, NumberFormat.getCurrencyInstance()));
		addStatCollector("price", new MatchCountStatCollector<Integer>(Integer.class, 
				new IntegerFilter(NumericMatchStyle.LESS_THAN, 60000)));
		addStatCollector("price", new MatchCountStatCollector<Integer>(Integer.class, new AggregateFilter<Integer>(
				new IntegerFilter(NumericMatchStyle.GREATER_THAN_OR_EQUALS, 60000),
				new IntegerFilter(NumericMatchStyle.LESS_THAN, 80000))));
		addStatCollector("price", new MatchCountStatCollector<Integer>(Integer.class, 
				new IntegerFilter(NumericMatchStyle.GREATER_THAN_OR_EQUALS, 80000)));
		addStatCollector("cpoInv", new CountDistinctStatCollector<String>(String.class));
		addStatCollector("year", new CountDistinctStatCollector<Integer>(Integer.class));
	}
}
