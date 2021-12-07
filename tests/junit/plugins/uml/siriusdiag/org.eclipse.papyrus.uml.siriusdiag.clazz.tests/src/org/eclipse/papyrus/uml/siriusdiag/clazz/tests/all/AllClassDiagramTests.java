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
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All Class diagram tests
 **/
@RunWith(ClassificationSuite.class)
@SuiteClasses({
		AllTopNodesCreationTests.class,
		AllSubNodesCreationTests.class,
		AllEdgesCreationTests.class,
		AllTopNodesDropTests.class,
		AllSubNodesDropTests.class,
		AllDeleteTests.class,
})
public class AllClassDiagramTests {
}
