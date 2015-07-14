package org.grandtestauto;

/**
 * @author Tim Lavers
 */
public interface PackageResult {
    boolean passed();
    long timeTakenInMillis();
    String errorMessage();
}