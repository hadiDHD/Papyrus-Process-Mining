/******************************************************************************
 * Copyright (c) 2021 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Etienne Allogo (ARTAL) - etienne.allogo@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.statemachine.tests.creation;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Vertex;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;

public abstract class AbstractStatemachineTopNodeCreationTests extends AbstractPapyrusTest {


	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();


	public AbstractStatemachineTopNodeCreationTests() {
		super();
	}

	// Abstract test class for using the Classification Runner
	@After
	public void afterTest() {
		fixture.save();
	}

	public DNodeContainer getRootSiriusRegion() {
		final DiagramEditPart diagramEditpart = fixture.getActiveDiagram();

		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		final Diagram diagram = diagramEditpart.getDiagramView();

		Assert.assertEquals("The diagram must contain one element (region)", 1, diagram.getChildren().size());
		Object node = diagram.getChildren().get(0);
		Assert.assertTrue("The created element must be a View", node instanceof View);
		EObject siriusElement = ((View) node).getElement();
		Assert.assertTrue("The root element must be a DNodeContainer", siriusElement instanceof DNodeContainer);
		DNodeContainer container = (DNodeContainer) siriusElement;
		EObject stateMachine = container.getSemanticElements().iterator().next();
		Assert.assertTrue("The root element must be a UML state machine", stateMachine instanceof org.eclipse.uml2.uml.StateMachine);

		DNodeContainer regionSirius = (DNodeContainer) container.getOwnedDiagramElements().get(0);
		Assert.assertTrue("The root region semantic element is not an uml region", regionSirius.getSemanticElements().get(0) instanceof Region);
		return regionSirius;
	}


	/**
	 * @param oldOnes
	 * @param newOnes
	 * @return
	 */
	private Object findUniqueDiffs(Object[] oldOnes, Object[] newOnes) {
		Object newOne = null;
		for (Object newItem : newOnes) {
			boolean found = false;
			for (Object oldItem : oldOnes) {
				if (oldItem != null && oldItem.equals(newItem)) {
					found = true;
					break;
				}
			}

			if (!found) {
				if (newOne != null) {
					Assert.assertEquals("More than one item created after node creation command", newOne, newItem);

				}
				newOne = newItem;
			}
		}
		return newOne;
	}

	/**
	 * @return
	 */
	protected DNodeContainer checkAndGetEmptyStateNodeRegion() {
		final var regionSirius = getRootSiriusRegion();
		Assert.assertEquals("To create sub nodes the statemachine root region should conain one state node", 1, regionSirius.getOwnedDiagramElements().size());
	
		AbstractDNode existingNode = getExistingStateNode();
		Assert.assertTrue("The existing node should be state container node", existingNode instanceof DNodeContainer);
		DNodeContainer stateSiriusElement = (DNodeContainer) existingNode;
		Assert.assertEquals("The existing state should contain a region", 1, stateSiriusElement.getOwnedDiagramElements().size());
		DDiagramElement existingStateRegion = stateSiriusElement.getOwnedDiagramElements().get(0);
		Assert.assertTrue("The existing node region should be container node", existingStateRegion instanceof DNodeContainer);
		final var subStateContainer = (DNodeContainer) existingStateRegion;
		Assert.assertEquals("The state region should be empty before sub state createion", 0, subStateContainer.getOwnedDiagramElements().size());
		return subStateContainer;
	}

	/**
	 * @return
	 */
	protected AbstractDNode getExistingStateNode() {
		final var regionSirius = getRootSiriusRegion();
		if (!regionSirius.getOwnedDiagramElements().isEmpty()) {
			DDiagramElement existingNode = regionSirius.getOwnedDiagramElements().get(0);
			return (AbstractDNode) existingNode;
		}
		return null;

	}

	protected AbstractDNode createStateNode(DNodeContainer container, String toolName) {
		final var nbElements = container.getOwnedDiagramElements().size();
		Object[] oldOnes = container.getOwnedDiagramElements().toArray();
		fixture.applyNodeCreationTool(toolName, container.getParentDiagram(), container);
		fixture.flushDisplayEvents();

		// check UI via sirius
		Assert.assertEquals("The diagram must contain one element after creating a top node", nbElements + 1, container.getOwnedDiagramElements().size());
		Object[] newOnes = container.getOwnedDiagramElements().toArray();
		Object newOne = findUniqueDiffs(oldOnes, newOnes);
		Assert.assertTrue("No new created item found in the diagram", newOne instanceof AbstractDNode);
		return (AbstractDNode) newOne; // from getOwnedDiagramElements
	}

	protected AbstractDNode createStateNode(String toolName) {
		return createStateNode(getRootSiriusRegion(), toolName);
	}

	/**
	 * @param toolName
	 *            TODO
	 * @param siriusExpectedClazz
	 * @param umlExpectedClazz
	 * @return
	 */
	AbstractDNode createAndCheckStateNode(String toolName, Class<? extends AbstractDNode> siriusExpectedClazz, Class<? extends Element> umlExpectedClazz, DNodeContainer container) {

		// create node
		final var nbElements = container.getOwnedDiagramElements().size();
		Object[] oldOnes = container.getOwnedDiagramElements().toArray();

		DDiagramElement ownedDiagramElement = createStateNode(container, toolName);
		Assert.assertTrue("The created element must be a " + siriusExpectedClazz.getSimpleName() + " and not of type " + classname(ownedDiagramElement), siriusExpectedClazz.isInstance(ownedDiagramElement));
		AbstractDNode element = siriusExpectedClazz.cast(ownedDiagramElement);

		// check uml semantic
		EObject containerSemantic = container.getSemanticElements().get(0);
		Assert.assertTrue("Semantic Container should be an uml element", containerSemantic instanceof Element);
		Element containerUml = (Element) containerSemantic;
		Assert.assertEquals("The root must only contains one additional element after the creation", nbElements + 1, containerUml.getOwnedElements().size());
		var umlElement = element.getSemanticElements().get(0);
		Assert.assertTrue("he newly created semantic element should be attached to the newly created sirius element", containerUml.getOwnedElements().contains(umlElement));
		Element semmanticElement = (Element) umlElement;
		Assert.assertTrue("The created element must be a " + umlExpectedClazz.getSimpleName() + " and not of type " + classname(semmanticElement), umlExpectedClazz.isInstance(semmanticElement));

		// undo
		this.fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();

		Assert.assertEquals("The region must be empty after undoing the creation", nbElements, container.getOwnedDiagramElements().size());
		Assert.assertEquals("The root must contains the same number of elements as initially", nbElements, containerUml.getOwnedElements().size());

		// redo
		this.fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram must contain one element after creating a top node", nbElements + 1, container.getOwnedDiagramElements().size());
		Object[] newOnes = container.getOwnedDiagramElements().toArray();
		ownedDiagramElement = (DDiagramElement) findUniqueDiffs(oldOnes, newOnes);

		Assert.assertTrue("The created element must be a " + siriusExpectedClazz.getSimpleName(), siriusExpectedClazz.isInstance(ownedDiagramElement));
		element = siriusExpectedClazz.cast(ownedDiagramElement);
		// check uml semantic
		Assert.assertEquals("The root must only contains one additional element after the creation", nbElements + 1, containerUml.getOwnedElements().size());
		umlElement = element.getSemanticElements().get(0);
		Assert.assertTrue("he newly created semantic element should be attached to the newly created sirius element", containerUml.getOwnedElements().contains(umlElement));
		semmanticElement = (Element) element.getSemanticElements().get(0);
		Assert.assertTrue("The created element must be a " + umlExpectedClazz.getSimpleName(), umlExpectedClazz.isInstance(semmanticElement));

		fixture.flushDisplayEvents();
		return element;
	}

	/**
	 * @param semmanticElement
	 * @return
	 */
	private String classname(EObject semmanticElement) {
		return semmanticElement == null ? null : semmanticElement.eClass().getName();
	}


	AbstractDNode createAndCheckStateNode(String toolName, Class<? extends AbstractDNode> siriusExpectedClazz, Class<? extends Vertex> umlExpectedClazz) {
		return createAndCheckStateNode(toolName, siriusExpectedClazz, umlExpectedClazz, getRootSiriusRegion() /* root */);
	}

}