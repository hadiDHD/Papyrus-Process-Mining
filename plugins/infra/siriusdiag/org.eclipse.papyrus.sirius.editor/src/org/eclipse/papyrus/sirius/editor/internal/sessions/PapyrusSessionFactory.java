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
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.editor.internal.sessions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.sirius.editor.internal.sessions.services.IPapyrusSessionFactory;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.internal.session.SessionFactoryImpl;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.description.util.DescriptionResourceImpl;

/**
 * Papyrus extension of sirius session factory.
 *
 */
public class PapyrusSessionFactory extends SessionFactoryImpl implements IPapyrusSessionFactory {

	@Override
	public final Session createPapyrusSession(URI sessionResourceURI, TransactionalEditingDomain ted) throws CoreException {
		return createPapyrusSession(sessionResourceURI, new NullProgressMonitor(), ted);
	}

	@Override
	public Session createPapyrusSession(final URI sessionResourceURI, IProgressMonitor monitor, TransactionalEditingDomain ted) throws CoreException {
		preparePapyrusEditingDomain(sessionResourceURI, ted);
		boolean alreadyExistingResource = ted.getResourceSet().getURIConverter().exists(sessionResourceURI, null);
		Session session = null;
		if (alreadyExistingResource) {
			session = loadSessionModelResource(sessionResourceURI, ted, monitor);
		} else {
			session = createSessionResource(sessionResourceURI, ted, true, monitor);
		}
		return session;
	}

	@Override
	public Session createDefaultPapyrusSession(URI sessionResourceURI, TransactionalEditingDomain ted) throws CoreException {
		return createPapyrusSession(sessionResourceURI, new NullProgressMonitor(), ted);
	}

	/**
	 * This method comes in replacement of SessionImpl#prepareEditingDomain
	 *
	 * @param sessionResourceURI
	 * @param transactionalEditingDomain
	 * @return
	 */
	protected final TransactionalEditingDomain preparePapyrusEditingDomain(final URI sessionResourceURI, final TransactionalEditingDomain transactionalEditingDomain) {
		// ResourceSet set = ResourceSetFactory.createFactory().createResourceSet(sessionResourceURI);
		// final TransactionalEditingDomain transactionalEditingDomain = EditingDomainFactoryService.INSTANCE.getEditingDomainFactory().createEditingDomain(set);

		// Configure the resource set, its is done here and not before the
		// editing domain creation which could provide its own resource set.
		ResourceSet set = transactionalEditingDomain.getResourceSet();

		set.getLoadOptions().put(DescriptionResourceImpl.OPTION_USE_URI_FRAGMENT_AS_ID, true);

		configureDomain(transactionalEditingDomain, sessionResourceURI);

		return transactionalEditingDomain;
	}

	/**
	 * Create a {@link Session} for the given {@link DAnalysis}.
	 *
	 * @param analysis
	 *            the main DAnalysis of the {@link Session} to create.
	 * @param transactionalEditingDomain
	 *            the editing domain, might be usefull to help subclasses to create their sessions.
	 * @return a {@link Session}
	 */
	@Override
	protected Session createSession(DAnalysis analysis, TransactionalEditingDomain transactionalEditingDomain) {
		if (transactionalEditingDomain.getResourceSet() instanceof ModelSet) {
			return new PapyrusSession(analysis, transactionalEditingDomain);
		} else {
			return super.createSession(analysis, transactionalEditingDomain);
		}
	}
}
