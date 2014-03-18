package ca.utoronto.filter.internal;

import org.cytoscape.filter.model.HolisticTransformer;
import org.cytoscape.filter.model.HolisticTransformerFactory;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;

public class TopologyTransformerFactory implements HolisticTransformerFactory<CyNetwork, CyIdentifiable> {

	@Override
	public HolisticTransformer<CyNetwork, CyIdentifiable> createHolisticTransformer() {
		return new TopologyTransformer();
	}
	
	@Override
	public String getId() {
		return TopologyTransformer.ID;
	}

}
