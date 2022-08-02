/*****************************************************************************
 * Copyright (c) 2022 CEA LIST
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Vincent Lorenzo (CEA LIST) <vincent.lorenzo@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.creation.subNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.CreationToolsIds;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.MappingTypes;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_ClassLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_DataTypeLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_EnumerationLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_InterfaceLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_OperationLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_PrimitiveTypeLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_PropertyLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_ReceptionLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_SignalLabelNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.ClassSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.DataTypeSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.EnumerationSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.InterfaceSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.OperationSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.PrimitiveTypeSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.PropertySemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.ReceptionSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.SignalSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.IGraphicalNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.ISemanticNodeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.SemanticAndGraphicalCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.creation.topNodes.AbstractCreateNodeTests;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the creation of subnodes for Class Node
 */
@PluginResource("resources/creation/subNodes/classSubNodes/classSubNodes.di") // the resource to import for the test in the workspace
public class ClassSubNodes_CreationTest extends AbstractCreateNodeTests {

	private static final String SEMANTIC_ONWER_NAME = "Class1"; //$NON-NLS-1$

	private static final String DIAGRAM_NAME = "ClassSubNodesDiagram"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.creation.subNodes.AbstractSubNodeListElementCreationTests#getSemanticOwner()
	 *
	 * @return
	 */
	@Override
	protected org.eclipse.uml2.uml.Class getSemanticOwner() {
		return (org.eclipse.uml2.uml.Class) this.root.getMember(SEMANTIC_ONWER_NAME);
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.creation.topNodes.AbstractCreateNodeTests#getTopGraphicalContainer()
	 *
	 * @return
	 */
	@Override
	protected EObject getTopGraphicalContainer() {
		final DDiagram ddiagram = getDDiagram();
		Assert.assertEquals(1, ddiagram.getOwnedDiagramElements().size());
		final DDiagramElement element = ddiagram.getOwnedDiagramElements().get(0);
		Assert.assertEquals(getSemanticOwner(), element.getSemanticElements().get(0));
		Assert.assertEquals(MappingTypes.CLASS_NODE_TYPE, element.getMapping().getName());
		return element;
	}

	// ---inside the property compartment

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPropertyLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_ATTRIBUTES_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new PropertySemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_PropertyLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__PROPERTY__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	// ---inside the operation compartment

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createOperationLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_OPERATIONS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new OperationSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_OperationLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__OPERATION__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createReceptionLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_OPERATIONS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new ReceptionSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_ReceptionLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__RECEPTION__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	// ---inside the nested classifier compartment

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createClassLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new ClassSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_ClassLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__CLASS__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createDataTypeLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new DataTypeSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_DataTypeLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__DATATYPE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createEnumerationLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new EnumerationSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_EnumerationLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__ENUMERATION__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createInterfaceLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new InterfaceSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_InterfaceLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__INTERFACE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPrimitiveTypeLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new PrimitiveTypeSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_PrimitiveTypeLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__PRIMITIVETYPE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createSignalLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new SignalSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_SignalLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__SIGNAL__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}

}

