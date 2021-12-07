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
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes.SubNode_ClassToPackage_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes.SubNode_OperationToClass_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes.SubNode_PackageToModel_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes.SubNode_PropertyToClass_DropTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All sub nodes drop tests
 */
@RunWith(ClassificationSuite.class)
@SuiteClasses({
		SubNode_ClassToPackage_DropTest.class,
		SubNode_OperationToClass_DropTest.class,
		SubNode_PackageToModel_DropTest.class,
		SubNode_PropertyToClass_DropTest.class,
})
public class AllSubNodesDropTests {
}
