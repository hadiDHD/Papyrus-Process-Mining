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
package org.eclipse.papyrus.uml.sirius.xtext.integration.ui.editpart;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.Activator;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.configuration.ICustomDirectEditorConfiguration;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.configuration.IDirectEditorConfiguration;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.utils.DirectEditorsUtil;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.utils.IDirectEditorsIds;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeSpec;
import org.eclipse.sirius.diagram.description.tool.DirectEditLabel;
import org.eclipse.sirius.diagram.tools.internal.command.builders.DirectEditCommandBuilder;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.ToolBasedLabelDirectEditPolicy;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode3EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.NotationViewIDs;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.SiriusEditPartFactory;
import org.eclipse.sirius.diagram.ui.internal.edit.policies.SiriusTextSelectionEditPolicy;
import org.eclipse.sirius.diagram.ui.internal.providers.SiriusElementTypes;
import org.eclipse.sirius.diagram.ui.internal.providers.SiriusParserProvider;
import org.eclipse.sirius.diagram.ui.tools.internal.figure.ViewNodeFigure;
import org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.SiriusWrapLabel;
import org.eclipse.sirius.viewpoint.DMappingBased;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

/**
 * The Class XtextSiriusDNode3EditPart.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
@SuppressWarnings("restriction")
public class XtextSiriusDNode3EditPart extends DNode3EditPart implements ITextAwareEditPart {


	/**
	 * The parser.
	 *
	 * @was-generated
	 */
	protected IParser parser;
	/**
	 * The manager.
	 *
	 * @was-generated
	 */
	protected DirectEditManager manager;

	/** The configuration. */
	protected IDirectEditorConfiguration configuration;

	/**
	 * Instantiates a new xtext sirius DNode list element edit part.
	 *
	 * @param view
	 *            the view
	 */
	public XtextSiriusDNode3EditPart(View view) {
		super(view);
	}

	/**
	 * Perform direct edit request.
	 *
	 * @param request
	 *            the request
	 * @see org.eclipse.sirius.diagram.ui.internal.edit.parts.AbstractGeneratedDiagramNameEditPart#performDirectEditRequest(org.eclipse.gef.Request)
	 */

	@Override
	protected void performDirectEditRequest(final Request request) {

		EObject resolveSemanticElement = resolveSemanticElement();
		if (resolveSemanticElement instanceof DNodeSpec) {
			EObject target = ((DNodeSpec) resolveSemanticElement).getTarget();
			final String languagePreferred = Activator.getDefault().getPreferenceStore().getString(IDirectEditorsIds.EDITOR_FOR_ELEMENT + target.eClass().getInstanceClassName());
			if (languagePreferred != null && !languagePreferred.equals("")) {
				configuration = DirectEditorsUtil.findEditorConfiguration(languagePreferred, target, this);
			} else {
				super.performDirectEditRequest(request);
				configuration = DirectEditorsUtil.findEditorConfiguration(IDirectEditorsIds.UML_LANGUAGE, target, this);
			}
			configuration.preEditAction(target);
		}

		if (configuration instanceof ICustomDirectEditorConfiguration) {
			setManager(((ICustomDirectEditorConfiguration) configuration).createDirectEditManager(this));
			initializeDirectEditManager(request);
		}
	}

	/**
	 * Initialize direct edit manager.
	 *
	 * @param request
	 *            the request
	 */
	protected void initializeDirectEditManager(final Request request) {
		// initialize the direct edit manager
		try {
			getEditingDomain().runExclusive(new Runnable() {
				@Override
				public void run() {
					if (isActive() && isEditModeEnabled()) {
						if (request.getExtendedData()
								.get(RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR) instanceof Character) {
							Character initialChar = (Character) request.getExtendedData()
									.get(RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR);
							performDirectEdit(initialChar.charValue());
						} else {
							performDirectEdit();
						}
					}
				}
			});
		} catch (InterruptedException e) {
			Activator.log.error(e);
		}
	}

	/**
	 * Perform direct edit.
	 *
	 * @param initialCharacter
	 *            the initial character
	 */
	protected void performDirectEdit(char initialCharacter) {
		if (getManager() instanceof TextDirectEditManager) {
			((TextDirectEditManager) getManager()).show(initialCharacter);
		} else {
			performDirectEdit();
		}
	}

	/**
	 * Perform direct edit.
	 *
	 * @see org.eclipse.sirius.diagram.ui.internal.edit.parts.AbstractGeneratedDiagramNameEditPart#performDirectEdit()
	 */

	protected void performDirectEdit() {
		BusyIndicator.showWhile(Display.getDefault(), new java.lang.Runnable() {

			@Override
			public void run() {
				getManager().show();
			}
		});
	}

	/**
	 * Gets the completion processor.
	 *
	 * @return the completion processor
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart#getCompletionProcessor()
	 */

	@Override
	public IContentAssistProcessor getCompletionProcessor() {
		if (resolveSemanticElement() == null || getParser() == null) {
			return null;
		}
		return getParser().getCompletionProcessor(new EObjectAdapter(resolveSemanticElement()));
	}

	/**
	 * Gets the edits the text.
	 *
	 * @return the edits the text
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart#getEditText()
	 */

	@Override
	public String getEditText() {
		if (resolveSemanticElement() == null || getParser() == null) {
			return ""; //$NON-NLS-1$
		}
		return getParser().getEditString(new EObjectAdapter(resolveSemanticElement()), getParserOptions().intValue());
	}

	/**
	 * Gets the edits the text validator.
	 *
	 * @return the edits the text validator
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart#getEditTextValidator()
	 */

	@Override
	public ICellEditorValidator getEditTextValidator() {
		return new ICellEditorValidator() {

			@Override
			public String isValid(final Object value) {
				if (value instanceof String) {
					final EObject element = resolveSemanticElement();
					final IParser validationParser = getParser();
					try {
						final IParserEditStatus valid = (IParserEditStatus) getEditingDomain().runExclusive(new RunnableWithResult.Impl() {

							@Override
							public void run() {
								setResult(validationParser.isValidEditString(new EObjectAdapter(element), (String) value));
							}
						});
						return valid.getCode() == ParserEditStatus.EDITABLE ? null : valid.getMessage();
					} catch (final InterruptedException ie) {
						ie.printStackTrace();
					}
				}

				// shouldn't get here
				return null;
			}
		};
	}

	/**
	 * Gets the parser.
	 *
	 * @return the parser
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart#getParser()
	 */

	@Override
	public IParser getParser() {
		if (parser == null) {
			final String parserHint = Integer.toString(NotationViewIDs.DNODE_NAME_EDIT_PART_VISUAL_ID);
			final IAdaptable hintAdapter = new SiriusParserProvider.HintAdapter(SiriusElementTypes.DNode_2001, resolveSemanticElement(), parserHint);
			parser = ParserService.getInstance().getParser(hintAdapter);
		}
		return parser;
	}


	/**
	 * Gets the parser options.
	 *
	 * @return the parser options
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart#getParserOptions()
	 */

	@Override
	public ParserOptions getParserOptions() {
		return ParserOptions.NONE;
	}

	/**
	 * Sets the label text.
	 *
	 * @param text the new label text
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart#setLabelText(java.lang.String)
	 */

	@Override
	public void setLabelText(String text) {
		setLabelTextHelper(getContentPane(), text);
		final Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		if (pdEditPolicy instanceof SiriusTextSelectionEditPolicy) {
			((SiriusTextSelectionEditPolicy) pdEditPolicy).refreshFeedback();
		}
	}

	/**
	 * Sets the label text helper.
	 *
	 * @param figure the figure
	 * @param text the text
	 * @not-generated 
	 */
	protected void setLabelTextHelper(final IFigure figure, final String text) {
		if (figure instanceof SiriusWrapLabel) {
			((SiriusWrapLabel) figure).setText(text);
		}
		else if (figure instanceof ViewNodeFigure) {
			SiriusWrapLabel nodeLabel = ((ViewNodeFigure) figure).getNodeLabel();
			setLabelTextHelper(nodeLabel, text);
		} else if (figure instanceof Label) {
			((Label) figure).setText(text);
		}
	}
	/**
	 * Gets the adapter.
	 *
	 * @param key
	 *            the key
	 * @return the adapter
	 * @see org.eclipse.sirius.diagram.ui.edit.api.part.AbstractBorderedDiagramElementEditPart#getAdapter(java.lang.Class)
	 */

	@Override
	public Object getAdapter(Class key) {
		if (key == EObject.class) {
			return resolveTargetSemanticElement();
		}
		return super.getAdapter(key);
	}

	/**
	 * Gets the manager.
	 *
	 * @return the manager
	 * @was-generated
	 */
	protected DirectEditManager getManager() {
		if (manager == null) {
			setManager(new TextDirectEditManager(this, SiriusEditPartFactory.getTextCellEditorClass(this), SiriusEditPartFactory.getTextCellEditorLocator(this)));
		}
		return manager;
	}

	/**
	 * Sets the manager.
	 *
	 * @param manager
	 *            the new manager
	 * @was-generated
	 */
	protected void setManager(final DirectEditManager manager) {
		this.manager = manager;
	}

	/**
	 * Creates the default edit policies.
	 *
	 * @see org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode3EditPart#createDefaultEditPolicies()
	 */
	
	@Override
	protected void createDefaultEditPolicies() {
		// We want a special behavior with direct editing.
		removeEditPolicy(EditPolicy.DIRECT_EDIT_ROLE);
		if (isDirectEditEnabled()) {
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ToolBasedLabelDirectEditPolicy(getEditingDomain()));
		}
		super.createDefaultEditPolicies();
	}

	/**
	 * Test if this edit part must provides the direct edit.
	 * 
	 * @return true if the semantic element have a mapping with a direct edit tool and if the parent diagram is not in
	 *         LayoutingMode, false otherwise
	 */
	protected boolean isDirectEditEnabled() {
		boolean directEditEnabled = false;
		final EObject eObj = resolveSemanticElement();
		if (eObj instanceof DMappingBased) {
			final DMappingBased mappingBasedObject = (DMappingBased) eObj;
			if (mappingBasedObject.getMapping() != null && mappingBasedObject instanceof DDiagramElement
					&& ((DDiagramElement) mappingBasedObject).getDiagramElementMapping().getLabelDirectEdit() != null) {
				// check precondition
				DirectEditLabel labelDirectEdit = ((DDiagramElement) mappingBasedObject).getDiagramElementMapping().getLabelDirectEdit();
				final DirectEditCommandBuilder builder = new DirectEditCommandBuilder((DDiagramElement) mappingBasedObject, labelDirectEdit, null);
				directEditEnabled = builder.canDirectEdit();
			}
		}
		return directEditEnabled;
	}
}
