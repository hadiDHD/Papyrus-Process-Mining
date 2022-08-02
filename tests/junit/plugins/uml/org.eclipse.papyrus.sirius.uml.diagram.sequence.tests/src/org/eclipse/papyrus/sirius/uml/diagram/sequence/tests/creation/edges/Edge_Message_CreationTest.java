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
import org.eclipse.uml2.uml.MessageSort;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Create a Message edge between two lifelines
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/twoLifelines/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Edge_Message_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";
	
	/** The created message name */
	private static final String MESSAGE_NAME = "Message1";
	
	/** The send event message name */
	private static final String SEND_EVENT_MESSAGE_NAME = "MessageOccurrenceSpecification1SendEvent";
	
	/** The receive event message name */
	private static final String RECEIVER_EVENT_MESSAGE_NAME = "MessageOccurrenceSpecification2ReceiveEvent";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createMessageEdgeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbEdges = diagram.getEdges().size();

		Assert.assertEquals("The diagram must contain two Lifelines before creating the Message", 2, diagram.getChildren().size());
		Assert.assertEquals("The diagram does not contain any edge", 0, diagram.getEdges().size());		

        // Create a message between the two lifelines
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram contains two Lifelines", 2, lifeLines.size());
		Lifeline firstLifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		Lifeline secondLifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(1)).get();

		EdgeTarget firstLifelineDRep = (EdgeTarget)firstLifeLine.getNotationNode().getElement();
		EdgeTarget secondLifelineDRep = (EdgeTarget)secondLifeLine.getNotationNode().getElement();
		IGraphicalEditPart firstLifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(firstLifelineDRep);
		IGraphicalEditPart secondLifelineEditPart = (IGraphicalEditPart)  fixture.findEditPart(secondLifelineDRep);
		Point messageAbsoluteStartConnectionPoint = firstLifelineEditPart.getFigure().getBounds().getTopLeft().translate(0, 15);
		Point messageAbsoluteEndConnectionPoint = secondLifelineEditPart.getFigure().getBounds().getTopLeft().translate(0, 15);		
		fixture.applyEdgeCreationToolFromPalette("Message Create", diagramRepresentation, firstLifelineDRep, secondLifelineDRep, messageAbsoluteStartConnectionPoint, messageAbsoluteEndConnectionPoint);
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram must contain one addiional edge after the creation of a Message", nbEdges + 1 , diagram.getEdges().size());		
		Object element = diagram.getChildren().get(0);
		EObject siriusNewRepresentation = ((View) element).getElement();
		DNodeSpec lifeLineRep = (DNodeSpec) siriusNewRepresentation;
		EObject firstLifelineSemanticElement = lifeLineRep.getSemanticElements().iterator().next();
		element = diagram.getChildren().get(1);
		siriusNewRepresentation = ((View) element).getElement();
		lifeLineRep = (DNodeSpec) siriusNewRepresentation;
		EObject secondLifelineSemanticElement = lifeLineRep.getSemanticElements().iterator().next();

		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		EObject msgSemanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Message", msgSemanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((org.eclipse.uml2.uml.Message) msgSemanticElement).getName());
		Assert.assertEquals("Check the message sort", MessageSort.CREATE_MESSAGE_LITERAL, ((org.eclipse.uml2.uml.Message) msgSemanticElement).getMessageSort());

		// check the added Fragment elements
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		EList<InteractionFragment> fragments = interactionElem.getFragments();
		Assert.assertNotNull("The list of fragments is not null", fragments);
		Assert.assertEquals("Two fragments have been added to the interaction element", 2, fragments.size());
		
		Object sendEvent = ((Message) msgSemanticElement).getSendEvent();
		Assert.assertTrue("The send event be a UML MessageOccurrenceSpecification", sendEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("Check the send event message name", SEND_EVENT_MESSAGE_NAME, ((MessageOccurrenceSpecification) sendEvent).getName());
		Assert.assertEquals("The send event is on the first Lifeline", ((MessageOccurrenceSpecification) sendEvent).getCovereds().get(0), firstLifelineSemanticElement);
		Object receiveEvent = ((Message) msgSemanticElement).getReceiveEvent();
		Assert.assertTrue("The receive event must be a UML MessageOccurrenceSpecification", receiveEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("Check the receive event message name", RECEIVER_EVENT_MESSAGE_NAME, ((MessageOccurrenceSpecification) receiveEvent).getName());
		Assert.assertEquals("The receiver event is on the second Lifeline", ((MessageOccurrenceSpecification) receiveEvent).getCovereds().get(0), secondLifelineSemanticElement);

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element does not contain any Fragment element after undoing the creation of the message", 0, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element does not contain any Message element after undoing the creation of the message", 0, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram does not contain any edge after undoing the creation of the Message edge", nbEdges, diagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element contains two Fragment element after redoing the creation of the message", 2, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element contains one Message element after redoing the creation of the message", 1, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram contains one edge after redoing the creation of the Message edge", nbEdges + 1, diagram.getEdges().size());
		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		msgSemanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Message", msgSemanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((org.eclipse.uml2.uml.Message) msgSemanticElement).getName());
		Assert.assertEquals("Check the message sort", MessageSort.CREATE_MESSAGE_LITERAL, ((org.eclipse.uml2.uml.Message) msgSemanticElement).getMessageSort());

	}
	
}
