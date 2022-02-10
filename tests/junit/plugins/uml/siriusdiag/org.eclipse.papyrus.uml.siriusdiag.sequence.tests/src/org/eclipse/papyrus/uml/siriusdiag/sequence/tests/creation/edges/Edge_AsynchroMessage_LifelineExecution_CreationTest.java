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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.sirius.diagram.model.business.internal.spec.DEdgeSpec;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Execution;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceElementAccessor;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceEvent;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Lifeline;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.Signal;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Create an Asynchronous message edge between a Lifeline and an execution
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/twoLifelines/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Edge_AsynchroMessage_LifelineExecution_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";
	
	/** The execution specification name */
	private static final String EXECUTION_NAME = "ExecutionOccurrenceSpecification";
	
	/** The created message name */
	private static final String MESSAGE_NAME = "Message_0";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createAsynchroMessageEdgeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbElement = fixture.getModel().getOwnedElements().size();
		int nbDiagramChild = diagram.getChildren().size();
		int nbEdges = diagram.getEdges().size();

		Assert.assertEquals("The diagram contains two lifelines before the creation of an AsynchronousMessage", 2, nbDiagramChild);		
		Assert.assertEquals("The root model contains one interaction element", 1, nbElement);
		Assert.assertEquals("The diagram does not contain any edge", 0, nbEdges);

		// check the initial fragments
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		EList<InteractionFragment> fragments = interactionElem.getFragments();
		int fragmentsSize = fragments.size();
		Assert.assertEquals("The interaction element does not contain any fragment", 0, fragmentsSize);

		// Compute the execution position 
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();		
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain two lifelines", 2, lifeLines.size());
		Lifeline firstLifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		Lifeline secondLifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(1)).get();		
		EdgeTarget firstLifelineDRep = (EdgeTarget)firstLifeLine.getNotationNode().getElement();
		EdgeTarget secondLifelineDRep = (EdgeTarget)secondLifeLine.getNotationNode().getElement();
		IGraphicalEditPart firstLifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(firstLifelineDRep);
    	Point executionPosition = firstLifelineEditPart.getFigure().getBounds().getTop().translate(0, 15);
		
		// 1. Create the execution on the second lifeline
		fixture.applyNodeCreationToolFromPalette("Behavior Execution Specification", diagramRepresentation, secondLifelineDRep, executionPosition, null);
		fixture.flushDisplayEvents();	
		
		Assert.assertEquals("The diagram contains two additional elements after creating a BehaviorExecution node", nbDiagramChild + 2, diagram.getChildren().size());

		// check the added first element
		Object element = diagram.getChildren().get(2);
		Assert.assertTrue("The created first element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		EObject executionStart = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created first element must be an UML ExecutionOccurrenceSpecification", executionStart instanceof org.eclipse.uml2.uml.ExecutionOccurrenceSpecification);
		Assert.assertEquals("Check the execution start name", EXECUTION_NAME + "1start", ((ExecutionOccurrenceSpecification) executionStart).getName());
		Assert.assertEquals("The created execution is on the second Lifeline", ((DNodeSpec) secondLifelineDRep).getTarget(), ((ExecutionOccurrenceSpecification) executionStart).getCovered());

		// check the added second element
		element = diagram.getChildren().get(3);
		Assert.assertTrue("The created second element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		EObject executionEnd = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created second element must be an UML ExecutionOccurrenceSpecification", executionEnd instanceof org.eclipse.uml2.uml.ExecutionOccurrenceSpecification);
		Assert.assertEquals("Check the execution finsih name", EXECUTION_NAME + "2finish", ((ExecutionOccurrenceSpecification) executionEnd).getName());
		Assert.assertEquals("The created execution is on the second Lifeline", ((DNodeSpec) secondLifelineDRep).getTarget(), ((ExecutionOccurrenceSpecification) executionEnd).getCovered());

		Object createdExecution = ((ExecutionOccurrenceSpecification) executionEnd).getExecution();
		Assert.assertTrue("The created execution must be a UML BehaviorExecutionSpecification", createdExecution instanceof org.eclipse.uml2.uml.BehaviorExecutionSpecification);
		Assert.assertEquals("Check the execution start element", executionStart, ((BehaviorExecutionSpecification) createdExecution).getStart());
		Assert.assertEquals("Check the execution finish element", executionEnd, ((BehaviorExecutionSpecification) createdExecution).getFinish());

		// check the added fragments
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		fragments = interactionElem.getFragments();
		Assert.assertEquals("Three fragments have been added to the interaction element", fragmentsSize + 3, fragments.size());
		fragmentsSize = fragments.size();

		// 2. Create an Asynchronous message between the created execution and the first lifeline
		// Compute the message start point
		ISequenceEvent execution = secondLifeLine.getSubEvents().stream().filter(e -> (e instanceof Execution)).findFirst().get();
		Rectangle  executionBounds = execution.getProperLogicalBounds();
		int yMiddle = executionBounds.getTopLeft().y + executionBounds.height /2;
		Point executionMessageStartPoint = executionPosition.getCopy();
		executionMessageStartPoint.y = yMiddle;
		
        // Create the message
		EdgeTarget executionRep = (EdgeTarget) execution.getNotationView().getElement();
		fixture.applyEdgeCreationToolFromPalette("CR_Asynchronous_Operation", diagramRepresentation, firstLifelineDRep, executionRep, executionMessageStartPoint, executionBounds.getCenter());
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram must contain one additional edge", nbEdges + 1, diagram.getEdges().size());		
		Assert.assertEquals("The root model contains one additional element after the creation of an Asynchronus message", nbElement + 1 , fixture.getModel().getOwnedElements().size());
		
		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		EObject msgSemanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be a UML Message", msgSemanticElement instanceof org.eclipse.uml2.uml.Message);
		
		// check the added message fragments
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		fragments = interactionElem.getFragments();
		Assert.assertEquals("Two fragments have been added to the interaction element", fragmentsSize + 2, fragments.size());

		Object sendEvent = ((Message) msgSemanticElement).getSendEvent();
		Assert.assertTrue("The send event must be an UML MessageOccurrenceSpecification", sendEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("The send event must be on the first lifeline", ((DNodeSpec) firstLifelineDRep).getTarget(), ((MessageOccurrenceSpecification) sendEvent).getCovered());
		Assert.assertEquals("Check the send event message name", MESSAGE_NAME  + "_sender", ((MessageOccurrenceSpecification) sendEvent).getName());
		Object receiveEvent = ((Message) msgSemanticElement).getReceiveEvent();
		Assert.assertTrue("The receive event must be an UML MessageOccurrenceSpecification", receiveEvent instanceof org.eclipse.uml2.uml.MessageOccurrenceSpecification);
		Assert.assertEquals("The receive event must be on the second lifeline", ((DNodeSpec) secondLifelineDRep).getTarget(), ((MessageOccurrenceSpecification) receiveEvent).getCovered());
		Assert.assertEquals("Check the receive event message name", MESSAGE_NAME  + "_receiver", ((MessageOccurrenceSpecification) receiveEvent).getName());
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((Message) msgSemanticElement).getName());
	
		// check the created Signal element
		EObject signalElem = fixture.getModel().getOwnedElements().get(1);
		Assert.assertTrue("An UML Signal element is created", signalElem instanceof org.eclipse.uml2.uml.Signal);
		Assert.assertEquals("Check the name of the created Signal element", MESSAGE_NAME + "_signal", ((Signal) signalElem).getName());
	
		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element contains only the fragments corresponding to the Execution element after undoing teh creation of the message", 3, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element does not contain any Message after undoing teh creation of the message", 0, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram does not contain any edge after undoing the creation of the Message", nbEdges, diagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The interaction element contains five Fragments element after redoing teh creation of the message", 5, interactionElem.getFragments().size());
		Assert.assertEquals("The interaction element contains one Message after redoing teh creation of the message", 1, interactionElem.getMessages().size());
		Assert.assertEquals("The diagram contains one edge after redoing the creation of the Message", nbEdges + 1, diagram.getEdges().size());
		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		msgSemanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Message", msgSemanticElement instanceof org.eclipse.uml2.uml.Message);
		Assert.assertEquals("Check the message name", MESSAGE_NAME, ((org.eclipse.uml2.uml.Message) msgSemanticElement).getName());
	}

}
