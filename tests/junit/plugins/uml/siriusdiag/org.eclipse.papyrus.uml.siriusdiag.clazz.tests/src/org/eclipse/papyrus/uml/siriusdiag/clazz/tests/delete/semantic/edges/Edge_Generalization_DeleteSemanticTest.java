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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.edges;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.framework.classification.ClassificationRunner;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DEdgeSpec;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Delete Dependency edge from model test
 */
@PluginResource("resources/delete/edges/generalization/Edge_Generalization_DeleteSemanticTest.di")
@SuppressWarnings("nls")
@RunWith(ClassificationRunner.class)
public class Edge_Generalization_DeleteSemanticTest extends AbstractPapyrusTest {

	private static final String EDGE_SOURCE_NAME = "EdgeSource";

	private static final String CLASS_DIAGRAM_NAME = "Edge_Generalization_Delete_ClassDiagram";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void Delete_GeneralizationEdge_Semantic() {
		// setup
		Assert.assertTrue(fixture.getModel() instanceof Model);
		Model rootModel = (Model) fixture.getModel();
		ClassImpl edgeSource = (ClassImpl) rootModel.getMember(EDGE_SOURCE_NAME);
		Generalization edgeToBeDeleted = edgeSource.getGeneralizations().get(0);
		Assert.assertNotNull("We can't find the edge to destroy", edgeToBeDeleted);

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram classDiagram = diagramEditpart.getDiagramView();
		Assert.assertNotNull("We can't find the class diagram", classDiagram);

		Assert.assertEquals("The diagram must contains one edge", 1, classDiagram.getEdges().size());
		Object elementToBeDeleted = classDiagram.getEdges().get(0);
		Assert.assertTrue(elementToBeDeleted instanceof View);
		EObject siriusNewRepresentation = ((View) elementToBeDeleted).getElement();
		Assert.assertTrue(siriusNewRepresentation instanceof DEdgeSpec);
		Assert.assertEquals("The found view doesn't represent the edge to destroy", edgeToBeDeleted, ((DEdgeSpec) siriusNewRepresentation).getTarget());

		fixture.applySemanticDeletionTool((DEdgeSpec) siriusNewRepresentation);
		fixture.flushDisplayEvents();

		// the semantic element has been destroyed
		edgeSource = (ClassImpl) rootModel.getMember(EDGE_SOURCE_NAME);
		Assert.assertEquals("The UML model must not contain the destroyed edge", 0, edgeSource.getGeneralizations().size());

		// the view has been destroyed too in the diagram
		Assert.assertEquals("The class diagram must not contain the view of the edge after the destruction", 0, classDiagram.getEdges().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();

		// the semantic and the view elements have been reset
		edgeSource = (ClassImpl) rootModel.getMember(EDGE_SOURCE_NAME);
		edgeToBeDeleted = edgeSource.getGeneralizations().get(0);
		Assert.assertNotNull("The UML model must contain the destroyed edge after undoing the destruction", edgeToBeDeleted);
		Assert.assertEquals("The class diagram must contain the view of the edge after undoing the destruction", 1, classDiagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		edgeSource = (ClassImpl) rootModel.getMember(EDGE_SOURCE_NAME);
		Assert.assertEquals("The UML model must not contain the destroyed edge after redoing the destruction", 0, edgeSource.getGeneralizations().size());
		Assert.assertEquals("The class diagram must not contain the view of the edge after redoing the destruction", 0, classDiagram.getEdges().size());
	}
}
