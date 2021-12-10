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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes;

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
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.internal.impl.ModelImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/drop/subNode/packageToModel/SubNode_PackageToModel_DropTest.di")
@SuppressWarnings({ "nls", "restriction" })
@RunWith(ClassificationRunner.class)
public class SubNode_PackageToModel_DropTest extends AbstractPapyrusTest {

	private static final String DIAGRAM_NAME = "SubNode_PackageToModel_ClassDiagram";

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createPackageSubNodeTest() {

		Assert.assertTrue(fixture.getModel() instanceof Model);
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram diagram = diagramEditpart.getDiagramView();

		Object modelElement = diagram.getChildren().get(0);
		DNodeContainerSpec container = (DNodeContainerSpec) ((View) modelElement).getElement();
		NamedElement elementToBeDropped = ((ModelImpl) container.getTarget()).getPackagedElement("PackageToDrop");
		Assert.assertTrue("The element to be dropped is an instance of UML Package", elementToBeDropped instanceof org.eclipse.uml2.uml.Package);

		Assert.assertEquals("The diagram must contain the parent Model elment", 1, diagram.getChildren().size());

		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();

		fixture.applyContainerDropDescriptionTool(diagramRespresentation, "Package from Model", container, elementToBeDropped);
		fixture.flushDisplayEvents();

		modelElement = diagram.getChildren().get(0);
		EObject modelRepresentation = ((View) modelElement).getElement();
		EList<DDiagramElement> packageSubNodes = ((DNodeContainerSpec) modelRepresentation).getOwnedDiagramElements();
		Assert.assertEquals("The diagram children size does not change on droping a sub node", 1, diagram.getChildren().size());
		EObject siriusNewRepresentation = packageSubNodes.get(0);
		Assert.assertEquals("The package element contains one additional element", 1, packageSubNodes.size());
		Assert.assertTrue("The dropped sirus node must be a DNode", siriusNewRepresentation instanceof DNodeContainer);
		EObject semanticElement = ((DNodeContainer) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The dropped element must be a UML Package", semanticElement instanceof org.eclipse.uml2.uml.Package);

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The package element does not contain any element", 0, packageSubNodes.size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram children size does not change on dropping a sub node", 1, diagram.getChildren().size());
		siriusNewRepresentation = ((DNodeContainerSpec) modelRepresentation).getOwnedDiagramElements().get(0);
		Assert.assertEquals("The package element contains one additional element", 1, packageSubNodes.size());
		Assert.assertTrue("The dropped sirus node must be a DNode", siriusNewRepresentation instanceof DNodeContainer);
		semanticElement = ((DNodeContainer) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The dropped element must be a UML Package", semanticElement instanceof org.eclipse.uml2.uml.Package);
	}
}
