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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.delete.view.edges;

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
import org.eclipse.sirius.diagram.model.business.internal.spec.DEdgeSpec;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DEdgeEditPart;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * Delete Dependency edge from diagram test
 */
@PluginResource("resources/delete/edges/dependency/Edge_Dependency_DeleteSemanticTest.di")
@SuppressWarnings("nls")
public class Edge_Dependency_DeleteViewTest extends AbstractSiriusDiagramTests {

	private static final String VIEW_TO_DELETE_ELEMENT_NAME = "EdgeToDelete";

	private static final String CLASS_DIAGRAM_NAME = "Edge_Dependency_Delete_ClassDiagram";

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void Delete_DependencyEdge_View() {
		checkSiriusDiagramSynchronization(false);

		// setup
		Assert.assertTrue(fixture.getModel() instanceof Model);
		Model rootModel = (Model) fixture.getModel();
		NamedElement element = rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME);
		Assert.assertNotNull(element);
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram classDiagram = diagramEditpart.getDiagramView();
		Assert.assertNotNull("We can't find the class diagram", classDiagram);

		Assert.assertEquals("The diagram must contains one edge", 1, classDiagram.getEdges().size());
		Object elementToBeDeleted = classDiagram.getEdges().get(0);
		Assert.assertTrue(elementToBeDeleted instanceof View);
		EObject siriusNewRepresentation = ((View) elementToBeDeleted).getElement();
		Assert.assertTrue(siriusNewRepresentation instanceof DEdgeSpec);
		Assert.assertEquals("The found view doesn't represent the edge to destroy", element, ((DEdgeSpec) siriusNewRepresentation).getTarget());

		EditPart toSelect = null;
		final List<?> childEP = fixture.getActiveDiagram().getConnections();
		final Iterator<?> iter = childEP.iterator();
		while (iter.hasNext() && toSelect == null) {
			final Object current = iter.next();
			if (current instanceof EditPart) {
				final Object model = ((EditPart) current).getModel();
				siriusNewRepresentation = ((View) model).getElement();
				if (model instanceof View && ((DEdgeSpec) siriusNewRepresentation).getTarget() == rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME)) {
					toSelect = (EditPart) current;
					break;
				}
			}
		}

		Assert.assertNotNull("We don't found the Editpart to select", toSelect);
		fixture.getActiveDiagramEditor().getSite().getSelectionProvider().setSelection(new StructuredSelection(toSelect));
		fixture.flushDisplayEvents();

		StructuredSelection selecetdElement = (StructuredSelection) fixture.getActiveDiagramEditor().getSite().getSelectionProvider().getSelection();
		DEdgeEditPart selectedEdge = (DEdgeEditPart) selecetdElement.getFirstElement();
		EObject elemToDelete = ((View) selectedEdge.getModel()).getElement();
		fixture.applyGraphicalDeletionTool((DEdgeSpec) elemToDelete);
		fixture.flushDisplayEvents();

		// the semantic element has not been destroyed
		Assert.assertNotNull("The UML model must still contain the semantic element", rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME));
		// the view element has been destroyed
		Assert.assertEquals("The class diagram must not contain the view of the edge after delete from diagram", 0, classDiagram.getEdges().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		// the graphical element has been reset
		Assert.assertNotNull("The UML model must still contain the semantic element after undoing the destruction", rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME));
		Assert.assertEquals("The class diagram must contain the view of the edge after undoing the destruction", 1, classDiagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertNotNull("The UML model must still contain the semantic element redoing the destruction", rootModel.getMember(VIEW_TO_DELETE_ELEMENT_NAME));
		Assert.assertEquals("The class diagram must not contain the view of the edge after redoing the destruction", 0, classDiagram.getEdges().size());
	}
}
