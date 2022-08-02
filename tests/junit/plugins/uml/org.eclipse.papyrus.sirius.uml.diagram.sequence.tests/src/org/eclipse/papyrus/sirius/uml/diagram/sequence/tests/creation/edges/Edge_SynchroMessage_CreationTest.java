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
package org.eclipse.papyrus.sirius.uml.diagram.sequence.tests.creation.edges;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.sirius.diagram.model.business.internal.spec.DEdgeSpec;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceElementAccessor;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Lifeline;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Create a synchronous message edge between two lifelines
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/empty/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Edge_SynchroMessage_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";	
	
	/** The created message name */
	private static final String MESSAGE_NAME = "Message_0";


	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createSynchroMessageEdgeTest() {

		int nbElement = fixture.getModel().getOwnedElements().size();

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();
		int nbEdges = diagram.getEdges().size();

		Assert.assertEquals("The diagram must be empty before creating the top node", 0, nbDiagramChild);
		Assert.assertEquals("The diagram does not contain any edge", 0, nbEdges);		

		// Create a first lifeline 
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();
		fixture.applyNodeCreationToolFromPalette("Lifeline", diagramRepresentation, diagramRepresentation, new Point(100, 100), null);
		fixture.flushDisplayEvents();
		
		// lifeline is created 
		Assert.assertEquals("The diagram must contain one additional element after creating the first Lifeline", nbDiagramChild + 1, diagram.getChildren().size());		
		Object element = diagram.getChildren().get(0);
		nbDiagramChild = diagram.getChildren().size();
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		DNodeSpec firstLifeLineRep = (DNodeSpec) siriusNewRepresentation;
		EObject firstLifelineSemanticElement = firstLifeLineRep.getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be an UML Lifeline", firstLifelineSemanticElement instanceof org.eclipse.uml2.uml.Lifeline);
		Assert.assertEquals("The root model does not contain any additional element after the creation of a Lifeline", nbElement, fixture.getModel().getOwnedElements().size());
		
		// create a second lifeline to be able to create message 
		fixture.applyNodeCreationToolFromPalette("Lifeline", diagramRepresentation, diagramRepresentation, new Point(400, 100), null);
		fixture.flushDisplayEvents();
		
		// A second lifeline is created and it is a GMF view notation.node
		Assert.assertEquals("The diagram must contain one additional element after creating the second Lifeline", nbDiagramChild + 1, diagram.getChildren().size());		
		element = diagram.getChildren().get(1);
		nbDiagramChild = diagram.getChildren().size();
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();		
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		DNodeSpec secondLifeLineRep = (DNodeSpec) siriusNewRepresentation;
		EObject secondLifelineSemanticElement = ((DNodeSpec) secondLifeLineRep).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be an UML Lifeline", secondLifelineSemanticElement instanceof org.eclipse.uml2.uml.Lifeline);
		
        // Create a message between the two created lifelines
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain two Lifelines", 2, lifeLines.size());
		Lifeline firstLifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		Lifeline secondLifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(1)).get();

		EdgeTarget firstLifelineDRep = (EdgeTarget)firstLifeLine.getNotationNode().getElement();
		EdgeTarget secondLifelineDRep = (EdgeTarget)secondLifeLine.getNotationNode().getElement();
		IGraphicalEditPart firstLifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(firstLifelineDRep);
		IGraphicalEditPart secondLifelineEditPart = (IGraphicalEditPart)  fixture.findEditPart(secondLifelineDRep);
		Point messageAbsoluteStartConnectionPoint = firstLifelineEditPart.getFigure().getBounds().getTop().translate(0, 15);
		Point messageAbsoluteEndConnectionPoint = secondLifelineEditPart.getFigure().getBounds().getTop().translate(0, 15);		
		fixture.applyEdgeCreationToolFromPalette("CR_Synchronous_Operation", diagramRepresentation, firstLifelineDRep, secondLifelineDRep, messageAbsoluteStartConnectionPoint, messageAbsoluteEndConnectionPoint);
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram must contain 4 additional elements after creating a Synchronous message correspondin to the created execution", 6, diagram.getChildren().size());	
		Assert.assertEquals("The diagram must contain 2 edges after creating a Synchronous message", nbEdges + 2, diagram.getEdges().size());		
		Assert.assertEquals("The diagram contains 4 additional elements after creating a BehaviorExecution node", nbDiagramChild + 4, diagram.getChildren().size());
		
		// check the created messages
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		EList<InteractionFragment> fragments = interactionElem.getFragments();
		Assert.assertNotNull("The list of fragments is not null", fragments);
		Assert.assertEquals("Five fragments have been added to the interaction element", 5, fragments.size());	
		
		// check the created message
		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		EObject semanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Message", semanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((Message) semanticElement).getName());

		Object sendEvent = ((Message) semanticElement).getSendEvent();
		Assert.assertTrue("The send event must be an UML MessageOccurrenceSpecification", sendEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("The send event must be on the first lifeline", ((DNodeSpec) firstLifelineDRep).getTarget(), ((MessageOccurrenceSpecification) sendEvent).getCovered());
		Assert.assertEquals("Check the send event message name", MESSAGE_NAME  + "_sender", ((MessageOccurrenceSpecification) sendEvent).getName());
		Object receiveEvent = ((Message) semanticElement).getReceiveEvent();
		Assert.assertTrue("The receive event must be an UML MessageOccurrenceSpecification", receiveEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("The receive event must be on the second lifeline", ((DNodeSpec) secondLifelineDRep).getTarget(), ((MessageOccurrenceSpecification) receiveEvent).getCovered());
		Assert.assertEquals("Check the receive event message name", MESSAGE_NAME  + "_receiver", ((MessageOccurrenceSpecification) receiveEvent).getName());
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((Message) semanticElement).getName());

		// check the reply message
		element = diagram.getEdges().get(1);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		semanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be a UML Message", semanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME + "_reply", ((Message) semanticElement).getName());

		sendEvent = ((Message) semanticElement).getSendEvent();
		Assert.assertTrue("The send event must be an UML MessageOccurrenceSpecification", sendEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("Check the send event message name", MESSAGE_NAME  + "_reply_sender", ((MessageOccurrenceSpecification) sendEvent).getName());
		Assert.assertEquals("The send event must be on the seond Lifeline", ((MessageOccurrenceSpecification) sendEvent).getCovereds().get(0), secondLifelineSemanticElement);

		receiveEvent = ((Message) semanticElement).getReceiveEvent();
		Assert.assertTrue("The receive event must be an UML MessageOccurrenceSpecification", receiveEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("Check the receive event message name", MESSAGE_NAME  + "_reply_receiver", ((MessageOccurrenceSpecification) receiveEvent).getName());
		Assert.assertEquals("The receive event must be on the first Lifeline", ((MessageOccurrenceSpecification) receiveEvent).getCovereds().get(0), firstLifelineSemanticElement);

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element does not contain any fragments after undoing the creation of the message", 0, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element does not contain any Message after undoing the creation of the message", 0, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram does not contain any edge after undoing the creation of the Message", 0, diagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element contains five Fragments element after redoing the creation of the message", 5, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element contains one Message after redoing the creation of the message", 2, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram contains one edge after redoing the creation of the Message", 2, diagram.getEdges().size());
		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		semanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Message", semanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((org.eclipse.uml2.uml.Message) semanticElement).getName());
	}
	
}
