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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.diagram;

import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.sirius.diagram.architecture.internal.utils.Sirius_Diagram_Constants;
import org.junit.Test;

@PluginResource("resources/createDiagram/createDiagram.di")
public class CreateDiagramTest extends AbstractDiagramCreationTests {

	@Test
	public void createClassDiagramTest() throws Exception {
		checkDiagramCreationFromSiriusDiagramPrototype(this.rootModel, "newName", Sirius_Diagram_Constants.SIRIUS_CLASS_DIAGRAM_TYPE); //$NON-NLS-1$
	}

}
