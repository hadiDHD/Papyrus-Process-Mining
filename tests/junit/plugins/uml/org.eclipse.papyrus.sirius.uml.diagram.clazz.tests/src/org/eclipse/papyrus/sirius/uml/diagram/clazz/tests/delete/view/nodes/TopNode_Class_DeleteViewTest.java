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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.delete.view.nodes;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.AbstractSiriusDiagramTests;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * Delete Class from diagram test
 */
@PluginResource("resources/delete/nodes/class/TopNode_Class_DeleteSemanticTest.di")
@SuppressWarnings("nls")
public class TopNode_Class_DeleteViewTest extends AbstractSiriusDiagramTests {

	private static final String VIEW_TO_DELETE_ELEMENT_NAME = "ClassToDelete";

	private static final String CLASS_DIAGRAM_NAME = "TopNode_Class_Delete_ClassDiagram";

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void Delete_Class_View() {
		checkSiriusDiagramSynchronization(false);

		// setup
		Assert.assertTrue(fixture.getModel() instanceof Model);
		Model rootModel = (Model) fixture.getModel();
		NamedElement element = rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME);
		Assert.assertNotNull(element);
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram classDiagram = diagramEditpart.getDiagramView();
		Assert.assertNotNull("We can't find the class diagram", classDiagram);

		Assert.assertEquals("The diagram must contains one element", 1, classDiagram.getChildren().size());
		Object elementToBeDeleted = classDiagram.getChildren().get(0);
		Assert.assertTrue(elementToBeDeleted instanceof View);
		EObject siriusNewRepresentation = ((View) elementToBeDeleted).getElement();
		Assert.assertTrue(siriusNewRepresentation instanceof DNodeContainer);
		Assert.assertEquals("The found view doesn't represent the element to destroy", element, ((DNodeContainer) siriusNewRepresentation).getTarget());

		EditPart toSelect = null;
		final List<?> childEP = fixture.getActiveDiagram().getChildren();
		final Iterator<?> iter = childEP.iterator();
		while (iter.hasNext() && toSelect == null) {
			final Object current = iter.next();
			if (current instanceof EditPart) {
				final Object model = ((EditPart) current).getModel();
				siriusNewRepresentation = ((View) model).getElement();
				if (model instanceof View && ((DNodeContainerSpec) siriusNewRepresentation).getTarget() == rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME)) {
					toSelect = (EditPart) current;
				}
			}
		}

		Assert.assertNotNull("We don't found the Editpart to select", toSelect);
		fixture.getActiveDiagramEditor().getSite().getSelectionProvider().setSelection(new StructuredSelection(toSelect));
		fixture.flushDisplayEvents();

		StructuredSelection selecetdElement = (StructuredSelection) fixture.getActiveDiagramEditor().getSite().getSelectionProvider().getSelection();
		DNodeContainerEditPart selectedContainer = (DNodeContainerEditPart) selecetdElement.getFirstElement();
		EObject elemToDelete = ((View) selectedContainer.getModel()).getElement();
		fixture.applyGraphicalDeletionTool((DNodeContainer) elemToDelete);
		fixture.flushDisplayEvents();

		// the semantic element has not been destroyed
		Assert.assertNotNull("The UML model must still contain the semantic element", rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME));
		// the view element has been destroyed
		Assert.assertEquals("The class diagram must not contain the view after delete from diagram", 0, classDiagram.getChildren().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		// the graphical element has been reset
		Assert.assertNotNull("The UML model must still contain the semantic element after undo", rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME));
		Assert.assertEquals("The class diagram must contain view after destruction of the class after undo", 1, classDiagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertNotNull("The UML model must still contain the semantic element", rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME));
		Assert.assertEquals("The class diagram must not contain the view after delete from diagram", 0, classDiagram.getChildren().size());
	}
}
