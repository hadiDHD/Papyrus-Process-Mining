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
package org.eclipse.papyrus.uml.sirius.clazz.diagram.services;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.AssociationServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.DirectEditLabelSwitch;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.DisplayLabelSwitch;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.EditLabelSwitch;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.ElementServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.LabelServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.NodeInverseRefsServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.OperationServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.StereotypeServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.UIServices;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeListSpec;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.FontFormat;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DurationObservation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Observation;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.TimeObservation;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;

import com.google.common.collect.Lists;

/**
 * Services to handle typed Element concerns.
 *
 */
public class ClassDiagramServices {
	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final ClassDiagramServices INSTANCE = new ClassDiagramServices();

	/** Class diagram package name */
	public static final String CLASS_DIAGRAM_PACKAGE_NAME = "/org.eclipse.papyrus.uml.sirius.clazz.diagram";

	/** TimeObseravtion element symbol path */
	public static final String TIME_OBSERVATION_SYMBOL_PATH = CLASS_DIAGRAM_PACKAGE_NAME
			+ "/icons/symbols/timeObservation.png";

	/** DurationObseravtion element symbol path */
	public static final String DURATION_OBSERVATION_SYMBOL_PATH = CLASS_DIAGRAM_PACKAGE_NAME
			+ "/icons/symbols/durationObservation.png";

	/** INSTANCE_END for instance specification eAnnotation */
	protected static final String INSTANCE_END = "InstanceEnd";

	/** Dialog CANCEL button label */
	private static final String CANCEL_LABEL = "Cancel";

	/** Dialog OK button label */
	private static final String OK_LABEL = "OK";

	/** Annotation generic source name */
	private static final String ANNOTATION_GENERIC_SOURCE = "org.eclipse.papyrus";

	/** Annotation InstanceEnd source name */
	private static final String ANNOTATION_INSTANCE_END_SOURCE = "InstanceEnd";

	/** Annotation detail key */
	private static final String ANNOTATION_DETAIL_KEY = "nature";

	/** Annotation detail value */
	private static final String ANNOTATION_DETAIL_VALUE = "UML_Nature";

	/** underscore separator */
	private static final String UNDERSCORE = "_";

	/** ITEM_LABEL_PREFIX_START_TAG */
	private static final String ITEM_LABEL_PREFIX_START_TAG = "<<";

	/** ITEM_LABEL_PREFIX_END_TAG */
	private static final String ITEM_LABEL_PREFIX_END_TAG = ">>";

	/** InstanceSpecification edge */
	private InstanceSpecification _instanceSpec;

	/** Association type for a new created InstanceSpecification */
	private String _selectedAssosType = "";

	/**
	 * Move the given Element
	 * @param semanticObjectToDrop semantic element to drop
	 * @param targetContainerView the target container view
	 * @return the element
	 */
	public EObject dndElement(EObject semanticObjectToDrop, EObject targetContainerView) {
		if ((targetContainerView instanceof DSemanticDecorator) && (semanticObjectToDrop instanceof PackageableElement)) {
			Element element = (PackageableElement) semanticObjectToDrop;
			EObject target = ((DSemanticDecorator) targetContainerView).getTarget();

			Model targetModel = null;
			if (target instanceof Model) {
				targetModel = (Model) target;
			}
			if (targetModel != null) {
				targetModel.getPackagedElements().add((PackageableElement)element);
			}

		}
		return semanticObjectToDrop;
	}

	/**
	 * Create a new Abstraction Link between the source and the target.
	 * @param context, the current context
	 * @param sourceview, the source view
	 * @param source, the semantic source element
	 * @param target, the semantic target element
	 * @return nothing
	 */
	public static void createAbstractionLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				Abstraction abstraction = UMLFactory.eINSTANCE.createAbstraction();
				model.getPackagedElements().add(abstraction);
				abstraction.getClients().add((NamedElement) source);
				abstraction.getSuppliers().add((NamedElement) target);
			}
		}
	}

	/**
	 * Get diagram root.
	 * @param context the current context (view)
	 * @return the root diagram owning the element
	 */
	private static EObject getDiagramRoot(EObject sourceView) {
		DDiagramElement diagramElement = (DDiagramElement) sourceView;
		DSemanticDiagram diagram = (DSemanticDiagram) diagramElement.getParentDiagram();
		EObject root = diagram.getTarget();
		return root;
	}

	/**
	 * Create a new Containment Link between the source and the target
	 * @param context the current context
	 * @param source, the source element
	 * @param target, the target element
	 * @return nothing
	 */
	public static void createContainmentLink(EObject context, Element source, Element target) {
		if (source instanceof Class) {
			Class sourceElement = (Class) source;
			Classifier targetElement = (Classifier) target;
			sourceElement.getNestedClassifiers().add(targetElement);
		} else if (source instanceof Package) {
			Package sourceElement = (Package) source;
			PackageableElement targetElement = (PackageableElement) target;
			sourceElement.getPackagedElements().add(targetElement);
		}

	}

	/**
	 * Precondition test if sirius diagram or not.
	 * @param context the current context
	 * @return true if context is a DDiagram
	 */
	public boolean isDDiagram(EObject self) {
		return self instanceof DDiagram;
	}

	/**
	 * Test if element is ClassNodeList.
	 * @param context the current context
	 * @return true if class node list mapping
	 */
	public boolean isClassNodeListContainer(EObject self) {
		if (self instanceof DNodeList) {
			DNodeList node = (DNodeList) self;
			return node.getTarget() instanceof Class;
		} else {
			return false;
		}
	}
	
	/**
	 * Test if element is ClassNodeContainer.
	 * @param context the current context
	 * @return true if class node container mapping
	 */
	public boolean isClassNodeContainer(EObject self) {
		if (self instanceof DNodeContainer) {
			DNodeContainer node = (DNodeContainer) self;
			return node.getTarget() instanceof Package;
		} else {
			return false;
		}
	}


	/**
	 * Get the target element for the containment link.
	 * @param context the current context
	 * @return containment link lists
	 */
	public static EList<?> getContainmentLinkTarget(Element source) {
		if (source instanceof Class) {
			Class sourceElement = (Class) source;
			return sourceElement.getNestedClassifiers();
		} else if (source instanceof Package) {
			Package sourceElement = (Package) source;
			return sourceElement.getPackagedElements();
		}

		return null;
	}

	/**
	 * Get the feature which will contain the element.
	 * @param context the current context
	 * @return the name of the feature
	 */
	public String getFeatureName(EObject context) {
		if (context instanceof Constraint) {
			return "ownedRule";
		} else if (context instanceof Comment) {
			return "ownedComment";
		}
		return "packagedElement";
	}

	/**
	 * Precondition check is data type.
	 * @param context the current context
	 * @return true is DataType and not enumeration or PrimitiveType
	 */
	public boolean isDataType(EObject context) {
		return !(context instanceof Enumeration || context instanceof PrimitiveType);
	}

	/**
	 * Get the feature to contain the element
	 * 
	 * @param context the current context
	 * @return the target feature
	 */
	public String getTypeTarget(EObject context) {
		if (context instanceof Class || context instanceof Interface) {
			return "nestedClassifier";
		}

		return "packagedElement";
	}

	/**
	 * Check if the oldcontainer was a class.
	 * 
	 * @param context the current context
	 * @param oldContainer the current context
	 * @return true if the source is a class or an interface
	 */
	public boolean ifSourceIsClass(EObject context, EObject oldContainer) {
		if (oldContainer instanceof Class || oldContainer instanceof Interface) {
			return true;
		}

		return false;
	}

	/**
	 * Get the feature to contain the element
	 * 
	 * @param context the current context
	 * @param newContainerView the new container view
	 * @return the target name
	 */
	public String getTypeTarget(EObject context, EObject newContainerView) {
		if (newContainerView instanceof Class || newContainerView instanceof Interface) {
			return "nestedClassifier";
		}

		return "packagedElement";
	}

	/**
	 * Check if the container is valid or not
	 * 
	 * @param context the current context
	 * @param newContainerView the new container view
	 * @return true if the container can contain the element to create
	 */
	public boolean isValidContainer(EObject context, EObject newContainerView) {
		if (newContainerView instanceof DSemanticDiagramSpec) {
			return context.eContainer().equals(((DSemanticDiagramSpec) newContainerView).getTarget());

		} else if (newContainerView instanceof DNodeContainerSpec)

		{
			return context.eContainer().equals(((DNodeContainerSpec) newContainerView).getTarget());

		}
		return false;
	}


	/**
	 * Check if the source and target are valid
	 * 
	 * @param context the current context
	 * @param sourceView the source view
	 * @param targetView the target  view
	 * @param source the semantic source element
	 * @param target the semantic target element
	 * @return true if the source and target are valid
	 */
	public boolean isValidSourecAndTarget(EObject context, EObject sourceView, EObject targetView, Element source,
			Element target) {
		return !targetView.eContainer().equals(sourceView);
	}

	/**
	 * Create a new Dependency Link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createDependencyLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				Dependency dependency = UMLFactory.eINSTANCE.createDependency();
				dependency.getClients().add((NamedElement) source);
				dependency.getSuppliers().add((NamedElement) target);

				if (source instanceof Class) {
					model.getPackagedElements().add(dependency);
				} else if (source instanceof Package) {
					((Package) source).getPackagedElements().add(dependency);
				}
			}
		}
	}

	/**
	 * Create a new Association Class Link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public AssociationClass createAssociationClassLink(EObject context, EObject sourceView, Element source,
			Element target) {
		AssociationClass associationClass = null;
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				associationClass = UMLFactory.eINSTANCE.createAssociationClass();
				model.getPackagedElements().add(associationClass);
				associationClass.setName(LabelServices.INSTANCE.computeDefaultName(associationClass));

				final Property end1 = AssociationServices.INSTANCE.createAssociationClassEnd((Type) source);
				associationClass.getMemberEnds().add(end1);
				final Property end2 = AssociationServices.INSTANCE.createAssociationClassEnd((Type) target);
				associationClass.getMemberEnds().add(end2);

				end1.setAssociation(associationClass);
				end2.setAssociation(associationClass);
				associationClass.getOwnedEnds().add(end1);
				associationClass.getOwnedEnds().add(end2);

				EAnnotation eAnnotation = associationClass.createEAnnotation(ANNOTATION_GENERIC_SOURCE);
				eAnnotation.getDetails().put(ANNOTATION_DETAIL_KEY, ANNOTATION_DETAIL_VALUE);
				associationClass.getEAnnotations().add(eAnnotation);
			}
		}
		return associationClass;
	}

	/**
	 * Create a new Element Import Link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createElementImportLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				PackageableElement sourceElement = (PackageableElement) source;
				PackageableElement targetElement = (PackageableElement) target;
				ElementImport elemImport = UMLFactory.eINSTANCE.createElementImport();
				elemImport.setImportedElement(targetElement);
				((Namespace) sourceElement).getElementImports().add(elemImport);
			}
		}
	}

	/**
	 * Create a new Generalization Link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createGeneralizationLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Classifier sourceElement = (Classifier) source;
				Classifier targetElement = (Classifier) target;
				Generalization generalization = UMLFactory.eINSTANCE.createGeneralization();
				generalization.setGeneral(targetElement);
				sourceElement.getGeneralizations().add(generalization);
			}
		}
	}

	/**
	 * Create a new Information Flow Link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createInformationFlowLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				InformationFlow informationFlow = UMLFactory.eINSTANCE.createInformationFlow();
				informationFlow.getInformationSources().add((NamedElement) source);
				informationFlow.getInformationTargets().add((NamedElement) target);

				if (source instanceof Classifier) {
					model.getPackagedElements().add(informationFlow);
				} else if (source instanceof Package) {
					((Package) source).getPackagedElements().add(informationFlow);
				}
			}
		}
	}

	/**
	 * Check if the target element is an Interface or not.
	 */
	public boolean isNotInterfaceTarget(EObject elem) {
		return elem instanceof InterfaceRealization;
	}

	/**
	 * Create a new Interface Realization Link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public InterfaceRealization createInterfaceRealizationLink(EObject context, EObject sourceView, Element source,
			Element target) {
		InterfaceRealization interfaceRealization = null;
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				if (source instanceof Class && target instanceof Interface) {
					Class sourceElement = (Class) source;
					Interface targetElement = (Interface) target;
					interfaceRealization = UMLFactory.eINSTANCE.createInterfaceRealization();
					interfaceRealization.getClients().add(sourceElement);
					interfaceRealization.getSuppliers().add(targetElement);
					interfaceRealization.setContract(targetElement);
					sourceElement.getInterfaceRealizations().add(interfaceRealization);
				}
			}
		}
		return interfaceRealization;
	}

	/**
	 * Check if the current link type is corresponding to the linkTypeName.
	 */
	public boolean isCurrentLinkType(EObject elem, String linkTypeName) {
		return elem.getClass().getSimpleName().equalsIgnoreCase(linkTypeName);
	}

	/**
	 * Create a new Realization link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createRealizationLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				Realization realization = UMLFactory.eINSTANCE.createRealization();
				realization.getClients().add((NamedElement) source);
				realization.getSuppliers().add((NamedElement) target);

				if (source instanceof Package) {
					((Package) source).getPackagedElements().add(realization);
				} else {
					model.getPackagedElements().add(realization);
				}
			}
		}
	}

	/**
	 * Create a new Substitution link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createSubstitutionLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Classifier sourceElement = (Classifier) source;
				Classifier targetElement = (Classifier) target;
				Substitution substitution = UMLFactory.eINSTANCE.createSubstitution();
				substitution.getClients().add(sourceElement);
				substitution.getSuppliers().add(targetElement);
				substitution.setContract(targetElement);
				((Classifier) sourceElement).getSubstitutions().add(substitution);
			}
		}
	}

	/**
	 * Create a new Package Import link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createPackageImportLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package sourceElement = (Package) source;
				Package targetElement = (Package) target;
				PackageImport packageImport = UMLFactory.eINSTANCE.createPackageImport();
				packageImport.setImportedPackage(targetElement);
				sourceElement.getPackageImports().add(packageImport);
			}
		}

	}

	/**
	 * Create a new Package Merge link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createPackageMergeLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package sourceElement = (Package) source;
				Package targetElement = (Package) target;
				PackageMerge packageMerge = UMLFactory.eINSTANCE.createPackageMerge();
				packageMerge.setMergedPackage(targetElement);
				sourceElement.getPackageMerges().add(packageMerge);
			}
		}

	}

	/**
	 * Create a new Usage link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createUsageLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				NamedElement sourceElement = (NamedElement) source;
				NamedElement targetElement = (NamedElement) target;
				Usage usage = UMLFactory.eINSTANCE.createUsage();
				usage.getClients().add(sourceElement);
				usage.getSuppliers().add(targetElement);

				if (source instanceof Package) {
					((Package) source).getPackagedElements().add(usage);
				} else {
					model.getPackagedElements().add(usage);
				}
			}
		}

	}

	/**
	 * Create a new Generalization Set link.
	 * @param sourceView the source view
	 * @param target the semantic target element
	 * @param targetView the target  view
	 */
	public void createGeneralizationSetLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				Generalization generalization1 = (Generalization) source;
				Generalization generalization2 = (Generalization) target;

				GeneralizationSet generalizationSet = UMLFactory.eINSTANCE.createGeneralizationSet();
				model.getPackagedElements().add(generalizationSet);

				// build name
				String name = LabelServices.INSTANCE.computeDefaultName(generalizationSet);
				String firstGeneralisationClassName = ((Classifier) generalization1.getGeneral()).getName();
				String secondGeneralisationClassName = ((Classifier) generalization2.getGeneral()).getName();
				name = name + UNDERSCORE + firstGeneralisationClassName + UNDERSCORE + secondGeneralisationClassName;
				generalizationSet.setName(name);

				generalizationSet.getGeneralizations().add(generalization1);
				generalizationSet.getGeneralizations().add(generalization2);
			}
		}
	}

	/**
	 * Get the source element of the Generalization link.
	 */
	public EObject getSourceGeneralization(EObject elem) {
		if (elem instanceof GeneralizationSet) {
			GeneralizationSet generalizationSet = (GeneralizationSet) elem;
			return generalizationSet.getGeneralizations().get(0);
		}

		return null;
	}

	/**
	 * Get the target element of the Generalization link.
	 */
	public EObject getTargetGeneralization(EObject elem) {
		if (elem instanceof GeneralizationSet) {
			GeneralizationSet generalizationSet = (GeneralizationSet) elem;
			return generalizationSet.getGeneralizations().get(1);
		}

		return null;
	}

	/**
	 * Create a new Link link.
	 * @param sourceView the source view
	 * @param source the semantic source element
	 * @param target the semantic target element
	 */
	public void createLink(EObject context, EObject sourceView, Element source, Element target) {
		if (source instanceof Comment) {
			((Comment) source).getAnnotatedElements().add(target);
		} else if (source instanceof Constraint) {
			((Constraint) source).getConstrainedElements().add(target);
		}
		if (source instanceof TimeObservation && !(target instanceof Comment)) {
			((TimeObservation) source).setEvent((NamedElement) target);
		} else if (source instanceof DurationObservation && !(target instanceof Comment)) {
			((DurationObservation) source).getEvents().add((NamedElement) target);
		}
	}

	/**
	 * Get the target element of the Link link.
	 */
	public static EList<?> getLinkTarget(Element source) {
		EList<NamedElement> target = new BasicEList<NamedElement>();

		if (source instanceof Constraint) {
			Constraint sourceElement = (Constraint) source;
			return sourceElement.getConstrainedElements();
		} else if (source instanceof Comment) {
			Comment sourceElement = (Comment) source;
			return sourceElement.getAnnotatedElements();
		} else if (source instanceof TimeObservation) {
			TimeObservation sourceElement = (TimeObservation) source;
			target.add(sourceElement.getEvent());
			return target;
		} else if (source instanceof DurationObservation) {
			DurationObservation sourceElement = (DurationObservation) source;
			return sourceElement.getEvents();
		}

		return null;
	}

	/**
	 * Get the target element of the Link link.
	 * @param source the semantic source element
	 * @param target the semantic target element
	 */
	public boolean isNotObservationSourceWithCommentTarget(EObject context, Element source, Element target) {
		if ((source instanceof TimeObservation || source instanceof DurationObservation) && target instanceof Comment) {
			return false;
		}

		return true;
	}

	/**
	 * Get the Constraint label.
	 */
	public String getConstraintLabel(Element elem) {
		StringBuilder constLabel = new StringBuilder();
		String body = "";
		String lang = "";
		if (elem instanceof Constraint) {
			Constraint constraint = ((Constraint) elem);
			ValueSpecification valueSpec = constraint.getSpecification();
			if (valueSpec instanceof OpaqueExpression) {
				OpaqueExpression opaqueEsp = (OpaqueExpression) valueSpec;
				if (!opaqueEsp.getBodies().isEmpty()) {
					body = opaqueEsp.getBodies().get(0);
				}
				if (!opaqueEsp.getLanguages().isEmpty()) {
					lang = opaqueEsp.getLanguages().get(0);
				}

				List<Stereotype> appliedStereoTypes = constraint.getAppliedStereotypes();
				if (!appliedStereoTypes.isEmpty()) {
					constLabel.append("<<");
				}
				for (int i = 0; i < constraint.getAppliedStereotypes().size(); i++) {
					Stereotype stereoType = appliedStereoTypes.get(i);
					constLabel.append(stereoType.getName());
					if (i + 1 == appliedStereoTypes.size()) {
						constLabel.append(">>");
					} else {
						constLabel.append(",");
					}
				}
				constLabel.append(constraint.getName());
				constLabel.append(System.getProperty("line.separator"));
				constLabel.append("{{" + lang + "} " + body + "}");
			}
		}

		return constLabel.toString();
	}


	/**
	 * Get the Constraint text body.
	 */
	public String getBody(EObject elem) {
		Constraint constraint = ((Constraint) elem);
		ValueSpecification valueSpec = constraint.getSpecification();
		if (valueSpec instanceof OpaqueExpression) {
			OpaqueExpression opaqueEsp = (OpaqueExpression) valueSpec;
			if (!opaqueEsp.getBodies().isEmpty()) {
				return opaqueEsp.getBodies().get(0);
			}
		}
		return "";
	}

	/**
	 * Compute label for InformationItem
	 * 
	 * @param elem the current InformationItem element
	 * @return the label of InformationItem element
	 */
	public String buildLabel(Element elem) {
		StringBuilder labelBuilder = new StringBuilder();
		String name = LabelServices.INSTANCE.computeUmlLabel(elem);
		String prefix = elem.getClass().getSimpleName();
		int endIndex = prefix.indexOf("Impl");
		if (endIndex != -1) {
			prefix = ITEM_LABEL_PREFIX_START_TAG + prefix.substring(0, endIndex) + ITEM_LABEL_PREFIX_END_TAG;
		}

		if (!name.startsWith(prefix)) {
			labelBuilder.append(prefix);
			labelBuilder.append(System.getProperty("line.separator"));
			labelBuilder.append(name);
			return labelBuilder.toString();
		}

		return name;
	}

	/**
	 * Get the symbol path for decorator
	 * 
	 * @param elem the current  element
	 * @return the path of symbol to use
	 */

	public String GetSymbolPath(Element elem) {
		if (elem instanceof TimeObservation) {
			return TIME_OBSERVATION_SYMBOL_PATH;
		}
		return DURATION_OBSERVATION_SYMBOL_PATH;

	}

	/**
	 * Set the Constraint body.
	 */
	public void setConstraintBody(Element elem, String bodyValue) {
		if (elem instanceof Constraint) {
			ValueSpecification valueSpec = ((Constraint) elem).getSpecification();
			if (valueSpec instanceof OpaqueExpression) {
				OpaqueExpression opaqueEsp = (OpaqueExpression) valueSpec;

				if (!opaqueEsp.getBodies().isEmpty()) {
					opaqueEsp.getBodies().remove(0);
					opaqueEsp.getBodies().add(0, bodyValue);
				}
			}
		}
	}

	/**
	 * Create a new Instance Specification link.
	 * @param context the context element
	 * @param sourceView the source view
	 * @param source the semantic source element
	 * @param target the semantic target element
	 * @return the instance specification to create
	 */
	public InstanceSpecification createInstanceSpecification(EObject context, EObject sourceView, Element source,
			Element target) {
		JDialog dialog = new JDialog();
		GridLayout layout = new GridLayout(3, 1);
		layout.setVgap(5);
		dialog.setLayout(layout);
		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		dialog.setTitle("AssociationSelection");
		dialog.setSize(new Dimension(350, 200));

		dialog.add(new JLabel("Select the association for this instanceSpecification"));

		// create OK button for the dialog
		JButton okButton = new JButton(OK_LABEL);
		okButton.setEnabled(false);

		// create associationType list for the dialog
		DefaultListModel<String> assosTypesList = new DefaultListModel<String>();
		assosTypesList.addElement("<untyped>");
		JList<String> list = new JList<String>(assosTypesList);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					_selectedAssosType = list.getSelectedValue();
					okButton.setEnabled(true);
				}
			}
		});

		dialog.add(list);

		// add listener to OK button
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (sourceView instanceof DDiagramElement) {
					EObject root = getDiagramRoot(sourceView);
					if (root instanceof Package) {
						if (source instanceof InstanceSpecification && target instanceof InstanceSpecification) {
							_instanceSpec = UMLFactory.eINSTANCE.createInstanceSpecification();
							EAnnotation endtypes = _instanceSpec.createEAnnotation(ANNOTATION_INSTANCE_END_SOURCE);
							endtypes.getReferences().add(source);
							endtypes.getReferences().add(target);
						}
					}
				}
				dialog.setVisible(false);

			}
		});

		// create Cancel button for the dialog
		JButton cancelButton = new JButton(CANCEL_LABEL);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);

			}
		});

		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new GridLayout(1, 2));
		buttonsPane.add(okButton);
		buttonsPane.add(cancelButton);
		dialog.add(buttonsPane);
		dialog.setVisible(true);

		return _instanceSpec;
	}

	/**
	 * Add the new created instance specification to the model
	 * 
	 * @param context    the new created instance specification
	 * @param sourceView the source view
	 */
	public void addInstanceSpecificationToModel(EObject context, EObject sourceView) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				InstanceSpecification createdInstanceSpec = (InstanceSpecification) context;
				model.getPackagedElements().add(createdInstanceSpec);
				createdInstanceSpec.setName(LabelServices.INSTANCE.computeDefaultName(createdInstanceSpec));
			}
		}
	}

	/**
	 * Service used to determine if the selected containment edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectContainmentLinkPrecondition(Element context, Element target) {
		return (target instanceof Class || target instanceof Interface || target instanceof Model
				|| target instanceof Package || target instanceof DataType);
	}

	/**
	 * Service used to determine if the selected Dependency edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectDependencyLinkPrecondition(Element context, Element target) {
		return (target instanceof Class || target instanceof Package || target instanceof Interface
				|| target instanceof Enumeration || target instanceof PrimitiveType);
	}

	/**
	 * Service used to determine if the selected ElementImport edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectElementImportLinkPrecondition(Element context, Element target) {
		return (target instanceof Class || target instanceof Package || target instanceof Interface
				|| target instanceof Enumeration || target instanceof PrimitiveType);
	}

	/**
	 * Service used to determine if the selected Generalization edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectGeneralizationLinkPrecondition(Element context, Element target) {
		return (target instanceof Class || target instanceof Interface || target instanceof Enumeration
				|| target instanceof PrimitiveType);
	}

	/**
	 * Service used to determine if the selected GeneralizationSet edge target could
	 * be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectGeneralizationSetLinkPrecondition(Element context, Element target) {
		return target instanceof Generalization;
	}

	/**
	 * Service used to determine if the selected InformationFlow edge target could
	 * be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectInformationFlowLinkPrecondition(Element context, Element target) {
		return (target instanceof Class || target instanceof Package || target instanceof Interface
				|| target instanceof Enumeration || target instanceof PrimitiveType);
	}

	/**
	 * Service used to determine if the selected InstanceSpecification edge target
	 * could be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectInstanceSpecLinkPrecondition(Element context, Element target) {
		return target instanceof InstanceSpecification;
	}

	/**
	 * Service used to determine if the selected PackageImport/PackageMerge edge
	 * source could be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newSource  Represents the source element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectPackageImportMergeLinkSourcePrecondition(Element context, Element newSource) {
		return newSource instanceof Package && !(newSource instanceof Model);
	}

	/**
	 * Service used to determine if the selected Realization edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newSource  Represents the source element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectRealizationLinkPrecondition(Element context, Element newSource) {
		return newSource instanceof Class || newSource instanceof Interface || newSource instanceof Enumeration
				|| newSource instanceof PrimitiveType || newSource instanceof Package
				|| newSource instanceof InstanceSpecification;
	}

	/**
	 * Service used to determine if the selected Substitution edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newSource  Represents the source element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectSubstitutionLinkPrecondition(Element context, Element newSource) {
		return newSource instanceof Class || newSource instanceof Interface || newSource instanceof Enumeration
				|| newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected InterfaceRealization edge source
	 * could be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newSource  Represents the source element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectInterfaceRealizationLinkSourcePrecondition(Element context, Element newSource) {
		return newSource instanceof Class;
	}

	/**
	 * Service used to determine if the selected InterfaceRealization edge target
	 * could be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newTarget  Represents the target element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectInterfaceRealizationLinkTargetPrecondition(Element context, Element newTarget) {
		return newTarget instanceof Interface;
	}

	/**
	 * Service used to determine if the selected Usage edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newSource  Represents the source element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectUsageLinkPrecondition(Element context, Element newSource) {
		return newSource instanceof Class || newSource instanceof Package || newSource instanceof Interface
				|| newSource instanceof Enumeration || newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected Link edge source could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newSource  Represents the source element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectLinkSourcePrecondition(Element context, Element newSource) {
		return newSource instanceof Constraint || newSource instanceof Comment || newSource instanceof Observation;
	}

	/**
	 * Service used to determine if the selected Link edge target could be
	 * reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newSource  Represents the source element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectLinkTargetPrecondition(Element context, Element newSource) {
		return newSource instanceof Constraint || newSource instanceof Comment || newSource instanceof Observation
				|| newSource instanceof PrimitiveType || newSource instanceof Enumeration
				|| newSource instanceof Package || newSource instanceof Interface || newSource instanceof Class;
	}

	/**
	 * Service used to determine if the selected PackageImport/PackageMerge edge
	 * target could be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param newTarget  Represents the target element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectPackageImportMergeLinkTargetPrecondition(Element context, Element newtarget) {
		return newtarget instanceof Package;
	}

	/**
	 * Service used to determine if the selected Association edge source/target
	 * could be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectAssociationLinkPrecondition(Element context, Element target) {
		return target instanceof Class || target instanceof Interface || target instanceof Enumeration
				|| target instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected Abstraction edge source/target
	 * could be reconnected to an element.
	 *
	 * @param context Element attached to the existing edge
	 * @param target  Represents the semantic element pointed by the edge after
	 *                reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectAbstractionLinkPrecondition(Element context, Element target) {
		return target instanceof Class || target instanceof Interface || target instanceof Enumeration
				|| target instanceof PrimitiveType;
	}

	/**
	 * Service used to reconnect a Containment edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param sourceView Represents the graphical element pointed by the edge before
	 *                   reconnecting
	 * @param targetView Represents the graphical element pointed by the edge after
	 *                   reconnecting
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectContainmentEdgeSource(Element context, DEdge edgeView, EdgeTarget sourceView,
			EdgeTarget targetView, Element oldSource, Element newSource) {
		if (oldSource instanceof Class) // if the old source is a Class
		{
			// remove the target from the old source class and add it to the new source
			// class
			Classifier target = (Classifier) ((DNodeContainerSpec) edgeView.getTargetNode()).getTarget();
			((Class) oldSource).getNestedClassifiers().remove(target);
			if (newSource instanceof Class) {
				((Class) newSource).getNestedClassifiers().add(target);
			} else if (newSource instanceof Package) {
				((Package) newSource).getPackagedElements().add(target);
			}
		} else if (oldSource instanceof Package)// if the old source is a Package
		{
			// remove the target from the old source package and add it to the new source
			// package
			PackageableElement target = (PackageableElement) ((DNodeContainerSpec) edgeView.getTargetNode())
					.getTarget();
			((Package) oldSource).getPackagedElements().remove(target);

			if (newSource instanceof Class) {
				((Class) newSource).getNestedClassifiers().add((Classifier) target);
			} else if (newSource instanceof Package) {
				((Package) newSource).getPackagedElements().add(target);
			}
		}
	}

	/**
	 * Service used to reconnect a Containment edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param sourceView Represents the graphical element pointed by the edge before
	 *                   reconnecting
	 * @param targetView Represents the graphical element pointed by the edge after
	 *                   reconnecting
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectContainmentEdgeTarget(Element context, DEdge edgeView, EdgeTarget sourceView,
			EdgeTarget targetView, Element oldTarget, Element newTarget) {
		// get the root model of the diagram
		Model rootModel = getRootModel(oldTarget);

		// if the old target element is a Class
		if (oldTarget instanceof Class) {
			// remove old target from the class source and add the new target
			Class source = (Class) ((DNodeContainerSpec) edgeView.getSourceNode()).getTarget();
			((Class) source).getNestedClassifiers().remove(oldTarget);
			((Class) source).getNestedClassifiers().add((Classifier) newTarget);
		}
		// if the old target element is a Package
		else if (oldTarget instanceof Package) {
			// remove old target from the package source and add the new target
			Package source = (Package) ((DNodeContainerSpec) edgeView.getSourceNode()).getTarget();
			source.getPackagedElements().remove(oldTarget);
			source.getPackagedElements().add((PackageableElement) newTarget);
		}

		// keep the old target (Class or Package) in the root model
		if (oldTarget instanceof Class) {
			rootModel.getPackagedElements().add((Class) oldTarget);
		} else if (oldTarget instanceof Package) {
			rootModel.getPackagedElements().add((Package) oldTarget);
		}
	}

	/**
	 * Get the root model of the diagram
	 */
	private Model getRootModel(Element element) {
		Model currentModel = element.getModel();
		if (!element.equals(currentModel)) {
			currentModel = getRootModel(currentModel);
		}

		return currentModel;
	}

	/**
	 * Service used to reconnect a Dependency edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectDependencyEdgeSource(Element context, Element oldSource, Element newSource) {
		// get the root model of the diagram
		Model rootModel = getRootModel(oldSource);

		// remove the old source from the Dependency edge and to it the new source
		Dependency dependencyEdge = (Dependency) context;
		dependencyEdge.getClients().remove(oldSource);
		dependencyEdge.getClients().add((NamedElement) newSource);

		if (oldSource instanceof Package && !(newSource instanceof Package)) {
			// remove the dependencyEdge from the old source Package and keep it in the root
			// model
			((Package) oldSource).getPackagedElements().remove(dependencyEdge);
			rootModel.getPackagedElements().add(dependencyEdge);
		} else if (newSource instanceof Package && !(oldSource instanceof Package)) {
			// add the dependencyEdge to the new source Package and remove it from the root
			// model
			((Package) newSource).getPackagedElements().add(dependencyEdge);
			rootModel.getPackagedElements().remove(dependencyEdge);
		}
	}

	/**
	 * Service used to reconnect a Containment edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param sourceView Represents the graphical element pointed by the edge before
	 *                   reconnecting
	 * @param targetView Represents the graphical element pointed by the edge after
	 *                   reconnecting
	 * @param source     Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param target     Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectDependencyEdgeTarget(Element context, DEdge edgeView, EdgeTarget sourceView,
			EdgeTarget targetView, Element source, Element target) {
		Dependency dependencyEdge = (Dependency) context;
		dependencyEdge.getSuppliers().remove(source);
		dependencyEdge.getSuppliers().add((NamedElement) target);
	}

	/**
	 * Service used to reconnect an ElementImport edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param sourceView Represents the graphical element pointed by the edge before
	 *                   reconnecting
	 * @param targetView Represents the graphical element pointed by the edge after
	 *                   reconnecting
	 * @param source     Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param target     Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectElementImportEdgeSource(Element context, Element source, Element target) {
		ElementImport elementImportEdge = (ElementImport) context;
		((Namespace) source).getElementImports().remove(elementImportEdge);
		((Namespace) target).getElementImports().add(elementImportEdge);
	}

	/**
	 * Service used to reconnect an ElementImport edge target.
	 *
	 * @param context    Element attached to the existing edge
	 *                   reconnecting
	 * @param target     Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectElementImportEdgeTarget(Element context, Element source, Element target) {
		ElementImport elementImportEdge = (ElementImport) context;
		elementImportEdge.setImportedElement((PackageableElement) target);
	}

	/**
	 * Service used to reconnect a Generalization edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param source     Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param target     Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectGeneralizationEdgeSource(Element context, Element source, Element target) {
		Generalization generalizationEdge = (Generalization) context;
		((Classifier) source).getGeneralizations().remove(generalizationEdge);
		((Classifier) target).getGeneralizations().add(generalizationEdge);
	}

	/**
	 * Service used to reconnect a Generalization edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param source     Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param target     Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectGeneralizationEdgeTarget(Element context, Element source, Element target) {
		Generalization generalizationEdge = (Generalization) context;
		generalizationEdge.setGeneral((Classifier) target);
	}

	/**
	 * Service used to reconnect a GeneralizationSet edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectGeneralizationSetEdgeSource(Element context, Element oldSource, Element newSource) {
		// get the old and new source generalization elements
		GeneralizationSet generalizationSet = (GeneralizationSet) context;
		Generalization oldSourceGeneralization = (Generalization) oldSource;
		Generalization newSourceGeneralization = (Generalization) newSource;

		// delete the old source generalization from the generalizations set and add the
		// new source one
		generalizationSet.getGeneralizations().add(0, newSourceGeneralization);
		generalizationSet.getGeneralizations().remove(oldSource);

		// build new name for the generalization set
		String newSourceName = newSourceGeneralization.getGeneral().getName();
		String oldSourceName = oldSourceGeneralization.getGeneral().getName();

		String newName = generalizationSet.getName().replace(oldSourceName, newSourceName);
		generalizationSet.setName(newName);
	}

	/**
	 * Service used to reconnect a GeneralizationSet edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectGeneralizationSetEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		GeneralizationSet generalizationSet = (GeneralizationSet) context;
		Generalization oldTargetGeneralization = (Generalization) oldTarget;
		Generalization newTargetGeneralization = (Generalization) newTarget;

		generalizationSet.getGeneralizations().add(1, newTargetGeneralization);
		generalizationSet.getGeneralizations().remove(oldTargetGeneralization);

		// build new name
		String newTargetName = newTargetGeneralization.getGeneral().getName();
		String oldTargetName = oldTargetGeneralization.getGeneral().getName();

		String newName = generalizationSet.getName().replace(oldTargetName, newTargetName);
		generalizationSet.setName(newName);
	}

	/**
	 * Service used to reconnect an InformationFlow edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectInformationFlowEdgeSource(Element context, Element oldSource, Element newSource) {
		// get the root model of the diagram
		Model rootModel = getRootModel(oldSource);

		// remove the old source from the information flow element and add to it the new
		// source
		InformationFlow informationFlow = (InformationFlow) context;
		informationFlow.getInformationSources().remove(oldSource);
		informationFlow.getInformationSources().add((NamedElement) newSource);

		// keep the removed old source in the root model
		if (oldSource instanceof Package && !(newSource instanceof Package)) {
			((Package) oldSource).getPackagedElements().remove(informationFlow);
			rootModel.getPackagedElements().add(informationFlow);
		} else if (newSource instanceof Package && !(oldSource instanceof Package)) {
			((Package) newSource).getPackagedElements().add(informationFlow);
			rootModel.getPackagedElements().remove(informationFlow);
		}
	}

	/**
	 * Service used to reconnect an informationFlow edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectInformationFlowEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		InformationFlow informationFlow = (InformationFlow) context;
		informationFlow.getInformationTargets().remove(oldTarget);
		informationFlow.getInformationTargets().add((NamedElement) newTarget);
	}

	/**
	 * Service used to reconnect an InstanceSpecifictaion edge source/target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectInstanceSpecEdge(Element context, Element oldSource, Element newSource) {
		InstanceSpecification instanceSpec = (InstanceSpecification) context;
		EAnnotation eAnnotation = instanceSpec.getEAnnotations().get(0);
		eAnnotation.getReferences().remove(oldSource);
		eAnnotation.getReferences().add((InstanceSpecification) newSource);
	}

	/**
	 * Service used to reconnect an PacakgeImport edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectPackageImportEdgeSource(Element context, Element oldSource, Element newSource) {
		PackageImport packageImport = (PackageImport) context;
		((Package) oldSource).getPackageImports().remove(packageImport);
		((Package) newSource).getPackageImports().add(packageImport);
	}

	/**
	 * Service used to reconnect a PacakgeImport edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectPackageImportEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		PackageImport packageImport = (PackageImport) context;
		packageImport.setImportedPackage((Package) newTarget);
	}

	/**
	 * Service used to reconnect a PackageMerge edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectPackageMergeEdgeSource(Element context, Element oldSource, Element newSource) {
		PackageMerge packageMerged = (PackageMerge) context;
		((Package) oldSource).getPackageMerges().remove(packageMerged);
		((Package) newSource).getPackageMerges().add(packageMerged);
	}

	/**
	 * Service used to reconnect a PackageMerge edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectPackageMergeEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		PackageMerge packageMerged = (PackageMerge) context;
		packageMerged.setMergedPackage((Package) newTarget);
	}

	/**
	 * Service used to reconnect a Realization edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */

	public void reconnectRealizationEdgeSource(Element context, Element oldSource, Element newSource) {
		// get the root model of the diagram
		Model rootModel = getRootModel(oldSource);

		// remove the old source from the realization element and add the new source
		Realization realizationEdge = (Realization) context;
		realizationEdge.getClients().remove(oldSource);
		realizationEdge.getClients().add((NamedElement) newSource);

		// keep the removed old source in the root model
		if (oldSource instanceof Package && !(newSource instanceof Package)) {
			((Package) oldSource).getPackagedElements().remove(realizationEdge);
			rootModel.getPackagedElements().add(realizationEdge);
		} else if (newSource instanceof Package && !(oldSource instanceof Package)) {
			((Package) newSource).getPackagedElements().add(realizationEdge);
			rootModel.getPackagedElements().remove(realizationEdge);
		}
	}

	/**
	 * Service used to reconnect a Realization edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectRealizationEdgeTarget(Element context, Element source, Element target) {
		Realization realizationEdge = (Realization) context;
		realizationEdge.getSuppliers().remove(source);
		realizationEdge.getSuppliers().add((NamedElement) target);
	}

	/**
	 * Service used to reconnect a Substitution edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectSubstitutionEdgeSource(Element context, Element oldSource, Element newSource) {
		Substitution substitutionEdge = (Substitution) context;
		substitutionEdge.getClients().remove(oldSource);
		substitutionEdge.getClients().add((NamedElement) newSource);

		((Classifier) oldSource).getSubstitutions().remove(substitutionEdge);
		((Classifier) newSource).getSubstitutions().add(substitutionEdge);
	}

	/**
	 * Service used to reconnect a Substitution edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectSubstitutionEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		Substitution substitutionEdge = (Substitution) context;
		substitutionEdge.getSuppliers().remove(oldTarget);
		substitutionEdge.getSuppliers().add((NamedElement) newTarget);
		substitutionEdge.setContract((Classifier) newTarget);
	}

	/**
	 * Service used to reconnect a InterfaceRealization edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectInterfaceRealizationEdgeSource(Element context, Element oldSource, Element newSource) {
		InterfaceRealization interfaceRealizationEdge = (InterfaceRealization) context;
		interfaceRealizationEdge.getClients().remove(oldSource);
		interfaceRealizationEdge.getClients().add((NamedElement) newSource);

		((Class) oldSource).getInterfaceRealizations().remove(interfaceRealizationEdge);
		((Class) newSource).getInterfaceRealizations().add(interfaceRealizationEdge);
	}

	/**
	 * Service used to reconnect a InterfaceRealization edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectInterfaceRealizationEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		InterfaceRealization interfaceRealizationEdge = (InterfaceRealization) context;
		interfaceRealizationEdge.getSuppliers().remove(oldTarget);
		interfaceRealizationEdge.getSuppliers().add((NamedElement) newTarget);
		interfaceRealizationEdge.setContract((Interface) newTarget);
	}

	/**
	 * Service used to reconnect a Usage edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectUsageEdgeSource(Element context, Element oldSource, Element newSource) {
		// get the root model of the diagram
		Model rootModel = getRootModel(oldSource);

		// remove the old source from the Usage element and the new source
		Usage usageEdge = (Usage) context;
		usageEdge.getClients().remove(oldSource);
		usageEdge.getClients().add((NamedElement) newSource);

		// keep the removed old source in the root model
		if (oldSource instanceof Package && !(newSource instanceof Package)) {
			((Package) oldSource).getPackagedElements().remove(usageEdge);
			rootModel.getPackagedElements().add(usageEdge);
		} else if (newSource instanceof Package && !(oldSource instanceof Package)) {
			((Package) newSource).getPackagedElements().add(usageEdge);
			rootModel.getPackagedElements().remove(usageEdge);
		}
	}

	/**
	 * Service used to reconnect a Usage edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param source     Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param target     Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectUsageEdgeTarget(Element context, Element source, Element target) {
		Usage usageEdge = (Usage) context;
		usageEdge.getSuppliers().remove(source);
		usageEdge.getSuppliers().add((NamedElement) target);
	}

	/**
	 * Service used to reconnect a Link edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param oldsource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectLinkEdgeSource(Element context, DEdge edgeView, Element oldSource, Element newSource) {
		Element target = (Element) ((DNodeContainerSpec) edgeView.getTargetNode()).getTarget();

		// remove the target from the old source
		if (oldSource instanceof Comment) {
			((Comment) oldSource).getAnnotatedElements().remove(target);
		} else if (oldSource instanceof Constraint) {
			((Constraint) oldSource).getConstrainedElements().remove(target);
		} else if (oldSource instanceof TimeObservation) {
			((TimeObservation) oldSource).setEvent(null);
		} else if (oldSource instanceof DurationObservation) {
			((DurationObservation) oldSource).getEvents().remove((NamedElement) target);
		}

		// add the target to the new source
		if (newSource instanceof Comment) {
			((Comment) newSource).getAnnotatedElements().add(target);
		} else if (newSource instanceof Constraint) {
			((Constraint) newSource).getConstrainedElements().add(target);
		} else if (newSource instanceof TimeObservation && !(target instanceof Comment)) {
			((TimeObservation) newSource).setEvent((NamedElement) target);
		} else if (newSource instanceof DurationObservation && !(target instanceof Comment)) {
			((DurationObservation) newSource).getEvents().add((NamedElement) target);
		}
	}

	/**
	 * Service used to reconnect a Link edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectLinkEdgeTarget(Element context, DEdge edgeView, Element oldTarget, Element newTarget) {
		Element source = (Element) ((DNodeContainerSpec) edgeView.getSourceNode()).getTarget();

		if (source instanceof Comment) {
			((Comment) source).getAnnotatedElements().remove(oldTarget);
			((Comment) source).getAnnotatedElements().add(newTarget);
		} else if (source instanceof Constraint) {
			((Constraint) source).getConstrainedElements().remove(oldTarget);
			((Constraint) source).getConstrainedElements().add(newTarget);
		} else if (source instanceof TimeObservation && !(newTarget instanceof Comment)) {
			((TimeObservation) source).setEvent((NamedElement) newTarget);
		} else if (source instanceof DurationObservation && !(newTarget instanceof Comment)) {
			((DurationObservation) source).getEvents().remove((NamedElement) oldTarget);
			((DurationObservation) source).getEvents().add((NamedElement) newTarget);
		}
	}

	/**
	 * Service used to reconnect a Abstraction edge source.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectAbstractionEdgeSource(Element context, Element oldSource, Element newSource) {
		Abstraction abstractionEdge = (Abstraction) context;
		abstractionEdge.getClients().remove(oldSource);
		abstractionEdge.getClients().add((NamedElement) newSource);
	}

	/**
	 * Service used to reconnect a Abstraction edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectAbstractionEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		Abstraction abstractionEdge = (Abstraction) context;
		abstractionEdge.getSuppliers().remove(oldTarget);
		abstractionEdge.getSuppliers().add((NamedElement) newTarget);
	}

	/**
	 * Service used to reconnect a Link edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param oldSource  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newSource  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectAssociationEdgeSource(Element context, DEdge edgeView, Element oldSource, Element newSource) {
		// if reconnect the source of an AssociationClass edge
		if (context instanceof AssociationClass) {
			// set the ownedEnd to the new source
			for (Property ownedEnd : ((AssociationClass) context).getOwnedEnds()) {
				if (ownedEnd.getType().equals(oldSource)) {
					ownedEnd.setType((Type) newSource);
					ownedEnd.setName(((Type) newSource).getName());
					break;
				}
			}
		} else // if reconnect the source of an Association edge
		{
			// get the target of the edge
			Element target = null;
			if (edgeView.getTargetNode() instanceof DNodeListSpec) {
				target = (Element) ((DNodeListSpec) edgeView.getTargetNode()).getTarget();
			} else if (edgeView.getTargetNode() instanceof DNodeContainerSpec)

			{
				target = (Element) ((DNodeContainerSpec) edgeView.getTargetNode()).getTarget();
			}

			// find the Attribute in the old source to be modified
			EList<Property> attributes = null;
			if (oldSource instanceof Class) {
				attributes = ((Class) oldSource).getOwnedAttributes();
			} else if (oldSource instanceof Interface) {
				attributes = ((Interface) oldSource).getOwnedAttributes();
			} else if (oldSource instanceof DataType) {
				attributes = ((DataType) oldSource).getOwnedAttributes();
			}

			// remove attribute from the old source
			Property modifiedAttribute = findAttributToBeModified(attributes, (Element) target);
			attributes.remove(modifiedAttribute);

			// add attribute to the new source
			if (newSource instanceof Class) {
				((Class) newSource).getOwnedAttributes().add(modifiedAttribute);
			} else if (newSource instanceof Interface) {
				((Interface) newSource).getOwnedAttributes().add(modifiedAttribute);
			} else if (newSource instanceof DataType) {
				((DataType) newSource).getOwnedAttributes().add(modifiedAttribute);
			}

			Association association = (Association) modifiedAttribute.getAssociation();
			for (Property ownedEnd : association.getOwnedEnds()) {
				if (ownedEnd.getType().equals(oldSource)) {
					ownedEnd.setType((Type) newSource);
					ownedEnd.setName(((Type) newSource).getName());
					break;
				}
			}
		}
	}

	/**
	 * Find an attribute in a list of attributes
	 * 
	 * @param attributes list of attributes
	 * @param target     the searched attribute
	 * @return the found attribute
	 */
	private Property findAttributToBeModified(EList<Property> attributes, Element target) {
		for (Property attribut : attributes) {
			if (attribut.getType().equals(target)) {
				return attribut;
			}
		}

		return null;
	}

	/**
	 * Service used to reconnect a Link edge target.
	 *
	 * @param context    Element attached to the existing edge
	 * @param edgeView   Represents the graphical new edge
	 * @param oldTarget  Represents the semantic element pointed by the edge before
	 *                   reconnecting
	 * @param newTarget  Represents the semantic element pointed by the edge after
	 *                   reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectAssociationEdgeTarget(Element context, DEdge edgeView, Element oldTarget, Element newTarget) {
		// if reconnect the target of an AssociationClass edge
		if (context instanceof AssociationClass) {
			// set the ownedEnd to the new target
			for (Property ownedEnd : ((AssociationClass) context).getOwnedEnds()) {
				if (ownedEnd.getType().equals(oldTarget)) {
					ownedEnd.setType((Type) newTarget);
					ownedEnd.setName(((Type) newTarget).getName());
					break;
				}
			}
		} else // if reconnect the target of an Association edge
		{
			// get the source of the edge
			Element source = null;
			if (edgeView.getSourceNode() instanceof DNodeListSpec) {
				source = (Element) ((DNodeListSpec) edgeView.getSourceNode()).getTarget();
			} else if (edgeView.getSourceNode() instanceof DNodeContainerSpec)

			{
				source = (Element) ((DNodeContainerSpec) edgeView.getSourceNode()).getTarget();
			}

			// find the Attribute in the source to be modified
			EList<Property> attributes = null;
			if (source instanceof Class) {
				attributes = ((Class) source).getOwnedAttributes();
			} else if (source instanceof Interface) {
				attributes = ((Interface) source).getOwnedAttributes();
			} else if (source instanceof DataType) {
				attributes = ((DataType) source).getOwnedAttributes();
			}
			Property modifiedAttribute = findAttributToBeModified(attributes, (Element) oldTarget);

			// set the attribute to the new target
			modifiedAttribute.setType((Type) newTarget);
			modifiedAttribute.setName(((Type) newTarget).getName());
		}
	}

	/**
	 * Check if the current element is instance of instance specification Link or
	 * Class.
	 * 
	 * @param elem the current element to be checked
	 * @return true if instance specification Class, otherwise return false
	 */
	public boolean isNotInstanceSpecificationLink(Element elem) {
		return elem.getEAnnotations().isEmpty();
	}

	/**
	 * Check if the current element is instance of instance specification Link or
	 * Class.
	 * 
	 * @param elem the current element to be checked
	 * @return true if instance specification Class, otherwise return false
	 */
	public boolean isInstanceSpecificationEdge(EObject elem) {
		if (elem instanceof InstanceSpecification) {
			InstanceSpecification instanceSpecification = (InstanceSpecification) elem;
			return !instanceSpecification.getEAnnotations().isEmpty();
		}

		return false;
	}

	/**
	 * Get the target element of the instance specification link.
	 * 
	 * @param elem the instance specification Link
	 * @return the target element of the current instance specification link
	 */
	public EObject getTargetOfInstanceSpecification(EObject elem) {
		if (elem instanceof InstanceSpecification) {
			InstanceSpecification instanceSpecification = (InstanceSpecification) elem;
			if (!instanceSpecification.getEAnnotations().isEmpty()) {
				EAnnotation eAnnotation = instanceSpecification.getEAnnotations().get(0);
				if (!eAnnotation.getReferences().isEmpty()) {
					return eAnnotation.getReferences().get(1);
				}
			}
		}

		return null;
	}

	/**
	 * Get the source element of the instance specification link.
	 * 
	 * @param elem the instance specification Link
	 * @return the source element of the current instance specification link
	 */
	public EObject getSourceOfInstanceSpecification(EObject elem) {
		if (elem instanceof InstanceSpecification) {
			InstanceSpecification instanceSpecification = (InstanceSpecification) elem;
			if (!instanceSpecification.getEAnnotations().isEmpty()) {
				EAnnotation eAnnotation = instanceSpecification.getEAnnotations().get(0);
				if (!eAnnotation.getReferences().isEmpty()) {
					return eAnnotation.getReferences().get(0);
				}
			}
		}

		return null;
	}

	/**
	 * Compute the label of the given association.
	 *
	 * @param association the {@link Association} for which to retrieve a label.
	 * @return the computed label.
	 */
	public String computeAssociationBeginLabel(Association association) {
		return LabelServices.INSTANCE.computeAssociationBeginLabel(association);
	}

	public boolean isValidTest(EObject obj) {
		return true;
	}

	/**
	 * Compute the association edge begin name
	 * 
	 * @param association the current association
	 * @return the begin name
	 */
	public String computeAssociationClassBeginLabel(Association association) {
		Property source = AssociationServices.INSTANCE.getSource(association);
		return "+ " + source.getName();
	}

	/**
	 * Compute the association edge end name
	 * 
	 * @param association the current association
	 * @return the end name
	 */
	public String computeAssociationClassEndLabel(Association association) {
		Property target = AssociationServices.INSTANCE.getTarget(association);
		return "+ " + target.getName();
	}

	/**
	 * Compute the instance specification edge begin name
	 * 
	 * @param context the current instance specification
	 * @return the begin name
	 */
	public String computeInstanceSpecBeginLabel(EObject context) {
		InstanceSpecification instanceSpec = (InstanceSpecification) context;
		InstanceSpecification source = (InstanceSpecification) instanceSpec.getEAnnotations().get(0).getReferences()
				.get(0);
		return source.getName();
	}

	/**
	 * Compute the instance specification edge end name
	 * 
	 * @param context the current instance specification
	 * @return the end name
	 */
	public String computeInstanceSpecEndLabel(EObject context) {
		InstanceSpecification instanceSpec = (InstanceSpecification) context;
		InstanceSpecification target = (InstanceSpecification) instanceSpec.getEAnnotations().get(0).getReferences()
				.get(1);
		return target.getName();
	}

	/**
	 * Compute the label of the given association.
	 *
	 * @param association the {@link Association} for which to retrieve a label.
	 * @return the computed label.
	 */
	public String computeAssociationEndLabel(Association association) {
		return LabelServices.INSTANCE.computeAssociationEndLabel(association);
	}

	/**
	 * Compute label forn_ary association edge.
	 *
	 * @param association
	 * @param view        edge
	 * @return label
	 */
	public String computeAssociationNAryBeginLabel(Association association, DDiagramElement view) {
		return LabelServices.INSTANCE.computeAssociationBeginLabel(association, view);
	}

	/**
	 * Create an association between two elements.
	 *
	 * @param source association source
	 * @param target association target
	 * @return The association
	 */
	private Association createAssociation(Element source, Element target, int assocType) {
		// get the root model of the diagram
		Model rootModel = getRootModel(source);

		final Association association = UMLFactory.eINSTANCE.createAssociation();
		final Property end1 = AssociationServices.INSTANCE.createAssociationClassEnd((Type) source);
		association.getMemberEnds().add(end1);
		final Property end2 = AssociationServices.INSTANCE.createAssociationEnd((Type) target);
		association.getMemberEnds().add(end2);

		association.getOwnedEnds().add(end1);
		end2.setAssociation(association);
		end2.setUpper(1);

		switch (assocType) {
		case AggregationKind.SHARED:
			end2.setAggregation(AggregationKind.SHARED_LITERAL);
			break;
		case AggregationKind.COMPOSITE:
			end2.setAggregation(AggregationKind.COMPOSITE_LITERAL);
			break;
		default:
			break;
		}

		if (source instanceof Class) {
			((Class) source).getOwnedAttributes().add(end2);
		} else if (source instanceof Interface) {
			((Interface) source).getOwnedAttributes().add(end2);
		} else if (source instanceof DataType) {
			((DataType) source).getOwnedAttributes().add(end2);
		}

		EAnnotation eAnnotation = association.createEAnnotation(ANNOTATION_GENERIC_SOURCE);
		eAnnotation.getDetails().put(ANNOTATION_DETAIL_KEY, ANNOTATION_DETAIL_VALUE);
		association.getEAnnotations().add(eAnnotation);

		rootModel.getPackagedElements().add(association);

		return association;
	}

	public boolean isNotAssociation(EObject elem) {
		return ((Property) elem).getAssociation() == null;
	}

	public void createProfileApplicationLink(EObject object, Element source) {
		ProfileApplication profileApplication = UMLFactory.eINSTANCE.createProfileApplication();
		((Package) source).getProfileApplications().add(profileApplication);
	}

	/**
	 * Create a new association.
	 *
	 * @param object     Object
	 * @param source     selected source
	 * @param target     selected Target
	 * @param sourceView Source view
	 * @param targetView Target view
	 * @return Association
	 */
	public Association createAssociation(EObject object, Element source, Element target, EObject sourceView,
			EObject targetView, int assocType) {
		if (source.eContainer() instanceof Package) {
			// tool creation association edge
			if (!(source instanceof Association || target instanceof Association)) {
				return createAssociation(source, target, assocType);
			} else if ((source instanceof AssociationClass || target instanceof AssociationClass)
					&& (sourceView instanceof DEdge || targetView instanceof DEdge)) {
				// try to connect association from/to associationClas (edge part)
				return createAssociationAddEnd(source, target);
			} else if (source instanceof AssociationClass || target instanceof AssociationClass
					&& (sourceView instanceof DNodeList || targetView instanceof DNodeList)) {
				// try to connect association from/to associationClas (container part)
				return createAssociation(source, target, assocType);
			} else if (source instanceof Association || target instanceof Association) {
				return createAssociationAddEnd(source, target);
			}
		}
		return null;
	}

	/**
	 * Add an end to an existing association.
	 *
	 * @param source Association or element
	 * @param target element or association
	 */
	private Association createAssociationAddEnd(Element source, Element target) {
		Association association;
		Type type;
		if (source instanceof Association) {
			association = (Association) source;
			type = (Type) target;
		} else {
			association = (Association) target;
			type = (Type) source;
		}

		if (isBroken(association)) { // Look for broken association
			fixAssociation(association, type);
		} else { // create new end
			final Property end = AssociationServices.INSTANCE.createAssociationEnd(type);
			association.getNavigableOwnedEnds().add(end);
			association.getOwnedEnds().add(end);
		}
		return association;
	}

	/**
	 * Precondition for n-ary association creation.
	 *
	 * @param object selected association
	 * @return true if association is binary and no end have no qualifiers
	 */
	public boolean createNaryAssociationPrecondition(EObject object) {
		return AssociationServices.INSTANCE.createNaryAssociationPrecondition(object);
	}

	/**
	 * Create an operation in a class.
	 *
	 * @param type the container {@link org.eclipse.uml2.uml.Type} element
	 * @return New operation
	 */
	public Operation createOperation(org.eclipse.uml2.uml.Type type) {
		return OperationServices.INSTANCE.createOperation(type);
	}

	/**
	 * Create new qualifier for association
	 *
	 * @param association selected association
	 */
	public void createQualifier(Association association) {
		// Display a selection pop-up to choose the end
		final ListDialog dialog = new ListDialog(Display.getCurrent().getActiveShell());
		dialog.setTitle("Qualifier creation"); //$NON-NLS-1$
		dialog.setMessage("Please select the end to create new Qualifier:"); //$NON-NLS-1$
		dialog.setInput(association.getMemberEnds().toArray());
		dialog.setContentProvider(new ArrayContentProvider());
		dialog.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((NamedElement) element).getName();
			}
		});
		dialog.setInitialSelections(new Object[] { association.getMemberEnds().get(0) });

		final int status = dialog.open();
		if (status == Window.OK) {
			final Property qualifier = UMLFactory.eINSTANCE.createProperty();
			final Property end = (Property) dialog.getResult()[0];
			end.getQualifiers().add(qualifier);
			qualifier.setName(LabelServices.INSTANCE.computeDefaultName(qualifier));
		}
	}

	/**
	 * Delete selected edge fron a N-Ary association
	 *
	 * @param association association
	 * @param element     edge to delete
	 */
	public void deleteNAryAssociation(Association association, DDiagramElement element) {
		if (isNary(association)) {
			final Property end = AssociationServices.INSTANCE.getSourceEndAssociation(association, element);
			end.eContainer();
			EcoreUtil.delete(end);
		}
	}

	/**
	 * Edit the qualifier label
	 *
	 * @param context            property association end element
	 * @param editedLabelContent edit label content
	 * @return end
	 */
	public Element editQualifierLabel(Property context, String editedLabelContent) {
		final DisplayLabelSwitch displayLabelSwitch = new DisplayLabelSwitch();
		final EditLabelSwitch editLabel = new EditLabelSwitch();
		// Separator for direct edit
		final ArrayList<String> labels = new ArrayList<String>(
				Arrays.asList(editedLabelContent.split(DirectEditLabelSwitch.QUALIFIER_SEPARATOR)));
		// List qualifiers
		final EList<Property> qualifiers = context.getQualifiers();
		// Check for changes
		if (labels.size() != qualifiers.size()) {
			qualifiers.clear();
			for (final String label : labels) {
				final Property qualifier = UMLFactory.eINSTANCE.createProperty();
				qualifiers.add(qualifier);
				editLabel.setEditedLabelContent(label);
				editLabel.doSwitch(qualifier);
			}
		} else {
			// Only rename element
			int index = 0;
			for (final String label : labels) {
				if (!label.equals(displayLabelSwitch.doSwitch(qualifiers.get(index)))) {
					editLabel.setEditedLabelContent(label);
					editLabel.doSwitch(qualifiers.get(index));
				}
				index++;
			}
		}
		return context;
	}

	/**
	 * Iterate over the given {@link Collection} of root elements to find a
	 * {@link Type} element with the given name.
	 *
	 * @param roots    the elements to inspect
	 * @param typeName the name to match
	 * @return the found {@link Type} or <code>null</code>
	 */
	public Type findTypeByName(Collection<EObject> roots, String typeName) {
		return ElementServices.INSTANCE.findTypeByName(roots, typeName);
	}

	/**
	 * With the given {@link EObject} iterate over root elements to find a
	 * {@link Type} element with the given name.
	 *
	 * @param object   the elements to inspect
	 * @param typeName the name to match
	 * @return the found {@link Type} or <code>null</code>
	 */
	public Type findTypeByName(EObject object, String typeName) {
		return ElementServices.INSTANCE.findTypeByName(object, typeName);
	}

	private void fixAssociation(Association association, Type type) {
		final EList<Property> ends = association.getMemberEnds();
		final List<Property> brokenEnds = new ArrayList<Property>();
		for (final Property end : ends) {
			if (end.getType() == null) {
				brokenEnds.add(end);
			}
		}

		if (brokenEnds.size() > 1) {
			// If several broken links exist user have to select the link to reconnect.

			final ListDialog dialog = new ListDialog(Display.getCurrent().getActiveShell());
			dialog.setTitle("Reconnect broken association end:"); //$NON-NLS-1$
			dialog.setMessage("Please select the end to reconnect: "); //$NON-NLS-1$
			dialog.setInput(brokenEnds.toArray());
			dialog.setContentProvider(new ArrayContentProvider());
			dialog.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return ((NamedElement) element).getName();
				}
			});
			dialog.setInitialSelections(new Object[] { brokenEnds.get(0) });

			final int status = dialog.open();
			if (status == Window.OK) {
				final Object[] types = dialog.getResult();
				if (types != null && types.length == 1) {
					final Property endToFix = (Property) types[0];
					endToFix.setType(type);
					endToFix.setName(AssociationServices.INSTANCE.getAssociationEndsName(type));
				}
			}
		} else {
			final Property endToFix = brokenEnds.get(0);
			endToFix.setType(type);
			endToFix.setName(AssociationServices.INSTANCE.getAssociationEndsName(type));
		}
	}

	/**
	 * Fix association.
	 *
	 * @param host Host
	 * @param a    Association
	 * @param b    Association
	 */
	public void fixAssociation(EObject host, EObject a, EObject b) {
		if (a instanceof Association && b instanceof Type) {
			fixAssociation((Association) a, (Type) b);
		} else if (b instanceof Association && a instanceof Type) {
			fixAssociation((Association) b, (Type) a);
		}
	}

	/**
	 * Get abstract label format.
	 *
	 * @param object EObject
	 * @return Abstract label format
	 */
	public List<FontFormat> getAbstractBoldLabelFormat(EObject object) {
		// Fix to return bold/italic when bug will be fixed on sirius
		final List<FontFormat> fontFormats = new ArrayList<FontFormat>();
		fontFormats.add(FontFormat.BOLD_LITERAL);
		fontFormats.add(FontFormat.ITALIC_LITERAL);
		return fontFormats;
	}

	/**
	 * Get abstract label format.
	 *
	 * @param object EObject
	 * @return Abstract label format
	 */
	public FontFormat getAbstractItalicLabelFormat(EObject object) {
		// Fix to return bold/italic when bug will be fixed on sirius
		return FontFormat.ITALIC_LITERAL;
	}

	/**
	 * Get all the stereotype applications according to the selected diagram.
	 *
	 * @param diagram Current diagram
	 * @return Stereotype applications
	 */
	public Collection<Object> getAllStereotypeApplications(DDiagram diagram) {
		return org.eclipse.papyrus.uml.sirius.common.diagram.core.services.StereotypeServices.INSTANCE
				.getAllStereotypeApplications(diagram);
	}

	/**
	 * Get list of association. Check in diagram only two ends are presents.
	 *
	 * @param container package
	 * @param diagram   diagram
	 * @return list of binary association
	 */
	public List<Association> getAssociation(Package container, DDiagram diagram) {
		final List<Association> result = new ArrayList<Association>();
		final Collection<EObject> associations = getAssociationInverseRefs(diagram);
		for (final EObject object : associations) {
			final Association association = (Association) object;
			if (getVisibleAssociationEnds(association, diagram).size() <= 2) {
				result.add(association);
			}
		}
		return result;
	}

	/**
	 * Retrieve the cross references of the association of all the UML elements
	 * displayed as node in a Diagram. Note that a Property cross reference will
	 * lead to retrieve the cross references of this property.
	 *
	 * @param diagram a diagram.
	 * @return the list of cross reference of the given
	 */
	public Collection<EObject> getAssociationInverseRefs(DDiagram diagram) {
		return NodeInverseRefsServices.INSTANCE.getAssociationInverseRefs(diagram);
	}

	/**
	 * Get base class associated to a stereotype application.
	 *
	 * @param stereotypeApplication Stereotype application
	 * @return Base class
	 */
	public Element getBaseClass(EObject stereotypeApplication) {
		return StereotypeServices.INSTANCE.getBaseClass(stereotypeApplication);
	}

	/**
	 * Get broken associations.
	 *
	 * @param container the current container.
	 * @return a list of association which might be considered as "broken", we are
	 *         not able to display them as edges.
	 */
	public Collection<Association> getBrokenAssociations(EObject container) {
		final Collection<Association> result = new ArrayList<Association>();
		for (final EObject child : container.eContents()) {
			if (child instanceof Association && isBroken((Association) child)) {
				result.add((Association) child);
			}
		}
		return result;
	}

	/**
	 * Get list of n-ary association. Check in diagram if more than two ends are
	 * presents.
	 *
	 * @param container package
	 * @param diagram   diagram
	 * @return list of n-ary association
	 */
	public List<Association> getNAryAssociation(Package container, DDiagram diagram) {
		final List<Association> result = new ArrayList<Association>();
		final Collection<EObject> associations = getAssociationInverseRefs(diagram);
		for (final EObject object : associations) {
			final Association association = (Association) object;
			if (association.eContainer().equals(container)
					&& getVisibleAssociationEnds(association, diagram).size() > 2) {
				// Check if n-ary association container is current container to avoid to display
				// twice
				result.add(association);
			}
		}
		return result;
	}

	/**
	 * Get source for n-ary association.
	 *
	 * @param association association
	 * @return list of source.
	 */
	public EList<Type> getNAryAssociationSource(Association association) {
		return association.getEndTypes();
	}

	/**
	 * Get navigable owned end of an association
	 *
	 * @param association Association
	 * @return Association
	 */
	public List<Property> getNavigableOwnedEnds(Association association) {
		final List<Property> ends = Lists.newArrayList();
		final Property source = AssociationServices.INSTANCE.getSource(association);
		final Property target = AssociationServices.INSTANCE.getTarget(association);
		if (source != null) {
			ends.add(source);
		}
		if (target != null) {
			ends.add(target);
		}
		return ends;
	}

	/**
	 * Return a set of classes from model.
	 *
	 * @param element an element in model
	 * @return set of classes or empty collection
	 */
	public Collection<Class> getSemanticCandidatesClasses(Element element) {
		final Set<Class> classes = new HashSet<Class>();
		final TreeIterator<EObject> iterator = element.getModel().eAllContents();
		while (iterator.hasNext()) {
			final EObject object = iterator.next();
			if (isTypeOfClass(object) || object instanceof Component) {
				classes.add((Class) object);
			}
		}
		return classes;
	}

	/**
	 * Get association end qualifier for a classifier.
	 *
	 * @param classifier association end
	 * @param diagram    Diagram
	 * @return List of qualifier
	 */
	public List<Property> getSemanticCandidatesQualifier(Classifier classifier, DDiagram diagram) {
		final List<Property> qualifiers = new ArrayList<Property>();
		final Collection<EObject> associations = getAssociationInverseRefs(diagram);
		for (final EObject association : associations) {
			for (final Property end : ((Association) association).getMemberEnds()) {
				if (((Association) association).getMemberEnds().size() <= 2 && end.getType().equals(classifier)
						&& !end.getQualifiers().isEmpty()
						&& getVisibleAssociationEnds((Association) association, diagram).size() >= 2) {
					qualifiers.add(end);
				}
			}
		}
		return qualifiers;
	}

	/**
	 * Get the type of the association source end.
	 *
	 * @param association association
	 * @param diagram     diagram
	 * @return type of the source
	 */
	public Element getSourceType(Association association, DDiagram diagram) {
		final EList<DDiagramElement> elements = diagram.getDiagramElements();
		// List semantic elements visible in diagram
		final List<EObject> visibleEnds = new ArrayList<EObject>();
		for (final DDiagramElement element : elements) {
			visibleEnds.add(element.getTarget());
		}
		final EList<Property> ends = association.getMemberEnds();
		for (final Property end : ends) {
			if (visibleEnds.contains(end.getType())) {
				if (end.getQualifiers().isEmpty()) {
					return end.getType();
				}
				return end;
			}
		}
		return null;
	}

	/**
	 * Get stereotype application label.
	 *
	 * @param stereotypeApplication Stereotype application
	 * @return The stereotype name.
	 */
	public String getStereotypeApplicationLabel(EObject stereotypeApplication) {
		return org.eclipse.papyrus.uml.sirius.common.diagram.core.services.StereotypeServices.INSTANCE
				.getStereotypeApplicationLabel(stereotypeApplication);
	}

	/**
	 * Get tagged value label.
	 *
	 * @param feature Feature
	 * @param view    Stereotype application view
	 * @return Tagged value label featureName = value
	 */
	public String getTaggedValueLabel(EStructuralFeature feature, DDiagramElement view) {
		final DDiagramElement stereotypeApplicationView = (DDiagramElement) view.eContainer();
		final EObject stereotypeApplication = stereotypeApplicationView.getTarget();

		return feature.getName() + "=" + stereotypeApplication.eGet(feature); //$NON-NLS-1$
	}

	/**
	 * Get tagged values.
	 *
	 * @param stereotypeApplication Container
	 * @return Collection of tagged values
	 */
	public Collection<Object> getTaggedValues(EObject stereotypeApplication) {
		final Collection<Object> results = Lists.newArrayList();
		for (final EStructuralFeature feature : stereotypeApplication.eClass().getEAllStructuralFeatures()) {
			if (!feature.getName().startsWith("base_")) { //$NON-NLS-1$
				results.add(feature);
			}
		}

		return results;
	}

	/**
	 * Get the list of broken ends.
	 *
	 * @param association association
	 * @return List
	 */
	public List<Type> getTargetBrokenAssociationToClasse(Association association) {
		if (!isNary(association)) {
			return AssociationServices.INSTANCE.getTypes(association);
		}
		return null;
	}

	/**
	 * Get the type of the association target end.
	 *
	 * @param association association
	 * @param diagram     diagram
	 * @return Type of the target
	 */
	public Element getTargetType(Association association, DDiagram diagram) {

		final EList<DDiagramElement> elements = diagram.getDiagramElements();

		// List semantic elements visible in diagram
		final List<EObject> diagramElements = new ArrayList<EObject>();
		for (final DDiagramElement element : elements) {
			diagramElements.add(element.getTarget());
		}

		final List<Property> ends = association.getMemberEnds();
		// find source index in list
		int sourceIndex = 0;
		for (int i = 0; i < ends.size(); i++) {
			if (diagramElements.contains(ends.get(i).getType())) {
				sourceIndex = i;
				break;
			}
		}

		// find target from the end of the list
		int targetIndex = ends.size() - 1;
		while (targetIndex > 0 && targetIndex > sourceIndex) {
			if (diagramElements.contains(ends.get(targetIndex).getType())) {
				if (ends.get(targetIndex).getQualifiers().isEmpty()) {
					return ends.get(targetIndex).getType();
				}
				return ends.get(targetIndex);
			}
			targetIndex--;
		}
		return null;
	}

	/**
	 * Retrieve the cross references of the template binding of all the UML elements
	 * displayed as node in a Diagram. Note that a Property cross reference will
	 * lead to retrieve the cross references of this property.
	 *
	 * @param diagram a diagram.
	 * @return the list of cross reference of the given
	 */
	public Collection<EObject> getTemplateBindingInverseRefs(DDiagram diagram) {
		return NodeInverseRefsServices.INSTANCE.getTemplateBindingInverseRefs(diagram);
	}

	/**
	 * Return collection of visible association class in a diagram.
	 *
	 * @param diagram   Diagram
	 * @param container Container of the associationClass
	 * @return Set of visible association Classes or empty collection
	 */
	public Collection<EObject> getVisibleAssociationClass(DSemanticDiagram diagram, EObject container) {
		final Set<EObject> associationClasses = new HashSet<EObject>();
		final Collection<EObject> displayedNodes = UIServices.INSTANCE.getDisplayedNodes(diagram);
		final Collection<EObject> associations = getAssociationInverseRefs(diagram);
		for (final EObject association : associations) {
			if (association instanceof AssociationClass) {
				final Property source = AssociationServices.INSTANCE.getSource((AssociationClass) association);
				final Property target = AssociationServices.INSTANCE.getTarget((AssociationClass) association);
				if (source != null && target != null) {
					final Type sourceType = source.getType();
					final Type targetType = target.getType();
					final Package parent = ((AssociationClass) association).getNearestPackage();

					// An association class is visible in its parent if the parent is visible, else
					// it is visible
					// directly on the diagram
					if ((container.equals(parent)
							|| !displayedNodes.contains(parent) && container.equals(diagram.getTarget()))
							&& sourceType != null && displayedNodes.contains(sourceType) && targetType != null
							&& displayedNodes.contains(targetType)) {
						associationClasses.add(association);
					}
				}
			}
		}
		return associationClasses;
	}

	private List<Property> getVisibleAssociationEnds(Association association, DDiagram diagram) {
		final List<Property> ends = new ArrayList<Property>();
		// Association should be visible in self container
		// At least one of the ends is visible in diagram
		final EList<DDiagramElement> elements = diagram.getDiagramElements();
		// check if at least more than 2 ends are displayed in diagram
		final List<EObject> visibleEndsList = new ArrayList<EObject>();
		for (final DDiagramElement element : elements) {
			visibleEndsList.add(element.getTarget());
		}
		final EList<Property> associationEnds = association.getMemberEnds();
		for (final Property end : associationEnds) {
			if (visibleEndsList.contains(end.getType())) {
				ends.add(end);
			}
			if (end.getType() == null) { // Broken association case
				ends.add(end);
			}
		}
		return ends;
	}

	private boolean isBroken(Association child) {
		final EList<Property> ends = child.getMemberEnds();
		for (final Property end : ends) {
			if (end.getType() == null) {
				return true;
			}
		}
		return false;
	}

	private boolean isNary(Association association) {
		if (association != null && association.getMembers() != null && association.getMembers().size() > 2) {
			return true;
		}
		return false;
	}

	/**
	 * Check an element is not a Class.
	 *
	 * @param element Element
	 * @return return true if the element is not a Class
	 */
	public boolean isNotTypeOfClass(EObject element) {
		return !isTypeOfClass(element);
	}

	/**
	 * Check if an element is a package.
	 *
	 * @param element Element
	 * @return True if element is a package
	 */
	public boolean isPackage(EObject element) {
		return element instanceof Package;
	}

	/**
	 * Check is a feature is static.
	 *
	 * @param feature Feature
	 * @return True if it is a static feature
	 */
	public boolean isStatic(Feature feature) {
		return feature != null && feature.isStatic();
	}

	/**
	 * Check if an element is type of class.
	 *
	 * @param element Element
	 * @return True if element is a class
	 */
	public boolean isTypeOfClass(EObject element) {
		return "Class".equals(element.eClass().getName()); //$NON-NLS-1$
	}

	/**
	 * Is a composite or aggregation association is valid.
	 *
	 * @param association
	 * @return true if an aggregation or a composite association is binary
	 */
	public boolean isValidAggregationCompositeAssociation(Association association) {
		final EList<Property> ends = association.getMemberEnds();
		if (ends.size() > 2) {
			for (final Property end : ends) {
				if (end.getAggregation().getValue() != AggregationKind.NONE) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if an association can be created.
	 *
	 * @param self      association
	 * @param preSource selected element
	 * @return true if valid
	 */
	public boolean isValidAssociation(EObject self, Element preSource) {
		if (preSource instanceof Association) {
			// Verify association ends don't have qualifiers
			final EList<Property> ends = ((Association) preSource).getMemberEnds();
			for (final Property end : ends) {
				if (!end.getQualifiers().isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if an association can be created. Both selected elements are not
	 * association. If an association is selected is should not be an aggregation or
	 * a composite
	 *
	 * @param self      association
	 * @param preSource User select element as source
	 * @param preTarget user select element as target
	 * @return true if valid
	 */
	public boolean isValidAssociation(EObject self, Element preSource, Element preTarget) {

		if (preSource instanceof Association && preTarget instanceof Association) {
			return false; // Source and Target are not association
		} else if (preSource instanceof Association || preTarget instanceof Association) {
			// Verify association is not a binary association
			Association association;
			if (preSource instanceof Association) {
				association = (Association) preSource;
			} else {
				association = (Association) preTarget;
			}

			final EList<Property> ends = association.getMemberEnds();
			for (final Property end : ends) {
				if (AssociationServices.INSTANCE.isComposite(end) || AssociationServices.INSTANCE.isShared(end)) {
					return false;
				}
				if (!end.getQualifiers().isEmpty()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Check if an Nary association can be created. Both selected elements are not
	 * association. If an association is selected is should not be an aggregation or
	 * a composite
	 *
	 * @param self      association
	 * @param preSource User select element as source
	 * @param preTarget user select element as target
	 * @return true if valid
	 */
	public boolean isValidNaryAssociation(EObject self, Element preSource, Element preTarget) {

		if (preSource instanceof Association || preTarget instanceof Association) {
			return false;
		}

		return true;
	}

	/**
	 * Check is an association source is composite.
	 *
	 * @param association Association
	 * @return True if source is composite
	 */
	public boolean sourceIsComposite(Association association) {
		return AssociationServices.INSTANCE.sourceIsComposite(association);
	}

	/**
	 * Check is an association source is navigable.
	 *
	 * @param association Association
	 * @param element     Edge element
	 * @return True if source is navigable
	 */
	public boolean sourceIsNavigable(Association association, DDiagramElement element) {
		return AssociationServices.INSTANCE.sourceIsNavigable(association, element);
	}

	/**
	 * Check is an association source is navigable and composite.
	 *
	 * @param association Association
	 * @return True if source is navigable and composite
	 */
	public boolean sourceIsNavigableAndTargetIsComposite(Association association) {
		return AssociationServices.INSTANCE.sourceIsNavigableAndTargetIsComposite(association);
	}

	/**
	 * Check is an association source is navigable and shared.
	 *
	 * @param association Association
	 * @return True if source is navigable and shared
	 */
	public boolean sourceIsNavigableAndTargetIsShared(Association association) {
		return AssociationServices.INSTANCE.sourceIsNavigableAndTargetIsShared(association);
	}

	/**
	 * Check is an association source is shared.
	 *
	 * @param association Association
	 * @return True if source is shared
	 */
	public boolean sourceIsShared(Association association) {
		return AssociationServices.INSTANCE.sourceIsShared(association);
	}

	/**
	 * Check is an association target is composite.
	 *
	 * @param association Association
	 * @return True if target is composite
	 */
	public boolean targetIsComposite(Association association) {
		return AssociationServices.INSTANCE.targetIsComposite(association);
	}

	/**
	 * Check is an association target is navigable.
	 *
	 * @param association Association
	 * @return True if target is navigable
	 */
	public boolean targetIsNavigable(Association association) {
		return AssociationServices.INSTANCE.targetIsNavigable(association);
	}

	/**
	 * Check is an association target is navigable and composite.
	 *
	 * @param association Association
	 * @return True if target is navigable and composite
	 */
	public boolean targetIsNavigableAndSourceIsComposite(Association association) {
		return AssociationServices.INSTANCE.targetIsNavigableAndSourceIsComposite(association);
	}

	/**
	 * Check is an association target is navigable and shared.
	 *
	 * @param association Association
	 * @return True if target is navigable and shared
	 */
	public boolean targetIsNavigableAndSourceIsShared(Association association) {
		return AssociationServices.INSTANCE.targetIsNavigableAndSourceIsShared(association);
	}

	/**
	 * Check is an association target is shared.
	 *
	 * @param association Association
	 * @return True if target is shared
	 */
	public boolean targetIsShared(Association association) {
		return AssociationServices.INSTANCE.targetIsShared(association);
	}

}
