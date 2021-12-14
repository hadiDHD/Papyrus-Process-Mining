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

package org.eclipse.papyrus.infra.siriusdiag.ui.utils;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServiceNotFoundException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.AbstractServiceUtils;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.editor.NestedSiriusDiagramViewEditor;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Adapted from ServiceUtilsForGMF
 * Set of utility methods for accessing core Services. This methods are designed
 * to be used from code from Papyrus Sirius diagrams.
 *
 */
public final class ServiceUtilsForSirius extends AbstractServiceUtils<IDiagramEditDomain> {

	private final static ServiceUtilsForSirius INSTANCE = new ServiceUtilsForSirius();

	/**
	 *
	 * Constructor.
	 *
	 */
	private ServiceUtilsForSirius() {
		// to prevent instantiation
	}


	/**
	 * Get the singleton instance of the class.
	 *
	 * @return
	 */
	public static final ServiceUtilsForSirius getInstance() {
		return INSTANCE;
	}

	/**
	 * Get the {@link ServicesRegistry} from an {@link IDiagramEditDomain}.
	 * Throw an exception if the ServiceRegistry can't be found. <br>
	 * This method get the ServiceRegistry from the IDiagramEditDomain
	 * associated to an GMF EditPart. This IDiagramEditDomain can be obtained by
	 * calling:
	 * <ul>
	 * <li>from EditParts - editpart.getDiagramEditDomain()</li>
	 * <li>from EditPolicies - policy.getHost().getDiagramEditDomain()</li>
	 * </ul>
	 *
	 * @see org.eclipse.papyrus.infra.core.utils.AbstractServiceUtils#getServiceRegistry(java.lang.Object)
	 *
	 * @param domain
	 *            The domain associated to an EditPart.
	 * @return
	 * @throws ServiceException
	 *             If an error occurs
	 * @throws ServiceNotFoundException
	 *             If the ServiceRegistry can't be found from the domain
	 */
	@Override
	public ServicesRegistry getServiceRegistry(final IDiagramEditDomain domain) throws ServiceException {
		if (domain instanceof DiagramEditDomain) {
			IWorkbenchPart part = ((DiagramEditDomain) domain).getEditorPart().getEditorSite().getPart();
			if (part instanceof NestedSiriusDiagramViewEditor) {
				return ((NestedSiriusDiagramViewEditor) part).getServicesRegistry();
			}
		}

		throw new ServiceNotFoundException("Can't find ServiceRegistry from '" + domain + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
