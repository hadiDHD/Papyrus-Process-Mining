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

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.SequenceDiagramTestsTool;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeSpec;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ISequenceElementAccessor;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Lifeline;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionConstraint;
import org.eclipse.uml2.uml.InteractionOperand;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * CombinedFragment node creation test
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/oneLifeline/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Node_CombinedFragment_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";
	
	/** The created combined fragment name */
	private static final String COMBINED_FRAGMENT_NAME = "CombinedFragment1";
	
	/** The created interaction operand name */
	private static final String INTERACTION_OPERAND_NAME = "InteractionOperand1";


	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createCombinedFragmenNodeTest() {

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbDiagramChild = diagram.getChildren().size();
		Assert.assertEquals("The diagram must contains one Lifeline before creating the CombinedFragment node", 1, nbDiagramChild);
	
		// check the initial number of interaction's fragments
		Interaction interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		int fragmentsSize = interactionElem.getFragments().size();
		Assert.assertEquals("The interaction element does not conatin any fragment before the creation of the CombinedFragment", 0, fragmentsSize);

		// get the position/size of the CombinedFragment to be created
		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();		
		List<View> lifeLines = SequenceDiagramTestsTool.getView(fixture, Lifeline.viewpointElementPredicate());
		Lifeline lifeLine = ISequenceElementAccessor.getLifeline(lifeLines.get(0)).get();
		EdgeTarget lifelineDRep = (EdgeTarget)lifeLine.getNotationNode().getElement();
		Point cfStartPosition = lifeLine.getProperLogicalBounds().getTopLeft().translate(0,15);
		
		// create the combined fragment
		fixture.applyNodeCreationToolFromPalette("CombinedFragment", diagramRespresentation, diagramRespresentation, cfStartPosition, new Dimension(120,80));
		fixture.flushDisplayEvents();
		
		Assert.assertEquals("The diagram contains one additional element after creating a CombinedFragment node", nbDiagramChild + 1, diagram.getChildren().size());
		Object element = diagram.getChildren().get(1);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeContainerSpec);
		EObject semanticElement = ((DNodeContainerSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML CombinedFragment", semanticElement instanceof org.eclipse.uml2.uml.CombinedFragment);
		
		// check the number of added fragments
		interactionElem = (Interaction) fixture.getModel().getOwnedElements().get(0);
		Assert.assertEquals("The interaction element conatins one additional fragment after the creation of the combined fragment", fragmentsSize + 1, interactionElem.getFragments().size());

		// check the combined fragment
		CombinedFragment combinedFragment = (CombinedFragment) semanticElement;
		Assert.assertEquals("The CombinedFragment is on the Lifeline", ((DNodeSpec) lifelineDRep).getTarget(), combinedFragment.getCovereds().get(0));
	 
		// check the eAnnotations
		EList<EAnnotation> cfEAnnotations = combinedFragment.getEAnnotations();
		Assert.assertEquals("The created CombinedFragment element has two eAnnotation elements", 2, cfEAnnotations.size());
		EAnnotation eAnnotation = cfEAnnotations.get(0);
		Assert.assertEquals("Check the first eAnnotation name", COMBINED_FRAGMENT_NAME + "_start", eAnnotation.getSource());
		eAnnotation = cfEAnnotations.get(1);
		Assert.assertEquals("Check the second eAnnotation name", COMBINED_FRAGMENT_NAME + "_end", eAnnotation.getSource());
		
		// check the created operand element
		EList<InteractionOperand> operands = combinedFragment.getOperands();
		Assert.assertNotNull("The Operands list of the created CombinedFragment is not null", operands);
		Assert.assertEquals("The created CombinedFragment element has one Operand element", 1, operands.size());
		InteractionOperand operand = operands.get(0);
		Assert.assertEquals("Check the name of the created Operand element", INTERACTION_OPERAND_NAME, operand.getName());

		// check the eAnnotations of the operand
		EList<EAnnotation> operandEAnnotations = operand.getEAnnotations();
		Assert.assertEquals("The created Operand element has two eAnnotation elements", 2, operandEAnnotations.size());
		eAnnotation = operandEAnnotations.get(0);
		Assert.assertEquals("Check the first eAnnotation name", INTERACTION_OPERAND_NAME + "_start", eAnnotation.getSource());
		eAnnotation = operandEAnnotations.get(1);
		Assert.assertEquals("Check the second eAnnotation name", INTERACTION_OPERAND_NAME + "_end", eAnnotation.getSource());
		
		// check the created InteractionConstraint of the operand element
		InteractionConstraint guard = operand.getGuard();
		Assert.assertNotNull("The Operand has one InteractionConstraint element after the creation of the combined fragment", guard);
		
		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram does not contain any additional element after undoing the creation of a CombinedFragment", nbDiagramChild, diagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();	
		Assert.assertEquals("The diagram contains one additional element after redoing the creation of a CombinedFragment", nbDiagramChild + 1, diagram.getChildren().size());
		element = diagram.getChildren().get(1);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeContainerSpec);
		semanticElement = ((DNodeContainerSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be a UML CombinedFragment", semanticElement instanceof org.eclipse.uml2.uml.CombinedFragment);
	}
	
}
