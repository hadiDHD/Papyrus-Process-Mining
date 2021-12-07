/*****************************************************************************
 * Copyright (c) 2021 CEA LIST and others.
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

package org.eclipse.papyrus.infra.siriusdiag.sirius;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.core.services.IService;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.description.DiagramDescription;

/**
 * Interface to use to access to the Sirius Session
 */
public interface ISiriusSessionService extends IService {

	/**
	 * The ID of the service. This ID is also used in the plugin.xml to define the service
	 */
	public static final String SERVICE_ID = "org.eclipse.papyrus.infra.siriusdiag.sirius.ISiriusSessionService"; //$NON-NLS-1$

	/**
	 *
	 * @return
	 *         the Sirius Session associated to the current model
	 */
	public Session getSiriusSession();

	/**
	 * This method allows to get the DiagramDescription from the current Sirius Session. The shortcut {@link SiriusDiagramPrototype#getDiagramDescription()} is not enough, because
	 * the returned DiagramDescription won't be in the correct ResourceSet, so we won't be able to find it from the Session.
	 *
	 * @param siriusDiagramPrototype
	 *            the Papyrus {@link SiriusDiagramPrototype}
	 * @param semanticContext
	 *            the context used to create the diagram
	 * @return
	 *         the DiagramDescription from the Sirius Session
	 */
	public DiagramDescription getSiriusDiagramDescriptionFromPapyrusPrototype(final SiriusDiagramPrototype siriusDiagramPrototype, final EObject semanticContext);
}
