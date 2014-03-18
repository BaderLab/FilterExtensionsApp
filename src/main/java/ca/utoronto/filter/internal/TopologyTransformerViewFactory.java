package ca.utoronto.filter.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cytoscape.filter.model.Transformer;
import org.cytoscape.filter.view.TransformerViewFactory;

public class TopologyTransformerViewFactory implements TransformerViewFactory {

	@Override
	public String getId() {
		return TopologyTransformer.ID;
	}

	@Override
	public JComponent createView(Transformer<?, ?> transformer) {
		Controller controller = new Controller((TopologyTransformer) transformer);
		View view = new View(controller);
		return view;
	}

	class Controller {
		private TopologyTransformer model;
		
		public Controller(TopologyTransformer model) {
			this.model = model;
		}
		
		public void setThreshold(Integer threshold) {
			model.setThreshold(threshold);
		}

		public void setDistance(Integer distance) {
			model.setDistance(distance);
		}
		
		void synchronize(TopologyTransformerView view) {
			view.getDistanceField().setValue(model.getDistance());
			view.getThresholdField().setValue(model.getThreshold());
		}
	}
	
	@SuppressWarnings("serial")
	class View extends JPanel implements TopologyTransformerView {
		private JFormattedTextField thresholdField;
		private JFormattedTextField distanceField;

		public View(final Controller controller) {
			ViewUtil.configureFilterView(this);
			
			thresholdField = new JFormattedTextField(ViewUtil.createIntegerFormatter(0, Integer.MAX_VALUE));
			thresholdField.setHorizontalAlignment(JTextField.TRAILING);
			thresholdField.setColumns(3);
			thresholdField.addPropertyChangeListener("value", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					Integer value = (Integer) thresholdField.getValue();
					controller.setThreshold(value);
				}
			});
			
			distanceField = new JFormattedTextField(ViewUtil.createIntegerFormatter(1, Integer.MAX_VALUE));
			distanceField.setHorizontalAlignment(JTextField.TRAILING);
			distanceField.setColumns(3);
			distanceField.addPropertyChangeListener("value", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					Integer value = (Integer) distanceField.getValue();
					controller.setDistance(value);
				}
			});
			
			JLabel label1 = new JLabel("Node neighbourhoods with at least ");
			JLabel label2 = new JLabel(" neighbours");
			JLabel label3 = new JLabel("within distance ");
			
			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			
			layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
						.addComponent(label1)
						.addComponent(thresholdField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label2))
					.addGroup(layout.createSequentialGroup()
						.addComponent(label3)
						.addComponent(distanceField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		
			layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createBaselineGroup(false, false)
						.addComponent(label1)
						.addComponent(thresholdField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label2))
					.addGroup(layout.createBaselineGroup(false, false)
						.addComponent(label3)
						.addComponent(distanceField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
			
			controller.synchronize(this);
		}
		
		@Override
		public JFormattedTextField getThresholdField() {
			return thresholdField;
		}
		
		@Override
		public JFormattedTextField getDistanceField() {
			return distanceField;
		}
	}
}
