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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.nodes;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.papyrus.junit.framework.classification.ClassificationRunner;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Delete Class from diagram test
 */
@PluginResource("resources/delete/nodes/durationObservation/TopNode_DurationObservation_DeleteSemanticTest.di")
@SuppressWarnings("nls")
public class TopNode_DurationObservation_DeleteViewTest extends AbstractPapyrusTest {

	private static final String ELEMENT_TO_DESTROY_NAME = "DurationObservationToDelete";

	private static final String CLASS_DIAGRAM_NAME = "TopNode_DurationObservation_ClassDiagram";
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void Delete_DurationObservation_View() {

		// setup
		Assert.assertTrue(fixture.getModel() instanceof Model);
		Model rootModel = (Model) fixture.getModel();
		NamedElement element = rootModel.getMember(ELEMENT_TO_DESTROY_NAME);
		Assert.assertNotNull(element);
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram classDiagram = diagramEditpart.getDiagramView();
		Assert.assertNotNull("We can't find the class diagram", classDiagram);

		Assert.assertEquals("The diagram must contains one element", 1, classDiagram.getChildren().size());
		Object elementToBeDeleted = classDiagram.getChildren().get(0);
		Assert.assertTrue(elementToBeDeleted instanceof View);
		EObject siriusNewRepresentation = ((View) elementToBeDeleted).getElement();
		Assert.assertTrue(siriusNewRepresentation instanceof DNodeContainerSpec);
		Assert.assertEquals("The found view doesn't represent the element to destroy", element, ((DNodeContainerSpec) siriusNewRepresentation).getTarget());

		EditPart toSelect = null;
		final List<?> childEP = fixture.getActiveDiagram().getChildren();
		final Iterator<?> iter = childEP.iterator();
		while (iter.hasNext() && toSelect == null) {
			final Object current = iter.next();
			if (current instanceof EditPart) {
				final Object model = ((EditPart) current).getModel();
				siriusNewRepresentation = ((View) model).getElement();
				if (model instanceof View && ((DNodeContainerSpec) siriusNewRepresentation).getTarget() == rootModel.getMember(ELEMENT_TO_DESTROY_NAME)) {
					toSelect = (EditPart) current;
					break;
				}
			}
		}

		Assert.assertNotNull("We don't found the Editpart to select", toSelect);
		fixture.getActiveDiagramEditor().getSite().getSelectionProvider().setSelection(new StructuredSelection(toSelect));
		fixture.flushDisplayEvents();

		StructuredSelection selecetdElement = (StructuredSelection) fixture.getActiveDiagramEditor().getSite().getSelectionProvider().getSelection();
		DNodeContainerEditPart selectedContainer = (DNodeContainerEditPart) selecetdElement.getFirstElement();
		EObject elemToDelete = ((View) selectedContainer.getModel()).getElement();
		fixture.applyGraphicalDeletionTool((DNodeContainerSpec) elemToDelete);
		fixture.flushDisplayEvents();

		// the semantic element has not been destroyed
		Assert.assertNotNull("The UML model must still contain the semantic element", rootModel.getMember(ELEMENT_TO_DESTROY_NAME));
		// the view element has been destroyed
		Assert.assertEquals("The class diagram must not contain the view after delete from diagram", 0, classDiagram.getChildren().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		// the graphical element has been reset
		Assert.assertNotNull("The UML model must still contain the semantic element", rootModel.getMember(ELEMENT_TO_DESTROY_NAME));
		Assert.assertEquals("The class diagram must contain view after destruction of the class after undo", 1, classDiagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertNotNull("The UML model must still contain the semantic element", rootModel.getMember(ELEMENT_TO_DESTROY_NAME));
		Assert.assertEquals("The class diagram must not contain the view after delete from diagram", 0, classDiagram.getChildren().size());
	}
}
