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

import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.core.services.IServiceFactory;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;

/**
 * The factory used to create the Sirius Session Service
 */
public class SemanticServiceFactory implements IServiceFactory {

	/**
	 * The Papyrus service registry
	 */
	private ServicesRegistry servReg;

	/**
	 * the created {@link SessionService}
	 */
	private SiriusSemanticService semanticService;

	/**
	 * @see org.eclipse.papyrus.infra.core.services.IService#init(org.eclipse.papyrus.infra.core.services.ServicesRegistry)
	 *
	 * @param servicesRegistry
	 * @throws ServiceException
	 */
	@Override
	public void init(ServicesRegistry servicesRegistry) throws ServiceException {
		this.servReg = servicesRegistry;
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.services.IService#startService()
	 *
	 * @throws ServiceException
	 */
	@Override
	public void startService() throws ServiceException {
		// nothing to do
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.services.IService#disposeService()
	 *
	 * @throws ServiceException
	 */
	@Override
	public void disposeService() throws ServiceException {
		this.servReg = null;
		if (this.semanticService != null) {
			this.semanticService.disposeService();
		}
		this.semanticService = null;
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.services.IServiceFactory#createServiceInstance()
	 *
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Object createServiceInstance() throws ServiceException {
		if (this.servReg == null) {
			throw new ServiceException(NLS.bind("The service factory {0} seems disposed.", this.getClass().getName())); //$NON-NLS-1$
		}
		this.semanticService = new SiriusSemanticService();
		semanticService.init(this.servReg);
		semanticService.startService();
		return semanticService;
	}

}
