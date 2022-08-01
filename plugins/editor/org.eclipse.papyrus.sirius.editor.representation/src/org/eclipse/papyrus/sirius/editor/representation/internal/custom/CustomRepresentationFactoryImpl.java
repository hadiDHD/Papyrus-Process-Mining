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

package org.eclipse.papyrus.sirius.editor.representation.internal.custom;

import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.sirius.editor.representation.impl.RepresentationFactoryImpl;

/**
 * Custom factory for to manipulate the representation model
 */
public class CustomRepresentationFactoryImpl extends RepresentationFactoryImpl {

	/**
	 * @see org.eclipse.papyrus.sirius.editor.representation.impl.RepresentationFactoryImpl#createSiriusDiagramPrototype()
	 *
	 * @return
	 */
	@Override
	public SiriusDiagramPrototype createSiriusDiagramPrototype() {
		return new CustomSiriusDiagramPrototypeImpl();
	}

}
