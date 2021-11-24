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
package org.eclipse.papyrus.sirius.bundles.tests.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.osgi.util.NLS;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * Utility methods for bundle
 *
 * TODO : move me into the Papyrus API Tests
 */
public class BundleTestsUtils {

	/**
	 * Duplicated fields, because the class org.eclipse.papyrus.bundles.tests.BundleTestsUtils is not distributed
	 */
	public static final String JAVA_VERSION_11 = "JavaSE-11"; //$NON-NLS-1$

	public static final String BUNDLE_REQUIREDEXECUTIONENVIRONMENT = "Bundle-RequiredExecutionEnvironment"; //$NON-NLS-1$

	public static final String BUNDLE_IMPORT_PACKAGE = "Import-Package"; //$NON-NLS-1$

	public static final String BUNDLE_NAME = "Bundle-Name"; //$NON-NLS-1$

	public static final String BUNDLE_VENDOR = "Bundle-Vendor"; //$NON-NLS-1$

	public static final String VENDOR_NAME = "Eclipse Modeling Project"; //$NON-NLS-1$

	public static final String REQUIRE_BUNDLE = "Require-Bundle"; //$NON-NLS-1$

	public static final String AUTOMATIC_MODULE_NAME = "Automatic-Module-Name"; //$NON-NLS-1$

	public static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName"; //$NON-NLS-1$

	/**
	 * The incubation keyword, with a space before it
	 */
	private static final String INCUBATION_KEYWORD = " (Incubation)"; //$NON-NLS-1$


	/**
	 * The allowed java version for the Papyrus-Sirius bundles
	 */
	public static final String JAVA_VERSION_REGEX = Stream.of(JAVA_VERSION_11).map(Pattern::quote).collect(Collectors.joining("|")); //$NON-NLS-1$


	/**
	 *
	 * @param prefix
	 *            the prefix to use to find bundles
	 * @return
	 *         all bundle matching the prefix
	 */
	public static final List<Bundle> getBundleByPrefix(final String prefix) {
		final List<Bundle> papyrusBundle = new ArrayList<>();
		BundleContext context = FrameworkUtil.getBundle(org.eclipse.papyrus.sirius.bundles.tests.internal.BundleTestsUtils.class).getBundleContext();
		org.osgi.framework.Bundle[] bundles = context.getBundles();
		for (int i = 0; i < bundles.length; i++) {
			String currentName = bundles[i].getSymbolicName();
			// currently, on hudson we don't found all existing bundles..., so we keep this sysout until this bug will be resolved.
			System.out.println("bundle name found:" + currentName); //$NON-NLS-1$
			if (currentName.startsWith(prefix)) {
				papyrusBundle.add(bundles[i]);
			}
		}
		return papyrusBundle;
	}

	/**
	 * Tests if the file is owned by the bundle
	 *
	 * @param bundles
	 *            the bundles to check
	 * @param filepath
	 *            the file path
	 */
	public static final void fileTest(final List<Bundle> bundles, final String filepath) {
		StringBuffer buffer = new StringBuffer();
		int nb = 0;
		for (final Bundle current : bundles) {
			URL url = current.getEntry(filepath);
			if (url == null) {
				if (buffer.length() == 0) {
					buffer.append(NLS.bind("The following bundles don't have the file {0}.", filepath)); //$NON-NLS-1$
				}
				buffer.append("\n");//$NON-NLS-1$
				buffer.append(current.getSymbolicName());
				nb++;
			}
		}
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append(nb);
		errorMessage.append(" problems!\n"); //$NON-NLS-1$
		errorMessage.append(buffer.toString());
		Assert.assertTrue(errorMessage.toString(), buffer.toString().isEmpty());
	}

	/**
	 *
	 * @param bundles
	 *            a list of bundles to tests
	 * @param property
	 *            the name of the property to test
	 * @param regex
	 *            a regex to use to test the property value
	 * @param mustBeNull
	 *            if <code>true</code>, the tested property must be <code>null</code>
	 * @param onlyOnJavaProject
	 *            do the check only on java project
	 */
	public static final void testManifestProperty(final List<Bundle> bundles, final String property, final String regex,
			final boolean mustBeNull, final boolean onlyOnJavaProject) {
		org.hamcrest.Matcher<String> regexMatcher = new org.hamcrest.BaseMatcher<>() {

			@Override
			public boolean matches(Object item) {
				return item instanceof String && ((String) item).matches(regex);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("Matching regex("); //$NON-NLS-1$
				description.appendValue(regex);
				description.appendText(")"); //$NON-NLS-1$
			}

		};

		testManifestProperty(bundles, property, regexMatcher, mustBeNull, onlyOnJavaProject);
	}

	/**
	 *
	 * @param bundles
	 *            a list of bundles to tests
	 * @param property
	 *            the name of the property to test
	 * @param matcher
	 *            the matcher to use to test the property value
	 * @param mustBeNull
	 *            if <code>true</code>, the tested property must be <code>null</code>
	 * @param onlyOnJavaProject
	 *            do the check only on java project
	 */
	public static final void testManifestProperty(final List<Bundle> bundles, final String property,
			final org.hamcrest.Matcher<String> matcher, final boolean mustBeNull, final boolean onlyOnJavaProject) {
		String message = null;
		int nb = 0;
		for (final Bundle current : bundles) {
			if (onlyOnJavaProject && !BundleTestsUtils.isJavaProject(current)) {
				continue; // useful for oep.infra.gmfdiag.css.theme for example
			}
			final String value = current.getHeaders().get(property);
			boolean result = false;
			if (mustBeNull) {
				result = (value == null);
			} else if (value != null) {
				result = matcher.matches(value); // Don't fail yet if invalid
			}
			if (!result) {
				if (message == null) {
					message = "Wrong " + property + " for :"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				message += "\n "; //$NON-NLS-1$
				message += current.getSymbolicName();
				nb++;
			}
		}
		Assert.assertNull(nb + " problems!", message); //$NON-NLS-1$
	}

	/**
	 *
	 * @param bundle
	 *            a bundle
	 * @return
	 *         <code>true</code> if the bundle represents a Java Project
	 */
	// TODO : duplicated of Papyrus, not sure of the efficiency of this method...
	public static boolean isJavaProject(final Bundle bundle) {
		// we are looking for folders "org/eclipse/papyrus" that contains classes. If not, it is not a Java project
		URL res = bundle.getResource("org/eclipse/papyrus"); //$NON-NLS-1$
		return res != null;
	}

	/**
	 * Method to check the bundle name contains Incubation or not
	 *
	 * @param bundles
	 *            a list of bundle
	 * @param mustContains
	 *            if <code>true</code> the bundle name must contains the Incubation word
	 */
	public static final void checkBundleNameContainsIncubationTest(final List<Bundle> bundles, final boolean mustContains) {
		org.hamcrest.Matcher<String> matcher = new BaseMatcher<>() {

			@Override
			public boolean matches(Object item) {
				return item instanceof String && mustContains == ((String) item).endsWith(INCUBATION_KEYWORD);
			}

			@Override
			public void describeTo(Description description) {
				if (mustContains) {
					description.appendText("Does not contain "); //$NON-NLS-1$
				} else {
					description.appendText("Does contain "); //$NON-NLS-1$
				}

				description.appendText(INCUBATION_KEYWORD);
			}
		};
		testManifestProperty(bundles, BundleTestsUtils.BUNDLE_NAME, matcher, false, false);
	}

	/**
	 * This method checks the Bundle Name doesn't contains double space
	 *
	 * @param bundles
	 *            a list of bundles
	 */
	public static final void checkBundleNameDontContainsDoubleSpace(final List<Bundle> bundles) {
		org.hamcrest.Matcher<String> matcher = new BaseMatcher<>() {

			@Override
			public boolean matches(Object item) {
				return item instanceof String && !((String) item).contains("  ");//$NON-NLS-1$
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("Does not contain double space"); //$NON-NLS-1$
				description.appendText(INCUBATION_KEYWORD);
			}
		};
		testManifestProperty(bundles, BUNDLE_NAME, matcher, false, false);
	}

	/**
	 *
	 * @param bundles
	 *            a list of bundle
	 * @param toTest
	 *            the string for which we want to verify its spelling (uppercase/lowercase
	 */
	public static final void checkBundleNameCaseSentivity(final List<Bundle> bundles, final String toTest) {
		org.hamcrest.Matcher<String> matcher = new BaseMatcher<>() {

			@Override
			public boolean matches(Object item) {
				if (!(item instanceof String)) {
					return false;
				}
				final String string = (String) item;
				if (string.toLowerCase().contains(toTest.toLowerCase())) {
					return string.contains(toTest);
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("UpperCase/LowerCase are not as expected"); //$NON-NLS-1$
				description.appendText(INCUBATION_KEYWORD);
			}
		};

		testManifestProperty(bundles, BundleTestsUtils.BUNDLE_NAME, matcher, false, false);
	}

	/**
	 *
	 * @param bundles
	 *            a list of bundle
	 * @param prefix
	 *            the expected prefix for all bundles
	 */
	public static final void checkBundleNamePrefix(final List<Bundle> bundles, final String prefix) {
		org.hamcrest.Matcher<String> matcher = new BaseMatcher<>() {

			@Override
			public boolean matches(Object item) {
				return item instanceof String && ((String) item).startsWith(prefix);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("Does not starts with "); //$NON-NLS-1$
				description.appendText(prefix);
			}
		};
		testManifestProperty(bundles, BundleTestsUtils.BUNDLE_NAME, matcher, false, false);
	}

	/**
	 *
	 * @param importPackageFound
	 *            the imported package found for each bundles
	 * @param allowedImportedPackages
	 *            the allowed imported packages by bundle
	 * @param packageAlwaysAllowed
	 */
	public static void checkImportedPackages(final Map<Bundle, List<BundleRepresentation>> importPackageFound, final Map<String, List<String>> allowedImportedPackages, final List<String> packageAlwaysAllowed) {
		StringBuilder builder = null;
		int nbProblems = 0;
		for (final Entry<Bundle, List<BundleRepresentation>> entrySet : importPackageFound.entrySet()) {
			final String currentBundleName = entrySet.getKey().getSymbolicName();
			// the plugin can have imported package. We check them
			for (final BundleRepresentation representation : entrySet.getValue()) {
				if (!packageAlwaysAllowed.contains(representation.getSymbolicName())) {
					final List<String> allowedPackages = allowedImportedPackages.get(currentBundleName);
					if (null == allowedPackages || (null != allowedPackages && !allowedPackages.contains(representation.getSymbolicName()))) {
						if (null == builder) {
							builder = new StringBuilder("We found forbidden import packages:\n"); //$NON-NLS-1$
						}
						nbProblems++;
						builder.append(NLS.bind("The plugin {0} declares {1} as imported package and it should not\n.", currentBundleName, representation.getSymbolicName())); //$NON-NLS-1$
					}
				}
			}
		}
		if (null != builder) {
			builder.insert(0, NLS.bind("{0} problems founds.", nbProblems)); //$NON-NLS-1$
		}
		Assert.assertEquals(null == builder ? "" : builder.toString(), 0, nbProblems); //$NON-NLS-1$
	}

	/**
	 *
	 * @param dependenciesFound
	 *            the dependencies declared, by bundle
	 * @param allowedReexportedDependencies
	 *            the allowed reexported dependencies, for each bundles
	 * @param dependenciesAlwaysAllowed
	 *            the reexported dependencies always allowed
	 */
	public static void checkReexportedDependencies(final Map<Bundle, List<BundleRepresentation>> dependenciesFound, final Map<String, List<String>> allowedReexportedDependencies, final List<String> dependenciesAlwaysAllowed) {
		StringBuilder builder = null;
		int nbProblems = 0;
		for (final Entry<Bundle, List<BundleRepresentation>> entrySet : dependenciesFound.entrySet()) {
			final String currentBundleName = entrySet.getKey().getSymbolicName();
			// the plugin can have imported package. We check them
			for (final BundleRepresentation representation : entrySet.getValue()) {
				if (representation.isReexported()) {
					if (dependenciesAlwaysAllowed.contains(representation.getSymbolicName())) {
						continue;
					}
					final List<String> allowedPackages = allowedReexportedDependencies.get(currentBundleName);
					if (null == allowedPackages || (null != allowedPackages && !allowedPackages.contains(representation.getSymbolicName()))) {
						if (null == builder) {
							builder = new StringBuilder("We found forbidden reexported dependencies :\n"); //$NON-NLS-1$
						}
						nbProblems++;
						builder.append(NLS.bind("The plugin {0} declares {1} as reexported dependencies and it should not.\n", currentBundleName, representation.getSymbolicName())); //$NON-NLS-1$
					}
				}
			}
		}
		if (null != builder) {
			builder.insert(0, NLS.bind("{0} problems founds. ", nbProblems)); //$NON-NLS-1$
		}
		Assert.assertEquals(null == builder ? "" : builder.toString(), 0, nbProblems); //$NON-NLS-1$
	}

	/**
	 * This tests checks that the field Automatic-Module-Name exists and has the same value than the Symbolic-Name
	 *
	 * TODO : add me in Papyrus
	 *
	 * @param bundles
	 *            a list of bundle
	 *
	 */
	public static void checkAutomaticBundleName(List<Bundle> bundles) {
		StringBuilder builder = new StringBuilder();
		for (Bundle current : bundles) {
			final String bundleSymbolicName = current.getHeaders().get(BUNDLE_SYMBOLIC_NAME);
			final String value = current.getHeaders().get(AUTOMATIC_MODULE_NAME);
			if (null == value) {
				builder.append(NLS.bind("The plugin {0} don't declare the property Automatic-Module-Name.\n", bundleSymbolicName)); //$NON-NLS-1$
			} else if (!bundleSymbolicName.equals(value)) {
				builder.append(NLS.bind("The plugin {0} don't have the same value for the field Automatic-Module-Name and Symbolic-Name.\n", bundleSymbolicName)); //$NON-NLS-1$
			}
		}
		Assert.assertTrue(builder.toString(), builder.length() == 0);

	}

}
