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
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_ClassCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_CommentCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_ComponentCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_ConstraintCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_DataTypeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_EnumerationCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_InformationItemCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_InstanceSpecificationCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_InterfaceCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_ModelCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_PackageCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_PrimitiveTypeCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical.CD_SignalCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.ClassSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.CommentSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.ComponentSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.ConstraintSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.DataTypeSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.EnumerationSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.InformationItemSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.InstanceSpecificationSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.InterfaceSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.ModelSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.PackageSemanticCreationChecker;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic.PrimitiveTypeSemanticCreationChecker;
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
 * This class tests the creation of subnodes for Package Node
 */
@PluginResource("resources/creation/subNodes/modelSubNodes/modelSubNodes.di")
public class ModelSubNodes_CreationTest extends AbstractCreateNodeTests {

	private static final String SEMANTIC_ONWER_NAME = "Model1"; //$NON-NLS-1$

	private static final String DIAGRAM_NAME = "ModelSubNodesDiagram"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.creation.subNodes.AbstractSubNodeListElementCreationTests#getSemanticOwner()
	 *
	 * @return
	 */
	@Override
	protected org.eclipse.uml2.uml.Model getSemanticOwner() {
		return (org.eclipse.uml2.uml.Model) this.root.getMember(SEMANTIC_ONWER_NAME);
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
		Assert.assertEquals(MappingTypes.MODEL_NODE_TYPE, element.getMapping().getName());
		return element;
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createClassNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new ClassSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ClassCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__CLASS__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createCommentNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new CommentSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_CommentCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__COMMENT__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createComponentNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new ComponentSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ComponentCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__COMPONENT__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createConstraintNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new ConstraintSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ConstraintCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__CONSTRAINT__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createDataTypeNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new DataTypeSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_DataTypeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__DATATYPE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createEnumerationNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new EnumerationSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_EnumerationCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__ENUMERATION__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createInformationItemNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new InformationItemSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_InformationItemCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__INFORMATION_ITEM__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createInstanceSpecificationNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new InstanceSpecificationSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_InstanceSpecificationCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__INSTANCE_SPECIFICATION__NODE_TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createInterfaceNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new InterfaceSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_InterfaceCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__INTERFACE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createModelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new ModelSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ModelCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__MODEL__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPackageNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new PackageSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_PackageCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__PACKAGE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPrimitiveTypeNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new PrimitiveTypeSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_PrimitiveTypeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__PRIMITIVETYPE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createSignalNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new SignalSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_SignalCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__SIGNAL__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker), graphicalContainer);
	}

}
