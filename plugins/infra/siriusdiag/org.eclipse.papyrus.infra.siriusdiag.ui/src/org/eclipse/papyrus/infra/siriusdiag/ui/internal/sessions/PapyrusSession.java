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

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl;
import org.eclipse.sirius.viewpoint.DAnalysis;

/**
 * Papyrus extension to use session with .
 *
 * This custom session doesn't nothing for save. The save action must be managed by Papyrus itself.
 *
 */
@SuppressWarnings("restriction")
public class PapyrusSession extends DAnalysisSessionImpl {

	/**
	 * Constructor.
	 *
	 * @param mainDAnalysis
	 */
	public PapyrusSession(final DAnalysis mainDAnalysis, TransactionalEditingDomain transactionalEditingDomain) {
		super(mainDAnalysis);
	}

	/**
	 *
	 * @see org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl#save(java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 *
	 * @param options
	 * @param monitor
	 */
	@Override
	public void save(Map<?, ?> options, IProgressMonitor monitor) {
		// do nothing on save, delegate to Papyrus
	}

	/**
	 * @see org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl#doSave(java.util.Map, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 *
	 * @param options
	 * @param monitor
	 * @param runExclusive
	 */
	// @Override
	// protected void doSave(Map<?, ?> options, IProgressMonitor monitor, boolean runExclusive) {
	// // do nothing on save, delegate to Papyrus
	// }

	public void papyrusSave(Map<?, ?> options, IProgressMonitor monitor) {
		super.save(options, monitor);
	}
}
