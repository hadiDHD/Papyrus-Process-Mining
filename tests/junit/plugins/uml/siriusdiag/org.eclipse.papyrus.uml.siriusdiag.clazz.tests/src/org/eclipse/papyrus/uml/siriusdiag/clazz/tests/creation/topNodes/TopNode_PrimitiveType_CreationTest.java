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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.framework.classification.ClassificationRunner;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/creation/topNodes/TopNode_CreationTest.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class TopNode_PrimitiveType_CreationTest extends AbstractPapyrusTest {

	private static final String DIAGRAM_NAME = "TopNode_ClassDiagram";

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */); // TODO you probably need to add sirius file extension here, but not really sure

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createPrimitiveTypeNodeTest() {

		int nbElement = fixture.getModel().getOwnedElements().size();

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		Assert.assertEquals("The diagram must be empty before creating the top node", 0, diagram.getChildren().size());

		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();
		fixture.applyGenericTool("PrimitiveType", diagramRepresentation, diagramRepresentation);
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram must contain one element after creating a top node", 1, diagram.getChildren().size());
		Object element = diagram.getChildren().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNodeContainer", siriusNewRepresentation instanceof DNodeContainer);
		EObject semanticElement = ((DNodeContainer) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML PrimitiveType", semanticElement instanceof org.eclipse.uml2.uml.PrimitiveType);
		Assert.assertEquals("The root must only contains one additional element after the creation", nbElement + 1, fixture.getModel().getOwnedElements().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must be empty after undoing the creation", 0, diagram.getChildren().size());
		Assert.assertEquals("The root must contains the same number of elements as initially", nbElement, fixture.getModel().getOwnedElements().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must contain one element after creating a top node", 1, diagram.getChildren().size());
		element = diagram.getChildren().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNodeList", siriusNewRepresentation instanceof DNodeContainer);
		semanticElement = ((DNodeContainer) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML PrimitiveType", semanticElement instanceof org.eclipse.uml2.uml.PrimitiveType);
		Assert.assertEquals("The root must only contains one additional element after the creation", nbElement + 1, fixture.getModel().getOwnedElements().size());
	}
}
