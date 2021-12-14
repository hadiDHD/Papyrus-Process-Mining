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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.provider;

import org.eclipse.papyrus.uml.sirius.sequence.diagram.utils.ReorderSequenceRegistry;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider;

/**
 * The Class RefreshSequenceExtensionProvider.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class RefreshSequenceExtensionProvider implements IRefreshExtensionProvider {

	/**
	 * Constructor.
	 *
	 */
	public RefreshSequenceExtensionProvider() {
	}

	/**
	 * Provides Refresh Extension for Sequence Diagram.
	 *
	 * @param viewPoint
	 *            the view point
	 * @return true, if successful
	 * @see org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider#provides(org.eclipse.sirius.diagram.DDiagram)
	 */

	@Override
	public boolean provides(DDiagram viewPoint) {
		return viewPoint.getDescription().getName().equals("SequenceDiagram");
	}

	/**
	 * Gets the refresh extension.
	 *
	 * @param viewPoint the view point
	 * @return the refresh extension
	 * @see org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider#getRefreshExtension(org.eclipse.sirius.diagram.DDiagram)
	 */

	@Override
	public IRefreshExtension getRefreshExtension(DDiagram viewPoint) {
		return new IRefreshExtension() {

			@Override
			public void postRefresh(DDiagram dDiagram) {
				ReorderSequenceRegistry.getInstance().clear();

			}

			@Override
			public void beforeRefresh(DDiagram dDiagram) {

			}
		};
	}

}
