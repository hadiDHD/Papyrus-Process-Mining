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

package org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions;

import org.eclipse.sirius.viewpoint.description.Viewpoint;

/**
 * Interface to use to be able to update the Viewpoint applied to the current Sirius Session
 */
public interface ISiriusSessionViewpointUpdater {

	/**
	 * Update the applied {@link Viewpoint}
	 */
	public void updateAppliedSiriusViewpoints();
}
