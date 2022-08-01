/******************************************************************************
 * Copyright (c) 2021, 2022 CEA LIST, Artal Technologies, Obeo and others.
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
 *  Jessy MALLET (OBEO) <jessy.mallet@obeo.fr> - Bug 579782
 *****************************************************************************/
package org.eclipse.papyrus.sirius.editor.internal.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gmf.runtime.common.ui.util.IPartSelector;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.InsertAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PromptingDeleteAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PromptingDeleteFromModelAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.ToggleRouterAction;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.infra.internationalization.common.editor.IInternationalizationEditor;
import org.eclipse.papyrus.infra.ui.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.ui.lifecycleevents.ISaveAndDirtyService;
import org.eclipse.papyrus.infra.widgets.util.IRevealSemanticElement;
import org.eclipse.papyrus.infra.widgets.util.NavigationTarget;
import org.eclipse.papyrus.sirius.editor.Activator;
import org.eclipse.papyrus.sirius.editor.internal.sessions.SessionPrinter;
import org.eclipse.papyrus.sirius.editor.sirius.ISiriusSessionService;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.sirius.business.api.dialect.command.RefreshRepresentationsCommand;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.internal.session.SessionTransientAttachment;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramCommandStack;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar.Tabbar;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Sirius Diagram Editor.
 *
 * This editor is contributed throw the extension point org.eclipse.papyrus.infra.ui.papyrusDiagram.
 *
 * In order to get the new child menu, we register the action bar contribution using this same extension point and we use if for this editor.
 */
@SuppressWarnings("restriction")
public class NestedSiriusDiagramViewEditor extends DDiagramEditorImpl implements IEditingDomainProvider, IInternationalizationEditor, IRevealSemanticElement, NavigationTarget {

	/** the service registry */
	protected ServicesRegistry servicesRegistry;

	private Session session;

	private URI uri;

	private TransactionalEditingDomain editingDomain;

	private DSemanticDiagram diagram;

	private KeyHandler keyHandler;

	/**
	 *
	 * Constructor.
	 *
	 * @param servicesRegistry
	 *            the Papyrus service registry, it can't be <code>null</code>
	 * @param prototype
	 *            the edited element, it can't be <code>null</code>
	 */
	public NestedSiriusDiagramViewEditor(ServicesRegistry servicesRegistry, DSemanticDiagram diagram) {
		super();
		this.servicesRegistry = servicesRegistry;

		ISaveAndDirtyService saveAndDirtyService = null;
		try {
			saveAndDirtyService = servicesRegistry.getService(ISaveAndDirtyService.class);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		saveAndDirtyService.registerIsaveablePart(this);

		this.diagram = diagram;
		try {
			this.editingDomain = servicesRegistry.getService(TransactionalEditingDomain.class);
			// this.session = (PapyrusSession) PapyrusSessionManager.INSTANCE.getSession(diagram.eResource().getURI(), new NullProgressMonitor(), this.editingDomain);
			this.session = getCurrentSession();
			SessionPrinter.print(session, this.getClass().getCanonicalName() + " " + "Constructor");

			this.uri = diagram.eResource().getURI().appendFragment(diagram.eResource().getURIFragment(diagram));

			Assert.isNotNull(this.diagram, "The edited diagram is null. The Diagram Editor creation failed"); //$NON-NLS-1$
			Assert.isNotNull(this.servicesRegistry, "The papyrus ServicesRegistry is null. The Diagram Editor creation failed."); //$NON-NLS-1$
			initializeEditingDomain();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	private ISiriusSessionService getSiriusSessionService() {
		try {
			return (ISiriusSessionService) this.servicesRegistry.getService(ISiriusSessionService.SERVICE_ID);
		} catch (ServiceException e) {
			Activator.log.error(e);
		}
		return null;
	}

	private Session getCurrentSession() {
		final ISiriusSessionService service = getSiriusSessionService();
		if (service != null) {
			return service.getSiriusSession();
		}
		return null;
	}

	/**
	 *
	 * @see org.eclipse.papyrus.sirius.editor.internal.editor.CustomDocumentStructureTemplateEditor#initializeEditingDomain()
	 *
	 */
	public void initializeEditingDomain() {
		if (this.servicesRegistry == null) {
			return;
		}
		initDomainAndStack();
	}

	/**
	 * @see org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 *
	 * @param part
	 * @param selection
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (getSite().getPage().getActiveEditor() instanceof IMultiDiagramEditor) {
			IMultiDiagramEditor editor = (IMultiDiagramEditor) getSite().getPage().getActiveEditor();
			// If not the active editor, ignore selection changed.
			IEditorPart activeEditor = editor.getActiveEditor();

			if (this.equals(activeEditor)) {
				super.selectionChanged(activeEditor, selection);
				updateActions(getSelectionActions());
				rebuildStatusLine();

			} else {
				super.selectionChanged(part, selection);
			}
		} else {
			super.selectionChanged(part, selection);
		}
	}

	/**
	 * @see org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl#createTabbar(org.eclipse.swt.widgets.Composite, org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart)
	 *
	 * @param parentComposite
	 * @param part
	 * @return
	 */
	@Override
	protected Tabbar createTabbar(Composite parentComposite, IDiagramWorkbenchPart part) {
		return new PapyrusTabbar(parentComposite, this);
	}

	/**
	 * this method is in charge to init the Editing Domain and the CommandStack
	 */
	protected void initDomainAndStack() {
		this.editingDomain.getCommandStack().addCommandStackListener(new CommandStackListener() {

			@Override
			public void commandStackChanged(final EventObject event) {
				if (getSite() != null) {
					getSite().getShell().getDisplay().asyncExec(() -> {
						firePropertyChange(IEditorPart.PROP_DIRTY);
					});
				}
			}
		});

	}

	/**
	 * @generated
	 */
	@Override
	protected void configureDiagramEditDomain() {
		// TODO this method should probably do nothing in order to reuse papyrus stack and domain

		DiagramEditDomain editDomain = new DiagramEditDomain(this);
		editDomain.setActionManager(createActionManager());
		setEditDomain(editDomain);

		if (editDomain != null) {
			final CommandStack stack = editDomain.getCommandStack();

			if (stack != null) {
				// dispose the old stack
				stack.dispose();
			}

			// create and assign the new stack
			final DiagramCommandStack diagramStack = new DDiagramCommandStack(getDiagramEditDomain());
			diagramStack.setOperationHistory(getOperationHistory());

			// changes made on the stack can be undone from this editor
			diagramStack.setUndoContext(getUndoContext());

			editDomain.setCommandStack(diagramStack);
		}

	}


	/**
	 * 
	 * @see org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl#getEditingDomain()
	 *
	 * @return
	 */
	@Override
	public TransactionalEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	/**
	 * 
	 * @see org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 *
	 * @param site
	 * @param input
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) {// throws PartInitException {
		setSite(site);
		// Session is closed when we reopen eclipse
		try {
			TransactionalEditingDomain ted = this.session.getTransactionalEditingDomain();
			ServicesRegistry serviceRegistry = ServiceUtilsForEObject.getInstance().getServiceRegistry(this.diagram);
			ModelSet modelSet = serviceRegistry.getService(ModelSet.class);
			if (!this.session.isOpen()) {
				this.session.open(new NullProgressMonitor());

				IEditingSession uiSession = SessionUIManager.INSTANCE.getOrCreateUISession(session);
				uiSession.open();

				// TODO : done in another place
				UmlModel uml = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
				this.session.addSemanticResource(uml.getResourceURI(), new NullProgressMonitor());

				this.diagram.getTarget().eAdapters().add(new SessionTransientAttachment(session));
				modelSet.getResources().add(session.getSessionResource());
			}
		} catch (ServiceException e1) {
			Activator.log.error(e1);
		}
		final SiriusDiagramEditorInput diagramViewEditorInput = new SiriusDiagramEditorInput(this.diagram, uri, session);
		this.editingDomain.getCommandStack().execute(new RecordingCommand(this.editingDomain) {
			@Override
			protected void doExecute() {
				try {
					NestedSiriusDiagramViewEditor.super.init(site, diagramViewEditorInput);
				} catch (PartInitException e) {
					Activator.log.error(e);
				}
			}
		});
	}

	/**
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor#doSetInput(org.eclipse.ui.IEditorInput, boolean)
	 *
	 * @param input
	 * @param releaseEditorContents
	 * @throws CoreException
	 */
	@Override
	public void doSetInput(IEditorInput input, boolean releaseEditorContents) throws CoreException {
		super.doSetInput(input, releaseEditorContents);
	}

	/**
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor#isDirty()
	 *
	 * @return
	 */
	@Override
	public boolean isDirty() {
		// manage by the Papyrus main editor
		return false;
	}

	/**
	 * 
	 * @see org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditor#isSaveAsAllowed()
	 *
	 * @return
	 */
	@Override
	public boolean isSaveAsAllowed() {
		// manage by the Papyrus main editor
		return false;
	}

	// Need to be added because palette manager is null when closing and reopening a papyrus model
	@Override
	public void createPartControl(Composite parent) {
		try {
			// recreate session with the good transactional editing domain
			TransactionalEditingDomain ted = this.session.getTransactionalEditingDomain();
			ServicesRegistry serviceRegistry = ServiceUtilsForEObject.getInstance().getServiceRegistry(this.diagram);
			ModelSet modelSet = serviceRegistry.getService(ModelSet.class);
			// PapyrusSession.setTransactionalEditingDomain(modelSet.getTransactionalEditingDomain());
			// this.session = PapyrusSessionFactory.INSTANCE.createSession(session.getSessionResource().getURI(), new NullProgressMonitor(), ted);
			// PapyrusSessionFactory.INSTANCE.setServiceRegistry(serviceRegistry);
			// PapyrusSessionManager.INSTANCE.add(session);
			// TODO : VL : we must find a way to remove a UML dependency in this plugin...
			UmlModel uml = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
			this.session.addSemanticResource(uml.getResourceURI(), new NullProgressMonitor());

			this.diagram.getTarget().eAdapters().add(new SessionTransientAttachment(session));
			modelSet.getResources().add(session.getSessionResource());

			if (this.session != null && !this.session.isOpen()) {
				this.session.open(new NullProgressMonitor());
			}

			SessionPrinter.print(session, this.getClass().getCanonicalName() + " " + "createPartControl");

		} catch (ServiceException e1) {
			Activator.log.error(e1);
		}
		super.createPartControl(parent);
	}

	/**
	 * reveal all editpart that represent an element in the given list.
	 *
	 * @see org.eclipse.papyrus.infra.core.ui.IRevealSemanticElement#revealSemanticElement(java.util.List)
	 *
	 */
	@Override
	public void revealSemanticElement(List<?> elementList) {
		revealElement(elementList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean revealElement(Object element) {
		return revealElement(Collections.singleton(element));
	}

	public Object getSemanticElement(Object wrapper) {
		if (wrapper instanceof IGraphicalEditPart) {
			return ((IGraphicalEditPart) wrapper).resolveSemanticElement();
		}
		if (wrapper instanceof IAdaptable) {
			return ((IAdaptable) wrapper).getAdapter(EObject.class);
		}
		return null;
	}


	/**
	 * {@inheritDoc}
	 *
	 * reveal all editpart that represent an element in the given list.
	 *
	 * @see org.eclipse.papyrus.infra.core.ui.IRevealSemanticElement#revealSemanticElement(java.util.List)
	 *
	 */
	@Override
	public boolean revealElement(Collection<?> elementList) {

		// get the graphical viewer
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		if (graphicalViewer != null) {

			// look amidst all edit part if the semantic is contained in the list
			Iterator<?> iter = graphicalViewer.getEditPartRegistry().values().iterator();
			IGraphicalEditPart researchedEditPart = null;
			List<?> clonedList = new ArrayList<>(elementList);
			List<IGraphicalEditPart> partSelection = new ArrayList<>();

			while (iter.hasNext() && !clonedList.isEmpty()) {
				Object currentEditPart = iter.next();
				// look only admit IPrimary editpart to avoid compartment and labels of links
				if (currentEditPart instanceof IPrimaryEditPart) {
					Object currentElement = getSemanticElement(currentEditPart);
					if (clonedList.contains(currentElement)) {
						clonedList.remove(currentElement);
						researchedEditPart = ((IGraphicalEditPart) currentEditPart);
						partSelection.add(researchedEditPart);

					}
				}
			}

			// We may also search for a GMF View (Instead of a semantic model Element)
			if (!clonedList.isEmpty()) {
				for (Iterator<?> iterator = clonedList.iterator(); iterator.hasNext();) {
					Object element = iterator.next();
					if (graphicalViewer.getEditPartRegistry().containsKey(element) && !clonedList.isEmpty()) {
						iterator.remove();
						researchedEditPart = (IGraphicalEditPart) graphicalViewer.getEditPartRegistry().get(element);
						partSelection.add(researchedEditPart);
					}
				}
			}

			// the second test, as the model element is not a PrimaryEditPart, is to allow the selection even if the user selected it with other elements
			// and reset the selection if only the model is selected
			if (clonedList.isEmpty() || (clonedList.size() == 1 && clonedList.get(0) == getDiagram().getElement())) {
				// all parts have been found
				IStructuredSelection sSelection = new StructuredSelection(partSelection);
				// this is used instead of graphicalViewer.select(IGraphicalEditPart) as the later only allows the selection of a single element
				graphicalViewer.setSelection(sSelection);
				if (!partSelection.isEmpty()) {
					graphicalViewer.reveal(partSelection.get(0));
				}
				return true;
			}
		}

		return false;
	}



	/**
	 * @see org.eclipse.papyrus.infra.internationalization.common.editor.IInternationalizationEditor#modifyPartName(java.lang.String)
	 *
	 * @param name
	 */
	@Override
	public void modifyPartName(final String name) {
		setPartName(name);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.papyrus.infra.internationalization.common.editor.IInternationalizationEditor#refreshEditorPart()
	 */
	@Override
	public void refreshEditorPart() {
		if (null == getDiagramEditPart()) {
			return;// see bug 551530
		}

		// TODO: Implement a better refresh
		DRepresentation representation = getRepresentation();
		if (representation != null) {
			Session session = SessionManager.INSTANCE.getSession(representation.eContainer());
			// TODO, FiXME : it seems be a very bad idea to make a refresh command in the command stack...
			session.getTransactionalEditingDomain().getCommandStack().execute(new RefreshRepresentationsCommand(session.getTransactionalEditingDomain(), new NullProgressMonitor(), representation));
		}
		// old version DiagramHelper.forceRefresh(getDiagramEditPart());
	}

	/**
	 * Returns the KeyHandler with common bindings for both the Outline and
	 * Graphical Views. For example, delete is a common action.
	 *
	 * @return KeyHandler
	 */
	@Override
	protected KeyHandler getKeyHandler() {
		if (keyHandler == null) {
			keyHandler = new KeyHandler();

			ActionRegistry registry = getActionRegistry();
			IAction action;

			action = new PromptingDeleteAction(this);
			action.setText(DiagramUIMessages.DiagramEditor_Delete_from_Diagram);
			registry.registerAction(action);
			getSelectionActions().add(action.getId());

			action = new InsertAction(this);
			action.setText(""); //$NON-NLS-1$ // no text necessary since this is not a visible action
			registry.registerAction(action);
			getSelectionActions().add(action.getId());

			PromptingDeleteFromModelAction deleteModelAction = new PromptingDeleteFromModelAction(
					this);
			deleteModelAction.init();

			registry.registerAction(deleteModelAction);

			action = new DirectEditAction((IWorkbenchPart) this);
			registry.registerAction(action);
			getSelectionActions().add(action.getId());

			action = new ZoomInAction(getZoomManager());
			action.setText(""); //$NON-NLS-1$ // no text necessary since this is not a visible action
			registry.registerAction(action);
			getSelectionActions().add(action.getId());

			action = new ZoomOutAction(getZoomManager());
			action.setText(""); //$NON-NLS-1$ // no text necessary since this is not a visible action
			registry.registerAction(action);
			getSelectionActions().add(action.getId());

			action = new ToggleRouterAction(((IWorkbenchPart) this).getSite().getPage());
			((ToggleRouterAction) action).setPartSelector(new IPartSelector() {
				@Override
				public boolean selects(IWorkbenchPart part) {
					return part == this;
				}
			});
			action.setText(""); //$NON-NLS-1$ // no text necessary since this is not a visible action
			registry.registerAction(action);
			getSelectionActions().add(action.getId());

			keyHandler.put(KeyStroke.getPressed(SWT.INSERT, 0),
					getActionRegistry().getAction(InsertAction.ID));

			keyHandler.put(KeyStroke.getPressed(SWT.DEL, 100, SWT.CTRL),
					getActionRegistry().getAction("deleteFromDiagramAction"));
			keyHandler.put(KeyStroke.getPressed(SWT.BS, 8, 0),
					getActionRegistry().getAction(ActionFactory.DELETE.getId()));

			keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
					getActionRegistry().getAction(
							ActionIds.ACTION_DELETE_FROM_MODEL));
			keyHandler.put(/* CTRL + '=' */
					KeyStroke.getPressed('=', 0x3d, SWT.CTRL),
					getActionRegistry().getAction(
							GEFActionConstants.ZOOM_IN));
			keyHandler.put(/* CTRL + '-' */
					KeyStroke.getPressed('-', 0x2d, SWT.CTRL),
					getActionRegistry().getAction(
							GEFActionConstants.ZOOM_OUT));
			keyHandler.put(/* CTRL + L */
					KeyStroke.getPressed((char) 0xC, 108, SWT.CTRL),
					getActionRegistry().getAction(
							ActionIds.ACTION_TOGGLE_ROUTER));
			keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry()
					.getAction(GEFActionConstants.DIRECT_EDIT));
		}
		return keyHandler;
	}

	/**
	 * @return
	 *         the {@link ServicesRegistry} used by this editor
	 */
	public ServicesRegistry getServicesRegistry() {
		return this.servicesRegistry;
	}


}
