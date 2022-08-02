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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.delete.semantic.edges;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.framework.classification.ClassificationRunner;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.model.business.internal.spec.DEdgeSpec;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Delete Dependency edge from model test
 */
@PluginResource("resources/delete/edges/abstraction/Edge_Abstraction_DeleteSemanticTest.di")
@SuppressWarnings("nls")
public class Edge_Abstraction_DeleteSemanticTest extends AbstractPapyrusTest {

	private static final String ELEMENT_TO_DESTROY_NAME = "EdgeToDelete";

	private static final String CLASS_DIAGRAM_NAME = "Edge_GeneralizationSet_ClassDiagram";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void Delete_AbstractionEdge_Semantic() {
		// setup
		Assert.assertTrue(fixture.getModel() instanceof Model);
		Model rootModel = (Model) fixture.getModel();
		NamedElement element = rootModel.getMember(ELEMENT_TO_DESTROY_NAME);
		Assert.assertNotNull("We can't find the edge to destroy", element);

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram classDiagram = diagramEditpart.getDiagramView();
		Assert.assertNotNull("We can't find the class diagram", classDiagram);

		Assert.assertEquals("The diagram must contains one edge", 1, classDiagram.getEdges().size());
		Object elementToBeDeleted = classDiagram.getEdges().get(0);
		Assert.assertTrue(elementToBeDeleted instanceof View);
		EObject siriusNewRepresentation = ((View) elementToBeDeleted).getElement();
		Assert.assertTrue(siriusNewRepresentation instanceof DEdgeSpec);
		Assert.assertEquals("The found view doesn't represent the edge to destroy", element, ((DEdgeSpec) siriusNewRepresentation).getTarget());

		fixture.applySemanticDeletionTool((DEdgeSpec) siriusNewRepresentation);
		fixture.flushDisplayEvents();

		// the semantic element has been destroyed
		Assert.assertNull("The UML model must not contain the destroyed edge", rootModel.getMember(ELEMENT_TO_DESTROY_NAME));

		// the view has been destroyed too in the diagram
		Assert.assertEquals("The class diagram must not contain the view of teh edge after the destruction", 0, classDiagram.getEdges().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();

		// the semantic and the view elements have been reset
		Assert.assertNotNull("The UML model must contain the destroyed edge after undoing the destruction", rootModel.getMember(ELEMENT_TO_DESTROY_NAME));
		Assert.assertEquals("The class diagram must contain the view of teh edge after undoing the destruction", 1, classDiagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertNull("The UML model must not contain the destroyed edge after redoing the destruction", rootModel.getMember(ELEMENT_TO_DESTROY_NAME));
		Assert.assertEquals("The class diagram must not contain the view of teh edge after redoing the destruction", 0, classDiagram.getEdges().size());
	}
}
