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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.MappingTypes;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.SemanticDropToolsIds;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the semantic drop into a CD_Package
 */
@PluginResource("resources/drop/subNode/dropIntoModel.di")
public class SemanticDropIntoModelTest extends AbstractSemanticDropSubNodeTests<Model> {

	private static final String DIAGRAM_NAME = "ClassDiagram"; //$NON-NLS-1$

	private static final String SEMANTIC_ONWER_NAME = "Model";//$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes.AbstractSemanticDropSubNodeTests#getSemanticOwner()
	 *
	 * @return
	 */

	@Override
	protected Model getSemanticOwner() {
		return (org.eclipse.uml2.uml.Model) this.root.getMember(SEMANTIC_ONWER_NAME);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void dropPackageIntoModel() {
		final NamedElement toDrop = getSemanticOwner().getMember("PackageToDrop"); //$NON-NLS-1$
		final DDiagramElement createdElement = dropNodeInDNodeContainer(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE, SemanticDropToolsIds.DROP_PACKAGE_TOOL, MappingTypes.PACKAGE_NODE_TYPE, toDrop);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertEquals("The dropped element is not the expected one", toDrop, semantic); //$NON-NLS-1$
	}
}
