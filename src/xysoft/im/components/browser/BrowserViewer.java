package xysoft.im.components.browser;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class describing a particular type of component capable of rendering html.
 *
 * @author Derek DeMoro
 * @see NativeBrowserViewer
 */
public abstract class BrowserViewer extends JPanel {
	private static final long serialVersionUID = -5389246902135069702L;
	private List<BrowserListener> listeners = new ArrayList<>();

    /**
     * Add a BrowserListener.
     *
     * @param listener the listener.
     */
    public void addBrowserListener(BrowserListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a BrowserListener.
     *
     * @param listener the listener.
     */
    public void removeBrowserListener(BrowserListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fire all BrowserListeners.
     *
     * @param document the document that has been downloaded.
     */
    public void fireBrowserListeners( String document )
    {
        for ( BrowserListener listener : listeners )
        {
            try
            {
                listener.documentLoaded( document );
            }
            catch ( Exception e )
            {
               // Log.error( "A BrowserListener (" + listener + ") threw an exception while processing a 'downloaded' event for document: " + document, e );
            }
        }
    }


    public void documentLoaded(String document) {
        fireBrowserListeners(document);
    }

    /**
     * Should create the UI necessary to display html.
     */
    public abstract void initializeBrowser();

    /**
     * Should load the given url.
     *
     * @param url the url to load.
     */
    public abstract void loadURL(String url);

    /**
     * Should go back in history one page.
     */
    public abstract void goBack();

}
