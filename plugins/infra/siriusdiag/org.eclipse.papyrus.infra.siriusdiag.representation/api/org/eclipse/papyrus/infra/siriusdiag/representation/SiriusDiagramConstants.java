/*****************************************************************************
 * Copyright (c) 2021 CEA LIST and others.
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
 *
 *****************************************************************************/

package org.eclipse.papyrus.infra.siriusdiag.representation;

import org.eclipse.sirius.viewpoint.description.DAnnotation;

/**
 * Constants used by Papyrus Sirius - Diagram
 */
public class SiriusDiagramConstants {

	private SiriusDiagramConstants() {
		// to prevent instantiation
	}

	/**
	 * source used in a {@link DAnnotation} to identify a Sirius Diagram created with Papyrus
	 */
	public static final String PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_SOURCE = "Papyrus-SiriusDiagram"; //$NON-NLS-1$

	/**
	 * key used in a {@link DAnnotation} to get the ImplementationId value defined in the Papyrus architecture framework for the current diagram
	 */
	public static final String PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_KEY = "Papyrus-SiriusDiagram-ImplementationId"; //$NON-NLS-1$
}
