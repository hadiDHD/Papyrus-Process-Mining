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
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Execution;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceElementAccessor;
import org.eclipse.uml2.uml.ActionExecutionSpecification;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Create a DurationObservation edge between two action executions
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/twoActionExecutions/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Edge_DurationObservation_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createDurationObservationEdgeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();
		int nbEdges = diagram.getEdges().size();
		int nbElement = fixture.getModel().getOwnedElements().size();

		Assert.assertEquals("The diagram must contain one lifeline and two Action exectuions before creating the DurationObservation edge", nbDiagramChild, diagram.getChildren().size());
		Assert.assertEquals("The diagram does not contain any edge", nbEdges, diagram.getEdges().size());		
		Assert.assertEquals("The root element contains only the interaction element", nbElement, fixture.getModel().getOwnedElements().size());		

        // Create a DurationObservation between the two Action executions
		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		List<View> actionExecutions = SequenceDiagramTestsTool.getView(fixture, Execution.viewpointElementPredicate());
		Execution firstExecution = ISequenceElementAccessor.getExecution(actionExecutions.get(0)).get();
		Execution secondExecution = ISequenceElementAccessor.getExecution(actionExecutions.get(1)).get();

		EdgeTarget firstExecutionDRep = (EdgeTarget)firstExecution.getNotationNode().getElement();
		EdgeTarget secondExecutionDRep = (EdgeTarget)secondExecution.getNotationNode().getElement();
		IGraphicalEditPart firstExecutionEditPart = (IGraphicalEditPart) fixture.findEditPart(firstExecutionDRep);
		IGraphicalEditPart secondExecutionEditPart = (IGraphicalEditPart)  fixture.findEditPart(secondExecutionDRep);
		Point startConnectionPoint = firstExecutionEditPart.getFigure().getBounds().getTop().translate(0, 15);
		Point endConnectionPoint = secondExecutionEditPart.getFigure().getBounds().getTop().translate(0, 15);		
		fixture.applyEdgeCreationToolFromPalette("Duration Observation", diagramRespresentation, firstExecutionDRep, secondExecutionDRep, startConnectionPoint, endConnectionPoint);
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram must contain one edge after creating a DurationObservation", nbEdges + 1, diagram.getEdges().size());	
	    Assert.assertEquals("The root element contains one additional elements after the creation of a DurationObservation", nbElement + 1, fixture.getModel().getOwnedElements().size());		

	    Object durationElement = fixture.getModel().getOwnedElements().get(1);
		Assert.assertTrue("The first created element is an UML DurationObservation", durationElement instanceof org.eclipse.uml2.uml.DurationObservation);
		
		Object element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		EObject semanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML DurationObservation", semanticElement instanceof org.eclipse.uml2.uml.DurationObservation);
		Assert.assertEquals("The observation element is applied to two elements", 2, ((org.eclipse.uml2.uml.DurationObservation) semanticElement).getEvents().size());
	
		Object firstEvent = ((org.eclipse.uml2.uml.DurationObservation) semanticElement).getEvents().get(0);
		Object secondEvent = ((org.eclipse.uml2.uml.DurationObservation) semanticElement).getEvents().get(1);		
		Object firstExecEnd = ((ActionExecutionSpecification ) ((DNodeSpec) firstExecutionDRep).getTarget()).getFinish();
		Object secondExecBegin = ((ActionExecutionSpecification ) ((DNodeSpec) secondExecutionDRep).getTarget()).getStart();		
		Assert.assertEquals("The duration observation starts at the end of the first execution", firstExecEnd, firstEvent);
		Assert.assertEquals("The duration observation ends at the begin of the second execution", secondExecBegin, secondEvent);

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram does not contain any edge after undoing the creation of a DurationObservation edge", nbEdges, diagram.getEdges().size());	
	    Assert.assertEquals("The root element does not contain any additional elements after undoing the creation of a DurationObservation edge", nbElement, fixture.getModel().getOwnedElements().size());		

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must contain one edge after redoing the creation of a DurationObservation message", nbEdges + 1, diagram.getEdges().size());	
	    Assert.assertEquals("The root element contains two additional elements after redoing the creation of a DurationObservation", nbElement + 1, fixture.getModel().getOwnedElements().size());		

	    durationElement = fixture.getModel().getOwnedElements().get(1);
		Assert.assertTrue("The created element is an UML DurationObservation", durationElement instanceof org.eclipse.uml2.uml.DurationObservation);
		
		element = diagram.getEdges().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		semanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML DurationObservation", semanticElement instanceof org.eclipse.uml2.uml.DurationObservation);
		Assert.assertEquals("The duration observation is applied to two elements", 2, ((org.eclipse.uml2.uml.DurationObservation) semanticElement).getEvents().size());
	}
	
}
