/*****************************************************************************
 * Copyright (c) 2021 CEA LIST.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Vincent Lorenzo (CEA LIST) vincent.lorenzo@cea.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.bundles.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.papyrus.sirius.bundles.tests.internal.BundleDependenciesVersionTestsUtils;
import org.eclipse.papyrus.sirius.bundles.tests.internal.BundleRepresentation;
import org.eclipse.papyrus.sirius.bundles.tests.internal.BundleTestsUtils;
import org.eclipse.papyrus.sirius.bundles.tests.internal.PapyrusSiriusBundleUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * TODO duplicated and adapted from Model2doc
 * + In addition, there is a test about Automatic-module-name field. This test doesn't yet exist on Model2doc.
 * 
 */
public class PapyrusSiriusBundleTest {

	/**
	 * The list of the sirius bundles
	 */
	private static List<Bundle> siriusBundles;

	/**
	 * This map contains the list of dependencies of for each analyzed bundles.
	 */
	private static Map<Bundle, List<BundleRepresentation>> mapOfDependencies;

	/**
	 * This map contains the list of dependencies of for each analyzed bundles.
	 */
	private static Map<Bundle, List<BundleRepresentation>> mapOfImportedPackages;

	/**
	 * A map with the allowed imported package for each bundle
	 */
	private static Map<String, List<String>> importedPackageExceptions;

	/**
	 * a list of imported-package always allowed
	 */
	private static List<String> packageAlwaysAllowed;

	/**
	 * a map with the allowed reexported dependencies for each bundle
	 */
	private static Map<String, List<String>> reexportedDependenciesExceptions;

	/**
	 * a list of reexported dependencies always allowed
	 */
	private static List<String> dependenciesAlwaysAllowed;

	/**
	 * the Papyrus-Sirius Bundle name prefix
	 */
	private static final String PAPYRUS_SIRIUS_BUNDLE_NAME_PREFIX = "Papyrus-Sirius -"; //$NON-NLS-1$

	@BeforeClass
	public static void initTestsClass() {
		siriusBundles = PapyrusSiriusBundleUtils.getPapyrusSiriusBundles();
		mapOfDependencies = new HashMap<>();
		for (final Bundle bundle : siriusBundles) {
			mapOfDependencies.put(bundle, PapyrusSiriusBundleUtils.getBundleDependencies(bundle));
		}
		mapOfImportedPackages = new HashMap<>();
		for (final Bundle bundle : siriusBundles) {
			final List<BundleRepresentation> representations = PapyrusSiriusBundleUtils.getBundleImportPackage(bundle);
			if (representations != null && !representations.isEmpty()) {
				mapOfImportedPackages.put(bundle, representations);
			}
		}

		// currently, there is no exception defined by plugin
		importedPackageExceptions = new HashMap<>();
		// there is an exception always valid for osgi (due to EMF generation)
		packageAlwaysAllowed = Collections.singletonList("org.osgi.framework"); //$NON-NLS-1$

		// currently, we don't have reexported dependencies always allowed
		dependenciesAlwaysAllowed = Collections.emptyList();

		reexportedDependenciesExceptions = new HashMap<>();

		// to ease usage of LibreOffice API!
		List<String> allowedReexport = new ArrayList<>();
		allowedReexport.add("org.apache.commons.jxpath"); //$NON-NLS-1$
		allowedReexport.add("org.apache.xerces"); //$NON-NLS-1$
		allowedReexport.add("org.apache.xml.serializer"); //$NON-NLS-1$
		reexportedDependenciesExceptions.put("org.eclipse.papyrus.sirius.odt.lib", allowedReexport); //$NON-NLS-1$
	}



	/**
	 * This test check the bundle's name start with Papyrus-Sirius
	 */
	@Test
	public void checkBundleNameContaineSirius() {
		BundleTestsUtils.checkBundleNamePrefix(siriusBundles, PAPYRUS_SIRIUS_BUNDLE_NAME_PREFIX);
	}


	/**
	 * Tests the java version
	 */
	@Test
	public void javaVersionTest() {
		BundleTestsUtils.testManifestProperty(siriusBundles, BundleTestsUtils.BUNDLE_REQUIREDEXECUTIONENVIRONMENT, BundleTestsUtils.JAVA_VERSION_REGEX, false, true);
	}

	/**
	 * Tests if the file about.html is included to the plugin
	 */
	@Test
	public void aboutTest() {
		BundleTestsUtils.fileTest(siriusBundles, "/about.html"); //$NON-NLS-1$
	}

	/**
	 * This test checks EMF is written in upper case in the bundle name
	 */
	@Test
	public void checkBundleNameEMFSpelling() {
		BundleTestsUtils.checkBundleNameCaseSentivity(siriusBundles, "EMF"); //$NON-NLS-1$
	}

	/**
	 * This test checks GMF is written in upper case in the bundle name
	 */
	@Test
	public void checkBundleNameGMFSpelling() {
		BundleTestsUtils.checkBundleNameCaseSentivity(siriusBundles, "GMF"); //$NON-NLS-1$
	}

	/**
	 * This test checks NatTable is written with a N and a T in upper case in the bundle name
	 */
	@Test
	public void checkBundleNameNatTableSpelling() {
		BundleTestsUtils.checkBundleNameCaseSentivity(siriusBundles, "NatTable"); //$NON-NLS-1$
	}

	/**
	 * This test checks there is no double space in the bundle name
	 */
	@Test
	public void checkNoDoubleSpaceInBundleName() {
		BundleTestsUtils.checkBundleNameDontContainsDoubleSpace(siriusBundles);
	}

	/**
	 * This tests checks there is no imported packages (with some exceptions)
	 */
	@Test
	public void checkImportPackage() {
		BundleTestsUtils.checkImportedPackages(mapOfImportedPackages, importedPackageExceptions, packageAlwaysAllowed);
	}

	/**
	 * This tests checks there is no reexported dependencies (with some exceptions)
	 */
	@Test
	public void checkReexportedDependencies() {
		// temporary intermediate map creation , to avoid to clean deprecated plugins for running this JUnit test
		Map<Bundle, List<BundleRepresentation>> newMap = new HashMap<>();
		for (Bundle bundle : mapOfDependencies.keySet()) {
			if (false == bundle.getSymbolicName().contains("org.eclipse.papyrus.sirius.documentview")) { //$NON-NLS-1$
				newMap.put(bundle, mapOfDependencies.get(bundle));
			}
		}
		BundleTestsUtils.checkReexportedDependencies(newMap, reexportedDependenciesExceptions, dependenciesAlwaysAllowed);
	}

	/**
	 * Tests the provider name (should be Eclipse Modeling Project)
	 */
	@Test
	public void vendorTest() {
		BundleTestsUtils.testManifestProperty(siriusBundles, BundleTestsUtils.BUNDLE_VENDOR, BundleTestsUtils.VENDOR_NAME, false, false);
	}

	/**
	 * Check if the bundles contains the incubation word
	 */
	@Test
	public void checkBundleNameContainsIncubationTest() {
		BundleTestsUtils.checkBundleNameContainsIncubationTest(siriusBundles, true);
	}

	/**
	 * This tests checks all plugins depends on the same version of others ones
	 */
	@Test
	public void checkUnicityVersionOfDependencies() {
		// temporary intermediate map creation , to avoid to clean deprecated plugins for running this JUnit test
		Map<Bundle, List<BundleRepresentation>> newMap = new HashMap<>();
		for (Bundle bundle : mapOfDependencies.keySet()) {
			newMap.put(bundle, mapOfDependencies.get(bundle));
		}
		BundleDependenciesVersionTestsUtils.checkUnicityVersionOfDependencies(newMap);
	}

	/**
	 * This test checks a minor/major version is defined for each dependency
	 */
	@Test
	public void checkDependenciesVersionAreDefined() {
		BundleDependenciesVersionTestsUtils.checkDependencyVersionIsDeclared(mapOfDependencies);
	}

	/**
	 * This test checks the minor version is always declared as included
	 */
	@Test
	public void checkMinDependenciesVersionAreIncluded() {
		BundleDependenciesVersionTestsUtils.checkIncludedMinorDependencyVersion(mapOfDependencies);
	}

	/**
	 * This test checks the minor version is always declared as excluded
	 */
	@Test
	public void checkMaxDependenciesVersionAreExcluded() {
		BundleDependenciesVersionTestsUtils.checkDependencyVersionIsDeclared(mapOfDependencies);
	}

	/**
	 * This tests checks that the field Automatic-Module-Name exists and has the same value than the Symbolic-Name
	 */
	@Test
	public void checkHasAutomaticModuleName() {
		BundleTestsUtils.checkAutomaticBundleName(siriusBundles);
	}
}
