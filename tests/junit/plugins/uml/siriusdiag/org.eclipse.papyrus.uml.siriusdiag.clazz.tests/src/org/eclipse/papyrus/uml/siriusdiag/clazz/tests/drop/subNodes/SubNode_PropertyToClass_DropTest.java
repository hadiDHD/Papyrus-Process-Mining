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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeListElementSpec;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeListSpec;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Class;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/drop/subNode/propertyOperationToClass/SubNode_PropertyOperationToClass_DropTest.di")
@SuppressWarnings({ "nls", "restriction" })
public class SubNode_PropertyToClass_DropTest extends AbstractPapyrusTest {

	private static final String DIAGRAM_NAME = "SubNode_PropertyOperationToClass_ClassDiagram";

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture(/* Collections.singletonList("aird") */);

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void createPropertySubNodeTest() {

		Assert.assertTrue(fixture.getModel() instanceof Model);
		DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Diagram diagram = diagramEditpart.getDiagramView();

		Object classElement = diagram.getChildren().get(0);
		DNodeContainerSpec classRepresentation = (DNodeContainerSpec) ((View) classElement).getElement();
		DNodeListSpec propertyContainer = (DNodeListSpec) classRepresentation.getOwnedDiagramElements().get(1);
		NamedElement elementToBeDropped = ((Class) classRepresentation.getTarget()).getOwnedAttributes().get(0);
		DDiagram diagramRespresentation = (DDiagram) diagram.getElement();
		fixture.applyContainerDropDescriptionTool(diagramRespresentation, "Attributes or Operations from Model", propertyContainer, elementToBeDropped);
		fixture.flushDisplayEvents();

		classElement = diagram.getChildren().get(0);
		classRepresentation = (DNodeContainerSpec) ((View) classElement).getElement();
		DNodeListSpec element = (DNodeListSpec) classRepresentation.getOwnedDiagramElements().get(1);
		Object siriusNewRepresentation = element.getOwnedElements().get(0);
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeListElementSpec);
		EObject semanticElement = ((DNodeListElementSpec) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML Property", semanticElement instanceof org.eclipse.uml2.uml.Property);
		Class classElem = (Class) classRepresentation.getTarget();
		Assert.assertEquals("The Class must contains one additional Attribut after the creation", 1, classElem.getOwnedAttributes().size());

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The class must have no attribut after undoing the Drop action", 0, element.getOwnedElements().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram children size does not change on dropping a sub node", 1, classElem.getOwnedAttributes().size());
		siriusNewRepresentation = element.getOwnedElements().get(0);
		Assert.assertTrue("The created sirus node must be a DNode", siriusNewRepresentation instanceof DNodeListElementSpec);
		semanticElement = ((DNodeListElementSpec) siriusNewRepresentation).getSemanticElements().iterator().next();
		Assert.assertTrue("The created element must be a UML Property", semanticElement instanceof org.eclipse.uml2.uml.Property);
		classElem = (Class) classRepresentation.getTarget();
		Assert.assertEquals("The Class must contains one additional Attribut after the Drop action", 1, classElem.getOwnedAttributes().size());

	}
}
