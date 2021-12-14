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
package org.eclipse.papyrus.uml.sirius.common.diagram.hyperlink;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.core.sashwindows.di.service.IPageManager;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.emf.gmf.command.INonDirtying;
import org.eclipse.papyrus.infra.emf.gmf.util.CommandUtils;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.infra.gmfdiag.common.utils.ServiceUtilsForEditPart;
import org.eclipse.papyrus.infra.gmfdiag.hyperlink.Activator;
import org.eclipse.papyrus.infra.gmfdiag.navigation.preference.INavigationPreferenceConstant;
import org.eclipse.papyrus.infra.hyperlink.helper.AbstractHyperLinkHelper;
import org.eclipse.papyrus.infra.hyperlink.helper.HyperLinkHelperFactory;
import org.eclipse.papyrus.infra.hyperlink.object.HyperLinkObject;
import org.eclipse.papyrus.infra.hyperlink.service.HyperlinkService;
import org.eclipse.papyrus.infra.hyperlink.ui.EditorNavigationDialog;
import org.eclipse.papyrus.infra.hyperlink.ui.HyperLinkManagerShell;
import org.eclipse.papyrus.infra.hyperlink.util.HyperLinkHelpersRegistrationUtil;
import org.eclipse.papyrus.infra.ui.editorsfactory.IPageIconsRegistry;
import org.eclipse.papyrus.infra.ui.editorsfactory.PageIconsRegistry;
import org.eclipse.papyrus.infra.ui.util.EditorHelper;
import org.eclipse.swt.widgets.Shell;

/**
 * This class is used to open a new diagram when the double click is detected.
 * It is dependent of papyrus environment
 */
public class NavigationEditPolicy extends OpenEditPolicy {

	public static final String NAVIGATION_POLICY = "NavigationEditPolicy"; //$NON-NLS-1$

	public NavigationEditPolicy() {
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy#getOpenCommand(org.eclipse.gef.Request)
	 *
	 * @param request
	 * @return
	 */
	@Override
	protected Command getOpenCommand(Request request) {
		Command openCommand = getHyperlinkOpenCommand(request);
		if (openCommand == null || !openCommand.canExecute()) {
			openCommand = getShortCutOpenCommand(request);
		}
		return openCommand;
	}


	/**
	 *
	 * @param request
	 * @return get the command to open a new diagram
	 */
	protected Command getHyperlinkOpenCommand(Request request) {
		final IGraphicalEditPart gep;

		// in order to obtain the list of default diagram we need to fin the
		// edit part that refers to default diagram

		// if this a label of a compartment, the good editpart is the parent
		EditPart target = getHost();
		while (false == target instanceof IPrimaryEditPart && target != null) {
			target = target.getParent();
		}

		if (false == target instanceof IGraphicalEditPart) {
			return UnexecutableCommand.INSTANCE;
		}

		gep = (IGraphicalEditPart) target;
		final EObject semanticElement = gep.resolveSemanticElement();

		// defaultHyperlinks
		final ArrayList<HyperLinkObject> defaultHyperLinkObject = new ArrayList<>();
		final ArrayList<HyperLinkObject> hyperLinkObjectList;

		if (semanticElement == null) {
			return UnexecutableCommand.INSTANCE;
		}
		// initialization of code to extract hyperlinks, in the future to do with
		// extension points
		ArrayList<AbstractHyperLinkHelper> hyperLinkHelpers = new ArrayList<>();
		// hyperLinkHelpers.add(new DiagramHyperLinkHelper());
		// hyperLinkHelpers.add(new DocumentHyperLinkHelper());
		// hyperLinkHelpers.add(new WebHyperLinkHelper());
		hyperLinkHelpers.addAll(HyperLinkHelpersRegistrationUtil.INSTANCE.getAllRegisteredHyperLinkHelper());
		final HyperLinkHelperFactory hyperlinkHelperFactory = new HyperLinkHelperFactory(hyperLinkHelpers);

		try {
			// fill the list of default hyperlinks
			hyperLinkObjectList = (ArrayList<HyperLinkObject>) hyperlinkHelperFactory.getAllreferenced(gep.getNotationView());

			Iterator<HyperLinkObject> iterator = hyperLinkObjectList.iterator();
			while (iterator.hasNext()) {
				HyperLinkObject hyperlinkObject = iterator.next();
				if (hyperlinkObject.getIsDefault()) {

					defaultHyperLinkObject.add(hyperlinkObject);
				}

			}

			// test which kind of navigation by consulting preference
			String navigationKind = org.eclipse.papyrus.infra.gmfdiag.preferences.Activator.getDefault().getPreferenceStore().getString(INavigationPreferenceConstant.PAPYRUS_NAVIGATION_DOUBLECLICK_KIND);

			// no navigation
			if (navigationKind.equals(INavigationPreferenceConstant.NO_NAVIGATION)) {
				// do nothing
				return UnexecutableCommand.INSTANCE;
			}

			// Create default hyperlinks by contributors
			if (navigationKind.equals(INavigationPreferenceConstant.EXPLICIT_IMPLICIT_NAVIGATION)) {
				// If clicked-on object is a diagram shortcut, we do not add hyperlinks by contributors
				if (!(semanticElement instanceof Diagram) && defaultHyperLinkObject.size() == 0) {
					HyperlinkService hyperlinkService = ServiceUtilsForEObject.getInstance().getServiceRegistry(semanticElement).getService(HyperlinkService.class);
					defaultHyperLinkObject.addAll(hyperlinkService.getHyperlinks(semanticElement));
					for (HyperLinkObject hyperlink : defaultHyperLinkObject) {
						hyperlink.setIsDefault(true);
					}
				}
			}

			if (defaultHyperLinkObject.size() == 1) {
				// open the diagram
				final HyperLinkObject hyperlinkObject = defaultHyperLinkObject.get(0);
				class NavigateHyperlinkCommand extends Command implements INonDirtying {

					private ICommand openLinkCommand;

					NavigateHyperlinkCommand() {
						super("Navigate hyperlink");
					}

					@Override
					public void execute() {

						if (hyperlinkObject.needsOpenCommand()) {
							try {
								TransactionalEditingDomain editingDomain = ServiceUtilsForEditPart.getInstance().getTransactionalEditingDomain(getHost());
								openLinkCommand = new OpenCommand(editingDomain, hyperlinkObject);
								openLinkCommand.execute(new NullProgressMonitor(), null);
							} catch (ServiceException | ExecutionException ex) {
								Activator.log.error(ex);
							}
						} else {
							hyperlinkObject.openLink();
						}
					}

					@Override
					public void undo() {
						if (openLinkCommand != null && openLinkCommand.canUndo()) {
							try {
								openLinkCommand.undo(new NullProgressMonitor(), null);
							} catch (ExecutionException ex) {
								Activator.log.error(ex);
							}
						}
					}

					@Override
					public void redo() {
						if (openLinkCommand != null && openLinkCommand.canRedo()) {
							try {
								openLinkCommand.redo(new NullProgressMonitor(), null);
							} catch (ExecutionException ex) {
								Activator.log.error(ex);
							}
						}
					}

					@Override
					public void dispose() {
						if (openLinkCommand != null) {
							openLinkCommand.dispose();
							openLinkCommand = null;
						}

						super.dispose();
					}
				}
				;
				return new NavigateHyperlinkCommand();
			}

			if (defaultHyperLinkObject.size() > 1) {
				// open a dialog to choose a diagram
				EditorNavigationDialog diagramNavigationDialog = new EditorNavigationDialog(getHost().getViewer().getControl().getShell(), defaultHyperLinkObject, semanticElement);
				diagramNavigationDialog.open();
				final List<HyperLinkObject> hList = diagramNavigationDialog.getSelectedHyperlinks();

				class NavigateHyperlinksCommand extends Command implements INonDirtying {

					private CompositeCommand openLinksCommand;

					NavigateHyperlinksCommand() {
						super("Navigate hyperlinks");
					}

					@Override
					public void execute() {
						Iterator<HyperLinkObject> iter = hList.iterator();
						openLinksCommand = CommandUtils.nonDirtyingGMFComposite("Navigate hyperlinks");

						try {
							TransactionalEditingDomain editingDomain = ServiceUtilsForEditPart.getInstance().getTransactionalEditingDomain(getHost());
							while (iter.hasNext()) {
								final HyperLinkObject hyperlinkObject = iter.next();
								if (hyperlinkObject.needsOpenCommand()) {
									openLinksCommand.add(new OpenCommand(editingDomain, hyperlinkObject));
								} else {
									hyperlinkObject.openLink();
								}
							}

							if (openLinksCommand.isEmpty()) {
								return;
							}

							openLinksCommand.execute(new NullProgressMonitor(), null);
						} catch (ServiceException | ExecutionException ex) {
							Activator.log.error(ex);
						}
					}

					@Override
					public void undo() {
						if (openLinksCommand != null && openLinksCommand.canUndo()) {
							try {
								openLinksCommand.undo(new NullProgressMonitor(), null);
							} catch (ExecutionException ex) {
								Activator.log.error(ex);
							}
						}
					}

					@Override
					public void redo() {
						if (openLinksCommand != null && openLinksCommand.canRedo()) {
							try {
								openLinksCommand.redo(new NullProgressMonitor(), null);
							} catch (ExecutionException ex) {
								Activator.log.error(ex);
							}
						}
					}
				}
				;

				return new NavigateHyperlinksCommand();
			}

			// No default hyperlinks, so we open the manager shell if the clicked-on object is not a diagram shotcut
			if (!(semanticElement instanceof Diagram)) {
				if (defaultHyperLinkObject.size() == 0) {
					class AddHyperlinkCommand extends Command {
						private Command addLinkCommand;

						private AddHyperlinkCommand() {
							super("Add hyperlink");
						}

						@Override
						public void execute() {
							addLinkCommand = new Command("Add Hyperlink") {
								@Override
								public void execute() {
									Shell parentShell = EditorHelper.getActiveShell();
									if (((IGraphicalEditPart) getHost()).getNotationView().getElement() instanceof EModelElement) {
										HyperLinkManagerShell hyperLinkManagerShell = new HyperLinkManagerShell(parentShell, createEditorRegistry(), ((IGraphicalEditPart) getHost()).getEditingDomain(),

												(EModelElement) ((IGraphicalEditPart) getHost()).getNotationView().getElement(),
												((IGraphicalEditPart) getHost()).getNotationView(), hyperlinkHelperFactory);
										hyperLinkManagerShell.setInput(hyperLinkObjectList);
										hyperLinkManagerShell.open();
									}
								}
							};
							addLinkCommand.execute();
						}

						@Override
						public void undo() {
							if (addLinkCommand != null && addLinkCommand.canUndo()) {
								addLinkCommand.undo();
							}
						}

						@Override
						public void redo() {
							if (addLinkCommand != null && addLinkCommand.canRedo()) {
								addLinkCommand.redo();
							}
						}

						@Override
						public void dispose() {
							if (addLinkCommand != null) {
								addLinkCommand.dispose();
								addLinkCommand = null;
							}

							super.dispose();
						}
					}
					;

					return new AddHyperlinkCommand();
				}
			}
		} catch (Exception e) {
			Activator.log.error("Impossible to load hyperlinks", e);
		}
		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * Return the EditorRegistry for nested editor descriptors. Subclass should
	 * implements this method in order to return the registry associated to the
	 * extension point namespace.
	 *
	 * @return the EditorRegistry for nested editor descriptors
	 */
	protected IPageIconsRegistry createEditorRegistry() {
		try {
			return ServiceUtilsForEditPart.getInstance().getService(IPageIconsRegistry.class, getHost());
		} catch (ServiceException e) {
			// Return an empty registry always providing null;
			return new PageIconsRegistry();
		}
	}

	//
	// Nested types
	//

	class OpenCommand extends AbstractTransactionalCommand implements INonDirtying {
		private final HyperLinkObject hyperlinkObject;

		OpenCommand(TransactionalEditingDomain editingDomain, HyperLinkObject hyperlinkObject) {
			super(editingDomain, "Navigate hyperlink", null);

			this.hyperlinkObject = hyperlinkObject;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			hyperlinkObject.openLink();
			return CommandResult.newOKCommandResult();
		}
	};

	/**
	 * The Class OpenDiagramCommand.
	 */
	private static class OpenDiagramCommand extends AbstractTransactionalCommand {

		/** The diagram to open. */
		private Diagram diagramToOpen = null;

		/**
		 * Instantiates a new open diagram command.
		 *
		 * @param domain
		 *            the domain
		 * @param diagram
		 *            the diagram
		 */
		public OpenDiagramCommand(TransactionalEditingDomain domain, Diagram diagram) {
			super(domain, "open diagram", null);
			diagramToOpen = diagram;
		}

		/**
		 * {@inheritedDoc}
		 */
		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			try {
				IPageManager pageMngr = ServiceUtilsForEObject.getInstance().getService(IPageManager.class, diagramToOpen);
				if (pageMngr.isOpen(diagramToOpen)) {
					pageMngr.selectPage(diagramToOpen);
				} else {
					pageMngr.openPage(diagramToOpen);
				}
				return CommandResult.newOKCommandResult();
			} catch (Exception e) {
				throw new ExecutionException("Can't open diagram", e); //$NON-NLS-1$
			}
		}
	}


	/**
	 * Get the open command previously defined by ShortCutEditPolicy
	 *
	 * @param request
	 * @return
	 */
	protected Command getShortCutOpenCommand(Request request) {
		IGraphicalEditPart host = (IGraphicalEditPart) getHost();
		View view = host.getNotationView();
		EObject element = (view == null) ? null : view.getElement();
		if ((element instanceof Diagram) && (element.eResource() != null)) {
			OpenDiagramCommand openDiagramCommand = new OpenDiagramCommand(host.getEditingDomain(), (Diagram) element);
			return new ICommandProxy(openDiagramCommand);
		} else {
			return UnexecutableCommand.INSTANCE;
		}
	}

}
