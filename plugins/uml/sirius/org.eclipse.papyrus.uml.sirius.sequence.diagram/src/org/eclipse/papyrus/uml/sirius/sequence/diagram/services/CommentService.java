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
 *    Yann Binot (ARTAL) - yann.binot@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;

/**
 * The Class CommentService.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class CommentService {
	/** The service. */
	static private CommentService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private CommentService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public CommentService getInstance() {
		if (service == null) {
			service = new CommentService();
		}
		return service;
	}

	/**
	 * Return all constriantedElement for given Constraint <b> Exception for PartDeploymentLink (return its
	 * DeployedElement) <b>.
	 *
	 * @param comment the comment
	 * @return the list
	 */
	public List<?> targeFinderExpressionForComment(Comment comment) {
		List<EObject> result = new ArrayList<>();
		EList<Element> annotatedElements = comment.getAnnotatedElements();
		for (Element element : annotatedElements) {
			result.add(element);
		}


		return result;
	}

}
