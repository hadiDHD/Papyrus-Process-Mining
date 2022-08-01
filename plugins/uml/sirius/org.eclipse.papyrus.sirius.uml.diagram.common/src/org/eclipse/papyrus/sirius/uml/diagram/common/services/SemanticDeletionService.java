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
package org.eclipse.papyrus.sirius.uml.diagram.common.services;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
/**
 * 
 * use a change context object with a such query to call one of my method aql:self.createNewElementFromEClassName(containerView,'ownedType')
 *
 */
public class SemanticDeletionService extends AbstractSemanticEditionService {
	
	public EObject deleteElement(final EObject current, final EObject containerView) {
		if (isPapyrusResource(current) || true) {//currently true to be able to test this method in a pure Sirius context
				final DestroyElementRequest request = new DestroyElementRequest(current, false);
				if (request != null) {
					Command cmd = getEMFEditCommand(current, request);
					final TransactionalEditingDomain domain = getEditingDomain(current);
					if (domain != null) {
						domain.getCommandStack().execute(cmd);
						
					}
				}
		}
		return null;
	}
}