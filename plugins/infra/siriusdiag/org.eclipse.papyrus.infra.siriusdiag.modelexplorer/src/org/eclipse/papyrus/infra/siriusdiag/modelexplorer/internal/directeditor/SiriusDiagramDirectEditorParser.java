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
package org.eclipse.papyrus.infra.siriusdiag.modelexplorer.internal.directeditor;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.papyrus.infra.internationalization.utils.utils.LabelInternationalizationUtils;
//import org.eclipse.papyrus.infra.internationalization.utils.utils.LabelInternationalizationUtils;
import org.eclipse.sirius.business.api.query.DRepresentationQuery;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;

/**
 * Specific Parser for the Sirius Diagram Direct Editor.
 */
public class SiriusDiagramDirectEditorParser implements IParser {

	/**
	 * Name of the current Sirius Diagram.
	 */
	private String textToEdit;

	/**
	 * Determinate if this is a label modification.
	 */
	private boolean isLabelModification;

	/**
	 * Constructor.
	 *
	 * @param textToEdit
	 *            The text to edit.
	 * @param isLabelModification
	 *            Determinate if this is a label modification.
	 */
	public SiriusDiagramDirectEditorParser(final String textToEdit, final boolean isLabelModification) {
		this.textToEdit = textToEdit;
		this.isLabelModification = isLabelModification;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEditString(IAdaptable element, int flags) {
		return textToEdit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICommand getParseCommand(final IAdaptable element, final String newString, final int flags) {
		CompositeCommand result = new CompositeCommand("Rename"); //$NON-NLS-1$
		final EObject eObjectElement = element.getAdapter(EObject.class);
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(eObjectElement);

		AbstractTransactionalCommand cmd = new AbstractTransactionalCommand(editingDomain, "RenameCommand", null) { //$NON-NLS-1$

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
				if ((eObjectElement instanceof DSemanticDiagram) && (null != newString && !newString.isEmpty())) {
					if (isLabelModification) {
						LabelInternationalizationUtils.setLabel((eObjectElement), newString, null);
					} else {
						DRepresentationDescriptor representationDescriptor = new DRepresentationQuery((DRepresentation) eObjectElement).getRepresentationDescriptor();
						if (representationDescriptor != null) {
							representationDescriptor.setName(newString);
						}
					}
					return CommandResult.newOKCommandResult();
				}

				return CommandResult.newCancelledCommandResult();
			}
		};

		result.add(cmd);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrintString(IAdaptable element, int flags) {
		return textToEdit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAffectingEvent(Object event, int flags) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IParserEditStatus isValidEditString(IAdaptable element, String editString) {
		// Not used
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IContentAssistProcessor getCompletionProcessor(IAdaptable element) {
		// Not used
		return null;
	}

}
