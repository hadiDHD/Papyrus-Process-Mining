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
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeListElementSpec;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeListSpec;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/creation/subNodes/classSubNodes/SubNode_PropertyOperation_CreationTest.di") // the resource to import for the test in the workspace
@SuppressWarnings({ "nls", "restriction" })
@RunWith(ClassificationRunner.class)
public class SubNode_Operation_CreationTest extends AbstractPapyrusTest {

	private static final String DIAGRAM_NAME = "SubNode_PropertyOperation_ClassDiagram";

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createOperationNodeTest() {

		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart);
		Diagram diagram = diagramEditpart.getDiagramView();
		System.err.println(DIAGRAM_NAME + " ==? " + diagram.getName());
		Assert.assertEquals("The root model must have only one Class element before creating the sub node", 1, diagram.getChildren().size());

		Object classElement = diagram.getChildren().get(0);
		DNodeContainerSpec classRepresentation = (DNodeContainerSpec) ((View) classElement).getElement();
		EObject subNodeContainer = classRepresentation.getOwnedDiagramElements().get(0);
		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		fixture.applyContainerCreationTool("Operation", diagramRespresentation, subNodeContainer);
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram children size does not change on adding a sub node", 1, diagram.getChildren().size());

		classElement = diagram.getChildren().get(0);
		classRepresentation = (DNodeContainerSpec) ((View) classElement).getElement();
		DNodeListSpec element = (DNodeListSpec) classRepresentation.getOwnedDiagramElements().get(0);
		Object siriusNewRepresentation = element.getOwnedElements().get(0);
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeListElementSpec);
		EObject semanticElement = ((DNodeListElementSpec) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML Operation", semanticElement instanceof org.eclipse.uml2.uml.Operation);
		ClassImpl classElem = (ClassImpl) classRepresentation.getTarget();
		Assert.assertEquals("The Class must contains one additional Operation after the creation", 1, classElem.getOwnedOperations().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The class must have no attribut after undoing the creation", 0, classElem.getOwnedOperations().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The class must contain one Operation after creating a sub node", 1, classElem.getOwnedOperations().size());
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeListElementSpec);
		Assert.assertTrue("The created element must be a UML Operation", semanticElement instanceof org.eclipse.uml2.uml.Operation);
	}
}
