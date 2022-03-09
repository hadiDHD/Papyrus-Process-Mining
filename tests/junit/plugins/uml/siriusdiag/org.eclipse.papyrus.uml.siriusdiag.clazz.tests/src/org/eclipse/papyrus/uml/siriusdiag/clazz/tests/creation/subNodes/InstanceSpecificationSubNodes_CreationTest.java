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
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.CreationToolsIds;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.MappingTypes;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.uml2.uml.Slot;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the creation of subnode for InstanceSpecification Node
 */
@PluginResource("resources/creation/subNodes/instancespecificationSubNodes/instancespecificationSubNodes.di")
public class InstanceSpecificationSubNodes_CreationTest extends AbstractSubNodeListElementCreationTests<org.eclipse.uml2.uml.InstanceSpecification>{

	private static final String SEMANTIC_ONWER_NAME = "InstanceSpecification1"; //$NON-NLS-1$

	private static final String DIAGRAM_NAME = "InstanceSpecificationSubNodesDiagram"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.subNodes.AbstractSubNodeListElementCreationTests#getSemanticOwner()
	 *
	 * @return
	 */
	@Override
	protected org.eclipse.uml2.uml.InstanceSpecification getSemanticOwner() {
		return (org.eclipse.uml2.uml.InstanceSpecification) this.root.getMember(SEMANTIC_ONWER_NAME);
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createSlotLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.INSTANCESPECIFICATION_NODE_SLOTS_COMPARTMENT_TYPE, CreationToolsIds.CREATE__SLOT__TOOL, MappingTypes.SLOT_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be a Slot instead of a {0}.", semantic.eClass().getName()),semantic instanceof Slot); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getSlots().contains(semantic)); //$NON-NLS-1$
	}

}