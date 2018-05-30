package xysoft.im.components.browser;



/**
 * Responsible for determining what type of Browser to return. On windows,
 * either IE or Mozilla will be returned. Otherwise, we will return a simple
 * HTMLViewer using JDK 1.4+ HTMLEditor.
 */
public class BrowserFactory {

	
    /**
     * Empty Constructor.
     */
    private BrowserFactory() {

    }

    /**
     * Returns the Browser UI to use for system Spark is currently running on.
     *
     * @return the BrowserViewer.
     * @see NativeBrowserViewer
     * @see HTMLViewer
     */
    public static BrowserViewer getBrowser() {
    	BrowserViewer browserViewer = new EmbeddedBrowserViewer();
    	
        browserViewer.initializeBrowser();
        return browserViewer;
    }
}
