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
 *  Rengin Battal (ARTAL) - rengin.battal@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.sequence.tests.creation.nodes;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.papyrus.sirius.uml.diagram.sequence.tests.SequenceDiagramTestsTool;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceElementAccessor;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Lifeline;
import org.eclipse.uml2.uml.Gate;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Gate node creation test
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/oneLifeline/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Node_Gate_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";

	/** The created Gate name */
	private static final String GATE_NAME = "Gate1";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createGateNodeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();

		Assert.assertEquals("The diagram contains one Lifeline", 1, diagram.getChildren().size());		
		Assert.assertEquals("The root model contains one interaction element", 1, fixture.getModel().getOwnedElements().size());

		// Compute the Gate position 
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();		
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain one lifeline", 1, lifeLines.size());
		Lifeline lifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		EdgeTarget lifelineDRep = (EdgeTarget)lifeLine.getNotationNode().getElement();
		IGraphicalEditPart lifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(lifelineDRep);
    	Point gatePosition = lifelineEditPart.getFigure().getBounds().getTop().translate(0, 15);
		
		// Create the Gate
		fixture.applyNodeCreationToolFromPalette("Gate", diagramRepresentation, diagramRepresentation, gatePosition, new Dimension (100, 50));
		fixture.flushDisplayEvents();	
		
		Assert.assertEquals("The diagram contains one additional element after the creation of a Gate node", nbDiagramChild + 1 , diagram.getChildren().size());
		Object element = diagram.getChildren().get(1);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		EObject semanticElement = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Comment", semanticElement instanceof org.eclipse.uml2.uml.Gate);
		Assert.assertEquals("Check the name of the gate", GATE_NAME, ((Gate) semanticElement).getName());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram does not contain any additional element after undoing the creation of a Gate node", nbDiagramChild , diagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram contains one additional element after redoing the creation of a Gate node", nbDiagramChild + 1 , diagram.getChildren().size());
		element = diagram.getChildren().get(1);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		semanticElement = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Comment", semanticElement instanceof org.eclipse.uml2.uml.Gate);
		Assert.assertEquals("Check the name of the gate", GATE_NAME, ((Gate) semanticElement).getName());
		
	}

}
