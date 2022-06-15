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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.CreationToolsIds;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_ClassCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_CommentCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_ComponentCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_ConstraintCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_DataTypeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_EnumerationCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_InformationItemCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_InstanceSpecificationCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_InterfaceCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_ModelCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_PackageCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_PrimitiveTypeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_SignalCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.ClassSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.CommentSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.ComponentSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.ConstraintSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.DataTypeSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.EnumerationSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.InformationItemSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.InstanceSpecificationSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.InterfaceSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.ModelSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.PackageSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.PrimitiveTypeSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.SignalSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.IGraphicalNodeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.ISemanticNodeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.SemanticAndGraphicalCreationChecker;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

/**
 * This class groups all Tests about allowed creations on the root of the Diagram represented by a {@link Model}
 */
@PluginResource("resources/creation/topNodes/TopNode_CreationTest.di") // the resource to import for the test in the workspace
public class CreateTopNodeOnClassDiagram_ModelOwner_Test extends AbstractCreateTopNodeOnDiagramTests {

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createClassNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new ClassSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ClassCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__CLASS__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createCommentNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new CommentSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_CommentCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__COMMENT__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createComponentNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new ComponentSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ComponentCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__COMPONENT__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createConstraintNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new ConstraintSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ConstraintCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__CONSTRAINT__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createDataTypeNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new DataTypeSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_DataTypeCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__DATATYPE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createEnumerationNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new EnumerationSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_EnumerationCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__ENUMERATION__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createInformationItemNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new InformationItemSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_InformationItemCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__INFORMATION_ITEM__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createInstanceSpecificationNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new InstanceSpecificationSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_InstanceSpecificationCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__INSTANCE_SPECIFICATION__NODE_TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createInterfaceNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new InterfaceSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_InterfaceCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__INTERFACE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createModelNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new ModelSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_ModelCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__MODEL__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPackageNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new PackageSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_PackageCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__PACKAGE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPrimitiveTypeNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new PrimitiveTypeSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_PrimitiveTypeCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__PRIMITIVETYPE__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createSignalNodeTest() {
		final Diagram diagram = getDiagram();
		final ISemanticNodeCreationChecker semanticChecker = new SignalSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalNodeCreationChecker = new CD_SignalCreationChecker(diagram, getTopGraphicalContainer());
		createNode(CreationToolsIds.CREATE__SIGNAL__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalNodeCreationChecker));
	}

}
