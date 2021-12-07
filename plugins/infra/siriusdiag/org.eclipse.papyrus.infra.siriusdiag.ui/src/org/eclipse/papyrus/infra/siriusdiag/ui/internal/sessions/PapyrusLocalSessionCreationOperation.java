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
package org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.siriusdiag.ui.Activator;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.services.IPapyrusSessionFactory;
import org.eclipse.sirius.business.api.session.DefaultLocalSessionCreationOperation;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.factory.SessionFactory;
import org.eclipse.sirius.viewpoint.Messages;

/**
 * A common operation to create a session and open it.
 */
// TODO provide patch to Sirius to get less overriden code
public class PapyrusLocalSessionCreationOperation extends DefaultLocalSessionCreationOperation {

	private IProgressMonitor monitor;

	private TransactionalEditingDomain transactionalEditingDomain;

	/**
	 * Constructor.
	 *
	 * @param sessionResourceURI
	 *                                       the {@link URI} of the Resource {@link Session} model
	 * @param monitor
	 *                                       {@link IProgressMonitor} to show progression of
	 *                                       {@link Session} creation
	 * @param transactionalEditingDomain
	 *                                       the editing domain to use
	 */
	public PapyrusLocalSessionCreationOperation(final URI sessionResourceURI, final IProgressMonitor monitor, final TransactionalEditingDomain transactionalEditingDomain) {
		super(sessionResourceURI, monitor);
		this.transactionalEditingDomain = transactionalEditingDomain;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.sirius.business.api.session.SessionCreationOperation#execute()
	 */
	@Override
	public void execute() throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		try {
			monitor.beginTask(Messages.DefaultLocalSessionCreationOperation_createResoureMsg, 3);
			monitor.subTask(Messages.DefaultLocalSessionCreationOperation_createSessionMsg);
			final SessionFactory factory = SessionFactory.INSTANCE;
			if (factory instanceof IPapyrusSessionFactory) {
				IPapyrusSessionFactory papyrusFactory = (IPapyrusSessionFactory) factory;
				session = papyrusFactory.createPapyrusSession(sessionResourceURI, SubMonitor.convert(monitor, 1), transactionalEditingDomain);

			} else {
				throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						NLS.bind("We expected to get the '{0}', so we are not able to create the Sirius Session for Papyrus", IPapyrusSessionFactory.class.getName()))); //$NON-NLS-1$
			}
			monitor.subTask(Messages.DefaultLocalSessionCreationOperation_sessionOpenMsg);
			session.open(SubMonitor.convert(monitor, 1));
		} finally {
			monitor.done();
		}
	}

}
