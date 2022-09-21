/*****************************************************************************
 * Copyright (c) 2022 CEA LIST
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
 *****************************************************************************/
package org.eclipse.papyrus.sirius.editor.internal.runnables;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.session.Session;

/**
 * This runnable is used to register the semantic resource into the created Sirius Session.
 */
public class RegisterSemanticResourceRunnable implements Runnable {

	/**
	 * the Sirius Session
	 */
	protected final Session session;

	/**
	 * the URI of the semantic resource
	 */
	private final URI semanticResourceURI;

	/**
	 * 
	 * Constructor.
	 *
	 * @param session
	 *            the Sirius Session
	 * @param modelSet
	 *            the Papyrus ModelSet
	 * @param semanticResourceURI
	 *            the URI of the semantic resource to register into the Sirius Session
	 */
	public RegisterSemanticResourceRunnable(final Session session, final URI semanticResourceURI) {
		this.session = session;
		this.semanticResourceURI = semanticResourceURI;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.session.addSemanticResource(this.semanticResourceURI, new NullProgressMonitor());
		this.session.save(new NullProgressMonitor());
	}
}
