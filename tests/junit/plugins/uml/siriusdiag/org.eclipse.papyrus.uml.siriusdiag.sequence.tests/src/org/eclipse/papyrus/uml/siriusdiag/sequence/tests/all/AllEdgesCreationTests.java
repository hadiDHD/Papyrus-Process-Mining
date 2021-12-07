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
package org.eclipse.papyrus.uml.siriusdiag.sequence.tests.all;

import org.eclipse.papyrus.junit.framework.classification.ClassificationSuite;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_AsynchroMessage_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_AsynchroMessage_LifelineExecution_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_DeleteMessage_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_DurationConstraint_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_DurationObservation_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_GeneralOrdering_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_MessageFound_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_MessageLost_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_Message_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_ReplyMessage_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.edges.Edge_SynchroMessage_CreationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All Edges creation tests
 */
@RunWith(ClassificationSuite.class)
@SuiteClasses({
			Edge_AsynchroMessage_CreationTest.class,
			Edge_AsynchroMessage_LifelineExecution_CreationTest.class,
			Edge_SynchroMessage_CreationTest.class,
			Edge_Message_CreationTest.class,
			Edge_DeleteMessage_CreationTest.class,
			Edge_ReplyMessage_CreationTest.class,
			Edge_DurationConstraint_CreationTest.class,
			Edge_DurationObservation_CreationTest.class,
			Edge_GeneralOrdering_CreationTest.class,
			Edge_MessageFound_CreationTest.class,
			Edge_MessageLost_CreationTest.class
	})
	public class AllEdgesCreationTests {

	}