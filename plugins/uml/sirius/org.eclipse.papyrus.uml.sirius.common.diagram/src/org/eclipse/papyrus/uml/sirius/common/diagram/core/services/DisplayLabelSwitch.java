/******************************************************************************
 * Copyright (c) 2009, 2022 Obeo, CEA LIST, Artal Technologies
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
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

import java.util.Iterator;
import java.util.List;

import org.eclipse.papyrus.uml.internationalization.utils.utils.UMLLabelInternationalization;
import org.eclipse.papyrus.uml.internationalization.utils.utils.UMLQualifiedNameUtils;
//import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataStoreNode;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Duration;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Expression;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.InstanceValue;
import org.eclipse.uml2.uml.Interval;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralNull;
import org.eclipse.uml2.uml.LiteralReal;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.Pin;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ProtocolStateMachine;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;
import org.eclipse.uml2.uml.TemplateableElement;
import org.eclipse.uml2.uml.TimeExpression;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.edit.UMLEditPlugin;
import org.eclipse.uml2.uml.util.UMLSwitch;

import com.google.common.base.Strings;

/**
 * A switch that handle the label computation for each UML types.
 *
 */
public class DisplayLabelSwitch extends UMLSwitch<String> implements ILabelConstants {

	/**
	 * Spaced column constant.
	 */
	private static final String SPACED_COLUMN = " : "; //$NON-NLS-1$

	/**
	 * Spaced column constant.
	 */
	private static final String SPACED_COMMA = ", "; //$NON-NLS-1$

	/**
	 * Closing brace constant.
	 */
	private static final String CLOSING_BRACE = "]"; //$NON-NLS-1$

	/**
	 * Opening brace constant.
	 */
	private static final String OPENING_BRACE = "["; //$NON-NLS-1$

	/**
	 * Line separator.
	 */
	public static final String END_OF_LINE = System.lineSeparator();

	/**
	 * Space.
	 */
	public static final String SPACE = " "; //$NON-NLS-1$

	private static boolean showStereotypeFilter = true;

	private static boolean showQualifiedNameFilter = true;

	/**
	 * Compute the {@link Stereotype} label part for the given {@link Element}.
	 *
	 * @param element the context element.
	 * @return the {@link Stereotype} label.
	 */
	public static String computeStereotypes(Element element) {

		if (showStereotypeFilter) {
			final Iterator<Stereotype> it = element.getAppliedStereotypes().iterator();

			if (!it.hasNext()) {
				return ""; //$NON-NLS-1$
			}

			final StringBuffer stereotypeLabel = new StringBuffer();
			stereotypeLabel.append(ILabelConstants.ST_LEFT);
			for (;;) {
				final Stereotype appliedStereotype = it.next();

				stereotypeLabel.append(appliedStereotype.getName());
				if (it.hasNext()) {
					stereotypeLabel.append(", "); //$NON-NLS-1$
				} else {
					break;
				}
			}
			stereotypeLabel.append(ILabelConstants.ST_RIGHT);
			if (element instanceof Feature) {
				stereotypeLabel.append(" "); //$NON-NLS-1$
			} else {
				stereotypeLabel.append(NL);
			}

			return stereotypeLabel.toString();
		}
		return ""; //$NON-NLS-1$
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseActivity(Activity object) {
		return ILabelConstants.ST_LEFT + "Activity" + ILabelConstants.ST_RIGHT + caseBehavior(object); //$NON-NLS-1$
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseClass(Class object) {
		return caseTemplateableElement(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseComponentRealization(org.eclipse.uml2.uml.ComponentRealization object) {
		return computeStereotypes(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseConstraint(Constraint object) {
		final StringBuilder label = new StringBuilder();
		label.append('{');
		if (object != null) {
			final String constraintName = object.getName();
			if (!Strings.isNullOrEmpty(constraintName)) {
				label.append(constraintName);
				label.append(':');
			}
			final ValueSpecification specification = object.getSpecification();
			if (specification == null) {
				label.append("missing specification"); //$NON-NLS-1$
			} else {
				label.append(specification.stringValue());
			}
		}
		label.append('}');
		return label.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseDataStoreNode(DataStoreNode object) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(ILabelConstants.ST_LEFT);
		buffer.append("Datastore"); //$NON-NLS-1$
		buffer.append(ILabelConstants.ST_RIGHT);
		buffer.append(NL);
		buffer.append(caseNamedElement(object));
		return buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseDependency(Dependency object) {
		return computeStereotypes(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseDeployment(org.eclipse.uml2.uml.Deployment object) {
		return computeStereotypes(object) + ILabelConstants.ST_LEFT + "deploy"+ ILabelConstants.ST_RIGHT; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseDuration(Duration object) {
		return object.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseElementImport(ElementImport object) {
		return computeStereotypes(object) + ILabelConstants.ST_LEFT + "import"+ ILabelConstants.ST_RIGHT;  //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseEnumerationLiteral(EnumerationLiteral object) {
		return object.getLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseExecutionSpecification(ExecutionSpecification execution) {
		if (execution instanceof BehaviorExecutionSpecification) {
			if (((BehaviorExecutionSpecification) execution).getBehavior() != null
					&& ((BehaviorExecutionSpecification) execution).getBehavior().getSpecification() != null) {
				return caseOperation(
						(Operation) ((BehaviorExecutionSpecification) execution).getBehavior().getSpecification());
			}
		}
		return execution.getLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseExpression(Expression object) {
		return object.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String casePackageMerge(PackageMerge object) {
		return computeStereotypes(object) + ILabelConstants.ST_LEFT + "merge" + ILabelConstants.ST_RIGHT; //$NON-NLS-1$
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseElement(Element object) {
		return computeStereotypes(object);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseSubstitution(Substitution object) {
		return computeStereotypes(object) + ILabelConstants.ST_LEFT + "substitue>>" + ILabelConstants.ST_RIGHT; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseInstanceSpecification(InstanceSpecification object) {
		final StringBuilder label = new StringBuilder(caseNamedElement(object));

		if (object.getClassifiers() != null && object.getClassifiers().size() > 0) {
			label.append(SPACED_COLUMN);
			final Iterator<Classifier> it = object.getClassifiers().iterator();
			while (it.hasNext()) {
				final Classifier classifier = it.next();
				label.append(doSwitch(classifier).replace("\n", " ")); //$NON-NLS-1$ //$NON-NLS-2$
				if (it.hasNext()) {
					label.append(SPACED_COMMA);
				}
			}
		}
		return label.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseInstanceValue(InstanceValue object) {
		return LabelServices.INSTANCE.computeUmlLabel(object.getInstance());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseInterfaceRealization(org.eclipse.uml2.uml.InterfaceRealization object) {
		return computeStereotypes(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseInterval(Interval object) {
		final String minLabel = LabelServices.INSTANCE.computeUmlLabel(object.getMin());
		final String maxLabel = LabelServices.INSTANCE.computeUmlLabel(object.getMax());
		if (minLabel != null && minLabel.length() > 0 && maxLabel != null && maxLabel.length() > 0) {
			return OPENING_BRACE + minLabel + " " + maxLabel + CLOSING_BRACE; //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseLifeline(Lifeline lifeline) {
		final StringBuilder label = new StringBuilder();
		if (lifeline.getRepresents() != null && isDependencyDescribed(lifeline)) {
			for (final NamedElement context : lifeline.getClientDependencies().get(0).getSuppliers()) {
				label.append(context.getLabel());
				label.append(SPACED_COLUMN.trim());
				label.append(SPACED_COLUMN.trim());
			}
			label.append(doSwitch(lifeline.getRepresents()));
		} else if (lifeline.getRepresents() == null && isDependencyDescribed(lifeline)) {
			for (final NamedElement context : lifeline.getClientDependencies().get(0).getSuppliers()) {
				label.append(doSwitch(context));
			}
		} else if (lifeline.getRepresents() instanceof Property) {
			// label.append(SPACED_COLUMN);
			label.append(caseProperty((Property) lifeline.getRepresents()));
		} else {
			label.append(caseNamedElement(lifeline));
		}
		return label.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseLiteralBoolean(LiteralBoolean object) {
		return object.stringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseLiteralInteger(LiteralInteger object) {
		return object.stringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseLiteralNull(LiteralNull object) {
		return "null"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseLiteralReal(LiteralReal object) {
		return object.stringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseLiteralString(LiteralString object) {
		return object.stringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseLiteralUnlimitedNatural(LiteralUnlimitedNatural object) {
		return object.stringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseMultiplicityElement(MultiplicityElement object) {
		return getMultiplicity(object.getLower(), object.getUpper());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseNamedElement(NamedElement object) {
		if (showQualifiedNameFilter) {
			return computeStereotypes(object)
					+ (object.getName() == null ? "" : UMLQualifiedNameUtils.getQualifiedName(object, ":")); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return computeStereotypes(object) + (object.getName() == null ? "" //$NON-NLS-1$
					: UMLLabelInternationalization.getInstance().getLabel(object, shouldTranslate()));
		}
	}

	protected boolean shouldTranslate() {
		return UMLEditPlugin.INSTANCE.shouldTranslate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseOpaqueExpression(OpaqueExpression object) {
		final String expr = object.getBodies().get(0);
		if (expr != null && !"".equalsIgnoreCase(expr) && !"true".equalsIgnoreCase(expr) //$NON-NLS-1$ //$NON-NLS-2$
				&& !"1".equalsIgnoreCase(expr)) { //$NON-NLS-1$
			return expr;
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseOperation(Operation object) {
		final StringBuilder label = new StringBuilder(caseNamedElement(object));
		label.append("("); //$NON-NLS-1$

		boolean first = true;
		for (final Parameter parameter : object.getOwnedParameters()) {
			if (!ParameterDirectionKind.RETURN_LITERAL.equals(parameter.getDirection())) {
				if (!first) {
					label.append(", "); //$NON-NLS-1$
				} else {
					first = false;
				}
				label.append(caseTypedElement(parameter));
			}
		}
		label.append(")"); //$NON-NLS-1$
		if (object.getType() != null) {
			label.append(SPACED_COLUMN + object.getType().getName());
			label.append(getMultiplicity(object.getLower(), object.getUpper()));
		}
		final StringBuilder operProperties = new StringBuilder();
		if (object.getRedefinedElements() != null && object.getRedefinedElements().size() > 0
				&& object.getRedefinedElements().get(0) != null) {
			operProperties.append("redefines " + object.getRedefinedElements().get(0).getName()); //$NON-NLS-1$
		}
		if (object.isQuery()) {
			if (operProperties.length() > 0) {
				operProperties.append(", "); //$NON-NLS-1$
			}
			operProperties.append("query"); //$NON-NLS-1$
		}
		if (object.isOrdered()) {
			if (operProperties.length() > 0) {
				operProperties.append(", "); //$NON-NLS-1$
			}
			operProperties.append("ordered"); //$NON-NLS-1$
		}
		if (!object.isUnique()) {
			if (operProperties.length() > 0) {
				operProperties.append(", "); //$NON-NLS-1$
			}
			operProperties.append("nonunique"); //$NON-NLS-1$
		}
		if (operProperties.length() > 0) {
			label.append("{" + operProperties + "}"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return label.toString();

	}

	@Override
	public String caseParameter(Parameter object) {
		final StringBuilder label = new StringBuilder(caseNamedElement(object));
		return label.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String casePin(Pin object) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(caseTypedElement(object));
		buffer.append(caseMultiplicityElement(object));
		return buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String casePort(Port object) {
		String tilde = ""; //$NON-NLS-1$
		if (object.isConjugated()) {
			tilde = "~"; //$NON-NLS-1$
		}
		if (object.getType() != null) {
			return caseNamedElement(object) + SPACED_COLUMN + tilde + object.getType().getName();
		}
		return caseNamedElement(object) + SPACED_COLUMN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseProfile(Profile profile) {
		return profile.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseProperty(Property property) {
		if (property != null) {
			// Qualifier are end properties displayed as bordered node
			if (!property.getQualifiers().isEmpty()) {
				String label = ""; //$NON-NLS-1$
				boolean first = true;
				for (final Property qualifier : property.getQualifiers()) {
					if (first) {
						label += SPACE + computeLabel(qualifier);
						first = false;
					} else {
						label += END_OF_LINE;
						label += SPACE + computeLabel(qualifier);
					}
				}
				return label;
			}
		}
		return computeLabel(property);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseProtocolStateMachine(ProtocolStateMachine object) {
		return object.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseSlot(Slot object) {
		if (object.getDefiningFeature() == null) {
			return "<UNDEFINED>"; //$NON-NLS-1$
		}
		final StringBuilder label = new StringBuilder(object.getDefiningFeature().getName());
		label.append(" = "); //$NON-NLS-1$
		final List<ValueSpecification> values = object.getValues();
		boolean first = true;
		for (final ValueSpecification valueSpecification : values) {
			if (first) {
				first = false;
			} else {
				label.append(SPACED_COMMA);
			}

			if (valueSpecification instanceof InstanceValue) {
				final InstanceValue anInstanceValue = (InstanceValue) valueSpecification;
				label.append(anInstanceValue.getInstance().getName());
			} else if (valueSpecification instanceof LiteralString) {
				final LiteralString aLiteralString = (LiteralString) valueSpecification;
				label.append(aLiteralString.getValue());
			} else if (valueSpecification instanceof LiteralInteger) {
				final LiteralInteger aLiteralInteger = (LiteralInteger) valueSpecification;
				label.append(aLiteralInteger.getValue());
			} else if (valueSpecification instanceof LiteralBoolean) {
				final LiteralBoolean aLiteralBoolean = (LiteralBoolean) valueSpecification;
				label.append(aLiteralBoolean.booleanValue());
			} else {
				label.append(valueSpecification.getName());
			}
		}
		return label.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseStateMachine(StateMachine object) {
		return computeStereotypes(object) + ILabelConstants.ST_LEFT + "StateMachine" + ILabelConstants.ST_RIGHT + caseBehavior(object); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseStereotype(Stereotype stereotype) {
		final String name = stereotype.getName();
		final String profileName = stereotype.containingProfile().getName();
		return profileName + " :: " + name; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseStructuralFeature(StructuralFeature object) {
		return caseTypedElement(object) + " " + caseMultiplicityElement(object); //$NON-NLS-1$
	}

	@Override
	public String caseTemplateableElement(TemplateableElement object) {
		final String templateParameters = LabelServices.INSTANCE.getTemplatedParameters(object);
		if (templateParameters != null) {
			return computeStereotypes(object) + caseNamedElement((NamedElement) object) + templateParameters;
		}
		return caseNamedElement((NamedElement) object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseTemplateBinding(TemplateBinding object) {
		final List<TemplateParameterSubstitution> parameterSubstitutions = object.getParameterSubstitutions();
		final StringBuffer binding = new StringBuffer();
		boolean first = true;
		for (final TemplateParameterSubstitution parameterSubstitution : parameterSubstitutions) {
			if (first) {
				first = false;
			} else {
				binding.append(", "); //$NON-NLS-1$
			}
			final ParameterableElement formal = parameterSubstitution.getFormal().getDefault();
			if (formal instanceof NamedElement) {
				binding.append(((NamedElement) formal).getName() + "->"); //$NON-NLS-1$
				final ParameterableElement actual = parameterSubstitution.getActual();
				if (actual != null && actual instanceof NamedElement) {
					binding.append(((NamedElement) actual).getName());
				} else {
					binding.append("?"); //$NON-NLS-1$
				}
			}
		}
		return computeStereotypes(object) + binding.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseTimeExpression(TimeExpression object) {
		return object.stringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseTransition(Transition object) {
		// {trigger ','}* [guard] /behavior_spec
		// Triggers
		String triggersLabel = null;
		if (object.getTriggers() != null && object.getTriggers().size() > 0) {
			for (final Trigger trigger : object.getTriggers()) {
				if (triggersLabel != null) {
					triggersLabel += ","; //$NON-NLS-1$
				} else {
					triggersLabel = ""; //$NON-NLS-1$
				}
				triggersLabel += LabelServices.INSTANCE.computeUmlLabel(trigger);
			}
		}
		// Guard
		String guardLabel = null;
		final Constraint constraint = object.getGuard();
		if (constraint != null) {
			final ValueSpecification specification = constraint.getSpecification();

			if (specification != null) {
				final String specificationLabel = LabelServices.INSTANCE.computeUmlLabel(specification);
				if (specificationLabel != null && specificationLabel.length() > 0) {
					guardLabel = OPENING_BRACE + specificationLabel + CLOSING_BRACE;
				}
			}
		}

		// Behavior spec
		String effectLabel = null;
		final Behavior effect = object.getEffect();
		if (effect != null) {
			final String behaviorLabel = LabelServices.INSTANCE.computeUmlLabel(effect);
			if (behaviorLabel != null) {
				effectLabel = "/" + behaviorLabel; //$NON-NLS-1$
			}
		}

		final StringBuffer transitionLabel = new StringBuffer();
		if (triggersLabel != null && triggersLabel.length() > 0) {
			transitionLabel.append(triggersLabel);
		}
		if (guardLabel != null && guardLabel.length() > 0) {
			transitionLabel.append(guardLabel);
		}
		if (effectLabel != null && effectLabel.length() > 0) {
			transitionLabel.append(effectLabel);
		}

		return computeStereotypes(object) + transitionLabel.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseTypedElement(TypedElement object) {
		if (object.getType() != null) {
			return caseNamedElement(object) + SPACED_COLUMN + object.getType().getName();
		}
		return caseNamedElement(object) + SPACED_COLUMN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseUsage(org.eclipse.uml2.uml.Usage object) {
		return computeStereotypes(object) + ILabelConstants.ST_LEFT + "use" + ILabelConstants.ST_RIGHT; //$NON-NLS-1$
	}
	
	/**
	 * Compute label for a property.
	 *
	 * @param object property
	 * @return label
	 */
	private String computeLabel(Property object) {
		if (object.eContainer() instanceof Property
				&& object.eContainmentFeature().getFeatureID() == UMLPackage.PROPERTY__QUALIFIER) {
			return computeQualifierLabel(object);
		}
		return computePropertyLabel(object);

	}

	private String computePropertyLabel(Property object) {
		final StringBuilder label = new StringBuilder();

		if (object.isDerived()) {
			label.append("/"); //$NON-NLS-1$
		}
		label.append(caseStructuralFeature(object));
		if (object.getDefault() != null && !"".equals(object.getDefault().trim())) { //$NON-NLS-1$
			// is the label on multiple lines ?
			if (object.getDefault().contains(NL)) {
				label.append(" = ..."); //$NON-NLS-1$
			} else {
				label.append(" = " + object.getDefault()); //$NON-NLS-1$
			}
		} else if (object.getDefaultValue() instanceof InstanceValue) {
			label.append(" = " + ((InstanceValue) object.getDefaultValue()).getName()); //$NON-NLS-1$
		}

		final StringBuilder propertyModifier = new StringBuilder();
		if (object.getRedefinedElements() != null && object.getRedefinedElements().size() > 0
				&& object.getRedefinedElements().get(0) != null) {
			String parentName = null;
			if (object.getRedefinedElements().get(0).eContainer() instanceof NamedElement) {
				final NamedElement elem = (NamedElement) object.getRedefinedElements().get(0).eContainer();
				parentName = elem.getName();
			}
			String redefPropertyname = object.getRedefinedElements().get(0).getName();
			if (parentName != null && !parentName.isEmpty()) {
				redefPropertyname = parentName + "::" + object.getRedefinedElements().get(0).getName(); //$NON-NLS-1$
			}
			propertyModifier.append("redefines " + redefPropertyname); //$NON-NLS-1$
		}
		if (object.getSubsettedProperties() != null && object.getSubsettedProperties().size() > 0
				&& object.getSubsettedProperties().get(0) != null) {
			String parentName = null;
			if (object.getSubsettedProperties().get(0).eContainer() instanceof NamedElement) {
				final NamedElement elem = (NamedElement) object.getSubsettedProperties().get(0).eContainer();
				parentName = elem.getName();
			}
			String subsetPropertyname = object.getSubsettedProperties().get(0).getName();
			if (parentName != null && !parentName.isEmpty()) {
				subsetPropertyname = parentName + "::" + object.getSubsettedProperties().get(0).getName(); //$NON-NLS-1$
			}
			propertyModifier.append("subsets " + subsetPropertyname); //$NON-NLS-1$
		}
		if (object.isID()) {
			if (propertyModifier.length() > 0) {
				propertyModifier.append(", "); //$NON-NLS-1$
			}
			propertyModifier.append("id"); //$NON-NLS-1$
		}
		if (object.isReadOnly()) {
			if (propertyModifier.length() > 0) {
				propertyModifier.append(", "); //$NON-NLS-1$
			}
			propertyModifier.append("readOnly"); //$NON-NLS-1$
		}
		// Ordered applies on multivalued multiplicity only
		if (object.getUpper() != 1 && object.isOrdered()) {
			if (propertyModifier.length() > 0) {
				propertyModifier.append(", "); //$NON-NLS-1$
			}
			propertyModifier.append("ordered"); //$NON-NLS-1$
		}
		// Unique applies on multivalued multiplicity only
		if (object.getUpper() != 1 && object.isUnique()) {
			if (propertyModifier.length() > 0) {
				propertyModifier.append(", "); //$NON-NLS-1$
			}
			propertyModifier.append("unique"); //$NON-NLS-1$
		}
		if (!object.isUnique() && object.isOrdered()) {
			if (propertyModifier.length() > 0) {
				propertyModifier.append(", "); //$NON-NLS-1$
			}
			propertyModifier.append("seq"); //$NON-NLS-1$
		}
		if (propertyModifier.length() > 0) {
			label.append("{" + propertyModifier + "}"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return label.toString();
	}

	private String computeQualifierLabel(Property object) {
		if (object.getType() != null) {
			return caseNamedElement(object) + SPACED_COLUMN + object.getType().getName();
		}
		return caseNamedElement(object);
	}

	/**
	 * Compute label for association end.
	 *
	 * @param p the {@link Association}'s {@link Property} end.
	 * @return the label of the association end.
	 */
	public String getAssociationEndLabel(Property p) {
		final StringBuilder sb = new StringBuilder(""); //$NON-NLS-1$
		if (p.isDerived()) {
			sb.append("/"); //$NON-NLS-1$
		}
		if (p.getName() != null) {
			sb.append("+ " + p.getName()); //$NON-NLS-1$
		}
		sb.append(caseMultiplicityElement(p));
		return sb.toString();
	}

	/**
	 * Get multiplicity.
	 *
	 * @param lower Lower bound
	 * @param upper Upper bound
	 * @return Multiplicity
	 */
	private String getMultiplicity(int lower, int upper) {
		final StringBuffer label = new StringBuffer();
		if (lower == upper) {
			// [1..1]
			label.append("[" + lower + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (lower == 0 && upper == -1) {
			// [0..*]
			label.append("[*]"); //$NON-NLS-1$
		} else {
			label.append(OPENING_BRACE + lower + ".."); //$NON-NLS-1$
			if (upper == -1) {
				label.append("*]"); //$NON-NLS-1$
			} else {
				label.append(upper + CLOSING_BRACE);
			}
		}
		return label.toString();
	}

	/**
	 * Test if a context dependency is added to the lifeline.
	 *
	 * @param lifeline the lifeline
	 * @return true if any
	 */
	private boolean isDependencyDescribed(Lifeline lifeline) {
		return lifeline.getClientDependencies() != null && lifeline.getClientDependencies().size() > 0
				&& lifeline.getClientDependencies().get(0) != null
				&& lifeline.getClientDependencies().get(0).getSuppliers().size() > 0;
	}

	public static void setStereotypeFilter(boolean filterValue) {
		showStereotypeFilter = filterValue;
	}

	public static void setQualifiedNameFilter(boolean filterValue) {
		showQualifiedNameFilter = filterValue;
	}

}
