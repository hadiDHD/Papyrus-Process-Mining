/******************************************************************************
 * Copyright (c) 2014, 2022 Obeo, CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Obeo - initial API and implementation
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - adaptation to integrate in Papyrus
 *  Jessy Mallet (Obeo) - jessy.mallet@obeo.fr - Bug 579695
 *****************************************************************************/

package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.papyrus.uml.sirius.common.diagram.Activator;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.api.helper.graphicalfilters.HideFilterHelper;
import org.eclipse.sirius.diagram.business.api.query.DDiagramElementQuery;
import org.eclipse.sirius.diagram.business.internal.helper.task.operations.CreateViewTask;
import org.eclipse.sirius.diagram.description.AbstractNodeMapping;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.tool.CreateView;
import org.eclipse.sirius.diagram.description.tool.ToolFactory;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeContainerSpec;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.FeatureNotFoundException;
import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.MetaClassNotFoundException;
import org.eclipse.sirius.tools.api.command.CommandContext;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
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
                        Activator.log.error(e);
                    } catch (final FeatureNotFoundException e) {
                        Activator.log.error(e);
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

}
