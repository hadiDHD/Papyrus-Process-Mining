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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes;

import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.SemanticDropToolsIds;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_ClassCreationChecker;
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
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.SemanticDropChecker;
import org.eclipse.sirius.diagram.DragAndDropTarget;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.InformationItem;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Signal;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the drop of semantic element into the Diagram. The diagram background is a UML model
 */
@PluginResource("resources/drop/topNode/TopNode_DropTest.di")
public class DropTopNodeOnClassDiagram_ModelOwner_Test extends AbstractTopNodeDropTests {


	private static final String CLASS_DIAGRAM_NAME = "TopNode_Drop_ClassDiagram"; //$NON-NLS-1$

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropComment() {
		// TODO : firstly we need to create the tool and to move the Comment in the expected layer
		// final Comment elementToBeDropped = this.root.getOwnedComments().get(0);
		// Assert.assertTrue("The element to be dropped must be an instance of Class", elementToBeDropped instanceof org.eclipse.uml2.uml.Comment);//$NON-NLS-1$
		// dropNode(SemanticDropToolsIds.DROP__COMMENT__TOOL, new CD_CommentCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropClass() {
		final NamedElement elementToBeDropped = this.root.getMember("ClassToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Class", elementToBeDropped instanceof org.eclipse.uml2.uml.Class);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__CLASS__TOOL, new CD_ClassCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropComponent() {
		final NamedElement elementToBeDropped = this.root.getMember("ComponentToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Component", elementToBeDropped instanceof Component);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__COMPONENT__TOOL, new CD_ComponentCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropConstraint() {
		final Constraint elementToBeDropped = this.root.getOwnedElements().stream().filter(Constraint.class::isInstance).map(Constraint.class::cast).findFirst().get();
		Assert.assertTrue("The element to be dropped must be an instance of Constraint", elementToBeDropped instanceof org.eclipse.uml2.uml.Constraint);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__CONSTRAINT__TOOL, new CD_ConstraintCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropDatatype() {
		final NamedElement elementToBeDropped = this.root.getMember("DataTypeToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of DataType", elementToBeDropped instanceof DataType);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__DATATYPE__TOOL, new CD_DataTypeCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropEnumeration() {
		final NamedElement elementToBeDropped = this.root.getMember("EnumerationToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Enumeration", elementToBeDropped instanceof Enumeration);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__ENUMERATION__TOOL, new CD_EnumerationCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropInformationItem() {
		final NamedElement elementToBeDropped = this.root.getMember("InformationItemToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of InformationItem", elementToBeDropped instanceof InformationItem);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__INFORMATION_ITEM__TOOL, new CD_InformationItemCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropInstanceSpecification() {
		final NamedElement elementToBeDropped = this.root.getMember("InstanceSpecificationToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of InstanceSpecification", elementToBeDropped instanceof InstanceSpecification);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__INSTANCE_SPECIFICATION__TOOL, new CD_InstanceSpecificationCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropInterface() {
		final NamedElement elementToBeDropped = this.root.getMember("InterfaceToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Interface", elementToBeDropped instanceof Interface);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__INTERFACE__TOOL, new CD_InterfaceCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropModel() {
		final NamedElement elementToBeDropped = this.root.getMember("ModelToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Model", elementToBeDropped instanceof Model);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__MODEL__TOOL, new CD_ModelCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropPackage() {
		final NamedElement elementToBeDropped = this.root.getMember("PackageToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Package", elementToBeDropped instanceof Package);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__PACKAGE__TOOL, new CD_PackageCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropPrimitiveType() {
		final NamedElement elementToBeDropped = this.root.getMember("PrimitiveTypeToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of PrimitiveType", elementToBeDropped instanceof PrimitiveType);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__PRIMITIVETYPE__TOOL, new CD_PrimitiveTypeCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dropSignal() {
		final NamedElement elementToBeDropped = this.root.getMember("SignalToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Signal", elementToBeDropped instanceof Signal);//$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__SIGNAL__TOOL, new CD_SignalCreationChecker(this.diagram, this.diagramRepresentation), new SemanticDropChecker(elementToBeDropped), elementToBeDropped);
	}

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.AbstractTopNodeDropTests#getTopNodeGraphicalContainer()
	 *
	 * @return
	 */

	@Override
	protected DragAndDropTarget getTopNodeGraphicalContainer() {
		return this.diagramRepresentation;
	}
}
