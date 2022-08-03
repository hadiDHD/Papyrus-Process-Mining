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

package org.eclipse.papyrus.sirius.editor.representation.architecture;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.papyrus.emf.ui.providers.labelproviders.DelegatingToEMFLabelProvider;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.ServiceUtils;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.sirius.editor.representation.ICreateSiriusDiagramEditorCommand;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.sirius.editor.representation.architecture.commands.CreateSiriusDiagramEditorViewCommand;
import org.eclipse.papyrus.sirius.editor.representation.architecture.internal.messages.Messages;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.swt.widgets.Display;

/**
 * This class provides useful method to create a new Sirius Diagram and open its editor
 */
public abstract class AbstractCreateSiriusDiagramEditorCommand implements ICreateSiriusDiagramEditorCommand {

	/**
	 *
	 * @param dialogTitle
	 *            the dialog title
	 * @param proposedName
	 *            the proposed name
	 * @return
	 *         the name entered by the user, or <code>null</code> in case of cancel
	 */
	protected String askDiagramName(final String dialogTitle, final String proposedName) {
		final InputDialog dialog = new InputDialog(Display.getCurrent().getActiveShell(), dialogTitle, Messages.AbstractCreateSiriusDiagramEditorCommand_DialogMessage, proposedName, null);
		if (dialog.open() == Window.OK) {
			return dialog.getValue();
		}
		return null;
	}

	/**
	 *
	 * @param diagramTemplatePrototype
	 *            the diagram template prototype used to create the {@link diagram}
	 * @param diagramName
	 *            the name of the created diagram
	 * @param semanticContext
	 *            the semantic context used for the creation of the {@link diagram}
	 * @param openAfterCreation
	 *            if <code>true</code> the editor will be opened after the creation
	 * @return
	 *         the created {@link diagram}
	 */
	protected DSemanticDiagram execute(final SiriusDiagramPrototype diagramTemplatePrototype, final String diagramName, final EObject semanticContext, final boolean openAfterCreation, final String id) {
		return execute(diagramTemplatePrototype, diagramName, semanticContext, semanticContext, openAfterCreation, id);
	}

	/**
	 *
	 * @param diagram
	 *            the diagram template prototype used to create the {@link diagram}
	 * @param diagramName
	 *            the name of the created diagram
	 * @param semanticContext
	 *            the semantic context used for the creation of the {@link diagram}
	 * @param graphicalContext
	 *            the graphical context used for the creation of the {@link diagram}
	 * @param openAfterCreation
	 *            if <code>true</code> the editor will be opened after the creation
	 * @return
	 *         the created {@link diagram}
	 */
	public DSemanticDiagram execute(final SiriusDiagramPrototype diagram, final String diagramName, final EObject semanticContext, final EObject graphicalContext, final boolean openAfterCreation, final String id) {
		final Resource res = semanticContext.eResource();
		final URI semanticURI = res.getURI();
		if (semanticURI.isPlatformPlugin()) {
			Activator.log.error(new UnsupportedOperationException("Diagram for element stored as platform plugin is not yet supported")); //$NON-NLS-1$
			return null;
		}

		final TransactionalEditingDomain domain = getEditingDomain(semanticContext);
		if (null == domain) {
			return null;
		}
		final String siriusDiagramMainTitle = getSiriusDiagramMainTitle(semanticContext);

		final CreateSiriusDiagramEditorViewCommand command = createDSemanticDiagramEditorCreationCommand(domain, diagram, diagramName, siriusDiagramMainTitle, semanticContext, openAfterCreation, id);

		domain.getCommandStack().execute(command);

		return command.getCreatedEditorView();
	}


	/**
	 *
	 * @param editingDomain
	 *            the editing domain to use for the command
	 * @param diagramPrototype
	 *            * the diagram template prototype used to create the {@link diagram}
	 * @param diagramName
	 *            the name of the created diagram
	 * @param diagramMainTitle
	 *            the main title of the diagram
	 * @param semanticContext
	 *            the semantic context used for the creation of the {@link diagram}
	 * @param graphicalContext
	 *            the graphical context used for the creation of the {@link diagram}
	 * @param openAfterCreation
	 *            if <code>true</code> the editor will be opened after the creation
	 * @return
	 *         the created {@link diagram}
	 */
	public CreateSiriusDiagramEditorViewCommand createDSemanticDiagramEditorCreationCommand(final TransactionalEditingDomain editingDomain,
			final SiriusDiagramPrototype diagramPrototype,
			final String diagramName,
			final String diagramMainTitle,
			final EObject semanticContext,
			final EObject graphicalContext,
			final boolean openAfterCreation,
			final String diagramId) {
		return new CreateSiriusDiagramEditorViewCommand(editingDomain, diagramPrototype, diagramName, diagramMainTitle, semanticContext, graphicalContext, openAfterCreation, diagramId);
	}

	/**
	 *
	 * @param editingDomain
	 *            the editing domain to use for the command
	 * @param diagramPrototype
	 *            * the diagram template prototype used to create the {@link diagram}
	 * @param diagramName
	 *            the name of the created diagram
	 * @param diagramMainTitle
	 *            the main title of the diagram
	 * @param semanticContext
	 *            the semantic context used for the creation of the {@link diagram}
	 * @param openAfterCreation
	 *            if <code>true</code> the editor will be opened after the creation
	 * @return
	 *         the created {@link diagram}
	 */
	public CreateSiriusDiagramEditorViewCommand createDSemanticDiagramEditorCreationCommand(final TransactionalEditingDomain editingDomain,
			final SiriusDiagramPrototype diagramPrototype,
			final String diagramName,
			final String diagramMainTitle,
			final EObject semanticContext,
			final boolean openAfterCreation,
			final String diagramId) {
		return new CreateSiriusDiagramEditorViewCommand(editingDomain, diagramPrototype, diagramName, diagramMainTitle, semanticContext, openAfterCreation, diagramId);
	}

	/**
	 *
	 * @param modelElement
	 *            an element of the edited model
	 * @return
	 *         the service registry or <code>null</code> if not found
	 */
	protected final ServicesRegistry getServiceRegistry(final EObject modelElement) {
		try {
			return ServiceUtilsForEObject.getInstance().getServiceRegistry(modelElement);
		} catch (ServiceException e) {
			Activator.log.error("ServicesRegistry not found", e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 *
	 * @param modelElement
	 *            an element of the edited model
	 * @return
	 *         the editing domain or <code>null</code> if not found
	 */
	protected final TransactionalEditingDomain getEditingDomain(final EObject modelElement) {
		final ServicesRegistry servicesRegistry = getServiceRegistry(modelElement);
		if (null == servicesRegistry) {
			return null;
		}
		try {
			return ServiceUtils.getInstance().getTransactionalEditingDomain(servicesRegistry);
		} catch (ServiceException e) {
			Activator.log.error("EditingDomain not found", e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 *
	 * @param semanticContext
	 *            the semantic context for the create DSemanticDiagram
	 * @return
	 *         the label to use as main title for the generated diagram
	 */
	protected String getSiriusDiagramMainTitle(final EObject semanticContext) {
		return DelegatingToEMFLabelProvider.INSTANCE.getText(semanticContext);
	}

}
