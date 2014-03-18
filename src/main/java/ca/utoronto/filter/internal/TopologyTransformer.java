package ca.utoronto.filter.internal;

import java.util.IdentityHashMap;
import java.util.Map;

import org.cytoscape.filter.model.AbstractTransformer;
import org.cytoscape.filter.model.HolisticTransformer;
import org.cytoscape.filter.model.TransformerSink;
import org.cytoscape.filter.model.TransformerSource;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyEdge.Type;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.work.Tunable;

public class TopologyTransformer  extends AbstractTransformer<CyNetwork, CyIdentifiable> implements HolisticTransformer<CyNetwork, CyIdentifiable> {
	static final String ID = "ca.utoronto.TopologyTransformer";
	
	private Integer distance;
	private Integer threshold;

	@Tunable
	public Integer getDistance() {
		return distance;
	}
	
	public void setDistance(Integer distance) {
		this.distance = distance;
		notifyListeners();
	}
	
	@Tunable
	public Integer getThreshold() {
		return threshold;
	}
	
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
		notifyListeners();
	}
	
	@Override
	public String getName() {
		return "Topology Transformer";
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public Class<CyNetwork> getContextType() {
		return CyNetwork.class;
	}

	@Override
	public Class<CyIdentifiable> getElementType() {
		return CyIdentifiable.class;
	}

	@Override
	public void apply(CyNetwork network,
			TransformerSource<CyNetwork, CyIdentifiable> source,
			TransformerSink<CyIdentifiable> sink) {

		Map<CyNode, Object> results = new IdentityHashMap<CyNode, Object>();
		Map<CyNode, Object> seen = new IdentityHashMap<CyNode, Object>();
		for (CyIdentifiable element: source.getElementList(network)) {
			try {
				if (!(element instanceof CyNode)) {
					continue;
				}
				
				if (distance == null || threshold == null) {
					continue;
				}
				
				getNeighbours(network, (CyNode) element, distance, seen);
				seen.remove(element);
				
				if (seen.size() < threshold) {
					continue;
				}
				
				results.putAll(seen);
			} finally {
				seen.clear();
			}
		}
		
		for (CyNode node : results.keySet()) {
			sink.collect(node);
		}
	}
	
	private static void getNeighbours(CyNetwork network, CyNode node, int distance, Map<CyNode, Object> seen) {
		if (distance < 0 || seen.containsKey(node)) {
			return;
		}
		
		seen.put(node, null);
		for (CyEdge edge : network.getAdjacentEdgeIterable(node, Type.ANY)) {
			getNeighbours(network, edge.getSource(), distance - 1, seen);
			getNeighbours(network, edge.getTarget(), distance - 1, seen);
		}
	}
}
