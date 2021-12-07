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
 *  Rengin Battal (ARTAL) - rengin.battal@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.all;

import org.eclipse.papyrus.junit.framework.classification.ClassificationSuite;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Abstraction_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_AssociationClass_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Association_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_CompositeAssociation_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Containment_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Dependency_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_ElementImport_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_GeneralizationSet_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Generalization_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_InformationFlow_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_InstanceSpecification_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_InterfaceRealization_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Link_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_PackageImport_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_PackageMerge_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Realization_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_SharedAssociation_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Substitution_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.edges.Edge_Usage_CreationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All edges creation tests
 **/
@RunWith(ClassificationSuite.class)
@SuiteClasses({
		Edge_Abstraction_CreationTest.class,
		Edge_Association_CreationTest.class,
		Edge_AssociationClass_CreationTest.class,
		Edge_CompositeAssociation_CreationTest.class,
		Edge_SharedAssociation_CreationTest.class,
		Edge_Containment_CreationTest.class,
		Edge_Dependency_CreationTest.class,
		Edge_ElementImport_CreationTest.class,
		Edge_Generalization_CreationTest.class,
		Edge_GeneralizationSet_CreationTest.class,
		Edge_InformationFlow_CreationTest.class,
		Edge_InstanceSpecification_CreationTest.class,
		Edge_InterfaceRealization_CreationTest.class,
		Edge_Link_CreationTest.class,
		Edge_PackageImport_CreationTest.class,
		Edge_PackageMerge_CreationTest.class,
		Edge_Realization_CreationTest.class,
		Edge_SharedAssociation_CreationTest.class,
		Edge_Substitution_CreationTest.class,
		Edge_Usage_CreationTest.class,
})
public class AllEdgesCreationTests {
}
