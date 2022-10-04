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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.drop.subNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.MappingTypes;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.SemanticDropToolsIds;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the semantic drop into a CD_Package
 */
@PluginResource("resources/drop/subNode/dropIntoPackage.di")
public class SemanticDropIntoPackageTest extends AbstractSemanticDropSubNodeTests<org.eclipse.uml2.uml.Package> {

	private static final String DIAGRAM_NAME = "ClassDiagram"; //$NON-NLS-1$

	private static final String SEMANTIC_ONWER_NAME = "Package";//$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.drop.subNodes.AbstractSemanticDropSubNodeTests#getSemanticOwner()
	 *
	 * @return
	 */

	@Override
	protected org.eclipse.uml2.uml.Package getSemanticOwner() {
		return (org.eclipse.uml2.uml.Package) this.root.getMember(SEMANTIC_ONWER_NAME);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void dropClassIntoPackage() {
		final DSemanticDiagram siriusDiagram = this.fixture.getActiveSiriusDiagram();
		Assert.assertNotNull(siriusDiagram);
		Assert.assertFalse("The diagram must be unsynchronized to test drop", siriusDiagram.isSynchronized()); //$NON-NLS-1$
		final NamedElement toDrop = getSemanticOwner().getMember("ClassToDrop"); //$NON-NLS-1$
		final DDiagramElement createdElement = dropNodeInDNodeContainer(MappingTypes.PACKAGE_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE, SemanticDropToolsIds.DROP__CLASS__TOOL, MappingTypes.CLASS_NODE_TYPE, toDrop);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertEquals("The dropped element is not the expected one", toDrop, semantic); //$NON-NLS-1$
	}
}
