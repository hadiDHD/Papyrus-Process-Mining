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
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * DataType Class test
 */
@PluginResource("resources/drop/topNode/TopNode_DropTest.di")
public class DataType_TopNode_DropTest extends AbstractTopNodeDropTest {

	private static final String CLASS_DIAGRAM_NAME = "TopNode_Drop_ClassDiagram"; //$NON-NLS-1$


	@Test
	@ActiveDiagram(CLASS_DIAGRAM_NAME)
	public void dataType_DropTest() {
		final NamedElement elementToBeDropped = this.root.getMember("DataTypeToDrop"); //$NON-NLS-1$
		Assert.assertTrue("The element to be dropped must be an instance of DataType", elementToBeDropped instanceof DataType);//$NON-NLS-1$
		dropDNodeContainer(elementToBeDropped, SemanticDropToolsIds.DROP_DATATYPE_TOOL, MappingTypes.DATATYPE_NODE_TYPE);
	}
}
