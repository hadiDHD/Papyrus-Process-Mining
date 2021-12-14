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
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.emf.gmf.command.INonDirtying;
import org.eclipse.papyrus.infra.emf.gmf.util.CommandUtils;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.infra.gmfdiag.common.utils.ServiceUtilsForEditPart;
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
import org.eclipse.papyrus.uml.sirius.common.diagram.Activator;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.diagram.ui.internal.refresh.GMFHelper;
import org.eclipse.swt.widgets.Shell;

/**
 * This class is used to handle hyperlink on element double click.
 */
public class NavigateHyperlinkAction {

	/**
	 *
	 * @param request
	 * @return get the command to open a new diagram
	 */
	protected Command getHyperlinkOpenCommand(GraphicalEditPart gep) {

		// in order to obtain the list of default diagram we need to fin the
		// edit part that refers to default diagram

		EObject semanticElement = gep.resolveSemanticElement();
		
		if (semanticElement instanceof DDiagramElement) {
			semanticElement=((DDiagramElement)semanticElement).getTarget();
		}

		// defaultHyperlinks
		final ArrayList<HyperLinkObject> defaultHyperLinkObject = new ArrayList<HyperLinkObject>();
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
			hyperLinkObjectList = (ArrayList<HyperLinkObject>) hyperlinkHelperFactory
					.getAllreferenced(gep.getNotationView());

			Iterator<HyperLinkObject> iterator = hyperLinkObjectList.iterator();
			while (iterator.hasNext()) {
				HyperLinkObject hyperlinkObject = iterator.next();
				if (hyperlinkObject.getIsDefault()) {

					defaultHyperLinkObject.add(hyperlinkObject);
				}

			}

			// test which kind of navigation by consulting preference
			String navigationKind = org.eclipse.papyrus.infra.gmfdiag.preferences.Activator.getDefault()
					.getPreferenceStore().getString(INavigationPreferenceConstant.PAPYRUS_NAVIGATION_DOUBLECLICK_KIND);

			// no navigation
			if (navigationKind.equals(INavigationPreferenceConstant.NO_NAVIGATION)) {
				// do nothing
				return UnexecutableCommand.INSTANCE;
			}

			// Create default hyperlinks by contributors
			if (navigationKind.equals(INavigationPreferenceConstant.EXPLICIT_IMPLICIT_NAVIGATION)) {
				// If clicked-on object is a diagram shortcut, we do not add hyperlinks by
				// contributors
				if (!(semanticElement instanceof Diagram) && defaultHyperLinkObject.size() == 0) {//semanticElement n'est pas semantic encore
					HyperlinkService hyperlinkService = ServiceUtilsForEObject.getInstance()
							.getServiceRegistry(semanticElement).getService(HyperlinkService.class);
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
								TransactionalEditingDomain editingDomain = ServiceUtilsForEditPart.getInstance()
										.getTransactionalEditingDomain(gep);
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
				EditorNavigationDialog diagramNavigationDialog = new EditorNavigationDialog(
						gep.getViewer().getControl().getShell(), defaultHyperLinkObject, semanticElement);
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
							TransactionalEditingDomain editingDomain = ServiceUtilsForEditPart.getInstance()
									.getTransactionalEditingDomain(gep);
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

			// No default hyperlinks, so we open the manager shell if the clicked-on object
			// is not a diagram shotcut
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

									EObject object = gep.getNotationView().getElement();
									if (object instanceof DDiagramElement) {
										EModelElement elem=(EModelElement) ((DDiagramElement) object).getTarget();
										HyperLinkManagerShell hyperLinkManagerShell = new HyperLinkManagerShell(
												parentShell, createEditorRegistry(gep), gep.getEditingDomain(),
												elem, gep.getNotationView(), hyperlinkHelperFactory);
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
	protected IPageIconsRegistry createEditorRegistry(EditPart editPart) {
		try {
			return ServiceUtilsForEditPart.getInstance().getService(IPageIconsRegistry.class, editPart);
		} catch (ServiceException e) {
			// Return an empty registry always providing null;
			return new PageIconsRegistry();
		}
	}

	class OpenCommand extends AbstractTransactionalCommand implements INonDirtying {
		private final HyperLinkObject hyperlinkObject;

		OpenCommand(TransactionalEditingDomain editingDomain, HyperLinkObject hyperlinkObject) {
			super(editingDomain, "Navigate hyperlink", null);

			this.hyperlinkObject = hyperlinkObject;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			hyperlinkObject.openLink();
			return CommandResult.newOKCommandResult();
		}
	}

	public void createOrOpenHyperlink(DDiagramElement obj) {
		View view = SiriusGMFHelper.getGmfView(obj);
		GraphicalEditPart gef = (GraphicalEditPart) GMFHelper.getGraphicalEditPart(view).get();

		Command command = getHyperlinkOpenCommand(gef);
		command.execute();
	}

}