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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests;

import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.junit.Assert;
import org.junit.Rule;

/**
 * Abstract tests to use for Sirius Diagram Tests
 */
public abstract class AbstractSiriusDiagramTests {//extends AbstractPapyrusTest {

	/**
	 * The editor fixture
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	/**
	 * This method is used to check if the current diagram has the expected synchronization status
	 * 
	 * @param isSynchronized
	 *            <code>true</code> if the diagram must be synchronized
	 */
	protected void checkSiriusDiagramSynchronization(final boolean isSynchronized) {
		final DSemanticDiagram siriusDiagram = this.fixture.getActiveSiriusDiagram();
		Assert.assertNotNull("We don't found a Sirius active diagram", siriusDiagram); //$NON-NLS-1$
		Assert.assertEquals("The synchronization status of the diagram is not the expected one", Boolean.valueOf(isSynchronized), Boolean.valueOf(siriusDiagram.isSynchronized())); //$NON-NLS-1$
	}
}
