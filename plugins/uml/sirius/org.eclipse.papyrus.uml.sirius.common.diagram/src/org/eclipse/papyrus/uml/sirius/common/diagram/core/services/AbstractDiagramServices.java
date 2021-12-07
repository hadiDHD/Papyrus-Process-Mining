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

package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.api.helper.graphicalfilters.HideFilterHelper;
import org.eclipse.sirius.diagram.business.api.query.DDiagramElementQuery;
import org.eclipse.sirius.diagram.business.internal.helper.task.operations.CreateViewTask;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeSpec;
import org.eclipse.sirius.diagram.description.AbstractNodeMapping;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.tool.CreateView;
import org.eclipse.sirius.diagram.description.tool.ToolFactory;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.FeatureNotFoundException;
import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.MetaClassNotFoundException;
import org.eclipse.sirius.tools.api.command.CommandContext;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EncapsulatedClassifier;
import org.eclipse.uml2.uml.ExecutionEnvironment;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Manifestation;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UseCase;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * A set of services used by diagrams.
 */
@SuppressWarnings("restriction")
public abstract class AbstractDiagramServices {

    /**
     * Compute default name.
     *
     * @param element
     *            New element
     * @return Name for the new element, he name will looks like
     *         'ElementType'+total of existing elements of the same type.
     */
    public String computeDefaultName(final EObject element) {
        return LabelServices.INSTANCE.computeDefaultName(element);
    }

    /**
     * Compute the tooltip.
     *
     * @param element
     *            New element
     * @return Tooltip for the new element, the tooltip will looks like
     *         'pkg::name'.
     */
    public String computeTooltip(final EObject element) {
        return TooltipServices.INSTANCE.computeTooltip(element);
    }

    /**
     * Compute the label of the given element.
     *
     * @param element
     *            the {@link Element} for which to retrieve a label.
     * @return the computed label.
     */
    public String computeUmlLabel(Element element) {
        return LabelServices.INSTANCE.computeUmlLabel(element);
    }

    /**
     * @param semanticElement
     * @param containerView
     * @param containerViewExpression
     */
    public void createView(final EObject semanticElement, final DSemanticDecorator containerView, final String containerViewExpression) {
    	Session session=SessionManager.INSTANCE.getSession(semanticElement);
    	createView(semanticElement, containerView, session, containerViewExpression);
    }

    
    /**
     * Create view.
     *
     * @param semanticElement
     *            Semantic element
     * @param containerView
     *            Container view
     * @param session
     *            Session
     * @param containerViewVariable
     *            Name of the container view variable
     */
    protected void createView(final EObject semanticElement, final DSemanticDecorator containerView, final Session session, final String containerViewExpression) {
        // Get all available mappings applicable for the semantic element in the
        // current container
        final List<DiagramElementMapping> semanticElementMappings = getMappings(semanticElement, containerView, session);

        // Build a createView tool
        final CreateView createViewOp = ToolFactory.eINSTANCE.createCreateView();
        for (final DiagramElementMapping semanticElementMapping : semanticElementMappings) {
            final DiagramElementMapping tmpSemanticElementMapping = semanticElementMapping;
            createViewOp.setMapping(tmpSemanticElementMapping);
            createViewOp.setContainerViewExpression(containerViewExpression);

            session.getTransactionalEditingDomain().getCommandStack().execute(new RecordingCommand(session.getTransactionalEditingDomain()) {

                @Override
                protected void doExecute() {
                    try {
                        // Get the command context
                        DRepresentation representation = null;
                        if (containerView instanceof DRepresentation) {
                            representation = (DRepresentation) containerView;
                        } else if (containerView instanceof DDiagramElement) {
                            representation = ((DDiagramElement) containerView).getParentDiagram();
                        }

                        final CommandContext context = new CommandContext(semanticElement, representation);

                        // Execute the create view task
                        final CreateViewTask task = new CreateViewTask(context, session.getModelAccessor(), createViewOp, session.getInterpreter());
                        task.execute();

                        final Object createdView = session.getInterpreter().getVariable(createViewOp.getVariableName());
                        if (createdView instanceof DDiagramElement) {
                            final DDiagramElement element = (DDiagramElement) createdView;
                            HideFilterHelper.INSTANCE.reveal(element);
                        }
                    } catch (final MetaClassNotFoundException e) {
//                        UMLDesignerCorePlugin.log(IStatus.ERROR, NLS.bind(Messages.UmlModelWizard_UI_ErrorMsg_BadFileExtension, semanticElement), e);
                    } catch (final FeatureNotFoundException e) {
//                        UMLDesignerCorePlugin.log(IStatus.ERROR, NLS.bind(Messages.UmlModelWizard_UI_ErrorMsg_BadFileExtension, semanticElement), e);
                    }
                }
            });
        }
    }

    /**
     * Default height.
     *
     * @param any
     *            Any
     * @return The default height.
     */
    public int defaultHeight(EObject any) {
        return 10/*UIServices.INSTANCE.defaultHeight()*/;
    }

    /**
     * Default width.
     *
     * @param any
     *            Any
     * @return The default width.
     */
    public int defaultWidth(EObject any) {
        return 10/*UIServices.INSTANCE.defaultWidth()*/;
    }

    /**
     * Precondition for Dependency creation.
     *
     * @param preTarget
     *            target element
     * @return true if target element is a NamedElement
     */
    public boolean dependencyCreationCompletePrecondition(Element preTarget) {
        return preTarget instanceof NamedElement;
    }

    /**
     * Precondition for dependency creation.
     *
     * @param preSource
     *            source element
     * @return true if source is a NamedElement
     */
    public boolean dependencyCreationStartPrecondition(Element preSource) {
        return preSource instanceof NamedElement;
    }

    /**
     * Drop a semantic element and create the corresponding view in the given
     * container
     *
     * @param newContainer
     *            Semantic container
     * @param semanticElement
     *            Element to drop
     * @param containerView
     *            Container view
     * @param moveSemanticElement
     *            True to move the dropped semantic element or false to just
     *            show the element on a diagram
     */
    //TODO
    private void drop(final Element newContainer, final Element semanticElement, final DSemanticDecorator containerView, boolean moveSemanticElement) {
        final Session session = SessionManager.INSTANCE.getSession(newContainer);
        final Element oldContainer = semanticElement.getOwner();
        if (moveSemanticElement && oldContainer != newContainer) {
            // Manage stereotypes and profiles
            final List<Stereotype> stereotypesToApply = Lists.newArrayList();
            for (final Stereotype stereotype : semanticElement.getAppliedStereotypes()) {
                stereotypesToApply.add(stereotype);
            }

            // Move the semantic element to the selected container
            final TransactionalEditingDomain domain = session.getTransactionalEditingDomain();
            // The feature is set to null because the domain will deduce it
            Command cmd = AddCommand.create(domain, newContainer, null, semanticElement);
            if (cmd.canExecute()) {
                cmd.execute();
            }
            cmd = RemoveCommand.create(domain, oldContainer, null, semanticElement);
            if (cmd.canExecute()) {
                cmd.execute();
            }

            if (semanticElement instanceof UseCase) {
                // Reset the current element as subject
                cmd = SetCommand.create(domain, semanticElement, UMLPackage.Literals.USE_CASE__SUBJECT, SetCommand.UNSET_VALUE);
                if (cmd.canExecute()) {
                    cmd.execute();
                }
                final List<Element> subjects = new ArrayList<Element>();
                subjects.add(newContainer);
                cmd = SetCommand.create(domain, semanticElement, UMLPackage.Literals.USE_CASE__SUBJECT, subjects);
                if (cmd.canExecute()) {
                    cmd.execute();
                }
            }
            // Apply stereotypes on dropped element and apply the necessary
            // profiles automatically
            StereotypeServices.INSTANCE.applyAllStereotypes(semanticElement, stereotypesToApply);

            // Check if profile should be unapplied
            for (final Stereotype stereotype : stereotypesToApply) {
                StereotypeServices.INSTANCE.unapplyProfile(oldContainer, stereotype);
            }
        }

        // Show the semantic element on the diagram
        showView(semanticElement, containerView, session, "[self.getContainerView(newContainerView)/]"); //$NON-NLS-1$
    }

    /**
     * Drop a semantic element from a diagram and create the corresponding view
     * in the given container
     *
     * @param newContainer
     *            Semantic container
     * @param semanticElement
     *            Element to drop
     * @param containerView
     *            Container view
     */
    public void dropFromDiagram(final Element newContainer, final Element semanticElement, final DSemanticDecorator containerView) {
        drop(newContainer, semanticElement, containerView, true);
    }

    /**
     * Drop a semantic element and create the corresponding view in the given
     * container
     *
     * @param newContainer
     *            Semantic container
     * @param semanticElement
     *            Element to drop
     * @param containerView
     *            Container view
     */
    public void dropFromModel(final Element newContainer, final Element semanticElement, final DSemanticDecorator containerView) {
        final Session session = SessionManager.INSTANCE.getSession(semanticElement);
        //markForAutosize(semanticElement);
        showView(semanticElement, containerView, session, "[self.getContainerView(newContainerView)/]"); //$NON-NLS-1$
    }

    /**
     * Get all available root packages.
     *
     * @param element
     *            ELement
     * @return List of root packages
     */
    public List<Package> getAllAvailableRootPackages(Element element) {
        final List<Package> packages = Lists.newArrayList();
        packages.add(element.getModel());
        packages.addAll(Lists.newArrayList(Iterables.filter(element.getModel().getImportedPackages(), Model.class)));
        return packages;
    }

    /**
     * Get all the hidden existing diagram elements related to a semantic
     * element.
     *
     * @param semanticElement
     *            Semantic element
     * @param containerView
     *            Container view
     * @return List of all existing diagram elements for the given semantic
     *         element which are currently hidden in the diagram
     */
    //TODO
    private List<DDiagramElement> getHiddenExistingDiagramElements(EObject semanticElement, DSemanticDecorator containerView) {
        final List<DDiagramElement> existingDiagramElements = Lists.newArrayList();
        if (containerView instanceof DSemanticDiagram) {
            for (final DDiagramElement element : ((DSemanticDiagram) containerView).getDiagramElements()) {
                if (semanticElement.equals(element.getTarget())) {
                    final DDiagramElementQuery query = new DDiagramElementQuery(element);
                    if (query.isHidden()) {
                        existingDiagramElements.add(element);
                    }
                    // Get the hidden parent container of the element to reveal,
                    // in order to reveal all the
                    // hierarchy
                    existingDiagramElements.addAll(getHiddenParentContainerViews(element));
                }
            }
        }
        return existingDiagramElements;
    }

    /**
     * Get all the hidden diagram element in the hierarchy of a given diagram
     * element.
     *
     * @param diagramElement
     *            Diagram element
     * @return List of all the hidden parent container element
     */
    //TODO
    private List<DDiagramElement> getHiddenParentContainerViews(DDiagramElement diagramElement) {
        final List<DDiagramElement> containerViews = Lists.newArrayList();
        EObject containerView = diagramElement.eContainer();
        while (!(containerView instanceof DDiagram) && containerView instanceof DDiagramElement) {
            final DDiagramElementQuery query = new DDiagramElementQuery((DDiagramElement) containerView);
            if (query.isHidden()) {
                containerViews.add((DDiagramElement) containerView);
            }
            containerView = containerView.eContainer();
        }
        return containerViews;
    }

    /**
     * Get mappings available for a given container view.
     *
     * @param containerView
     *            Container view
     * @param session
     *            Session
     * @return List of mappings which could not be null
     */
    //TODO
    protected List<DiagramElementMapping> getMappings(final DSemanticDecorator containerView, Session session) {
        final List<DiagramElementMapping> mappings = new ArrayList<DiagramElementMapping>();

        if (containerView instanceof DSemanticDiagram) {

            for (final DiagramElementMapping mapping : ((DSemanticDiagram) containerView).getDescription().getContainerMappings()) {
                if (!mapping.isCreateElements()) {
                    mappings.add(mapping);
                }
            }
            for (final DiagramElementMapping mapping : ((DSemanticDiagram) containerView).getDescription().getNodeMappings()) {
                if (!mapping.isCreateElements()) {
                    mappings.add(mapping);
                }
            }
        } else if (containerView instanceof DNodeContainerSpec) {
            for (final DiagramElementMapping mapping : ((DNodeContainerSpec) containerView).getActualMapping().getSubContainerMappings()) {
                if (!mapping.isCreateElements()) {
                    mappings.add(mapping);
                }
            }
            for (final DiagramElementMapping mapping : ((DNodeContainerSpec) containerView).getActualMapping().getSubNodeMappings()) {
                if (!mapping.isCreateElements()) {
                    mappings.add(mapping);
                }
            }
        }
        return mappings;
    }

    /**
     * Get mappings available for a semantic element and a given container view.
     *
     * @param semanticElement
     *            Semantic element
     * @param containerView
     *            Container view
     * @param session
     *            Session
     * @return List of mappings which could not be null
     */
    //TODO
    protected List<DiagramElementMapping> getMappings(final EObject semanticElement, final DSemanticDecorator containerView, Session session) {
        final ModelAccessor modelAccessor = session.getModelAccessor();
        final List<DiagramElementMapping> mappings = new ArrayList<DiagramElementMapping>();

        if (containerView instanceof DSemanticDiagram) {
            mappings.addAll(AddElementToDiagramServices.INSTANCE.getValidMappingsForDiagram(semanticElement, (DSemanticDiagram) containerView, session));
        } else if (containerView instanceof DNodeContainerSpec) {
            for (final DiagramElementMapping mapping : ((DNodeContainerSpec) containerView).getActualMapping().getSubContainerMappings()) {
                final String domainClass = ((AbstractNodeMapping) mapping).getDomainClass();
                if (modelAccessor.eInstanceOf(semanticElement, domainClass) && !mapping.isCreateElements()) {
                    mappings.add(mapping);
                }
            }
            for (final DiagramElementMapping mapping : ((DNodeContainerSpec) containerView).getActualMapping().getSubNodeMappings()) {
                final String domainClass = ((AbstractNodeMapping) mapping).getDomainClass();
                if (modelAccessor.eInstanceOf(semanticElement, domainClass) && !mapping.isCreateElements()) {
                    mappings.add(mapping);
                }
            }
        }
        return mappings;
    }

    /**
     * return the list of semantic elements we should bind with the given
     * element in the property view.
     *
     * @param e
     *            a semantic element.
     * @return the list of semantic elements we should bind with the given
     *         element in the property view.
     */
    public Collection<EObject> getSemanticElements(EObject e) {
        return new SemanticElementsSwitch().getSemanticElements(e);
    }

    /**
     * Check if a semantic element can be represented in a given container view.
     *
     * @param container
     *            Semantic container
     * @param semanticElement
     *            Element to test
     * @param containerView
     *            Container view
     * @return True if element is valid for the current container view
     */
    public boolean isValidElementForContainerView(final Element container, final Element semanticElement, final DSemanticDecorator containerView) {
        final Session session = SessionManager.INSTANCE.getSession(container);

        // Get all available mappings applicable for the selected element in the
        // current container
        final List<DiagramElementMapping> semanticElementMappings = getMappings(semanticElement, containerView, session);

        return semanticElementMappings.size() > 0;
    }


    /**
     * Show the given semantic element on the diagram. If the element already
     * exists but is hidden juste reveal it, otherwise create a new view.
     *
     * @param semanticElement
     *            Semantic element
     * @param containerView
     *            Container view
     * @param session
     *            Session
     */
    protected void showView(final EObject semanticElement, final DSemanticDecorator containerView, final Session session, String containerViewExpression) {
        // Check if the dropped element already exists in the diagram but is
        // hidden
        final List<DDiagramElement> hiddenDiagramElements = getHiddenExistingDiagramElements(semanticElement, containerView);
        if (!hiddenDiagramElements.isEmpty()) {
            // Just reveal the elements
            for (final DDiagramElement existingDiagramElement : hiddenDiagramElements) {
                HideFilterHelper.INSTANCE.reveal(existingDiagramElement);
            }
        } else {
            // Else create a new element
            // Create the view for the dropped element
            createView(semanticElement, containerView, session, containerViewExpression);
        }
    }
}
