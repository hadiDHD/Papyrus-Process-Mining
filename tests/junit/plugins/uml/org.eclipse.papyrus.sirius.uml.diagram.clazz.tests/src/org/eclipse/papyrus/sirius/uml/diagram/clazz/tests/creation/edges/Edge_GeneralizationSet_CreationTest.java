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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.creation.edges;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.framework.classification.ClassificationRunner;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.CreationToolsIds;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.model.business.internal.spec.DEdgeSpec;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/creation/edges/generalizationSet/Edge_GeneralizationSet_CreationTest.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class Edge_GeneralizationSet_CreationTest extends AbstractPapyrusTest {

	private static final String DIAGRAM_NAME = "Edge_GeneralizationSet_ClassDiagram";

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createGeneralizationSetEdgeTest() {

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbElement = diagram.getChildren().size();
		int nbEdge = diagram.getEdges().size();

		Assert.assertEquals("The diagram must have at least two Generalization edges before creating the GeneralizationSet edge", nbEdge, diagram.getEdges().size());

		DDiagram diagramRepresentation = (DDiagram) diagram.getElement();

		// get the source of the GeneralizationSet edge (source = one of the Generalization edge)
		EdgeTarget edgeSource = (EdgeTarget) ((View) diagram.getEdges().get(0)).getElement();

		// get the target of the GeneralizationSet edge (target = the other Generalization edge)
		EdgeTarget edgeTarget = (EdgeTarget) ((View) diagram.getEdges().get(1)).getElement();

		fixture.applyEdgeCreationTool(CreationToolsIds.CREATE__GENERALISATION_SET__TOOL, diagramRepresentation, edgeSource, edgeTarget);
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram must contain one additional edge after creating an Edge", nbEdge + 1, diagram.getEdges().size());
		Object createdEdge = diagram.getEdges().get(2);
		Assert.assertTrue("The created edge must be a View", createdEdge instanceof View);
		EObject siriusNewRepresentation = ((View) createdEdge).getElement();
		Assert.assertTrue("The created sirus edge must be an DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		EObject semanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML GeneralizationSet", semanticElement instanceof org.eclipse.uml2.uml.GeneralizationSet);
		Assert.assertEquals("The root model must contain an additional element after the creation of an GeneralizationSet edge", nbElement + 1, fixture.getModel().getOwnedElements().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must contain the source and the target elements of the edge after undoing the creation of the Edge", nbElement, diagram.getChildren().size());
		Assert.assertEquals("The diagram must contain no more edges after undoing the creation of the Edge", nbEdge, diagram.getEdges().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must contain the source and the target elements of the edge after redoing the creation of the Edge", nbElement, diagram.getChildren().size());
		Assert.assertEquals("The diagram must contain one additional edge after redoing the creation of the Edge", nbEdge + 1, diagram.getEdges().size());
		createdEdge = diagram.getEdges().get(2);
		Assert.assertTrue("The created edge must be a View", createdEdge instanceof View);
		siriusNewRepresentation = ((View) createdEdge).getElement();
		Assert.assertTrue("The created sirus edge must be an DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		semanticElement = ((DEdgeSpec) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML GeneralizationSet", semanticElement instanceof org.eclipse.uml2.uml.GeneralizationSet);
		Assert.assertEquals("The root model must contain an additional element after the creation of an GeneralizationSet edge", nbElement + 1, fixture.getModel().getOwnedElements().size());
	}
}