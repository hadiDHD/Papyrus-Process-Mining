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
 *    Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.textedit;

import org.eclipse.sirius.diagram.ui.internal.providers.SiriusEditPartProvider;

/**
 * The Class XtextSiriusEditPartProvider allows to provide the new {@link XtextSiriusEditPartFactory}.
 */
public class XtextSiriusEditPartProvider extends SiriusEditPartProvider {
	
	/**
	 * Instantiates a new Xtext sirius edit part provider.
	 * This Provider allows to define a new specific factory to integrate Xtext in the Sirius diagrams.
	 */
	public XtextSiriusEditPartProvider() {
        setFactory(new XtextSiriusEditPartFactory());
        setAllowCaching(true);
    }
	

}
