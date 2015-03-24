package apps.projectmanagement.component.workflowchart.core;

import java.awt.Color;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.GraphNode;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.TextNode;
import apps.projectmanagement.registry.WorkflowRegistry;

/**
 * The Class WorkflowTool.
 */
public class WorkflowTool {

	/** The bottom left. */
	private SimpleButton bottomLeft;

	/** The bottom right. */
	private SimpleButton bottomRight;

	/** The content system. */
	private ContentSystem contentSystem;

	/** The gmanager. */
	private GraphManager gmanager;

	/** The top left. */
	private SimpleButton topLeft;

	/** The top right. */
	private SimpleButton topRight;

	/**
	 * Instantiates a new workflow tool.
	 *
	 * @param contentSystem
	 *            the content system
	 */
	public WorkflowTool(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
		gmanager = new GraphManager(contentSystem);
		init();
		this.setvisibility(false);
	}
	
	/**
	 * Adds the task node.
	 *
	 * @param location
	 *            the location
	 */
	private void addTaskNode(Location location) {
		TextNode taskNode = new TextNode(contentSystem, gmanager);
		taskNode.setLinkButtonLocation(GraphNode.BOTTOM_RIGHT_CORNER);
		taskNode.setEditPointLocation(GraphNode.BOTTOM_LEFT_CORNER);
		taskNode.setCloseButtonLocation(GraphNode.BOTTOM_LEFT_CORNER);
		taskNode.setLocation(location.x, location.y);

	}

	/**
	 * Inits the.
	 */
	private void init() {
		topLeft = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		topLeft.setBackgroundColour(Color.white);
		topLeft.setLocation(100, 704);
		topLeft.setAutoFitSize(false);
		topLeft.setWidth(80);
		topLeft.setHeight(30);
		topLeft.setText("Add Task");
		topLeft.setAlignment(Alignment.CENTER);
		topLeft.setBorderSize(2);
		topLeft.setBorderColour(Color.green);
		topLeft.setRotateTranslateScalable(false);
		topLeft.addItemListener(new ItemListener() {
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {

				addTaskNode(new Location(200, 375, 0));
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {

			}

		});

		topRight = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		topRight.setBackgroundColour(Color.white);
		topRight.setLocation(924, 704);
		topRight.setAutoFitSize(false);
		topRight.setWidth(80);
		topRight.setHeight(30);
		topRight.setText("Add Task");
		topRight.setAlignment(Alignment.CENTER);
		topRight.setBorderSize(2);
		topRight.setBorderColour(Color.green);
		topRight.setRotateTranslateScalable(false);
		topRight.addItemListener(new ItemListener() {
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {

				addTaskNode(new Location(824, 375, 0));
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {

			}

		});

		bottomLeft = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		bottomLeft.setBackgroundColour(Color.white);
		bottomLeft.setLocation(100, 50);
		bottomLeft.setAutoFitSize(false);
		bottomLeft.setWidth(80);
		bottomLeft.setHeight(30);
		bottomLeft.setText("Add Task");
		bottomLeft.setAlignment(Alignment.CENTER);
		bottomLeft.setBorderSize(2);
		bottomLeft.setBorderColour(Color.green);
		bottomLeft.setRotateTranslateScalable(false);
		bottomLeft.addItemListener(new ItemListener() {
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {

				addTaskNode(new Location(350, 200, 0));
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {

			}

		});

		bottomRight = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		bottomRight.setBackgroundColour(Color.white);
		bottomRight.setLocation(924, 50);
		bottomRight.setAutoFitSize(false);
		bottomRight.setWidth(80);
		bottomRight.setHeight(30);
		bottomRight.setText("Add Task");
		bottomRight.setAlignment(Alignment.CENTER);
		bottomRight.setBorderSize(2);
		bottomRight.setBorderColour(Color.green);
		bottomRight.setRotateTranslateScalable(false);
		bottomRight.addItemListener(new ItemListener() {
			
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {

				addTaskNode(new Location(600, 200, 0));
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {

			}
			
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {

			}

		});
	}

	/**
	 * Sets the editable.
	 *
	 * @param b
	 *            the new editable
	 */
	public void setEditable(boolean b) {

		topLeft.setVisible(b);
		topRight.setVisible(b);
		bottomLeft.setVisible(b);
		bottomRight.setVisible(b);

		WorkflowRegistry.getInstance().setEditable(b);
	}

	/**
	 * Sets the visibility.
	 *
	 * @param b
	 *            the new visibility
	 */
	public void setvisibility(boolean b) {

		topLeft.setVisible(b);
		topRight.setVisible(b);
		bottomLeft.setVisible(b);
		bottomRight.setVisible(b);

		if (b) {
			WorkflowRegistry.getInstance().showWorkflow();
		} else {
			WorkflowRegistry.getInstance().hideWorkflow();
		}

	}
}
