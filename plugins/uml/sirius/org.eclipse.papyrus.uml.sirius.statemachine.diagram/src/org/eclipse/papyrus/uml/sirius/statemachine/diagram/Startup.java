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
package org.eclipse.papyrus.uml.sirius.statemachine.diagram;

import org.eclipse.ui.IStartup;

/**
 * Early start-up hook for the externalized profile applications subsystem.
 */
public class Startup implements IStartup {

	/**
	 * Instantiates a new startup.
	 */
	public Startup() {
		super();
	}

	/**
	 * Early startup.
	 *
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */

	@Override
	public void earlyStartup() {
		// Kick the index
		Activator.getDefault();
	}
}
