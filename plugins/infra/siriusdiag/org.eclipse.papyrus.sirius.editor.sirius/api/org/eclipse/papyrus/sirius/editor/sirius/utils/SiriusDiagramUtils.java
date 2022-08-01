/*****************************************************************************
 * Copyright (c) 2022 CEA LIST and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Vincent Lorenzo (CEA LIST) <vincent.lorenzo@cea.fr> - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.sirius.utils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This class provides utility methods for Sirius GMF Diagrams
 */
public final class SiriusDiagramUtils {

	/**
	 *
	 * Constructor.
	 *
	 */
	private SiriusDiagramUtils() {
		// to prevent instantiation
	}

	/**
	 *
	 * @param diagram
	 *            a GMF diagram, it can't be <code>null</code>
	 * @return
	 *         <code>true</code> if the Diagram is a Papyrus-Sirius diagram
	 */
	public static final boolean isSiriusDiagram(final Diagram diagram) {
		Assert.isNotNull(diagram, "The diagram must not be null"); //$NON-NLS-1$
		return diagram.eContainer() != null; // we use this way to distinguish easily Papyrus GMF Diagram integration and Papyrus-Sirius Diagram integration
	}

	/**
	 *
	 * @param view
	 *            a GMF view, it can't be <code>null</code>
	 * @return
	 *         <code>true</code> if the view is owned by a Papyrus-Sirius diagram
	 */
	public static final boolean isSiriusDiagramView(final View view) {
		Assert.isNotNull(view, "The view must not be null"); //$NON-NLS-1$
		return isSiriusDiagram(view.getDiagram()); // we use this way to distinguish easily Papyrus GMF Diagram integration and Papyrus-Sirius Diagram integration
	}
}
