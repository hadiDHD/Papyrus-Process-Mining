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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.nodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.framework.classification.ClassificationRunner;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Model;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Delete Class from model test
 */
@PluginResource("resources/delete/nodes/comment/TopNode_Comment_DeleteSemanticTest.di")
@SuppressWarnings("nls")
public class TopNode_Comment_DeleteSemanticTest extends AbstractPapyrusTest {

	private static final String CLASS_DIAGRAM_NAME = "TopNode_Comment_DeleteSemanticTest";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void Delete_Comment_Semantic() {
		// setup
		Assert.assertTrue(fixture.getModel() instanceof Model);
		Model rootModel = (Model) fixture.getModel();
		Comment element = rootModel.getOwnedComments().get(0);
		Assert.assertNotNull("We can't find the Comment element to destroy", element);

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram classDiagram = diagramEditpart.getDiagramView();
		Assert.assertNotNull("We can't find the class diagram", classDiagram);

		Assert.assertEquals("The diagram must contains one Comment element", 1, classDiagram.getChildren().size());
		Object elementToBeDeleted = classDiagram.getChildren().get(0);
		Assert.assertTrue(elementToBeDeleted instanceof View);
		EObject siriusNewRepresentation = ((View) elementToBeDeleted).getElement();
		Assert.assertTrue(siriusNewRepresentation instanceof DNodeSpec);
		Assert.assertEquals("The found view doesn't represent the Comment element to destroy", element, ((DNodeSpec) siriusNewRepresentation).getTarget());

		fixture.applySemanticDeletionTool((DNodeSpec) siriusNewRepresentation);
		fixture.flushDisplayEvents();

		// the semantic element has been destroyed
		Assert.assertEquals("The UML model must not contain the destroyed Comment element", 0, rootModel.getOwnedComments().size());

		// the view has been destroyed too in the diagram
		Assert.assertEquals("The class diagram must not contain view after destruction of the Comment element", 0, classDiagram.getChildren().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		// the semantic and the view elements have been reset
		Assert.assertEquals("The UML model must not contain the destroyed Comment element", 1, rootModel.getOwnedComments().size());
		Assert.assertEquals("The class diagram must contain the view of the Comment element after undoing the destruction", 1, classDiagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The UML model must not contain the destroyed Comment element", 0, rootModel.getOwnedComments().size());
		Assert.assertEquals("The class diagram must not contain the view of the Comment element after redoing the destruction", 0, classDiagram.getChildren().size());
	}
}
