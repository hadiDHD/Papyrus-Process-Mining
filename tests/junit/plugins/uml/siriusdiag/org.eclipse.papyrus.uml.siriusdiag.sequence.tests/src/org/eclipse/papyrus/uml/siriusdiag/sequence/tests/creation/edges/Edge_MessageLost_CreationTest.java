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
package org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.SequenceDiagramTestsTool;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DEdgeSpec;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeSpec;
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
 * Create a MessageLost edge between two lifelines
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/oneLifeline/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Edge_MessageLost_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";
	
	/** The created message name */
	private static final String MESSAGE_NAME = "Message1";
	
	/** The send event message name */
	private static final String SEND_EVENT_MESSAGE_NAME = "MessageOccurrenceSpecification1SendEvent";
	
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createMessageLostEdgeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbEdges = diagram.getEdges().size();

		Assert.assertEquals("The diagram must contain one Lifelines before creating the MessageLost", 1, diagram.getChildren().size());
		Assert.assertEquals("The diagram does not contain any edge", 0, diagram.getEdges().size());		

        // Create a MessageLost
		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain one Lifeline before creating a MessageLost", 1, lifeLines.size());
		Lifeline lifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();

		EdgeTarget lifeLineDRep = (EdgeTarget)lifeLine.getNotationNode().getElement();
		IGraphicalEditPart lifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(lifeLineDRep);
		Point messageAbsoluteStartConnectionPoint = lifelineEditPart.getFigure().getBounds().getBottomLeft().translate(0, 15);

		fixture.applyNodeCreationToolFromPalette("Message Lost", diagramRespresentation, lifeLineDRep, messageAbsoluteStartConnectionPoint, null);
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram must contain one additional edge after the creation of a MessageLost", nbEdges + 1 , diagram.getEdges().size());		
		Object element = diagram.getChildren().get(0);
		EObject siriusNewRepresentation = ((View) element).getElement();
		DNodeSpec lifeLineRep = (DNodeSpec) siriusNewRepresentation;
		EObject lifelineSemanticElement = lifeLineRep.getSemanticElements().iterator().next();

		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		EObject msgSemanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Message", msgSemanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((org.eclipse.uml2.uml.Message) msgSemanticElement).getName());

		// check the added Fragment elements
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		EList<InteractionFragment> fragments = interactionElem.getFragments();
		Assert.assertNotNull("The list of fragments is not null", fragments);
		Assert.assertEquals("One fragment have been added to the interaction element", 1, fragments.size());
	
		Object sendEvent = ((Message) msgSemanticElement).getSendEvent();
		Assert.assertTrue("The send event be a UML MessageOccurrenceSpecification", sendEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("Check the send event message name", SEND_EVENT_MESSAGE_NAME, ((MessageOccurrenceSpecification) sendEvent).getName());
		Assert.assertEquals("The send event is on the first Lifeline", ((MessageOccurrenceSpecification) sendEvent).getCovereds().get(0), lifelineSemanticElement);

		EList<EAnnotation> eAnnotations = ((MessageOccurrenceSpecification) sendEvent).getEAnnotations();
		Assert.assertEquals("The sender fragment has on eEAnnotation", 1, eAnnotations.size());
		Assert.assertEquals("Check the eAnnotation source", SEND_EVENT_MESSAGE_NAME + "LOSTMESSAGE", eAnnotations.get(0).getSource());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element does not contain any Fragment element after undoing the creation of the message", 0, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element does not contain any Message element after undoing the creation of the message", 0, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram does not contain any edge after undoing the creation of the Message edge", nbEdges, diagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element contains one Fragment element after redoing the creation of the message", 1, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element contains one Message element after redoing the creation of the message", 1, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram contains one edge after redoing the creation of the Message edge", nbEdges + 1, diagram.getEdges().size());
		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		msgSemanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Message", msgSemanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((org.eclipse.uml2.uml.Message) msgSemanticElement).getName());
	}
	
}
