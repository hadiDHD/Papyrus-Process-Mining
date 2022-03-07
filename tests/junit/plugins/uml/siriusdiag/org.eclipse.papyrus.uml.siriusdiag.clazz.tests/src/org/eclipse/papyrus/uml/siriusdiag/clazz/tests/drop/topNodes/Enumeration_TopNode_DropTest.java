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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes;

import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.MappingTypes;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.SemanticDropToolsIds;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * Enumeration Class test
 */
@PluginResource("resources/drop/topNode/TopNode_DropTest.di")
public class Enumeration_TopNode_DropTest extends AbstractTopNodeDropTest {

	private static final String CLASS_DIAGRAM_NAME = "TopNode_Drop_ClassDiagram"; //$NON-NLS-1$

	
	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void enumeration_DropTest() {
		final NamedElement elementToBeDropped = this.root.getMember("EnumerationToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of Enumeration", elementToBeDropped instanceof Enumeration);//$NON-NLS-1$
		dropDNodeContainer(elementToBeDropped, SemanticDropToolsIds.DROP_ENUMERATION_TOOL, MappingTypes.ENUMERATION_NODE_TYPE);
	}
}
