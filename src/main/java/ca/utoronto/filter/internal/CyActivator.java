package ca.utoronto.filter.internal;

import java.util.Properties;

import org.cytoscape.filter.model.HolisticTransformerFactory;
import org.cytoscape.filter.view.TransformerViewFactory;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		registerService(context, new TopologyTransformerFactory(), HolisticTransformerFactory.class, new Properties());
		registerService(context, new TopologyTransformerViewFactory(), TransformerViewFactory.class, new Properties());
	}
}
