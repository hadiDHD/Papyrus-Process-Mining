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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges;

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
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.model.business.internal.spec.DEdgeSpec;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/creation/edges/common/Edge_CreationTest.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class Edge_Containment_CreationTest extends AbstractPapyrusTest {

	private static final String DIAGRAM_NAME = "Edge_ClassDiagram";

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createContainmentEdgeTest() {

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		int nbElement = diagram.getChildren().size();
		int nbEdge = diagram.getEdges().size();

		System.err.println(DIAGRAM_NAME + " ==? " + diagram.getName());
		Assert.assertEquals("The diagram must have the source and the target elements of the edge before creating it", nbElement, diagram.getChildren().size());
		Assert.assertEquals("The diagram have no edge before creating an Edge", nbEdge, diagram.getEdges().size());

		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		EdgeTarget edgeSource = (EdgeTarget) ((View) diagram.getChildren().get(0)).getElement();
		EdgeTarget edgeTarget = (EdgeTarget) ((View) diagram.getChildren().get(1)).getElement();
		fixture.applyEdgeCreationTool("Containment Link", diagramRespresentation, edgeSource, edgeTarget);
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram must contain one additional edge after creating an Edge", nbEdge + 1, diagram.getEdges().size());
		Object createdEdge = diagram.getEdges().get(0);
		Assert.assertTrue("The created edge must be a View", createdEdge instanceof View);
		EObject siriusNewRepresentation = ((View) createdEdge).getElement();
		Assert.assertTrue("The created sirus edge must be an DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		Classifier sourceNestedClassifier = ((ClassImpl) ((DEdgeSpec) siriusNewRepresentation).getTarget()).getNestedClassifiers().get(0);
		Classifier targetElem = (ClassImpl) ((DNodeContainer) ((DEdgeSpec) siriusNewRepresentation).getTargetNode()).getTarget();
		Assert.assertEquals("The target element is added to the liste of NestedClassifeirs of the source element", sourceNestedClassifier, targetElem);
		Assert.assertEquals("The root model contain one less element after the creation of a Containment edge", nbElement - 1, fixture.getModel().getOwnedElements().size());

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
		createdEdge = diagram.getEdges().get(0);
		Assert.assertTrue("The created edge must be a View", createdEdge instanceof View);
		siriusNewRepresentation = ((View) createdEdge).getElement();
		Assert.assertTrue("The created sirus edge must be an DEdge", siriusNewRepresentation instanceof DEdgeSpec);
		sourceNestedClassifier = ((ClassImpl) ((DEdgeSpec) siriusNewRepresentation).getTarget()).getNestedClassifiers().get(0);
		targetElem = (ClassImpl) ((DNodeContainer) ((DEdgeSpec) siriusNewRepresentation).getTargetNode()).getTarget();
		Assert.assertEquals("The target element is added to the liste of NestedClassifeirs of the source element", sourceNestedClassifier, targetElem);
		Assert.assertEquals("The root model contain one less element after the creation of a Containment edge", nbElement - 1, fixture.getModel().getOwnedElements().size());
	}
}
