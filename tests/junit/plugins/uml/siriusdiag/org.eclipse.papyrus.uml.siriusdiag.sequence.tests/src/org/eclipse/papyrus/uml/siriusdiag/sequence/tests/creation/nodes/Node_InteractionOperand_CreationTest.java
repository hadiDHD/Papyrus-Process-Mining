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
package org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceElementAccessor;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * InteractionOperand node creation test
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/combinedFragment/InteractionOperand_CreationTest.di") // the resource to import for the test in the workspace
public class Node_InteractionOperand_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "SequenceDiagram";
	
	/** The first created interaction operand name */
	private static final String INTERACTION_OPERAND1_NAME = "InteractionOperand2";
	
	/** The second created interaction operand name */
	private static final String INTERACTION_OPERAND2_NAME = "InteractionOperand3";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createInteractionOperandNodeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();
		Assert.assertEquals("The diagram must contains one Lifeline and one CombinedFragment before creating the InteractionOperand node", 4, nbDiagramChild);

		// check the initial Operand elements
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		InteractionFragment combinedFragment = interactionElem.getFragments().get(0);
		EList<InteractionOperand> operands = ((CombinedFragment) combinedFragment).getOperands();
		Assert.assertNotNull("The list of operands is not null", operands);
		int operandsSize = operands.size();
		Assert.assertEquals("The combinedFragment conatins only one InteractionOperand by default before the creation of an InteractionOperand node", operandsSize, operands.size());
		
		// get the position of the InteractinOperand to be created
		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();		
		Object interactionOperandContainer =  diagram.getChildren().get(1);
		org.eclipse.sirius.diagram.sequence.business.internal.elements.CombinedFragment cf = ISequenceElementAccessor.getCombinedFragment((View) interactionOperandContainer).get();
		EdgeTarget cfDRep = (EdgeTarget)cf.getNotationNode().getElement();
		IGraphicalEditPart cfEditPart = (IGraphicalEditPart) fixture.findEditPart(cfDRep);
    	Point interOperandPosition = cfEditPart.getFigure().getBounds().getTop().translate(0, 15);

		// create the first InteractionOperand fragment
		fixture.applyNodeCreationToolFromPalette("Interaction Operand", diagramRepresentation, cfDRep, interOperandPosition, null);
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram does not contain any additional element after creating an InteractionOperand sub node", nbDiagramChild, diagram.getChildren().size());

		// check the number of added operands
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		combinedFragment = interactionElem.getFragments().get(0);
		operands = ((CombinedFragment) combinedFragment).getOperands();
		Assert.assertNotNull("The list of operands is not null", operands);
		Assert.assertEquals("The CombinedFragment conatins one additional operand after the creation of an InteractionOperand node", operandsSize + 1, operands.size());

		InteractionOperand createdOperand = operands.get(1);
		Assert.assertEquals("Check the name of the created Operand element", INTERACTION_OPERAND1_NAME, createdOperand.getName());

		// check the eAnnotations of the operand
		EList<EAnnotation> operandEAnnotations = createdOperand.getEAnnotations();
		Assert.assertEquals("The created Operand element has two eAnnotation elements", 2, operandEAnnotations.size());
		EAnnotation eAnnotation = operandEAnnotations.get(0);
		Assert.assertEquals("Check the first eAnnotation name", INTERACTION_OPERAND1_NAME + "_start", eAnnotation.getSource());
		eAnnotation = operandEAnnotations.get(1);
		Assert.assertEquals("Check the second eAnnotation name", INTERACTION_OPERAND1_NAME + "_end", eAnnotation.getSource());
	
		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();	
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		combinedFragment = interactionElem.getFragments().get(0);
		operands = ((CombinedFragment) combinedFragment).getOperands();
		Assert.assertEquals("The CombinedFragment conatins only one Operand after undoing the creation of an InteractionOperand node", operandsSize, operands.size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();	
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		combinedFragment = interactionElem.getFragments().get(0);
		operands = ((CombinedFragment) combinedFragment).getOperands();
		Assert.assertEquals("The CombinedFragment conatins one additional Operand after redoing the creation of an InteractionOperand node", operandsSize + 1, operands.size());
		operandsSize = operands.size();

		// create the second InteractionOperand fragment
		fixture.applyNodeCreationToolFromPalette("Interaction Operand", diagramRepresentation, cfDRep, interOperandPosition, null);
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram does not contain any additional element after creating a second InteractionOperand node", nbDiagramChild, diagram.getChildren().size());

		// check the number of added operands
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		combinedFragment = interactionElem.getFragments().get(0);
		operands = ((CombinedFragment) combinedFragment).getOperands();
		Assert.assertNotNull("The list of operands is not null", operands);
		Assert.assertEquals("The CombinedFragment conatins one additional operand after the creation of a second InteractionOperand node", operandsSize + 1, operands.size());

		createdOperand = operands.get(2);
		Assert.assertEquals("Check the name of the created Operand element", INTERACTION_OPERAND2_NAME, createdOperand.getName());

		// check the eAnnotations of the operand
		operandEAnnotations = createdOperand.getEAnnotations();
		Assert.assertEquals("The created Operand element has two eAnnotation elements", 2, operandEAnnotations.size());
		eAnnotation = operandEAnnotations.get(0);
		Assert.assertEquals("Check the first eAnnotation name", INTERACTION_OPERAND2_NAME + "_start", eAnnotation.getSource());
		eAnnotation = operandEAnnotations.get(1);
		Assert.assertEquals("Check the second eAnnotation name", INTERACTION_OPERAND2_NAME + "_end", eAnnotation.getSource());
	
		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();	
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		combinedFragment = interactionElem.getFragments().get(0);
		operands = ((CombinedFragment) combinedFragment).getOperands();
		Assert.assertEquals("The CombinedFragment conatins only two Operand after undoing the creation of the second InteractionOperand node", operandsSize, operands.size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();	
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		combinedFragment = interactionElem.getFragments().get(0);
		operands = ((CombinedFragment) combinedFragment).getOperands();
		Assert.assertEquals("The CombinedFragment conatins two additional Operand after redoing the creation of the second InteractionOperand node", operandsSize + 1, operands.size());

	}
	
}
