/*****************************************************************************
 * Copyright (c) 2014, 2018, 2019 CEA, Christian W. Damus, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Christian W. Damus (CEA) - Initial API and implementation
 *   Christian W. Damus - bugs 433206, 465416, 434983, 483721, 469188, 485220, 491542, 497865, 533673, 533682, 533676, 533679, 536486
 *   Thanh Liem PHAN (ALL4TEC) thanhliem.phan@all4tec.net - Bug 521550
 *   EclipseSource - Bug 536631
 *   Vincent Lorenzo (CEA LIST) vincent.lorenzo@cea.fr - Bug 549108
 *****************************************************************************/
package org.eclipse.papyrus.sirusdiag.junit.utils.rules;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorWithFlyOutPalette;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.commands.wrappers.GEFtoEMFCommandWrapper;
import org.eclipse.papyrus.editor.PapyrusMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.sasheditor.DiModel;
import org.eclipse.papyrus.infra.core.sasheditor.editor.IComponentPage;
import org.eclipse.papyrus.infra.core.sasheditor.editor.IEditorPage;
import org.eclipse.papyrus.infra.core.sasheditor.editor.IPageVisitor;
import org.eclipse.papyrus.infra.core.sasheditor.editor.ISashWindowsContainer;
import org.eclipse.papyrus.infra.core.sashwindows.di.service.IPageManager;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.ServiceUtils;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.infra.gmfdiag.common.service.palette.AspectUnspecifiedTypeConnectionTool;
import org.eclipse.papyrus.infra.gmfdiag.common.service.palette.AspectUnspecifiedTypeCreationTool;
import org.eclipse.papyrus.infra.gmfdiag.common.service.palette.PaletteUtil;
import org.eclipse.papyrus.infra.gmfdiag.common.utils.DiagramEditPartsUtil;
import org.eclipse.papyrus.infra.nattable.common.editor.AbstractEMFNattableEditor;
import org.eclipse.papyrus.infra.nattable.common.modelresource.PapyrusNattableModel;
import org.eclipse.papyrus.infra.nattable.manager.table.INattableModelManager;
import org.eclipse.papyrus.infra.nattable.model.nattable.Table;
import org.eclipse.papyrus.infra.siriusdiag.sirius.ISiriusSessionService;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.SessionService;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.SiriusConstants;
import org.eclipse.papyrus.infra.siriusdiag.ui.modelresource.SiriusDiagramModel;
import org.eclipse.papyrus.infra.tools.util.PlatformHelper;
import org.eclipse.papyrus.infra.tools.util.TypeUtils;
import org.eclipse.papyrus.infra.ui.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.junit.matchers.CommandMatchers;
import org.eclipse.papyrus.junit.utils.CommandUtils;
import org.eclipse.papyrus.junit.utils.EditorUtils;
import org.eclipse.papyrus.junit.utils.JUnitUtils;
import org.eclipse.papyrus.junit.utils.rules.AbstractModelFixture;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.ActiveTable;
import org.eclipse.papyrus.junit.utils.rules.ProjectFixture;
import org.eclipse.papyrus.junit.utils.rules.ShowView;
import org.eclipse.papyrus.junit.utils.tests.AbstractEditorTest;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.views.modelexplorer.ModelExplorerPage;
import org.eclipse.papyrus.views.modelexplorer.ModelExplorerPageBookView;
import org.eclipse.papyrus.views.modelexplorer.ModelExplorerView;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DragAndDropTarget;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.description.Layer;
import org.eclipse.sirius.diagram.description.tool.ContainerCreationDescription;
import org.eclipse.sirius.diagram.description.tool.ContainerDropDescription;
import org.eclipse.sirius.diagram.description.tool.DirectEditLabel;
import org.eclipse.sirius.diagram.description.tool.EdgeCreationDescription;
import org.eclipse.sirius.diagram.description.tool.NodeCreationDescription;
import org.eclipse.sirius.diagram.description.tool.ReconnectEdgeDescription;
import org.eclipse.sirius.diagram.tools.api.command.DiagramCommandFactoryService;
import org.eclipse.sirius.diagram.tools.api.command.IDiagramCommandFactory;
import org.eclipse.sirius.tests.support.api.DiagramComponentizationTestSupport;
import org.eclipse.sirius.tools.api.command.ui.NoUICallback;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.description.tool.AbstractToolDescription;
import org.eclipse.sirius.viewpoint.description.tool.OperationAction;
import org.eclipse.sirius.viewpoint.description.tool.SelectionWizardDescription;
import org.eclipse.sirius.viewpoint.description.tool.ToolDescription;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.runner.Description;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;


/**
 * A fixture that presents editors on a model specified via an annotation as for {@link ProjectFixture}. The editor is closed automatically upon
 * completion of the test.
 */
public class SiriusDiagramEditorFixture extends AbstractModelFixture<TransactionalEditingDomain> {

	private static final class ShowViewDescriptor {

		private static final ShowView.Location DEFAULT_LOCATION_EDITORS = ShowView.Location.RIGHT;

		private static final ShowView.Location DEFAULT_LOCATION_VIEW = ShowView.Location.STACKED;

		private static final String DEFAULT_RELATIVE_TO = "org.eclipse.ui.editorss"; //$NON-NLS-1$

		static Iterable<ShowViewDescriptor> getDescriptors(final ShowView annotation) {
			ImmutableList.Builder<ShowViewDescriptor> result = ImmutableList.builder();

			String[] ids = annotation.value();
			for (int i = 0; i < ids.length; i++) {
				result.add(new ShowViewDescriptor(annotation, i));
			}

			return result.build();
		}

		private final ShowView.Location location;

		private final String relativeTo;

		private final String viewID;

		private ShowViewDescriptor(ShowView annotation, int index) {
			this.viewID = annotation.value()[index];

			String[] relativeTo = annotation.relativeTo();
			this.relativeTo = (relativeTo.length == 0) ? null : (relativeTo.length == 1) ? relativeTo[0] : relativeTo[index];

			ShowView.Location[] location = annotation.location();
			this.location = (location.length == 0) ? null : (location.length == 1) ? location[0] : location[index];
		}

		int location() {
			return ((location != null) ? location : (relativeTo == null) ? DEFAULT_LOCATION_EDITORS : DEFAULT_LOCATION_VIEW).toModelServiceLocation();
		}

		String relativeTo() {
			return (relativeTo != null) ? relativeTo : DEFAULT_RELATIVE_TO;
		}

		String viewID() {
			return viewID;
		}
	}

	private static boolean DUMP_COMMANDS = Boolean.getBoolean("dump.commands"); //$NON-NLS-1$

	/**
	 * Create a point location (useful as a static import for test readability).
	 *
	 * @param x
	 *            the x coördinate
	 * @param y
	 *            the y coördinate
	 * @return the point
	 *
	 * @since 2.2
	 */
	public static Point at(int x, int y) {
		return new Point(x, y);
	}

	/**
	 * Convert a point that is relative to the given part to a point relative to the
	 * current viewport (taking zoom and scroll into account). This can be used to get
	 * a "Mouse Location" to configure requests. Useful as a static import for test
	 * readability
	 *
	 * @param x
	 *            the relative x coördinate
	 * @param y
	 *            the relative y coördinate
	 * @param relativeTo
	 *            the edit-part in which coördinate space the {@code x} and {@code y}
	 *            are specified
	 * @return the point in absolute mouse-pointer coördinates
	 *
	 * @since 2.2
	 */
	public static Point at(int x, int y, IGraphicalEditPart relativeTo) {
		Point at = new Point(x, y);

		IFigure figure = relativeTo.getContentPane();
		Point layoutOrigin = figure.getClientArea().getLocation();

		at.performTranslate(layoutOrigin.x, layoutOrigin.y);
		figure.translateToParent(at);
		figure.translateToAbsolute(at);

		return at;
	}

	/**
	 * Create a size dimension (useful as a static import for test readability).
	 *
	 * @param width
	 *            the size width
	 * @param height
	 *            the the size height
	 * @return the size
	 *
	 * @since 2.2
	 */
	public static Dimension sized(int width, int height) {
		return new Dimension(width, height);
	}

	/**
	 * Obtain a fake supplier that just fails the test with the given {@code message}
	 * instead of supplying a result.
	 *
	 * @param message
	 *            the failure message
	 * @return the fake supplier
	 */
	private static <T> Supplier<T> failOnAbsence(String message) {
		return () -> {
			fail(message);
			return null;
		};
	}

	/**
	 * Obtain a typed stream over a raw-typed collection from a legacy pre-generics API
	 * such as GEF.
	 *
	 * @param rawCollection
	 *            a raw-typed collection
	 * @param type
	 *            the expected element type of the collection
	 * @return the typed stream
	 */
	private static <T> Stream<T> stream(@SuppressWarnings("rawtypes") Collection rawCollection, Class<T> type) {
		return ((Collection<?>) rawCollection).stream()
				.filter(type::isInstance).map(type::cast);
	}

	private DiagramEditorWithFlyOutPalette activeDiagramEditor;

	private AbstractEMFNattableEditor activeTableEditor;

	private IDiagramCommandFactory commandFactory;

	private IMultiDiagramEditor editor;

	private final Collection<IEditorPart> editorsToClose = Lists.newArrayList();

	private final boolean ensureOperationHistoryIntegrity;

	private final List<String> excludedTypeView = Arrays.asList(new String[] { "Note" });
	@SuppressWarnings("restriction")
	private org.eclipse.papyrus.infra.ui.internal.preferences.YesNo initialEditorLayoutStorageMigrationPreference;

	private ModelExplorerView modelExplorer;

	private ListMultimap<Description, IFile> modelFiles;

	private IOperationHistory operationHistory;

	private IOperationHistoryListener operationHistoryIntegrityListener;


	/// XXXX BEGIN SIRIUS DECLARATIONS +> form org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase
	/// => Remove unused papyrus stuff later

	private static final String TOOL_NAME_INCORRECT = "The tool name is not correct";
	private static final String LAYER_NAME_INCORRECT = "The layer name is not correct (not found in the diagram description of this diagram)";

	// /** the service registry */
	// protected ServicesRegistry servicesRegistry;

	private Session session;

	private Class<?> testClass;

	private Description testDescription;

	private Collection<IViewPart> viewsToClose;

	/**
	 * Initializes me with the assurance of undo/redo correspondence in the
	 * diagram and EMF views of the command history.
	 */
	public SiriusDiagramEditorFixture() {
		this(true);
	}

	/**
	 * Initializes me with the option to ensure integrity of the operation history
	 * by listening for command execution in the diagram context and, if an operation
	 * executed in the diagram does not have the editing-domain context, adds that
	 * context. This ensures that the diagram editor and the model explorer, for
	 * example, see the same undo/redo history (which they would not if some
	 * diagram-only commands were only in the diagram's history). Some tests do
	 * need to suppress this convenience in order to accurately represent the
	 * normal Papyrus run-time environment.
	 *
	 * @param ensureOperationHistoryIntegrity
	 *
	 * @since 2.0
	 */
	public SiriusDiagramEditorFixture(boolean ensureOperationHistoryIntegrity) {
		this(ensureOperationHistoryIntegrity, Collections.emptyList());
	}

	/**
	 *
	 * Constructor.
	 *
	 * @param ensureOperationHistoryIntegrity
	 *
	 * @param additionalFileExtension
	 *            a list of additional file extension to manage
	 * @since 2.3
	 */
	public SiriusDiagramEditorFixture(boolean ensureOperationHistoryIntegrity, final List<String> additionalFileExtension) {
		super(Lists.asList(SiriusConstants.SIRIUS_DIAGRAM_MODEL_FILE_EXTENSION, additionalFileExtension.toArray(new String[additionalFileExtension.size()])));
		this.ensureOperationHistoryIntegrity = ensureOperationHistoryIntegrity;
	}

	/**
	 *
	 * Constructor.
	 *
	 * @param ensureOperationHistoryIntegrity
	 *
	 * @param additionalFileExtension
	 *            a list of additional file extension to manage
	 * @since 2.3
	 */
	public SiriusDiagramEditorFixture(final List<String> additionalFileExtension) {
		super(additionalFileExtension);
		this.ensureOperationHistoryIntegrity = true;
	}

	public void activate() {
		if (editor != null) {
			activate(editor);
		}
	}

	public void activate(IWorkbenchPart part) {
		IWorkbenchPage page = part.getSite().getPage();

		if (page.getActivePart() != part) {
			page.activate(part);
			flushDisplayEvents();
		}
	}

	public SiriusDiagramEditorFixture activateDiagram(DiagramEditPart diagram) {
		return activateDiagram(editor, diagram);
	}

	/**
	 * @since 2.0
	 */
	public SiriusDiagramEditorFixture activateDiagram(IMultiDiagramEditor editor, final DiagramEditPart diagram) {
		activate(editor);

		final ISashWindowsContainer sashContainer = PlatformHelper.getAdapter(editor, ISashWindowsContainer.class);
		final org.eclipse.papyrus.infra.core.sasheditor.editor.IPage[] select = { null };

		sashContainer.visit(new IPageVisitor() {

			@Override
			public void accept(IComponentPage page) {
				// Pass
			}

			@Override
			public void accept(IEditorPage page) {
				DiagramEditorWithFlyOutPalette nested = TypeUtils.as(page.getIEditorPart(), DiagramEditorWithFlyOutPalette.class);
				if ((nested != null) && (nested.getDiagramEditPart() == diagram)) {
					select[0] = page;
					setActiveDiagramEditor(nested);
				}
			}
		});

		if (select[0] != null) {
			sashContainer.selectPage(select[0]);
			flushDisplayEvents();
		}

		return this;
	}

	/**
	 * @since 2.0
	 */
	public SiriusDiagramEditorFixture activateDiagram(IMultiDiagramEditor editor, final String name) {
		activate(editor);

		final ISashWindowsContainer sashContainer = PlatformHelper.getAdapter(editor, ISashWindowsContainer.class);
		final org.eclipse.papyrus.infra.core.sasheditor.editor.IPage[] select = { null };

		sashContainer.visit(new IPageVisitor() {

			@Override
			public void accept(IComponentPage page) {
				// Pass
			}

			@Override
			public void accept(IEditorPage page) {
				if (name.equals(page.getPageTitle()) && (page.getIEditorPart() instanceof DiagramEditorWithFlyOutPalette)) {
					select[0] = page;
					setActiveDiagramEditor((DiagramEditorWithFlyOutPalette) page.getIEditorPart());
				}
			}
		});

		if (select[0] != null) {
			sashContainer.selectPage(select[0]);
			flushDisplayEvents();
		}

		return this;
	}

	public SiriusDiagramEditorFixture activateDiagram(String name) {
		return activateDiagram(editor, name);
	}

	/**
	 * @since 2.0
	 */
	public SiriusDiagramEditorFixture activateTable(IMultiDiagramEditor editor, final String name) {
		activate(editor);

		final ISashWindowsContainer sashContainer = PlatformHelper.getAdapter(editor, ISashWindowsContainer.class);
		final org.eclipse.papyrus.infra.core.sasheditor.editor.IPage[] select = { null };

		sashContainer.visit(new IPageVisitor() {

			@Override
			public void accept(IComponentPage page) {
				// Pass
			}

			@Override
			public void accept(IEditorPage page) {
				if (name.equals(page.getPageTitle()) && (page.getIEditorPart() instanceof AbstractEMFNattableEditor)) {
					select[0] = page;
					setActiveTableEditor((AbstractEMFNattableEditor) page.getIEditorPart());
				}
			}
		});

		if (select[0] != null) {
			sashContainer.selectPage(select[0]);
			flushDisplayEvents();
		}

		return this;
	}

	public SiriusDiagramEditorFixture activateTable(String name) {
		return activateTable(editor, name);
	}



	public void close() {
		if (editor != null) {
			close(editor);
			editor = null;
		}
	}

	public void close(IEditorPart editor) {
		if (null != editor.getSite() && null != editor.getSite().getPage()) {
			editor.getSite().getPage().closeEditor(editor, false);
			flushDisplayEvents();
		}
	}

	public String closeDiagram() {
		String result = getActiveDiagramEditor().getDiagram().getName();
		closeDiagram(editor, result);
		return result;
	}

	/**
	 * @since 2.0
	 */
	public SiriusDiagramEditorFixture closeDiagram(IMultiDiagramEditor editor, final String name) {
		try {
			ModelSet modelSet = ServiceUtils.getInstance().getModelSet(editor.getServicesRegistry());
			NotationModel representation = (NotationModel) modelSet.getModel(NotationModel.MODEL_ID);
			Diagram diagram = representation.getDiagram(name);

			// If the diagram was deleted, then so too was its page
			if (diagram != null) {
				ServiceUtils.getInstance().getService(IPageManager.class, editor.getServicesRegistry()).closePage(diagram);
				flushDisplayEvents();
			}
		} catch (Exception e) {
			throw new IllegalStateException("Cannot close diagram", e);
		}

		return this;
	}

	public SiriusDiagramEditorFixture closeDiagram(String name) {
		return closeDiagram(editor, name);
	}

	/**
	 * Create a new shape in the {@code parent}. Fails if the shape cannot be created or
	 * cannot be found in the diagram after creation.
	 *
	 * @param parent
	 *            the parent edit-part in which to create a shape
	 * @param type
	 *            the type of shape to create
	 * @param location
	 *            the location (mouse pointer) at which to create the shape
	 * @param size
	 *            the size of the shape to create, or {@code null} for the default size as
	 *            would be created when just clicking in the diagram
	 * @return the newly created shape edit-part
	 *
	 * @since 2.2
	 */
	public IGraphicalEditPart createShape(EditPart parent, IElementType type, Point location, Dimension size) {
		CreateViewRequest request = CreateViewRequestFactory.getCreateShapeRequest(type, ((IGraphicalEditPart) parent).getDiagramPreferencesHint());

		request.setLocation(location);
		request.setSize(size);

		// Some edit-parts in some diagrams depend on the creation tool setting this
		CreateElementRequest semanticRequest = null;
		if (!request.getViewDescriptors().isEmpty()) {
			semanticRequest = (CreateElementRequest) ((CreateElementRequestAdapter) request.getViewDescriptors().get(0).getElementAdapter()).getAdapter(CreateElementRequest.class);
			if (semanticRequest != null) {
				semanticRequest.setParameter(AspectUnspecifiedTypeCreationTool.INITIAL_MOUSE_LOCATION_FOR_CREATION, location.getCopy());
			}
		}

		EditPart target = parent.getTargetEditPart(request);
		assertThat("No target edit part", target, notNullValue());
		org.eclipse.gef.commands.Command command = target.getCommand(request);
		execute(command);

		return getNewEditPart(parent, request.getViewDescriptors());
	}

	/**
	 * Create a new shape in the current diagram by automating the creation tool.
	 * Fails if the shape cannot be created or cannot be found in the diagram after creation.
	 *
	 * @param type
	 *            the type of shape to create
	 * @param location
	 *            the location (mouse pointer) at which to create the shape
	 * @param size
	 *            the size of the shape to create, or {@code null} for the default size as
	 *            would be created when just clicking in the diagram
	 * @return the newly created shape edit-part
	 *
	 * @since 2.2
	 */
	public IGraphicalEditPart createShape(IElementType type, Point location, Dimension size) {
		class MyTool extends AspectUnspecifiedTypeCreationTool {
			private Collection<? extends ViewDescriptor> results = Collections.emptyList();

			MyTool() {
				super(Collections.singletonList(type));
			}

			@Override
			protected Request getTargetRequest() {
				return super.getTargetRequest();
			}

			@Override
			protected void selectAddedObject(EditPartViewer viewer, @SuppressWarnings("rawtypes") Collection objects) {
				super.selectAddedObject(viewer, objects);

				results = ((Collection<?>) objects).stream()
						.filter(ViewDescriptor.class::isInstance).map(ViewDescriptor.class::cast)
						.collect(Collectors.toList());
			}

			Collection<? extends ViewDescriptor> getResults() {
				return results;
			}
		}

		EditPartViewer viewer = getActiveDiagram().getViewer();
		MyTool tool = new MyTool();

		Event mouse = new Event();
		mouse.display = editor.getSite().getShell().getDisplay();
		mouse.widget = viewer.getControl();
		mouse.x = location.x();
		mouse.y = location.y();

		viewer.getEditDomain().setActiveTool(tool);
		tool.setViewer(viewer);

		// Move to the place where we'll click
		mouse.type = SWT.MouseMove;
		tool.mouseMove(new MouseEvent(mouse), viewer);

		// Press down
		mouse.type = SWT.MouseDown;
		mouse.button = 1;
		tool.mouseDown(new MouseEvent(mouse), viewer);

		flushDisplayEvents();

		if (size == null) {
			// Just a click
			mouse.type = SWT.MouseUp;
			tool.mouseUp(new MouseEvent(mouse), viewer);
		} else {
			// Drag and release
			mouse.type = SWT.MouseMove;
			mouse.x = location.x() + size.width();
			mouse.y = location.y() + size.height();
			tool.mouseDrag(new MouseEvent(mouse), viewer);

			flushDisplayEvents();

			mouse.type = SWT.MouseUp;
			tool.mouseUp(new MouseEvent(mouse), viewer);
		}

		flushDisplayEvents();

		// Find the new edit-part
		return getNewEditPart(getActiveDiagram(), tool.getResults());
	}

	/**
	 * Delete one or more edit-parts from the diagram.
	 *
	 * @param editPart
	 *            the edit-parts to delete
	 *
	 * @since 2.2
	 *
	 * @throws IllegalArgumentException
	 *             if no edit-parts are specified
	 */
	public void delete(EditPart... editPart) {
		// In the diagram, deletion of a multiple selection is accomplished with separate
		// requests to each edit-part, so do the same here
		org.eclipse.gef.commands.Command delete = Stream.of(editPart).map(ep -> ep.getCommand(new EditCommandRequestWrapper(new DestroyElementRequest(false))))
				.reduce(org.eclipse.gef.commands.Command::chain)
				.orElseThrow(IllegalArgumentException::new);

		execute(delete);
	}

	public void deselect(EditPart editPart) {
		editPart.getViewer().getSelectionManager().deselect(editPart);
	}

	/**
	 * @since 2.0
	 */
	public void ensurePapyrusPerspective() {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				final String papyrus = "org.eclipse.papyrus.infra.core.perspective"; //$NON-NLS-1$
				final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IPerspectiveDescriptor perspective = activePage.getPerspective();
				if (!papyrus.equals(perspective.getId())) {
					perspective = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(papyrus);
					if (perspective != null) {
						activePage.setPerspective(perspective);
						flushDisplayEvents();
					}
				}
			}
		});
	}

	@Override
	public void execute(Command command) {
		super.execute(command);
		flushDisplayEvents();
	}

	@Override
	public IStatus execute(IUndoableOperation operation, IProgressMonitor monitor, IAdaptable info) {
		IStatus result = super.execute(operation, monitor, info);
		flushDisplayEvents();
		return result;
	}

	public void execute(org.eclipse.gef.commands.Command command) {
		assertThat("No command", command, notNullValue());
		assertThat(command, CommandMatchers.GEF.canExecute());
		getActiveDiagramEditor().getDiagramEditDomain().getDiagramCommandStack().execute(command);
		flushDisplayEvents();
	}

	/**
	 * Find an edit-part for a model element in a particular {@code scope}.
	 *
	 * @param scope
	 *            an edit part in which to search (its children) for an edit-part
	 * @param modelElement
	 *            the model element visualized by the edit-part to search for
	 *
	 * @return the matching edit-part, or {@code null} if none is found in the {@code scope}
	 */
	public EditPart findEditPart(EditPart scope, EObject modelElement) {
		EditPart result = null;

		View view = PlatformHelper.getAdapter(scope, View.class);
		if ((view != null) && (view.getElement() == modelElement)) {
			result = scope;
		}

		if (result == null) {
			// Search children
			for (Iterator<?> iter = scope.getChildren().iterator(); (result == null) && iter.hasNext();) {
				result = findEditPart((EditPart) iter.next(), modelElement);
			}
		}

		if ((result == null) && (scope instanceof GraphicalEditPart)) {
			// Search edges
			for (Iterator<?> iter = ((GraphicalEditPart) scope).getSourceConnections().iterator(); (result == null) && iter.hasNext();) {
				result = findEditPart((EditPart) iter.next(), modelElement);
			}
			if (result == null) {
				for (Iterator<?> iter = ((GraphicalEditPart) scope).getTargetConnections().iterator(); (result == null) && iter.hasNext();) {
					result = findEditPart((EditPart) iter.next(), modelElement);
				}
			}
		}

		return result;
	}

	public EditPart findEditPart(EObject modelElement) {
		return findEditPart(getActiveDiagramEditor(), modelElement);
	}

	public EditPart findEditPart(IDiagramWorkbenchPart editor, EObject modelElement) {
		DiagramEditPart diagram = editor.getDiagramEditPart();
		return findEditPart(diagram, modelElement);
	}

	public EditPart findEditPart(IDiagramWorkbenchPart editor, String name, Class<? extends NamedElement> type) {
		EditPart result = null;

		for (Iterator<View> views = Iterators.filter(editor.getDiagram().eAllContents(), View.class); views.hasNext();) {
			View next = views.next();
			EObject element = next.getElement();
			if (type.isInstance(element) && name.equals(type.cast(element).getName())) {
				result = (EditPart) editor.getDiagramGraphicalViewer().getEditPartRegistry().get(next);
				break;
			}
		}

		return result;
	}

	/**
	 * @since 2.0
	 */
	public EditPart findEditPart(IMultiDiagramEditor editor, EObject modelElement) {
		IEditorPart activeEditor = editor.getActiveEditor();
		assertThat("No diagram active", activeEditor, instanceOf(DiagramEditor.class));
		return findEditPart((DiagramEditor) activeEditor, modelElement);
	}

	/**
	 * @since 2.0
	 */
	public EditPart findEditPart(IMultiDiagramEditor editor, String name, Class<? extends NamedElement> type) {
		IEditorPart activeEditor = editor.getActiveEditor();
		assertThat("No diagram active", activeEditor, instanceOf(DiagramEditor.class));
		return findEditPart((DiagramEditor) activeEditor, name, type);
	}

	public EditPart findEditPart(String name, Class<? extends NamedElement> type) {
		return findEditPart(getActiveDiagramEditor(), name, type);
	}

	/**
	 * Find orphan edit part.
	 *
	 * @return the edits the part
	 */
	public EditPart findOrphanEditPart() {
		IDiagramWorkbenchPart activeEditor = (IDiagramWorkbenchPart) editor.getActiveEditor();
		EditPart result = null;
		for (Iterator<View> views = Iterators.filter(activeEditor.getDiagram().eAllContents(), View.class); views.hasNext();) {
			View next = views.next();

			String type = next.getType();
			EObject element = next.getElement();

			if (element == null && !excludedTypeView.contains(type)) {
				result = (EditPart) activeEditor.getDiagramGraphicalViewer().getEditPartRegistry().get(next);
				break;
			}
		}

		return result;

	}

	/**
	 * Find orphan edit part with a type.
	 *
	 * @param type
	 *            the type
	 * @return the edits the part
	 */
	public EditPart findOrphanEditPart(String type) {
		IDiagramWorkbenchPart activeEditor = (IDiagramWorkbenchPart) editor.getActiveEditor();
		EditPart result = null;
		for (Iterator<View> views = Iterators.filter(activeEditor.getDiagram().eAllContents(), View.class); views.hasNext();) {
			View next = views.next();
			EObject element = next.getElement();
			if (element == null && type.equals(next.getType())) {
				result = (EditPart) activeEditor.getDiagramGraphicalViewer().getEditPartRegistry().get(next);
				break;
			}
		}

		return result;

	}

	public void flushDisplayEvents() {
		for (;;) {
			try {
				if (Display.getCurrent() != null && !Display.getCurrent().readAndDispatch()) {
					break;
				}
			} catch (Exception e) {
				Bundle testBundle = FrameworkUtil.getBundle((testClass == null) ? SiriusDiagramEditorFixture.class : testClass);
				Platform.getLog(testBundle).log(new Status(IStatus.ERROR, testBundle.getSymbolicName(), "Uncaught exception in display runnable.", e));
			}
		}
	}

	public DiagramEditPart getActiveDiagram() {
		return getActiveDiagramEditor().getDiagramEditPart();
	}



	public DiagramEditorWithFlyOutPalette getActiveDiagramEditor() {
		DiagramEditorWithFlyOutPalette result = activeDiagramEditor;

		if (result == null) {
			IEditorPart activeEditor = getWorkbenchPage().getActiveEditor();
			if (activeEditor instanceof IMultiDiagramEditor) {
				activeEditor = ((IMultiDiagramEditor) activeEditor).getActiveEditor();
				if (activeEditor instanceof DiagramEditorWithFlyOutPalette) {
					result = (DiagramEditorWithFlyOutPalette) activeEditor;
					setActiveDiagramEditor(result);
				}
			}
		}

		assertThat("No diagram active", result, notNullValue());

		return result;
	}


	public AbstractEMFNattableEditor getActiveTableEditor() {
		AbstractEMFNattableEditor result = activeTableEditor;

		if (result == null) {
			IEditorPart activeEditor = getWorkbenchPage().getActiveEditor();
			if (activeEditor instanceof IMultiDiagramEditor) {
				activeEditor = ((IMultiDiagramEditor) activeEditor).getActiveEditor();
				if (activeEditor instanceof AbstractEMFNattableEditor) {
					result = (AbstractEMFNattableEditor) activeEditor;
					setActiveTableEditor(result);
				}
			}
		}

		assertThat("No table active", result, notNullValue());

		return result;
	}

	public INattableModelManager getActiveTableManager() {
		return (INattableModelManager) getActiveTableEditor().getAdapter(INattableModelManager.class);
	}

	/**
	 * <p>
	 * Return the Papyrus {@link CreateRequest} associated with the given palette tool.
	 * </p>
	 * <p>
	 * Note that this method is designed to work exclusively with Papyrus' AspectUnspecified tools and requests
	 * </p>
	 *
	 * @param tool
	 * @return
	 * @throws Exception
	 *
	 * @see AspectUnspecifiedTypeCreationTool
	 * @see AspectUnspecifiedTypeConnectionTool
	 * @see AspectUnspecifiedTypeConnectionTool.CreateAspectUnspecifiedTypeConnectionRequest
	 * @see AspectUnspecifiedTypeCreationTool.CreateAspectUnspecifiedTypeRequest
	 */
	public Request getAspectUnspecifiedCreateRequest(final Tool tool) throws Exception {
		final IDiagramGraphicalViewer viewer = getActiveDiagramEditor().getDiagramGraphicalViewer();

		AtomicReference<Exception> exception = new AtomicReference<>();
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					tool.setViewer(viewer);
				} catch (Exception ex) {
					exception.set(ex);
				}
			}
		});
		if (exception.get() != null) {
			throw exception.get();
		}

		if (tool instanceof AspectUnspecifiedTypeCreationTool) {
			AspectUnspecifiedTypeCreationTool creationTool = (AspectUnspecifiedTypeCreationTool) tool;
			return creationTool.createCreateRequest();
		} else if (tool instanceof AspectUnspecifiedTypeConnectionTool) {
			AspectUnspecifiedTypeConnectionTool connectionTool = (AspectUnspecifiedTypeConnectionTool) tool;
			return connectionTool.new CreateAspectUnspecifiedTypeConnectionRequest(connectionTool.getElementTypes(), false, getPreferencesHint());
		}

		throw new Exception("Unexpected kind of creation tool.");
	}

	/**
	 * @since 2.0
	 */
	public DiagramEditPart getDiagram(IMultiDiagramEditor editor, final String name) {
		final ISashWindowsContainer sashContainer = PlatformHelper.getAdapter(editor, ISashWindowsContainer.class);
		final org.eclipse.papyrus.infra.core.sasheditor.editor.IPage[] matchedPage = { null };

		sashContainer.visit(new IPageVisitor() {

			@Override
			public void accept(IComponentPage page) {
				// Pass
			}

			@Override
			public void accept(IEditorPage page) {
				if (name.equals(page.getPageTitle()) && (page.getIEditorPart() instanceof DiagramEditorWithFlyOutPalette)) {
					matchedPage[0] = page;
				}
			}
		});

		IEditorPage editorPage = TypeUtils.as(matchedPage[0], IEditorPage.class);
		IDiagramWorkbenchPart diagramPart = (editorPage == null) ? null : TypeUtils.as(editorPage.getIEditorPart(), IDiagramWorkbenchPart.class);

		// The lazy initialization, used in the patch for bug 519107, does not initialize the diagram edit part of UmlGmfDiagramEditor
		// So we call the setFocus method here to initialize it manually (see patch for bug 521353)
		if (null != diagramPart) {
			if (null == diagramPart.getDiagramEditPart()) {
				diagramPart.setFocus();
			}
			return diagramPart.getDiagramEditPart();
		}
		return null;
	}

	public DiagramEditPart getDiagram(String name) {
		return getDiagram(editor, name);
	}

	@Override
	public TransactionalEditingDomain getEditingDomain() {
		TransactionalEditingDomain result = null;

		if (editor != null) {
			result = getEditingDomain(editor);
		}

		return result;
	}

	/**
	 * @since 2.0
	 */
	public TransactionalEditingDomain getEditingDomain(IMultiDiagramEditor editor) {
		TransactionalEditingDomain result = null;

		try {
			result = getServiceRegistry(editor).getService(TransactionalEditingDomain.class);
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Failed to get editing domain from Papyrus editor: " + e.getLocalizedMessage());
		}

		return result;
	}

	/**
	 * @since 2.0
	 */
	public IMultiDiagramEditor getEditor() {
		return editor;
	}

	/**
	 * @since 2.0
	 */
	public IMultiDiagramEditor getEditor(String path) {
		IMultiDiagramEditor result = null;

		final String fileName = new Path(path).lastSegment();
		for (IEditorReference next : getWorkbenchPage().getEditorReferences()) {
			if (PapyrusMultiDiagramEditor.EDITOR_ID.equals(next.getId()) && fileName.equals(next.getName())) {
				result = (IMultiDiagramEditor) next.getEditor(true);
			}
		}

		return result;
	}

	// public DiagramEditPart getActiveSiriusDiagram() {
	// return getActiveDiagramEditor().getDiagramEditPart();
	// }

	public String getLabel(EObject object) {
		String result = null;

		try {
			EditingDomain domain = ServiceUtils.getInstance().getTransactionalEditingDomain(editor.getServicesRegistry());
			if (domain instanceof AdapterFactoryEditingDomain) {
				IItemLabelProvider labels = (IItemLabelProvider) ((AdapterFactoryEditingDomain) domain).getAdapterFactory().adapt(object, IItemLabelProvider.class);
				if (labels != null) {
					result = labels.getText(object);
				}
			}
		} catch (ServiceException e) {
			// Doesn't matter
		}

		if (result == null) {
			result = String.valueOf(object);
		}

		return result;
	}

	@Override
	public Package getModel() {
		return getModel(editor);
	}

	/**
	 * @since 2.0
	 */
	public Package getModel(IMultiDiagramEditor editor) {
		Package result = null;

		ModelSet modelSet = getModelSet(editor);
		UmlModel uml = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
		assertThat("No UML model present in resource set", uml.getResource(), notNullValue());

		result = (Package) EcoreUtil.getObjectByType(uml.getResource().getContents(), UMLPackage.Literals.PACKAGE);
		assertThat("Model resource contains no UML Package", result, notNullValue());

		return result;
	}

	public ModelExplorerView getModelExplorerView() {

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				ModelExplorerPageBookView view;
				try {
					view = (ModelExplorerPageBookView) getWorkbenchPage().showView(AbstractEditorTest.MODELEXPLORER_VIEW_ID);
				} catch (PartInitException e) {
					e.printStackTrace();
					return;
				}

				IPage currentPage = view.getCurrentPage();
				ModelExplorerPage page = (ModelExplorerPage) currentPage;
				IViewPart viewer = page.getViewer();
				modelExplorer = (ModelExplorerView) viewer;
			}
		});

		return modelExplorer;
	}

	public ModelSet getModelSet() {
		return getModelSet(editor);
	}

	/**
	 * @since 2.0
	 */
	public ModelSet getModelSet(IMultiDiagramEditor editor) {
		try {
			return getServiceRegistry(editor).getService(ModelSet.class);
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Failed to get model set from Papyrus editor: " + e.getLocalizedMessage());
			return null; // Unreachable
		}
	}

	/**
	 * @since 2.0
	 */
	public IPageManager getPageManager() {
		return getPageManager(editor);
	}

	/**
	 * @since 2.0
	 */
	public IPageManager getPageManager(IMultiDiagramEditor editor) {
		try {
			return getServiceRegistry(editor).getService(IPageManager.class);
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Failed to get page manager from Papyrus editor: " + e.getLocalizedMessage());
			return null; // Unreachable
		}
	}

	public PaletteViewer getPalette() {
		return getPalette(getActiveDiagramEditor());
	}

	public PaletteViewer getPalette(IDiagramWorkbenchPart editor) {
		return editor.getDiagramEditPart().getViewer().getEditDomain().getPaletteViewer();
	}

	/**
	 * @since 2.0
	 */
	public PaletteViewer getPalette(IMultiDiagramEditor editor) {
		IEditorPart activeEditor = editor.getActiveEditor();
		assertThat("No diagram active", activeEditor, instanceOf(DiagramEditor.class));
		return getPalette((DiagramEditor) activeEditor);
	}

	/**
	 * <p>
	 * Return the Palette Tool with the given toolId from the current active diagram editor
	 * </p>
	 *
	 * @param paletteToolId
	 * @return
	 */
	public Tool getPaletteTool(String paletteToolId) {
		PaletteRoot paletteRoot = getActiveDiagramEditor().getDiagramGraphicalViewer().getEditDomain().getPaletteViewer().getPaletteRoot();
		List<ToolEntry> allToolEntries = PaletteUtil.getAllToolEntries(paletteRoot);
		return allToolEntries.stream().filter(entry -> entry.getId().equals(paletteToolId)).findAny().map(ToolEntry::createTool).orElse(null);
	}

	public List<IWorkbenchPart> getPartStack(IWorkbenchPart part) {
		List<IWorkbenchPart> result;

		MPart mpart = part.getSite().getService(MPart.class);
		EModelService modelService = mpart.getContext().get(EModelService.class);
		MPartStack stack = (MPartStack) modelService.getContainer(mpart);

		result = Lists.newArrayListWithCapacity(stack.getChildren().size());
		for (MPart next : Iterables.filter(stack.getChildren(), MPart.class)) {
			IWorkbenchPart wbPart = next.getContext().get(IWorkbenchPart.class);
			if (wbPart != null) {
				result.add(wbPart);
			}
		}

		return result;
	}

	public PreferencesHint getPreferencesHint() {
		PreferencesHint result = PreferencesHint.USE_DEFAULTS;

		if (activeDiagramEditor != null) {
			RootEditPart rootEditPart = activeDiagramEditor.getDiagramGraphicalViewer().getRootEditPart();
			if (rootEditPart instanceof IDiagramPreferenceSupport) {
				result = ((IDiagramPreferenceSupport) rootEditPart).getPreferencesHint();
			}
		}

		return result;
	}

	public ServicesRegistry getServiceRegistry() {
		return getServiceRegistry(editor);
	}

	/**
	 * @since 2.0
	 */
	public ServicesRegistry getServiceRegistry(IMultiDiagramEditor editor) {
		return editor.getServicesRegistry();
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Get the shape compartment of an edit-part. Fails if the edit-part has no
	 * shape compartment.
	 *
	 * @param shapeEditPart
	 *            a shape edit part
	 * @return its shape compartment
	 *
	 * @since 2.2
	 */
	public EditPart getShapeCompartment(EditPart shapeEditPart) {
		return stream(shapeEditPart.getChildren(), EditPart.class)
				.filter(ShapeCompartmentEditPart.class::isInstance)
				.findFirst().orElseGet(failOnAbsence("No shape compartment"));
	}

	public IViewPart getView(String id, boolean open) {
		IViewPart result = null;

		IWorkbenchPage wbPage = getWorkbenchPage();

		try {
			result = wbPage.findView(id);
			if ((result == null) && open) {
				result = wbPage.showView(id);
			}

			if (result != null) {
				result.getSite().getPage().activate(result);
				flushDisplayEvents();
			}
		} catch (PartInitException e) {
			e.printStackTrace();
			fail("Failed to show a view: " + id);
		} finally {
			flushDisplayEvents();
		}

		return result;
	}

	public void move(GraphicalEditPart editPart, Point newLocation) {
		ChangeBoundsRequest move = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
		IFigure figure = editPart.getFigure();
		Rectangle bounds = figure.getBounds();
		move.setEditParts(editPart);
		move.setConstrainedMove(false);
		move.setMoveDelta(at(newLocation.x() - bounds.x(), newLocation.y() - bounds.y()));
		execute(editPart.getCommand(move));
	}

	/**
	 * @since 2.0
	 */
	public IMultiDiagramEditor open() {
		return openOne(testDescription);
	}

	/**
	 * @since 2.0
	 */
	public IMultiDiagramEditor open(String resourcePath) {
		return open(new Path(resourcePath).removeFileExtension().lastSegment(), ResourceKind.BUNDLE, resourcePath);
	}

	/**
	 * @since 2.0
	 */
	public IMultiDiagramEditor open(String targetPath, ResourceKind resourceKind, String resourcePath) {
		final IFile papyrusModel = getProject().getFile(initModelResource(targetPath, resourceKind, resourcePath).getURI().trimFileExtension().appendFileExtension(DiModel.DI_FILE_EXTENSION));
		return open(papyrusModel);
	}

	/**
	 * @since 2.0
	 */
	public IMultiDiagramEditor open(String targetPath, String resourcePath) {
		return open(targetPath, ResourceKind.BUNDLE, resourcePath);
	}

	/**
	 * @since 2.0
	 */
	public SiriusDiagramEditorFixture openDiagram(IMultiDiagramEditor editor, final String name) {
		activate(editor);

		try {
			ModelSet modelSet = ServiceUtils.getInstance().getModelSet(editor.getServicesRegistry());
			SiriusDiagramModel representation = (SiriusDiagramModel) modelSet.getModel(SiriusDiagramModel.SIRIUS_DIAGRAM_MODEL_ID);
			DSemanticDiagram diagram = representation.getDiagram(name);
			openEditor(diagram);
			flushDisplayEvents();

			activateDiagram(editor, name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Cannot initialize test", e);
		}

		return this;
	}

	public SiriusDiagramEditorFixture openDiagram(String name) {
		return openDiagram(editor, name);
	}

	/**
	 * @since 2.0
	 */
	public SiriusDiagramEditorFixture openTable(IMultiDiagramEditor editor, final String name) {
		activate(editor);

		try {
			ModelSet modelSet = ServiceUtils.getInstance().getModelSet(editor.getServicesRegistry());
			PapyrusNattableModel representation = (PapyrusNattableModel) modelSet.getModel(PapyrusNattableModel.MODEL_ID);
			Table table = representation.getTable(name);
			ServiceUtils.getInstance().getService(IPageManager.class, editor.getServicesRegistry()).openPage(table);
			flushDisplayEvents();

			activateTable(editor, name);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot initialize test", e); // NON-NLS-1
		}

		return this;
	}

	public SiriusDiagramEditorFixture openTable(String name) {
		return openTable(editor, name);
	}

	@Override
	public void redo() {
		super.redo();
		flushDisplayEvents();
	}

	/**
	 * Reopens the same test model that was previously {@link #open() opened} and the
	 * subsequently {@link #close() closed}. This is an important disction, as simply
	 * {@link #open() opening} the test model again would actually re-initialize it from
	 * the deployed test resources, potentially replacing any changes in the model files
	 * that may be significant to the test.
	 *
	 * @return the re-opened editor
	 * @since 2.0
	 */
	public IMultiDiagramEditor reopen() {
		return reopenOne(testDescription);
	}

	/**
	 * Require an edit-part for a model element in a particular {@code scope}.
	 *
	 * @param scope
	 *            an edit part in which to search (its children) for an edit-part
	 * @param modelElement
	 *            the model element visualized by the edit-part to search for
	 *
	 * @return the matching edit-part
	 *
	 * @throws AssertionError
	 *             if the required edit-part is found in the {@code scope}
	 */
	public EditPart requireEditPart(EditPart scope, EObject modelElement) {
		EditPart result = findEditPart(scope, modelElement);
		if (result == null) {
			String label = getLabel(modelElement);
			fail(String.format("No edit-part found for \"%s\" in %s", label, scope));
		}
		return result;
	}

	public void resize(GraphicalEditPart editPart, Dimension newSize) {
		ChangeBoundsRequest resize = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
		IFigure figure = editPart.getFigure();
		Rectangle bounds = figure.getBounds();
		resize.setEditParts(editPart);
		resize.setResizeDirection(PositionConstants.SOUTH_WEST);
		resize.setCenteredResize(false);
		resize.setConstrainedResize(false);
		resize.setSizeDelta(sized(newSize.width() - bounds.width(), newSize.height() - bounds.height()));
		execute(editPart.getCommand(resize));
	}

	public void save() {
		save(getEditor());
	}

	public void save(ISaveablePart part) {
		if (part.isDirty()) {
			try {
				part.doSave(new NullProgressMonitor());
			} catch (Exception e) {
				e.printStackTrace();
				fail("Failed to save editor/view: " + e.getLocalizedMessage());
			} finally {
				// Must flush display events because some steps (e.g. dependent editor reload)
				// are done asynchronously in a UI job
				flushDisplayEvents();
			}
		}
	}

	public void saveAll() {
		try {
			IWorkbenchPage page = editor.getSite().getPage();
			page.saveAllEditors(false);
		} finally {
			// Must flush display events because some steps (e.g. dependent editor reload)
			// are done asynchronously in a UI job
			flushDisplayEvents();
		}
	}

	public void select(EditPart editPart) {
		editPart.getViewer().getSelectionManager().appendSelection(editPart);
	}

	public void splitEditorArea(IEditorPart editorToMove, boolean splitHorizontally) {
		MPart editorPart = editorToMove.getSite().getService(MPart.class);
		EModelService modelService = editorPart.getContext().get(EModelService.class);
		MPartStack oldStack = (MPartStack) modelService.getContainer(editorPart);
		MPartStack newStack = modelService.createModelElement(MPartStack.class);
		modelService.insert(newStack, oldStack, splitHorizontally ? EModelService.RIGHT_OF : EModelService.BELOW, 0.5f);
		newStack.getChildren().add(editorPart);

		activate(editorToMove);
	}

	@Override
	public void undo() {
		super.undo();
		flushDisplayEvents();
	}

	private IGraphicalEditPart getNewEditPart(EditPart context, Collection<? extends ViewDescriptor> viewDescriptors) {
		return viewDescriptors.stream()
				.map(desc -> desc.getAdapter(View.class)).map(View.class::cast)
				.filter(Objects::nonNull)
				.map(view -> DiagramEditPartsUtil.getEditPartFromView(view, context))
				.filter(IGraphicalEditPart.class::isInstance).map(IGraphicalEditPart.class::cast)
				.findAny().orElseGet(failOnAbsence("Could not find newly created edit-part"));
	}

	/**
	 *
	 * @param servicesRegistry
	 *            the servicesRegistry
	 * @return
	 *         the page manager or <code>null</code> if not found
	 */
	private IPageManager getPageManager(final ServicesRegistry servicesRegistry) {
		try {
			return ServiceUtils.getInstance().getService(IPageManager.class, servicesRegistry);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;

	}

	private AbstractToolDescription getTool(final List<AbstractToolDescription> tools, final String toolName) {
		AbstractToolDescription theAbstractToolDescription = null;
		for (int i = 0; i < tools.size(); i++) {

			final AbstractToolDescription tool = tools.get(i);
			final String name = tool.getName();
			if (name != null && name.equals(toolName)) {
				theAbstractToolDescription = tool;
				break;
			}
		}
		return theAbstractToolDescription;
	}

	private void movePartRelativeTo(IWorkbenchPart part, String relativeTo, int where) {
		MPart mPart = part.getSite().getService(MPart.class);
		EModelService modelService = mPart.getContext().get(EModelService.class);
		MUIElement relativePart = modelService.find(relativeTo, modelService.getTopLevelWindowFor(mPart));
		if (relativePart instanceof MPartSashContainerElement) {
			MStackElement toMove = mPart;
			MPlaceholder placeHolder = mPart.getCurSharedRef();
			if (placeHolder != null) {
				toMove = placeHolder;
			}

			if (where < 0) {
				// Add it to the relative part's containing stack
				if (relativePart instanceof MPart) {
					MPart relativeMPart = (MPart) relativePart;
					if (relativeMPart.getCurSharedRef() != null) {
						// This is where the part is stacked
						relativePart = relativeMPart.getCurSharedRef();
					}
				}
				relativePart.getParent().getChildren().add(toMove);
			} else {
				// Insert it next to the relative part
				MPartStack newStack = modelService.createModelElement(MPartStack.class);
				newStack.getChildren().add(toMove);
				modelService.insert(newStack, (MPartSashContainerElement) relativePart, where, 0.3f);
			}
		}
	}

	private void setActiveDiagramEditor(DiagramEditorWithFlyOutPalette editor) {
		activeDiagramEditor = editor;
		activeTableEditor = null;
	}

	private void setActiveTableEditor(AbstractEMFNattableEditor editor) {
		activeTableEditor = editor;
		activeDiagramEditor = null;
	}

	/**
	 * Apply a container creation tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the graphical container, for instance the diagram
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyContainerCreationTool(final String toolName, final DDiagram diagram, final EObject container) {
		final AbstractToolDescription tool = getTool(diagram, toolName);
		if (tool instanceof ContainerCreationDescription || tool instanceof NodeCreationDescription || tool instanceof ToolDescription) {
			final Command command = getCommand(container, tool);
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(container);
			boolean canExecute = command.canExecute();
			domain.getCommandStack().execute(command);
			return canExecute;
		}
		throw new IllegalArgumentException(TOOL_NAME_INCORRECT);
	}


	/**
	 * Applies the {@link ContainerDropDescription} with the given name, on the given target container and the dropped
	 * dDiagram element. It simulates a DDiagramElement drop from the same diagram.
	 *
	 * @param diagram
	 *            the diagram in which the tool should be applied
	 * @param dndToolName
	 *            the name of the {@link ContainerDropDescription} tool
	 * @param dropContainer
	 *            the container in which element should be dropped
	 * @param droppedDDiagramElement
	 *            the dropped {@link DDiagramElement} from a diagram.
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyContainerDropDescriptionTool(final DDiagram diagram, final String dndToolName, DragAndDropTarget dropContainer, DDiagramElement droppedDDiagramElement) {
		boolean result = false;
		AbstractToolDescription dndTool = getTool(diagram, dndToolName);
		if (dndTool instanceof ContainerDropDescription) {
			Command command = getCommandFactory().buildDropInContainerCommandFromTool(dropContainer, droppedDDiagramElement, (ContainerDropDescription) dndTool);
			result = command.canExecute();
			session.getTransactionalEditingDomain().getCommandStack().execute(command);
		}
		return result;
	}

	/**
	 * Applies the {@link ContainerDropDescription} with the given name, on the given target container and the dropped
	 * semantic element. It simulates a semantic element drop from example the Model Explorer view.
	 *
	 * @param diagram
	 *            the diagram in which the tool should be applied
	 * @param dndToolName
	 *            the name of the {@link ContainerDropDescription} tool
	 * @param dropContainer
	 *            the container in which element should be dropped
	 * @param droppedElement
	 *            the dropped EObject (if the Drop is made from the Model content view) or the {@link DDiagramElement}
	 *            if the drop is made from an existing {@link DDiagramElement}.
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyContainerDropDescriptionTool(final DDiagram diagram, final String dndToolName, DragAndDropTarget dropContainer, EObject droppedElement) {
		boolean result = false;
		AbstractToolDescription dndTool = getTool(diagram, dndToolName);
		if (dndTool instanceof ContainerDropDescription) {
			Command command = getCommandFactory().buildDropInContainerCommandFromTool(dropContainer, droppedElement, (ContainerDropDescription) dndTool);
			result = command.canExecute();
			session.getTransactionalEditingDomain().getCommandStack().execute(command);
		}
		return result;
	}

	/**
	 * Apply a semantic deletion tool on a diagram element.
	 *
	 * @param element
	 *            the diagram element
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applySemanticDeletionTool(final DDiagramElement element) {
		boolean result = false;
		Command command = getCommandFactory().buildDeleteDiagramElement(element);
		session.getTransactionalEditingDomain().getCommandStack().execute(command);
		return result;
	}

	/**
	 * Apply a graphical deletion tool on a diagram element.
	 *
	 * @param element
	 *            the diagram element
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyGraphicalDeletionTool(final DDiagramElement element) {
		boolean result = false;
		Command command = getCommandFactory().buildDeleteFromDiagramCommand(element);
		session.getTransactionalEditingDomain().getCommandStack().execute(command);
		return result;
	}

	/**
	 * Apply a direct edit tool on a diagram element.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param element
	 *            the diagram element
	 * @param value
	 *            the new value to set
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyDirectEditTool(final String toolName, final DDiagram diagram, final DDiagramElement element, final String value) {
		boolean result = false;
		AbstractToolDescription tool = getTool(diagram, toolName);
		if (tool instanceof DirectEditLabel) {
			Command command = getCommandFactory().buildDirectEditLabelFromTool(element, (DirectEditLabel) tool, value);
			result = command.canExecute();
			session.getTransactionalEditingDomain().getCommandStack().execute(command);
		}
		return result;
	}

	/**
	 * Apply an edge creation tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param source
	 *            the graphical source element
	 * @param target
	 *            the graphical target element
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyEdgeCreationTool(final String toolName, final DDiagram diagram, final EdgeTarget source, final EdgeTarget target) {
		boolean result = false;
		AbstractToolDescription tool = getTool(diagram, toolName);
		if (tool instanceof EdgeCreationDescription) {
			Command command = getCommandFactory().buildCreateEdgeCommandFromTool(source, target, (EdgeCreationDescription) tool);
			result = command.canExecute();
			session.getTransactionalEditingDomain().getCommandStack().execute(command);
		}
		return result;
	}

	/**
	 * Apply an edge reconnection tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param edge
	 *            the edge
	 * @param source
	 *            the graphical source element
	 * @param target
	 *            the graphical target element
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyEdgeReconnectionTool(final String toolName, final DDiagram diagram, final DEdge edge, final EdgeTarget source, final EdgeTarget target) {
		boolean result = false;
		AbstractToolDescription tool = getTool(diagram, toolName);
		if (tool instanceof ReconnectEdgeDescription) {
			Command command = getCommandFactory().buildReconnectEdgeCommandFromTool((ReconnectEdgeDescription) tool, edge, source, target);
			result = command.canExecute();
			session.getTransactionalEditingDomain().getCommandStack().execute(command);
		}
		return result;
	}


	/**
	 * Apply a generic tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the graphical container, for instance the diagram
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyGenericTool(final String toolName, final DDiagram diagram, final EObject container) {
		final AbstractToolDescription tool = getTool(diagram, toolName);
		if (tool instanceof ToolDescription) {
			final Command command = getCommand(container, tool);
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(container);
			boolean canExecute = command.canExecute();
			domain.getCommandStack().execute(command);
			return canExecute;
		}
		throw new IllegalArgumentException(TOOL_NAME_INCORRECT);
	}

	/**
	 * Apply a node creation tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the graphical container, for instance the diagram
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyNodeCreationTool(final String toolName, final DDiagram diagram, final EObject container) {
		final AbstractToolDescription tool = getTool(diagram, toolName);
		if (tool != null) {
			final Command command = getCommand(container, tool);
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(container);
			boolean canExecute = command.canExecute();
			domain.getCommandStack().execute(command);
			return canExecute;
		}
		throw new IllegalArgumentException(TOOL_NAME_INCORRECT);
	}


	/**
	 * Apply a node creation tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param layerName
	 *            the layer name
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the graphical container, for instance the diagram
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyNodeCreationTool(final String layerName, final String toolName, final DDiagram diagram, final EObject container) {
		boolean result = false;
		Layer layer = getLayer(diagram, layerName);
		if (layer != null) {
			final AbstractToolDescription tool = getTool(layer, toolName);
			if (tool != null) {
				Command command = getCommand(container, tool);
				result = command.canExecute();
				session.getTransactionalEditingDomain().getCommandStack().execute(command);
			}
		}
		return result;
	}



	/**
	 * Apply a node creation tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the graphical container, for instance the diagram
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applySelectionTool(final String toolName, final DDiagram diagram, final EObject container) {
		boolean result = false;
		AbstractToolDescription tool = getTool(diagram, toolName);
		if (tool != null) {
			Collection<EObject> selectedElements = getSelectedEObject(tool, diagram, container);
			Command command = getCommand(container, tool, selectedElements);
			result = command.canExecute();
			session.getTransactionalEditingDomain().getCommandStack().execute(command);
		}
		return result;
	}

	/**
	 * Apply a node creation tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param layerName
	 *            the layer name
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the graphical container, for instance the diagram
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applySelectionTool(final String layerName, final String toolName, final DDiagram diagram, final EObject container) {
		boolean result = false;
		Layer layer = getLayer(diagram, layerName);
		if (layer != null) {
			AbstractToolDescription tool = getTool(layer, toolName);
			if (tool != null) {
				Collection<EObject> selectedElements = getSelectedEObject(tool, diagram, container);
				Command command = getCommand(container, tool, selectedElements);
				result = command.canExecute();
				session.getTransactionalEditingDomain().getCommandStack().execute(command);
			}
		}
		return result;
	}


	/**
	 * Performs a arrange all request on diagramEditPart.
	 *
	 * @param diagramEditPart
	 *            the {@link DiagramEditPart} on to do the request
	 */
	protected void arrangeAll(DiagramEditPart diagramEditPart) {
		ArrangeRequest arrangeRequest = new ArrangeRequest(ActionIds.ACTION_ARRANGE_ALL);
		List<Object> partsToArrange = new ArrayList<>();
		partsToArrange.add(diagramEditPart);
		arrangeRequest.setPartsToArrange(partsToArrange);
		diagramEditPart.performRequest(arrangeRequest);
	}

	protected void closeRequiredViews() {
		// Only close the Palette view if we opened it
		if (viewsToClose != null) {
			for (IViewPart closeMe : viewsToClose) {
				closeMe.getSite().getPage().hideView(closeMe);
			}
			viewsToClose = null;
			flushDisplayEvents();
		}
	}

	@Override
	protected TransactionalEditingDomain createEditingDomain() {
		// We don't create the domain; the editor does
		return null;
	}




	@SuppressWarnings("restriction")
	@Override
	protected void finished(Description description) {
		try {
			modelFiles = null;

			Exception exception = null;

			for (IEditorPart editor : ImmutableList.copyOf(editorsToClose)) {
				try {
					close(editor);
				} catch (Exception e) {
					if (exception == null) {
						exception = e;
					}
				}
			}

			if (exception != null) {
				exception.printStackTrace();
				fail("Failed to close an editor: " + exception.getLocalizedMessage());
			}
		} finally {
			try {
				if (operationHistoryIntegrityListener != null) {
					operationHistory.removeOperationHistoryListener(operationHistoryIntegrityListener);
					operationHistoryIntegrityListener = null;
					operationHistory = null;
				}

			} finally {
				editorsToClose.clear();
				editor = null;
				activeDiagramEditor = null;

				org.eclipse.papyrus.infra.ui.internal.preferences.EditorPreferences.getInstance().setConvertSharedPageLayoutToPrivate(initialEditorLayoutStorageMigrationPreference);

				try {
					if (hasRequiredViews()) {
						closeRequiredViews();
					}
				} finally {
					super.finished(description);
				}
			}
		}
	}

	/**
	 * Get a command for the tool.
	 *
	 * @param container
	 *            the container
	 * @param tool
	 *            the tool
	 * @return the command build to execute the tool's operation on the given container
	 */
	protected final Command getCommand(final EObject container, final AbstractToolDescription tool) {

		Command cmd = null;

		if (tool instanceof NodeCreationDescription) {
			if (container instanceof DDiagram) {
				cmd = getCommandFactory().buildCreateNodeCommandFromTool((DDiagram) container, (NodeCreationDescription) tool);
			} else if (container instanceof DDiagramElementContainer) {
				cmd = getCommandFactory().buildCreateNodeCommandFromTool((DDiagramElementContainer) container, (NodeCreationDescription) tool);
			} else if (container instanceof DNode) {
				cmd = getCommandFactory().buildCreateNodeCommandFromTool((DNode) container, (NodeCreationDescription) tool);
			}
		} else if (tool instanceof ContainerCreationDescription) {
			if (container instanceof DDiagram) {
				cmd = getCommandFactory().buildCreateContainerCommandFromTool((DDiagram) container, (ContainerCreationDescription) tool);
			} else if (container instanceof DDiagramElementContainer) {
				cmd = getCommandFactory().buildCreateContainerCommandFromTool((DDiagramElementContainer) container, (ContainerCreationDescription) tool);
			}
		} else if (tool instanceof ToolDescription) {
			cmd = getCommandFactory().buildGenericToolCommandFromTool(container, (ToolDescription) tool);
		}
		return cmd;
	}


	protected IDiagramCommandFactory getCommandFactory() {
		if (commandFactory == null) {
			commandFactory = DiagramCommandFactoryService.getInstance().getNewProvider().getCommandFactory(getEditingDomain());
			commandFactory.setUserInterfaceCallBack(new NoUICallback());
		}
		return commandFactory;
	}

	protected final ShowView getRequiredViews() {
		ShowView result = testDescription.getAnnotation(ShowView.class);

		if (result == null) {
			for (Class<?> clazz = testClass; (result == null) && (clazz != null) && (clazz != Object.class); clazz = clazz.getSuperclass()) {
				result = clazz.getAnnotation(ShowView.class);
			}
		}

		return result;
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
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Searches the given {@link DDiagram} for a tool of the given name and returns it.
	 *
	 * @param diagram
	 *            The diagram to search for a tool.
	 * @param toolName
	 *            The name of the searched tool (&quot;chapter&quot; for the &quot;Create new chapter&quot; tool).
	 * @return The searched tool, <code>null</code> if it cannot be found.
	 */
	protected final AbstractToolDescription getTool(final DDiagram diagram, final String toolName) {
		final List<AbstractToolDescription> tools = DiagramComponentizationTestSupport.getAllTools(session, diagram.getDescription());
		return getTool(tools, toolName);
	}

	protected final IWorkbenchPage getWorkbenchPage() {
		IWorkbench bench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = bench.getActiveWorkbenchWindow();
		if (window == null) {
			window = bench.getWorkbenchWindows()[0];
		}
		return window.getActivePage();
	}

	protected final boolean hasRequiredViews() {
		return getRequiredViews() != null;
	}

	/**
	 * @since 2.0
	 */
	protected IMultiDiagramEditor open(final IFile modelFile) {
		final boolean firstEditor = editorsToClose.isEmpty();

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					editor = EditorUtils.openPapyrusEditor(modelFile);
					editorsToClose.add(editor);
				} catch (Exception e) {
					e.printStackTrace();
					fail("Failed to open Papyrus editor: " + e.getLocalizedMessage());
				}
			}
		});

		if (firstEditor && !editorsToClose.isEmpty()) {
			final IWorkbenchPage page = editor.getSite().getPage();
			page.addPartListener(new IPartListener() {

				@Override
				public void partActivated(IWorkbenchPart part) {
					// Pass
				}

				@Override
				public void partBroughtToTop(IWorkbenchPart part) {
					// Pass
				}

				@Override
				public void partClosed(IWorkbenchPart part) {
					editorsToClose.remove(part);

					if (part == editor) {
						editor = null;
					}

					if (editorsToClose.isEmpty()) {
						page.removePartListener(this);
					}
				}

				@Override
				public void partDeactivated(IWorkbenchPart part) {
					// Pass
				}

				@Override
				public void partOpened(IWorkbenchPart part) {
					// Pass
				}
			});
		}

		flushDisplayEvents();

		return editor;
	}

	protected Iterable<IMultiDiagramEditor> openAll(Description description) {
		List<IMultiDiagramEditor> result = Lists.newArrayList();

		for (Resource resource : initModelResources(description)) {
			if (resource.getURI().fileExtension().equals(UmlModel.UML_FILE_EXTENSION)) {
				IFile papyrusModel = getProject().getFile(resource.getURI().trimFileExtension().appendFileExtension(DiModel.DI_FILE_EXTENSION));
				modelFiles.put(description, papyrusModel);
				result.add(open(papyrusModel));
			}
		}

		return result;
	}

	/**
	 * Open the editor for the diagram
	 *
	 * @param proto
	 *            the diagram
	 */
	protected final void openEditor(final DSemanticDiagram proto) {
		final ServicesRegistry sReg = getServiceRegistry(proto.getTarget());
		if (null == sReg) {
			return;
		}
		final IPageManager pageManager = getPageManager(sReg);
		if (null == pageManager) {
			return;
		}
		pageManager.openPage(proto);
	}

	/**
	 * @since 2.0
	 */
	protected IMultiDiagramEditor openOne(Description description) {
		IFile papyrusModel = getProject().getFile(Iterables.getOnlyElement(initModelResources(description)).getURI().trimFileExtension().appendFileExtension(DiModel.DI_FILE_EXTENSION));
		modelFiles.put(description, papyrusModel);
		return open(papyrusModel);
	}

	protected void openRequiredViews() {
		IWorkbenchPage page = getWorkbenchPage();

		for (ShowViewDescriptor next : ShowViewDescriptor.getDescriptors(getRequiredViews())) {
			IViewPart part = page.findView(next.viewID());
			if (part == null) {
				// Must open it
				try {
					part = page.showView(next.viewID());
					movePartRelativeTo(part, next.relativeTo(), next.location());

					if (viewsToClose == null) {
						viewsToClose = Lists.newArrayListWithExpectedSize(1);
					}
					viewsToClose.add(part);
				} catch (PartInitException e) {
					e.printStackTrace();
					fail("Failed to open required view: " + e.getLocalizedMessage());
				}
			}
		}

		flushDisplayEvents();
	}

	/**
	 * @since 2.0
	 */
	protected IMultiDiagramEditor reopenOne(Description description) {
		IFile papyrusModel = modelFiles.get(description).get(0);
		return open(papyrusModel);
	}

	@SuppressWarnings("restriction")
	@Override
	protected void starting(Description description) {
		testClass = description.getTestClass();
		testDescription = description;

		// Ensure that we won't see a dialog prompting the user to migrate page layout
		// storage from the DI file to the workspace-private sash file
		initialEditorLayoutStorageMigrationPreference = org.eclipse.papyrus.infra.ui.internal.preferences.EditorPreferences.getInstance().getConvertSharedPageLayoutToPrivate();
		org.eclipse.papyrus.infra.ui.internal.preferences.EditorPreferences.getInstance().setConvertSharedPageLayoutToPrivate(org.eclipse.papyrus.infra.ui.internal.preferences.YesNo.NO);

		if (hasRequiredViews()) {
			openRequiredViews();
		}

		modelFiles = ArrayListMultimap.create();
		openAll(description);

		ActiveDiagram activeDiagram = JUnitUtils.getAnnotation(description, ActiveDiagram.class);
		if (activeDiagram != null) {
			String name = activeDiagram.value();
			activateDiagram(name);
			if ((activeDiagramEditor == null) || !name.equals(getActiveDiagramEditor().getDiagram().getName())) {
				// OK, we need to open it, then
				openDiagram(name);
			}
		} else {
			ActiveTable activeTable = JUnitUtils.getAnnotation(description, ActiveTable.class);
			if (activeTable != null) {
				String name = activeTable.value();
				activateTable(name);
				if ((activeTableEditor == null) || !name.equals(getActiveTableEditor().getTable().getName())) {
					openTable(name);
				}
			}
		}

		super.starting(description);

		try {
			ModelSet modelSet = ServiceUtils.getInstance().getModelSet(editor.getServicesRegistry());
			SiriusDiagramModel siriusModel = (SiriusDiagramModel) modelSet.getModel(SiriusDiagramModel.SIRIUS_DIAGRAM_MODEL_ID);

			SessionService sessionService = (SessionService) editor.getServicesRegistry().getService(ISiriusSessionService.SERVICE_ID);
			session = sessionService.getSiriusSession();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (ensureOperationHistoryIntegrity) {
			// Ensure consistency of undo/redo with EMF operations
			final IWorkspaceCommandStack stack = (IWorkspaceCommandStack) getEditingDomain().getCommandStack();
			final IUndoContext emfContext = stack.getDefaultUndoContext();
			operationHistory = stack.getOperationHistory();
			operationHistoryIntegrityListener = new IOperationHistoryListener() {
				@Override
				public void historyNotification(OperationHistoryEvent event) {
					if (DUMP_COMMANDS && (event.getEventType() == OperationHistoryEvent.ABOUT_TO_EXECUTE)) {
						System.err.println(">> Executing command"); //$NON-NLS-1$
						CommandUtils.dump(event.getOperation());
						System.err.println();
					}
					if ((event.getEventType() == OperationHistoryEvent.DONE) && (activeDiagramEditor != null)) {
						IUndoContext diagramContext = activeDiagramEditor.getDiagramEditDomain().getDiagramCommandStack().getUndoContext();
						if (diagramContext != null) {
							IUndoableOperation undo = event.getOperation();
							if ((undo != null) && !undo.hasContext(emfContext)) {
								undo.addContext(emfContext);
							}
						}
					}
					// nothing to do for table
				}
			};
			operationHistory.addOperationHistoryListener(operationHistoryIntegrityListener);
		}
	}

	/**
	 * Searches the given {@link DDiagram} for a layer of the given name and returns it.
	 *
	 * @param diagram
	 *            The diagram to search for a tool.
	 * @param layerName
	 *            The name of the searched layer.
	 * @return The retrieved layer, or <code>null</code> if it cannot be found.
	 */
	protected final Layer getLayer(final DDiagram diagram, final String layerName) {
		final Collection<Layer> layers = DiagramComponentizationTestSupport.getAllLayers(session, diagram.getDescription());

		for (final Layer layer : layers) {
			if (layer.getName().equals(layerName)) {
				return layer;
			}
		}
		throw new IllegalArgumentException(LAYER_NAME_INCORRECT);
	}


	/**
	 * Searches the given {@link Layer} for a tool of the given name and returns it.
	 *
	 * @param layer
	 *            The layer to search for a tool.
	 * @param toolName
	 *            The name of the searched tool (&quot;chapter&quot; for the &quot;Create new chapter&quot; tool).
	 * @return The searched tool, <code>null</code> if it cannot be found.
	 */
	protected final AbstractToolDescription getTool(final Layer layer, final String toolName) {
		return getTool(layer.getAllTools(), toolName);
	}

	/**
	 * Get the selected objects.
	 *
	 * @param tool
	 *            the tool
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the container
	 * @return the selected objects
	 */
	protected Collection<EObject> getSelectedEObject(final AbstractToolDescription tool, final DDiagram diagram, final EObject container) {
		return Collections.<EObject> emptyList();
	}


	/**
	 * Get a selection command for the tool.
	 *
	 * @param container
	 *            the container
	 * @param tool
	 *            the tool
	 * @param selectedElements
	 *            the selected elements
	 * @return the command build to execute the tool's operation on the given container
	 */
	protected Command getCommand(final EObject container, final AbstractToolDescription tool, final Collection<EObject> selectedElements) {
		Command cmd = null;
		if (tool instanceof SelectionWizardDescription && container instanceof DSemanticDecorator) {
			cmd = getCommandFactory().buildSelectionWizardCommandFromTool((SelectionWizardDescription) tool, (DSemanticDecorator) container, selectedElements);
		} else if (tool instanceof OperationAction) {
			cmd = getCommandFactory().buildOperationActionFromTool((OperationAction) tool,
					selectedElements.stream().filter(DSemanticDecorator.class::isInstance).map(DSemanticDecorator.class::cast).collect(Collectors.toList()));
		}
		return cmd;
	}


	private class ToolBasedCreationFactory implements CreationFactory {

		AbstractToolDescription toolDesc;

		public ToolBasedCreationFactory(AbstractToolDescription toolDescription) {
			this.toolDesc = toolDescription;
		}

		@Override
		public Object getNewObject() {
			return toolDesc;
		}

		@Override
		public Object getObjectType() {
			return toolDesc.getClass();
		}

	}




	/**
	 *
	 * @author ymortier
	 */
	public static class DummyCreationTool extends CreationTool {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.gef.tools.TargetingTool#setTargetEditPart(org.eclipse.gef.EditPart)
		 */
		@Override
		public void setTargetEditPart(EditPart editpart) {
			super.setTargetEditPart(editpart);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.gef.tools.TargetingTool#getTargetRequest()
		 */
		@Override
		public Request getTargetRequest() {
			return super.getTargetRequest();
		}

	}

	/**
	 * Apply a node creation tool on a diagram.
	 *
	 * @param toolName
	 *            the tool name
	 * @param diagram
	 *            the diagram
	 * @param container
	 *            the graphical container, for instance the diagram
	 * @return <code>true</code> if the tool could be applied, <code>false</code> otherwise
	 */
	public final boolean applyNodeCreationToolFromPalette(final String toolName, final DDiagram diagram, final EObject container, Point location, Dimension size) {
		final AbstractToolDescription tool = getTool(diagram, toolName);

		if (tool != null) {
			DummyCreationTool creationTool = createPaletteTool(tool);
			final var targetEditPart = findEditPart(container);
			creationTool.setTargetEditPart(targetEditPart);


			final Request request = creationTool.getTargetRequest();
			if (request instanceof CreateRequest) {
				CreateRequest requestcreation = (CreateRequest) request;
				requestcreation.setLocation(location);
				requestcreation.setSize(size);
			}
			final org.eclipse.gef.commands.Command command = targetEditPart.getCommand(request);
			getEditingDomain().getCommandStack().execute(GEFtoEMFCommandWrapper.wrap(command));
			return command.canExecute();
		}
		throw new IllegalArgumentException(TOOL_NAME_INCORRECT);
	}

	/**
	 * @param tool
	 * @return
	 */
	private DummyCreationTool createPaletteTool(final AbstractToolDescription tool) {
		final AbstractToolDescription containerCreation = tool;
		final CreationFactory creationFactory = new ToolBasedCreationFactory(containerCreation);

		final CreationToolEntry paletteEntry = new CreationToolEntry(containerCreation.getName(), containerCreation.getDocumentation(), creationFactory, null, null);
		paletteEntry.setToolClass(DummyCreationTool.class);

		DummyCreationTool creationTool = (DummyCreationTool) paletteEntry.createTool();
		creationTool.setViewer(getActiveDiagramEditor().getDiagramGraphicalViewer());
		return creationTool;
	}

	/**
	 * @param string
	 * @param diagramRepresentation
	 * @param element
	 * @param element2
	 * @param absoluteEndPoint
	 * @param absoluteStartPoint
	 * @return
	 */
	public boolean applyEdgeCreationToolFromPalette(String toolName, DDiagram diagram, EdgeTarget element1, EdgeTarget element2, Point absoluteStartPoint, Point absoluteEndPoint) {

		AbstractToolDescription tool = getTool(diagram, toolName);

		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) findEditPart(element1);
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) findEditPart(element2);


		CreateConnectionRequest createConnectionRequest = new CreateConnectionRequest();
		createConnectionRequest.setSourceEditPart(sourceEditPart);
		createConnectionRequest.setTargetEditPart(targetEditPart);
		createConnectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
		createConnectionRequest.setLocation(absoluteStartPoint);
		createConnectionRequest.setFactory(new ToolBasedCreationFactory(tool));

		sourceEditPart.showSourceFeedback(createConnectionRequest);
		targetEditPart.showTargetFeedback(createConnectionRequest);
		try {
			org.eclipse.gef.commands.Command startCommand = sourceEditPart.getCommand(createConnectionRequest);
			if (startCommand.canExecute()) {
				createConnectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
				createConnectionRequest.setLocation(absoluteEndPoint);
				// => activate feedback for debug ?
				// lifelineAEditPart.showSourceFeedback(createConnectionRequest);
				// lifelineBEditPart.showTargetFeedback(createConnectionRequest);
				final org.eclipse.gef.commands.Command endCommand = targetEditPart.getCommand(createConnectionRequest);
				getEditingDomain().getCommandStack().execute(GEFtoEMFCommandWrapper.wrap(endCommand));
				return endCommand.canExecute();
			}
		} finally {
			sourceEditPart.eraseSourceFeedback(createConnectionRequest);
			targetEditPart.eraseTargetFeedback(createConnectionRequest);
		}
		return false;
	}


}
