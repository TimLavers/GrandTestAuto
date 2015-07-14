package org.grandtestauto;

/**
 * @author Tim Lavers
 */
class PackageResultImpl implements PackageResult {

    private Long start;
    private Long stop;
    private Boolean passed;
    private String errorMessage;

    PackageResultImpl() {
        this.start = System.currentTimeMillis();
    }

    public boolean passed() {
        return passed != null ? passed : false;
    }

    public long timeTakenInMillis() {
        assert (passed != null || errorMessage != null);
        return stop - start;
    }

    public String errorMessage() {
        return errorMessage;
    }

    void setResult( boolean passed ) {
        assert this.passed == null;
        assert errorMessage == null;
        this.passed = passed;
        stop = System.currentTimeMillis();
    }

    void setErrorMessage( String errorMessage ) {
        assert passed == null;
        assert this.errorMessage == null;
        this.errorMessage = errorMessage;
        stop = System.currentTimeMillis();
    }
}