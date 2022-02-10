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
package org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation;

import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.sequence.SequenceDDiagram;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Execution;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceElementAccessor;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceEvent;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Lifeline;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class DummyTest {

	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";


	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void dummyTest() {

		// Get diagram edit part as for all diagram
		final DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		final var diagramView = diagramEditpart.getDiagramView();
		// Get gmf notation.Diagram
		final Diagram diagram = diagramView;
		Assert.assertEquals("The diagram must be empty before creating the top node", 0, diagram.getChildren().size());

		// Create a lifeline from odesign tool -/org.eclipse.papyrus.uml.sirius.sequence.diagram/description/papyrus_sequence.odesign -  (id) 
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();
		// we need the sirius diagram representation
		fixture.applyNodeCreationToolFromPalette("Lifeline", diagramRepresentation, diagramRepresentation, new Point(100, 100), null);
		fixture.flushDisplayEvents();// it is required ?
		// lifeline is created and it is a GMF view notation.node
		Assert.assertEquals("The diagram must contain one element after creating a top node", 1, diagram.getChildren().size());

		// create a second lifeline to be able to create message 
		fixture.applyNodeCreationToolFromPalette("Lifeline", diagramRepresentation, diagramRepresentation, new Point(400, 100), null);
		fixture.flushDisplayEvents();// it is required ?
		// A second lifeline is created and it is a GMF view notation.node
		// - But we can navigate via Sequence digram API if necessary
		// - request the view 
		// -- the code is inspired from org.eclipse.sirius.tests.unit.diagram.sequence.structure.SequenceDiagramElementsIdentificationTests
		List<View> lifeLines = getView(Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain one element after creating a top node", 2, lifeLines.size());
		Lifeline lifeLine1 = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		Lifeline lifeLine2 = ISequenceElementAccessor.getLifeline(lifeLines.get(1)).get();

		
        // Create a message between 2 lifelines
		// from odesign tool -/org.eclipse.papyrus.uml.sirius.sequence.diagram/description/papyrus_sequence.odesign -  (id)
		final var lifeline1DRep = (EdgeTarget)lifeLine1.getNotationNode().getElement();
		final var lifeline2DRep = (EdgeTarget)lifeLine2.getNotationNode().getElement();
		IGraphicalEditPart lifelineAEditPart = (IGraphicalEditPart) fixture.findEditPart(lifeline1DRep);
		IGraphicalEditPart lifelineBEditPart = (IGraphicalEditPart)  fixture.findEditPart(lifeline2DRep);
		Point messageAbsoluteStartConnectionPoint = lifelineAEditPart.getFigure().getBounds().getTop().translate(0, 15);
		Point messageAbsoluteEndConnectionPoint = lifelineBEditPart.getFigure().getBounds().getTop().translate(0, 15);
		fixture.applyEdgeCreationToolFromPalette("CR_Synchronous_Operation", diagramRepresentation, lifeline1DRep, lifeline2DRep, messageAbsoluteStartConnectionPoint, messageAbsoluteEndConnectionPoint);
		fixture.flushDisplayEvents();// it is required ?

		
		List<View> executions = getView(Execution.viewpointElementPredicate());
		Assert.assertEquals("CR_Synchronous_Operation should create 1 execution", 1, executions.size());
		ISequenceEvent createdEvent = lifeLine2.getSubEvents().stream().filter(e -> e instanceof Execution).findFirst().get();
		Assert.assertEquals("The created event should be on the second lifeline", createdEvent.getNotationView(), executions.get(0)); 
		
		// Create a second execution
		// Execution position
		Rectangle endOfExecution = createdEvent.getProperLogicalBounds();
		Point point = endOfExecution.getBottomLeft().getCopy().translate(5, 0);
		fixture.applyNodeCreationToolFromPalette("Behavior Execution Specification", diagramRepresentation, lifeline2DRep, point, null);
		fixture.flushDisplayEvents();// it is required ?
		// ==> quick get
		ISequenceEvent execution2 = lifeLine2.getSubEvents().stream().filter(e -> (e instanceof Execution) && !e.equals(createdEvent)).findFirst().get();
		final var execution2Bounds = execution2.getProperLogicalBounds();
		int yMiddle = execution2Bounds.getTopLeft().y + execution2Bounds.height /2;
		final var execution2messageStartPoint = messageAbsoluteStartConnectionPoint.getCopy();
		execution2messageStartPoint.y = yMiddle;
		
        // Create a message between a lifeline and an execution
		final var execution2Rep = (EdgeTarget) execution2.getNotationView().getElement();
		fixture.applyEdgeCreationToolFromPalette("CR_Asynchronous_Operation", diagramRepresentation, lifeline1DRep, execution2Rep, execution2messageStartPoint, execution2Bounds.getCenter());
		fixture.flushDisplayEvents();// it is required ?

		
		// create combine fragment
		Point combinedFStart = lifeLine1.getProperLogicalBounds().getTopLeft();
		Point combinedEnd = endOfExecution.getBottomRight();
		Dimension cfSize = new Dimension(combinedEnd.x - combinedFStart.x , combinedEnd.y - combinedFStart.y);
		fixture.applyNodeCreationToolFromPalette("CombinedFragment", diagramRepresentation, diagramRepresentation, combinedFStart, cfSize);
		fixture.flushDisplayEvents();// it is required ?

		fixture.save();
		
	}

	// TODO  => there is aa accessor for view directy <Predicate<View> notationPredicate()> => replace ? to abstract class ?
	private List<View> getView(Predicate<DDiagramElement> viewpointElementPredicate) {
		final SequenceDDiagram element = (SequenceDDiagram) fixture.getActiveDiagram().getDiagramView().getElement();
		List<DDiagramElement> elements = Lists.newArrayList(Iterables.filter(element.getDiagramElements(), viewpointElementPredicate));
		assertFalse(elements.isEmpty());
		List<View> results = elements.stream().map(e -> fixture.findEditPart(e)).filter(e -> e instanceof IGraphicalEditPart).map(e -> ((IGraphicalEditPart) e).getNotationView()).collect(Collectors.toList());
		return results;
	}
}
