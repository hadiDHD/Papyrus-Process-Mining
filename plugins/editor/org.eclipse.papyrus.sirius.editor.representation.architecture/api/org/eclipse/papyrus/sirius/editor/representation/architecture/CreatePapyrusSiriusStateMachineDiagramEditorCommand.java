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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Bug 569174
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.representation.architecture;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.papyrus.infra.architecture.representation.PapyrusRepresentationKind;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.EditorNameInitializer;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForResourceSet;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.papyrus.sirius.editor.internal.viewpoint.SiriusDiagramViewPrototype;
import org.eclipse.papyrus.sirius.editor.representation.ICreateSiriusDiagramEditorCommand;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.sirius.editor.representation.architecture.internal.messages.Messages;
import org.eclipse.papyrus.uml.tools.utils.NamedElementUtil;
import org.eclipse.sirius.diagram.ContainerLayout;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DiagramFactory;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.business.api.helper.SiriusDiagramHelper;
import org.eclipse.sirius.diagram.business.api.helper.graphicalfilters.HideFilterHelper;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * This class allows to create new Sirius Diagram instance and open the papyrus editor for it
 */
public class CreatePapyrusSiriusStateMachineDiagramEditorCommand extends AbstractCreateSiriusDiagramEditorCommand implements ICreateSiriusDiagramEditorCommand {


	public static final int defaultX = 30;
	public static final int defaultY = 30;
	public static final int defaultWidth = 700;
	public static final int defaultHeight = 300;
	// Bug 513267: The initial header must be defined to 20 because when it is smaller, a first internal resize increase it automatically to 20 (CustomStateMachineResizeCommand).
	public static final int defaultHeader = 20;

	/**
	 * Prompts the user the future document's name
	 *
	 * @return The name, or <code>null</code> if the user cancelled the creation
	 */
	private String askName(final ViewPrototype prototype, final EObject semanticContext) {
		final String defaultName = getDefaultName(prototype, semanticContext);
		return askDiagramName(Messages.CreatePapyrusSiriusDiagramEditorCommand_CreateSiriusDiagramDialogTitle, defaultName);
	}

	/**
	 *
	 * @param prototype
	 *            the ViewPrototype
	 * @param semanticContext
	 *            the semantic context for the created DSemanticDiagram
	 * @return
	 *         the default name to use
	 */
	private String getDefaultName(final ViewPrototype prototype, final EObject semanticContext) {
		final StringBuilder nameBuilder = new StringBuilder(prototype.getLabel().replaceAll(" ", "")); //$NON-NLS-1$ //$NON-NLS-2$
		final String nameWithIncrement = EditorNameInitializer.getNameWithIncrement(DiagramPackage.eINSTANCE.getDDiagram(), ViewpointPackage.eINSTANCE.getDRepresentationDescriptor_Name(), nameBuilder.toString(),
				semanticContext);
		return nameWithIncrement;
	}

	/**
	 *
	 * @see org.eclipse.papyrus.sirius.editor.internal.ICreateSiriusDiagramEditorCommand.ICreateDSemanticDiagramEditorCommand#execute(org.eclipse.papyrus.sirius.editor.internal.viewpoint.PapyrusDSemanticDiagramViewPrototype,
	 *      org.eclipse.emf.ecore.EObject, java.lang.String)
	 *
	 * @param prototype
	 * @param name
	 * @param semanticContext
	 * @param openAfterCreation
	 * @return
	 */
	@Override
	public DSemanticDiagram execute(final ViewPrototype prototype, final String name, final EObject semanticContext, final boolean openAfterCreation) {
		return execute(prototype, name, semanticContext, semanticContext, openAfterCreation);
	}

	/**
	 * @see org.eclipse.papyrus.sirius.editor.representation.ICreateSiriusDiagramEditorCommand.ICreateDSemanticDiagramEditorCommand#execute(org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype, java.lang.String,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, boolean)
	 *
	 * @param prototype
	 * @param name
	 * @param semanticContext
	 * @param graphicalContext
	 * @param openAfterCreation
	 * @return
	 */
	@Override
	public DSemanticDiagram execute(final ViewPrototype prototype, final String name, EObject semanticContext, final EObject graphicalContext, boolean openAfterCreation) {
		if (prototype instanceof SiriusDiagramViewPrototype) {
			final PapyrusRepresentationKind representation = prototype.getRepresentationKind();
			Assert.isTrue(representation instanceof SiriusDiagramPrototype, "The representation associated to the PapyrusDSemanticDiagramViewPrototype must be an instanceof SiriusDiagramPrototype."); //$NON-NLS-1$
			SiriusDiagramPrototype docProto = (SiriusDiagramPrototype) representation;

			final String diagramName = (name == null || name.isEmpty()) ? askName(prototype, semanticContext) : name;
			if (null == diagramName) {
				return null; // the creation is cancelled
			}


			// if (semanticContext instanceof Model) {
			// Model model = (Model) semanticContext;
			// StateMachine sm = UMLFactory.eINSTANCE.createStateMachine();
			// sm.setName("StateMachine1");
			// semanticContext = sm;
			// sm.createRegion("Region1");
			try {

				ServicesRegistry serviceRegistry = ServiceUtilsForResourceSet.getInstance().getServiceRegistry(semanticContext.eResource().getResourceSet());
				TransactionalEditingDomain ted = serviceRegistry.getService(TransactionalEditingDomain.class);
				DSemanticDiagram[] result = new DSemanticDiagram[1];
				ted.getCommandStack().execute(new RecordingCommand(ted) {

					@Override
					protected void doExecute() {
						Element sm = initializeModel(semanticContext);
						DSemanticDiagram ddiagram = CreatePapyrusSiriusStateMachineDiagramEditorCommand.super.execute(docProto, diagramName, sm, graphicalContext, openAfterCreation, docProto.getImplementationID());
						DiagramDescription desc = ddiagram.getDescription();
						initializeDiagram(ddiagram, desc, sm);
						result[0] = ddiagram;
					}
				});
				return result[0];
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// return super.execute(docProto, diagramName, graphicalContext, semanticContext, openAfterCreation, docProto.getImplementationID());
		// }
		return null;
	};



	protected Element initializeModel(EObject owner) {
		if (owner instanceof StateMachine) {
			StateMachine sm = (StateMachine) owner;
			initDefaultRegion(sm);
			return sm;
		} else if (owner instanceof State) {
			State state = (State) owner;

			EList<Region> regions = state.getRegions();
			if (regions.isEmpty()) {
				Region region = UMLFactory.eINSTANCE.createRegion();
				regions.add(region);
				region.setName(NamedElementUtil.getDefaultNameWithIncrement(region, regions));
			}
			return state;
		} else {
			StateMachine stateMachine = UMLFactory.eINSTANCE.createStateMachine();
			if (owner instanceof BehavioredClassifier) {
				BehavioredClassifier behaviorClassifier = (BehavioredClassifier) owner;
				behaviorClassifier.getOwnedBehaviors().add(stateMachine);
			} else if (owner instanceof Package) {
				org.eclipse.uml2.uml.Package pack = (org.eclipse.uml2.uml.Package) owner;
				pack.getPackagedElements().add(stateMachine);
			}
			init_StateMachine_Shape(stateMachine);
			initDefaultRegion(stateMachine);
			return stateMachine;
		}
	}

	/**
	 * @param sm
	 */
	private void initDefaultRegion(StateMachine sm) {
		EList<Region> regions = sm.getRegions();
		Region region;
		if (regions.isEmpty()) {
			region = UMLFactory.eINSTANCE.createRegion();
			regions.add(region);
			region.setName(NamedElementUtil.getDefaultNameWithIncrement(region, regions));
		}
	}



	/**
	 * @generated
	 */
	public void init_StateMachine_Shape(StateMachine instance) {
		try {
			Object value_0 = name_StateMachine_Shape(instance);
			if (value_0 != null) {
				instance.setName(
						(String) value_0);
			}
			Region newInstance_1_0 = UMLFactory.eINSTANCE.createRegion();
			instance.getRegions()
					.add(newInstance_1_0);
			Object value_1_0_0 = name_region_StateMachine_Shape(newInstance_1_0);
			if (value_1_0_0 != null) {
				newInstance_1_0.setName(
						(String) value_1_0_0);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @generated
	 */
	private String name_StateMachine_Shape(StateMachine it) {
		return NamedElementUtil.getDefaultNameWithIncrement(it, it.getOwner().eContents());
	}

	/**
	 * @generated
	 */
	private String name_Region_Shape(Region it) {
		return NamedElementUtil.getDefaultNameWithIncrement(it, it.getOwner().eContents());
	}

	/**
	 * @generated
	 */
	private String name_region_StateMachine_Shape(Region it) {
		return NamedElementUtil.getDefaultNameWithIncrement(it, it.getOwner().eContents());
	}



	protected void initializeDiagram(DDiagram diagram, DiagramDescription desc, Element sm) {
		Region region = null;
		var regionMapping = "Region";
		if (sm instanceof StateMachine) {
			StateMachine stateMachine = (StateMachine) sm;
			region = stateMachine.getRegions().get(0);
			// IAdaptable regionAdaptable = new SemanticAdapter(region, null);
			// String semanticHint = ((IHintedType) UMLElementTypes.Region_Shape).getSemanticHint();
			// if (compartmentView != null) {
			// Node regionNode = ViewService.getInstance().createNode(regionAdaptable, compartmentView, semanticHint, -1, getPreferenceHint());
			// if (regionNode.getLayoutConstraint() == null) {
			// regionNode.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
			// }
			// // add region specifics
			// Zone.createRegionDefaultAnnotation(regionNode);
			// Zone.setWidth(regionNode, defaultWidth);
			// Zone.setHeight(regionNode, defaultHeight - defaultHeader);
			// }
		} else if (sm instanceof State) {
			regionMapping = "Region";
			State state = (State) sm;
			region = state.getRegions().get(0);

		}
		if (region != null) {
			DNodeContainer compartment = (DNodeContainer) diagram.getOwnedDiagramElements().get(0);
			final DNodeContainer newNode = DiagramFactory.eINSTANCE.createDNodeContainer();
			HideFilterHelper.INSTANCE.hideLabel(newNode);
			newNode.setTarget(region);
			final var selector = regionMapping;
			final var anyMatch = compartment.getActualMapping().getReusedContainerMappings().stream().filter(m -> selector.equals(m.getName())).findFirst();
			newNode.setActualMapping(anyMatch.get());
			newNode.setChildrenPresentation(ContainerLayout.FREE_FORM);
			SiriusDiagramHelper.addNodeInContainer(compartment, false, newNode);
		}
	}




}
