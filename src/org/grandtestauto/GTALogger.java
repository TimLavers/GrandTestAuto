package org.grandtestauto;

import org.jetbrains.annotations.Nullable;

/**
 * For logging test results and other testing messages.
 *
 * @author Tim Lavers
 */
public interface GTALogger {

    public void log(String message, @Nullable Throwable t);
}
