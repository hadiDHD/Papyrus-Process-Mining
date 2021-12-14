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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.refresh;

import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider;

public class CommonRefreshExtensionProvider implements IRefreshExtensionProvider {

	  private static final CommonDiagramRefreshExtension REFRESH_EXTENSION = new CommonDiagramRefreshExtension();

	  public CommonRefreshExtensionProvider() {
	    // empty constructor
	  }

	  /**
	   * @see org.eclipse.sirius.business.api.refresh.IRefreshExtensionProvider#getRefreshExtension(org.eclipse.sirius.DDiagram)
	   */
	  public IRefreshExtension getRefreshExtension(DDiagram viewPoint_p) {
	    return REFRESH_EXTENSION;
	  }

	  /**
	   * @see org.eclipse.sirius.business.api.refresh.IRefreshExtensionProvider#provides(org.eclipse.sirius.DDiagram)
	   */
	  public boolean provides(DDiagram viewPoint_p) {
	    return true;
	  }

	}
