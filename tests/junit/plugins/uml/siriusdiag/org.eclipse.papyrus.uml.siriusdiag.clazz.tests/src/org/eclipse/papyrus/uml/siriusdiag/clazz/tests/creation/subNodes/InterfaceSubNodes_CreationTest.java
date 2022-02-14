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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.subNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramConstants;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.CreationToolsIds;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.MappingTypes;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DNodeListElement;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * This class tests the creation of subnode for Interface Node
 */
@PluginResource("resources/creation/subNodes/interfaceSubNodes/interfaceSubNodes.di")
public class InterfaceSubNodes_CreationTest extends AbstractSubNodeListElementCreationTests<Interface> {

	private static final String SEMANTIC_ONWER_NAME = "Interface1"; //$NON-NLS-1$

	private static final String DIAGRAM_NAME = "InterfaceSubNodesDiagram"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.subNodes.AbstractSubNodeListElementCreationTests#getSemanticOwner()
	 *
	 * @return
	 */
	@Override
	protected Interface getSemanticOwner() {
		return (Interface) this.root.getMember(SEMANTIC_ONWER_NAME);
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createDataTypeLabelNodeTest() {
		final DDiagramElement createdElement = createClassSubNode(MappingTypes.INTERFACE_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_DATATYPE_TOOL, MappingTypes.DATATYPE_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be a DataType instead of a {0}.", semantic.eClass().getName()),semantic instanceof DataType); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getNestedClassifiers().contains(semantic)); //$NON-NLS-1$
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createEnumerationLabelNodeTest() {
		final DDiagramElement createdElement = createClassSubNode(MappingTypes.INTERFACE_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_ENUMERATION_TOOL, MappingTypes.ENUMERATION_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be an Enumeration instead of a {0}.", semantic.eClass().getName()),semantic instanceof Enumeration); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", semanticOwner.getNestedClassifiers().contains(semantic)); //$NON-NLS-1$
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPrimitiveTypeLabelNodeTest() {
		final DDiagramElement createdElement = createClassSubNode(MappingTypes.INTERFACE_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_PRIMITIVETYPE_TOOL, MappingTypes.PRIMITIVETYPE_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be a PrimitiveType instead of a {0}.", semantic.eClass().getName()),semantic instanceof PrimitiveType); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getNestedClassifiers().contains(semantic)); //$NON-NLS-1$
	}

}

