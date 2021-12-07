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

package org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.services;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;

/**
 *
 */
public interface IPapyrusSessionFactory {

	public Session createPapyrusSession(URI sessionResourceURI, TransactionalEditingDomain ted) throws CoreException;

	public Session createPapyrusSession(final URI sessionResourceURI, IProgressMonitor monitor, TransactionalEditingDomain ted) throws CoreException;

	public Session createDefaultPapyrusSession(URI sessionResourceURI, TransactionalEditingDomain ted) throws CoreException;
}
