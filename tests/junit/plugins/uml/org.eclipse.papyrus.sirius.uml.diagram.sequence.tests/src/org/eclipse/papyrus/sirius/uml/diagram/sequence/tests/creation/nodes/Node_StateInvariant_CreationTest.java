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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.StateInvariant;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * StateInvariant node creation test
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/oneLifeline/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Node_StateInvariant_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createStateInvariantNodeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();

		Assert.assertEquals("The diagram must contains one Lifeline", 1, diagram.getChildren().size());		
		Assert.assertEquals("The root model contains one interaction element", 1, fixture.getModel().getOwnedElements().size());

		// Compute the StateInvariant position 
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();		
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain one Lifeline", 1, lifeLines.size());
		Lifeline lifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		EdgeTarget lifelineDRep = (EdgeTarget)lifeLine.getNotationNode().getElement();
		IGraphicalEditPart lifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(lifelineDRep);
    	Point statePosition = lifelineEditPart.getFigure().getBounds().getTop().translate(0, 15);
		
		// Create the StateInvariant on the lifeline
		fixture.applyNodeCreationToolFromPalette("State Invariant", diagramRepresentation, lifelineDRep, statePosition, null);
		fixture.flushDisplayEvents();	
		
		Assert.assertEquals("The diagram contains one additional element after the creation of a StateInvariant node", 2 , diagram.getChildren().size());
				
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		EList<InteractionFragment> fragments = interactionElem.getFragments();
		Assert.assertNotNull("The list of fragments is not null", fragments);
		int fragmentsSize = fragments.size();
		Assert.assertEquals("One fragment has been added to the interaction element", 1, fragmentsSize);
		
		InteractionFragment fragment = fragments.iterator().next();
		Assert.assertTrue("The created element must be an UML StateInvariant", fragment instanceof org.eclipse.uml2.uml.StateInvariant);
		Assert.assertEquals("The StateVariant is on the Lifeline", ((DNodeSpec) lifelineDRep).getTarget(), ((StateInvariant) fragment).getCovereds().get(0));

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram does not contain any additional element after undoing the creation of a StateInvariant node", nbDiagramChild , diagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram contains one additional element after redoing the creation of a StateInvariant node", 2 , diagram.getChildren().size());
		fragment = fragments.iterator().next();
		Assert.assertTrue("The created element must be an UML StateInvariant", fragment instanceof org.eclipse.uml2.uml.StateInvariant);
		Assert.assertEquals("The StateVariant is on the Lifeline", ((DNodeSpec) lifelineDRep).getTarget(), ((StateInvariant) fragment).getCovereds().get(0));
	}

}
