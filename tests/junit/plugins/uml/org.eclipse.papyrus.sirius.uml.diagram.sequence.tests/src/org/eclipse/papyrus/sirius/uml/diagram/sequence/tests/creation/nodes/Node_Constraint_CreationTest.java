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
import org.eclipse.uml2.uml.Interaction;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Constraint node creation test
 * @author battal
 * 
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/oneLifeline/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Node_Constraint_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createCommentNodeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();

		Assert.assertEquals("The diagram contains one Lifeline", 1, nbDiagramChild);		
		Assert.assertEquals("The root model contains one interaction element", 1, fixture.getModel().getOwnedElements().size());
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		Assert.assertEquals("The interaction element does not contain any Constraint element", 0, interactionElem.getOwnedRules().size());

		// Compute the Constraint position 
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();		
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain one lifeline", 1, lifeLines.size());
		Lifeline lifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		EdgeTarget lifelineDRep = (EdgeTarget)lifeLine.getNotationNode().getElement();
		IGraphicalEditPart lifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(lifelineDRep);
    	Point constraintPosition = lifelineEditPart.getFigure().getBounds().getTop().translate(5, 15);
		
		// Create the Constraint 
		fixture.applyNodeCreationToolFromPalette("Constraint", diagramRepresentation, diagramRepresentation, constraintPosition, new Dimension(150,80));
		fixture.flushDisplayEvents();	
		
		Assert.assertEquals("The diagram contains one additional element after creating a top node", nbDiagramChild + 1, diagram.getChildren().size());
		Object element = diagram.getChildren().get(1);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		EObject semanticElement = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Constraint", semanticElement instanceof org.eclipse.uml2.uml.Constraint);
		Assert.assertEquals("The interaction element contains one Constraint element", 1, interactionElem.getOwnedRules().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The interaction element does not contain any Constraint element after undoing the creation of the Constraint", 0, interactionElem.getOwnedRules().size());
		Assert.assertEquals("The diagram does not contain any additional element after undoing the creation of the Constraint", nbDiagramChild, diagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram contains one additional element after redoing the creation of the Constraint", nbDiagramChild + 1, diagram.getChildren().size());
		element = diagram.getChildren().get(1);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		semanticElement = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Constraint", semanticElement instanceof org.eclipse.uml2.uml.Constraint);
		Assert.assertEquals("The interaction element contains one Constraint element", 1, interactionElem.getOwnedRules().size());
	}
	
}
