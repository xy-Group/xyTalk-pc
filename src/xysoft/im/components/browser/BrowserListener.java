package xysoft.im.components.browser;

/**
 * Implementation of <code>BrowserListener</code> allows for handling when documents
 * have been downloaded via a <code>BrowserViewer</code> implementation.
 *
 * @author Derek DeMoro
 */
public interface BrowserListener {

    /**
     * Called when a document/page has been fully loaded.
     *
     * @param documentURL URL of the document that was loaded.
     */
    void documentLoaded(String documentURL);
}
