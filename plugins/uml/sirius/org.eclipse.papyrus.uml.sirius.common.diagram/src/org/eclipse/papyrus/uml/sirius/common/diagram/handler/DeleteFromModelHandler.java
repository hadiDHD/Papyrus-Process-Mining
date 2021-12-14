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
package org.eclipse.papyrus.uml.sirius.common.diagram.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Handles the delete from model action (DEL key binding).
 * 
 * @author Aurelien
 */
public class DeleteFromModelHandler extends AbstractHandler {

    public boolean isEnabled() {
    	return true;
    }

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<IGraphicalEditPart> selection = getSelectedElements();
		for (IGraphicalEditPart selectedGEF: selection) {
			EObject current = selectedGEF.resolveSemanticElement();
			final DestroyElementRequest request = new DestroyElementRequest(current, false);
			if (request != null) {
				Command cmd = getEMFEditCommand(current, request);
				final TransactionalEditingDomain domain = getEditingDomain(current);
				if (domain != null) {
					domain.getCommandStack().execute(cmd);
					
				}
			}

			
		}
		return selection;
	}
	
	protected final TransactionalEditingDomain getEditingDomain(final EObject eobject) {
		return TransactionUtil.getEditingDomain(eobject);// TODO check another way in Papyrus
	}

	protected final IElementEditService getCommandProvider(final EObject eobject) {
		return ElementEditServiceUtils.getCommandProvider(eobject);
	}
	
	protected final ICommand getGMFEditCommand(final EObject eobject, final IEditCommandRequest request) {
		final IElementEditService provider = getCommandProvider(eobject);
		final ICommand cmd = provider.getEditCommand(request);
		return cmd;
	}

	protected final Command getEMFEditCommand(final EObject eobject, final IEditCommandRequest request) {
		final ICommand cmd = getGMFEditCommand(eobject, request);
		return cmd == null ? null : GMFtoEMFCommandWrapper.wrap(cmd);
	}


	/**
	 * Iterate over current selection and build a list of the {@link IGraphicalEditPart} contained in the selection.
	 *
	 * @return the currently selected {@link IGraphicalEditPart}
	 */
	protected List<IGraphicalEditPart> getSelectedElements() {
		List<IGraphicalEditPart> editparts = new ArrayList<IGraphicalEditPart>();
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			ISelection selection = activeWorkbenchWindow.getSelectionService().getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Iterator<?> it = structuredSelection.iterator();
				while (it.hasNext()) {
					Object object = it.next();
					if (object instanceof IGraphicalEditPart) {
						editparts.add((IGraphicalEditPart) object);
					}
				}
			} else if (selection instanceof IGraphicalEditPart) {
				editparts.add((IGraphicalEditPart) selection);
			}
		}
		return editparts;
	}

}
