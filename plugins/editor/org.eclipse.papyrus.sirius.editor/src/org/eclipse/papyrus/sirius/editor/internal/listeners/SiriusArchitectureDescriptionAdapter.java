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

package org.eclipse.papyrus.sirius.editor.internal.listeners;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.papyrus.infra.architecture.listeners.AbstractArchitectureDescriptionAdapter;
import org.eclipse.papyrus.sirius.editor.internal.sessions.ISiriusSessionViewpointUpdater;

/**
 * This adapter allows to be notified when the Papyrus Architecture changes and allows to update the Sirius Session consequently
 */
public class SiriusArchitectureDescriptionAdapter extends AbstractArchitectureDescriptionAdapter {

	/**
	 * the Sirius session viewpoint updater
	 */
	private final ISiriusSessionViewpointUpdater updater;

	/**
	 *
	 * Constructor.
	 *
	 * @param updater
	 *            the Sirius session viewpoint updater
	 */
	public SiriusArchitectureDescriptionAdapter(final ISiriusSessionViewpointUpdater updater) {
		this.updater = updater;
	}

	/**
	 * @see org.eclipse.papyrus.infra.architecture.listeners.AbstractArchitectureDescriptionAdapter#fireArchitectureContextChanged(org.eclipse.emf.common.notify.Notification)
	 *
	 * @param notification
	 */
	@Override
	public void fireArchitectureContextChanged(Notification notification) {
		this.updater.updateAppliedSiriusViewpoints();
	}

	/**
	 * @see org.eclipse.papyrus.infra.architecture.listeners.AbstractArchitectureDescriptionAdapter#fireArchitectureViewpointsChanged(org.eclipse.emf.common.notify.Notification)
	 *
	 * @param notification
	 */
	@Override
	public void fireArchitectureViewpointsChanged(Notification notification) {
		this.updater.updateAppliedSiriusViewpoints();
	}

}
