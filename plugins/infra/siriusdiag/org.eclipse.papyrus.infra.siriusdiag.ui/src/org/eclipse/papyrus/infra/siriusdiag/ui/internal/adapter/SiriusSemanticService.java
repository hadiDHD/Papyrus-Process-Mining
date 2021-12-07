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
package org.eclipse.papyrus.infra.siriusdiag.ui.internal.adapter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.emf.utils.ISemanticService;
import org.eclipse.sirius.diagram.DDiagramElement;

/**
 * This service is in charge to get semantic element from sirius element
 */
public class SiriusSemanticService implements ISemanticService {

	/**
	 * The service registry associated to this current SiriusSemanticService
	 */
	private ServicesRegistry servicesRegistry;

	public SiriusSemanticService() {
		// nothing to do
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.services.IService#init(org.eclipse.papyrus.infra.core.services.ServicesRegistry)
	 *
	 * @param servicesRegistry
	 *                             the service registry associated to the current model
	 * @throws ServiceException
	 */
	@Override
	public void init(final ServicesRegistry servicesRegistry) throws ServiceException {
		this.servicesRegistry = servicesRegistry;
		if (this.servicesRegistry == null) {
			throw new ServiceException(NLS.bind("The service {0} can't be initialized because the ServicesRegistry is not found", ISemanticService.SERVICE_ID));
		}
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.services.IService#startService()
	 *
	 * @throws ServiceException
	 */
	@Override
	public void startService() throws ServiceException {
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.services.IService#disposeService()
	 *
	 * @throws ServiceException
	 */
	@Override
	public void disposeService() throws ServiceException {
		this.servicesRegistry = null;
	}

	/**
	 * @see org.eclipse.papyrus.infra.emf.utils.ISemanticService#getSemanticObject()
	 *
	 * @return
	 */
	@Override
	public EObject getSemanticObject(EObject obj) {
		if (obj instanceof DDiagramElement) {
			return ((DDiagramElement) obj).getTarget();
		}
		;
		return obj;
	}



}
