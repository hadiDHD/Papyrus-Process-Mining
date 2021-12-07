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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and others
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.xtext.integration.ui;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode3EditPart;
import org.eclipse.sirius.diagram.ui.internal.parsers.MessageFormatParser;
import org.eclipse.sirius.diagram.ui.internal.providers.SiriusParserProvider;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

/**
 * The Class XTextSiriusParserProvider.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
@SuppressWarnings("restriction")
public class XTextSiriusParserProvider extends SiriusParserProvider {


	/**
	 * Gets the parser.
	 *
	 * @param visualID the visual ID
	 * @return the parser
	 * @see org.eclipse.sirius.diagram.ui.internal.providers.SiriusParserProvider#getParser(int)
	 */
	
	@Override
	protected IParser getParser(int visualID) {

		switch (visualID) {
		case DNode3EditPart.VISUAL_ID:
			return getDNodeBody_Parser();
		}

		return super.getParser(visualID);
	}

	/** The d edge name 6002 parser. */
	private IParser dNodeBodyParser;

	/**
	 * Gets the d node body parser.
	 *
	 * @return the d node body parser
	 */
	private IParser getDNodeBody_Parser() {
		if (dNodeBodyParser == null) {
			dNodeBodyParser = createDNodeBody_Parser();
		}
		return dNodeBodyParser;
	}

	/**
	 * Creates the D node body parser.
	 *
	 * @return the i parser
	 */
	protected IParser createDNodeBody_Parser() {
		EAttribute[] features = new EAttribute[] { ViewpointPackage.eINSTANCE.getDRepresentationElement_Name(), };
		MessageFormatParser parser = new MessageFormatParser(features);
		return parser;
	}


}
