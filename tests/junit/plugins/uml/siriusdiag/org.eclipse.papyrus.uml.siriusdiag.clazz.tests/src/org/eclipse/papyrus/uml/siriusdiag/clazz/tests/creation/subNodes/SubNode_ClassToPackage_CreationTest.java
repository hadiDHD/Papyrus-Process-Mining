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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.subNodes;

import org.eclipse.emf.common.util.EList;
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
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeContainerSpec;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/creation/subNodes/packageSubNodes/SubNode_Package_CreationTest.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class SubNode_ClassToPackage_CreationTest extends AbstractPapyrusTest {

	private static final String DIAGRAM_NAME = "SubNode_Package_ClassDiagram";

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */); // TODO you probably need to add sirius file extension here, but not really sure

	@SuppressWarnings("restriction")
	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createPackageSubNodeTest() {

		int nbElement = fixture.getModel().getOwnedElements().size();

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		System.err.println(DIAGRAM_NAME + " ==? " + diagram.getName());
		Assert.assertEquals("The diagram must have one Package element creating the sub node", 1, diagram.getChildren().size());

		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		Object packageElement = diagram.getChildren().get(0);
		EObject packageRepresentation = ((View) packageElement).getElement();
		fixture.applyGenericTool("Class", diagramRespresentation, packageRepresentation);
		fixture.flushDisplayEvents();

		EList<DDiagramElement> packageSubNodes = ((DNodeContainerSpec) packageRepresentation).getOwnedDiagramElements();
		Assert.assertEquals("The diagram children size does not change on adding a sub node", nbElement, diagram.getChildren().size());
		EObject siriusNewRepresentation = packageSubNodes.get(0);
		Assert.assertEquals("The package element contains one additional element", 1, packageSubNodes.size());
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeContainer);
		EObject semanticElement = ((DNodeContainer) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML Class", semanticElement instanceof org.eclipse.uml2.uml.Class);

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The package element does not contain any element", 0, packageSubNodes.size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram children size does not change on adding a sub node", nbElement, diagram.getChildren().size());
		siriusNewRepresentation = ((DNodeContainerSpec) packageRepresentation).getOwnedDiagramElements().get(0);
		Assert.assertEquals("The package element contains one additional element", 1, packageSubNodes.size());
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeContainer);
		semanticElement = ((DNodeContainer) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML Class", semanticElement instanceof org.eclipse.uml2.uml.Class);
	}
}
