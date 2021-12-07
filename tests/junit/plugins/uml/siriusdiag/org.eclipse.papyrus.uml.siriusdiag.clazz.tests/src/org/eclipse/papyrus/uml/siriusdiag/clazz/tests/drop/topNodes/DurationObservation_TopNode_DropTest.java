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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes;

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
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Drop Class test
 */
@PluginResource("resources/drop/topNode/TopNode_DropTest.di")
@SuppressWarnings("nls")
@RunWith(ClassificationRunner.class)
public class DurationObservation_TopNode_DropTest extends AbstractPapyrusTest {

	private static final String CLASS_DIAGRAM_NAME = "TopNode_Drop_ClassDiagram";

	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void DurationObservation_DropTest() {

		Assert.assertTrue(fixture.getModel() instanceof Model);
		Model rootModel = (Model) fixture.getModel();
		NamedElement elementToBeDropped = rootModel.getMember("DurationObservationToDrop");
		Assert.assertTrue("The element to be dropped is an instance of UML DurationObservation", elementToBeDropped instanceof org.eclipse.uml2.uml.DurationObservation);

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram diagram = diagramEditpart.getDiagramView();
		Assert.assertEquals("The diagram must not yet have children", 0, diagram.getChildren().size());

		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		fixture.applyContainerDropDescriptionTool(diagramRespresentation, "Observation from Model", diagramRespresentation, elementToBeDropped);
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram must have one child after the Drop action", 1, diagram.getChildren().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram does not contain any children after undoing the Drop action", 0, diagram.getChildren().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram contains one child after redoing the Drop action", 1, diagram.getChildren().size());
		Object element = diagram.getChildren().get(0);
		Assert.assertTrue("The created element must be a View", element instanceof View);
		EObject siriusNewRepresentation = ((View) element).getElement();
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeContainer);
		EObject semanticElement = ((DNodeContainer) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML DurationObservation", semanticElement instanceof org.eclipse.uml2.uml.DurationObservation);
	}
}
