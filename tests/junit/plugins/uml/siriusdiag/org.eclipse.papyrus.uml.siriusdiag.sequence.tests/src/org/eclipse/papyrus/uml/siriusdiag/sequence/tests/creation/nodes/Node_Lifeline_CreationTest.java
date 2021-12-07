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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeSpec;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Lifeline node creation test
 * @author battal
 *
 */
@SuppressWarnings("restriction")
@PluginResource("resource/SequenceDiagramTest/creation/empty/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class Node_Lifeline_CreationTest {

	/** The sequence diagram name */
	private static final String DIAGRAM_NAME = "TopNode_Creation_SequenceDiagram";


	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createLifelineNodeTest() {

		int nbElement = fixture.getModel().getOwnedElements().size();

		// Get diagram edit part as for all diagram
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		Assert.assertEquals("The diagram must be empty before creating the top node", 0, diagram.getChildren().size());
		int nbDiagramChild = diagram.getChildren().size();

		// Create a lifeline  
		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		fixture.applyNodeCreationToolFromPalette("Lifeline", diagramRespresentation, diagramRespresentation, new Point(100, 100), null);
		fixture.flushDisplayEvents();
		
		// Lifeline is created and it is a GMF view notation.node
		Assert.assertEquals("The diagram must contain one additional element after creating a Lifeline", nbDiagramChild + 1, diagram.getChildren().size());		
		Object element = diagram.getChildren().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		EObject semanticElement = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be an UML Lifeline", semanticElement instanceof org.eclipse.uml2.uml.Lifeline);
		Assert.assertEquals("The root does not contain any additional element after the creation of a Lifeline sub node", nbElement, fixture.getModel().getOwnedElements().size());
		
		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must be empty after undoing the creation of the Lifeline", nbDiagramChild, diagram.getChildren().size());
		Assert.assertEquals("The root must contains the same number of elements as initially", nbElement, fixture.getModel().getOwnedElements().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must contain one addtional element after redoing the creation of a Lifeline", nbDiagramChild + 1, diagram.getChildren().size());
		element = diagram.getChildren().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeSpec);
		semanticElement = ((DNodeSpec) siriusNewRepresentation).getSemanticElements().iterator().next(); 
		Assert.assertTrue("The created element must be an UML Lifeline", semanticElement instanceof org.eclipse.uml2.uml.Lifeline);
		Assert.assertEquals("The root does not contain any additional element after the creation of a sub node", nbElement, fixture.getModel().getOwnedElements().size());
	}
}
