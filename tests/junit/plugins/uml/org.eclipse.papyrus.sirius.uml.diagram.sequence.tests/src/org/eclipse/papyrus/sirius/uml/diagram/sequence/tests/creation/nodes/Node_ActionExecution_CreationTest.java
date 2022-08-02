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
import org.eclipse.uml2.uml.ActionExecutionSpecification;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * ActionExecution node creation test
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/actionExecution/TopNode_ActionExecution_CreationTest.di") // the resource to import for the test in the workspace
public class Node_ActionExecution_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_ActionExecution_SequenceDiagram";
	
	private static final String EXECUTION_NAME = "ExecutionOccurrenceSpecification";
	
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createActionExecutionNodeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();
		Assert.assertEquals("The diagram must contains one Lifeline", 1, nbDiagramChild);		
		Assert.assertEquals("The root model contains one interaction element", 1, fixture.getModel().getOwnedElements().size());
		
		// check the initial fragments of the interaction element
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		EList<InteractionFragment> fragments = interactionElem.getFragments();
		int fragmentsSize = fragments.size();
		Assert.assertEquals("The interaction element does not contain any fragment", 0, fragmentsSize);

		// Compute the execution position 
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();		
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Assert.assertEquals("The diagram must contain one lifeline", 1, lifeLines.size());
		Lifeline lifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		EdgeTarget lifelineDRep = (EdgeTarget)lifeLine.getNotationNode().getElement();
		IGraphicalEditPart lifelineEditPart = (IGraphicalEditPart) fixture.findEditPart(lifelineDRep);
    	Point executionPosition = lifelineEditPart.getFigure().getBounds().getTop().translate(0, 15);
		
		// Create the execution on the lifeline
		fixture.applyNodeCreationToolFromPalette("Action Execution Specification", diagramRepresentation, lifelineDRep, executionPosition, null);
		fixture.flushDisplayEvents();	
		
		Assert.assertEquals("The diagram contains two additional elements after creating an ActionExecution node", nbDiagramChild + 2, diagram.getChildren().size());
		Object element = diagram.getChildren().get(1);
		Assert.assertTrue("The created first element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		EObject executionStart = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created first element must be an UML ExecutionOccurrenceSpecification", executionStart instanceof org.eclipse.uml2.uml.ExecutionOccurrenceSpecification);
		Assert.assertEquals("Check the execution start name", EXECUTION_NAME + "1start", ((ExecutionOccurrenceSpecification) executionStart).getName());
		Assert.assertEquals("The execution is on the lifeline", ((DNodeSpec) lifelineDRep).getTarget(), ((ExecutionOccurrenceSpecification) executionStart).getCovered());

		element = diagram.getChildren().get(2);
		Assert.assertTrue("The created second element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		EObject executionEnd = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created second element must be a UML ExecutionOccurrenceSpecification", executionEnd instanceof org.eclipse.uml2.uml.ExecutionOccurrenceSpecification);
		Assert.assertEquals("check the execution finish name", EXECUTION_NAME + "2finish", ((ExecutionOccurrenceSpecification) executionEnd).getName());
		Assert.assertEquals("The execution is on the lifeline", ((DNodeSpec) lifelineDRep).getTarget(), ((ExecutionOccurrenceSpecification) executionEnd).getCovered());

		Object createdExecution = ((ExecutionOccurrenceSpecification) executionEnd).getExecution();
		Assert.assertTrue("The created execution must be an UML ActionExecutionSpecification", createdExecution instanceof org.eclipse.uml2.uml.ActionExecutionSpecification);
		Assert.assertEquals("Check the execution start element", executionStart, ((ActionExecutionSpecification) createdExecution).getStart());
		Assert.assertEquals("Check the execution finsh element", executionEnd, ((ActionExecutionSpecification) createdExecution).getFinish());

		// check the added fragments
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		Assert.assertEquals("Three fragments have been added to the interaction element", fragmentsSize + 3, interactionElem.getFragments().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram does not contain any additional element after undoing the creation of an ActionExecution node", nbDiagramChild, diagram.getChildren().size());
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		fragments = interactionElem.getFragments();
		Assert.assertEquals("Three interaction element does not contain any fragments after undoing the creation of an ActionExecution node", 0, fragments.size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram contains two additional elements after redoing the creation of an ActionExecution node", nbDiagramChild + 2, diagram.getChildren().size());
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		fragments = interactionElem.getFragments();
		Assert.assertEquals("Three interaction element contains three fragments after redoing the creation of an ActionExecution node", 3, fragments.size());

		element = diagram.getChildren().get(1);
		Assert.assertTrue("The created first element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		executionStart = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created first element must be a UML ExecutionOccurrenceSpecification", executionStart instanceof org.eclipse.uml2.uml.ExecutionOccurrenceSpecification);
		Assert.assertEquals("Check the execution start name", EXECUTION_NAME + "1start", ((ExecutionOccurrenceSpecification) executionStart).getName());
		Assert.assertEquals("The execution is on the lifeline", ((DNodeSpec) lifelineDRep).getTarget(), ((ExecutionOccurrenceSpecification) executionStart).getCovered());

		element = diagram.getChildren().get(2);
		Assert.assertTrue("The created second element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		executionEnd = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created second element must be a UML ExecutionOccurrenceSpecification", executionEnd instanceof org.eclipse.uml2.uml.ExecutionOccurrenceSpecification);
		Assert.assertEquals("check the execution finish name", EXECUTION_NAME + "2finish", ((ExecutionOccurrenceSpecification) executionEnd).getName());
		Assert.assertEquals("The execution is on the lifeline", ((DNodeSpec) lifelineDRep).getTarget(), ((ExecutionOccurrenceSpecification) executionEnd).getCovered());

		createdExecution = ((ExecutionOccurrenceSpecification) executionEnd).getExecution();
		Assert.assertTrue("The created execution must be an UML ActionExecutionSpecification", createdExecution instanceof org.eclipse.uml2.uml.ActionExecutionSpecification);
		Assert.assertEquals("Check the execution start element", executionStart, ((ActionExecutionSpecification) createdExecution).getStart());
		Assert.assertEquals("Check the execution finish element", executionEnd, ((ActionExecutionSpecification) createdExecution).getFinish());
	}
	
}
