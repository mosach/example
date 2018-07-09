package com.spring.example;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xhtmlrenderer.pdf.DefaultPDFCreationListener;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.simple.PDFRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

/**
 * <p/>
 * Min2pdf supports headless rendering of XHTML documents, outputting
 * to PDF format. There are two static utility methods, one for rendering
 * a {@link java.net.URL}, {@link #renderToPDF(String, String)} and one
 * for rendering a {@link File}, {@link #renderToPDF(File, String)}</p>
 * <p>You can use this utility from the command line by passing in
 * the URL or file location as first parameter, and PDF path as second
 * parameter:
 * <pre>
 * java -cp %classpath% Min2pdf <url> <pdf>
 * </pre>
 *
 * @author Minaret
 * @author Pete Brant
 * @author Patrick Wright
 */
public class Min2pdf extends DefaultPDFCreationListener {
    private static final Map versionMap = new HashMap();

    private int _exitValue = 0;

    static {
        versionMap.put("1.2", new Character(PdfWriter.VERSION_1_2));
        versionMap.put("1.3", new Character(PdfWriter.VERSION_1_3));
        versionMap.put("1.4", new Character(PdfWriter.VERSION_1_4));
        versionMap.put("1.5", new Character(PdfWriter.VERSION_1_5));
        versionMap.put("1.6", new Character(PdfWriter.VERSION_1_6));
        versionMap.put("1.7", new Character(PdfWriter.VERSION_1_7));
    }

    private void customize(ITextRenderer renderer) throws DocumentException, IOException {
	renderer.setListener(this);		// listen for PDFCreationListener calls
    }

    /**
     * Implements PDFCreationListener.
     * Checks for a subject metadata value in XHTML file with a page count.
     * Validates that page count against the PDF page count to check for
     * rendering issues and sets the subject accordingly.
     * <pre>
     * Success string: X source pages
     *   Error string: Page count mismatch (X source pages vs. Y PDF pages)
     * </pre>
     */
    public void preWrite(ITextRenderer iTextRenderer, int pageCount) {
	ITextOutputDevice od = iTextRenderer.getOutputDevice();
	Document doc = iTextRenderer.getDocument();
	//String s = od.getMetadataByName("subject");	// existing subject line
	if (doc != null) {
	    Node node = doc.getLastChild();
	    if ((node != null) && (node.getNodeType() == Node.COMMENT_NODE)) {
		String s = node.getNodeValue();
		int k = s.indexOf(" source page");
		if (k > 0) {
		    try {
			int j = 0;
			while (s.charAt(j) == ' ') {
			    j++;
			}
			String v = s.substring(j, k);
			int num = Integer.parseInt(v);
			StringBuffer sb = new StringBuffer();
			if (num != pageCount) {
			    _exitValue = 2;
			    sb.append("Page count mismatch (");
			}
			sb.append(num);
			sb.append(" source page");
			if (num > 1) {
			    sb.append("s");
			}
			if (num != pageCount) {
			    sb.append(" vs. ");
			    sb.append(pageCount);
			    sb.append(" PDF page");
			    if (pageCount > 1) {
				sb.append("s");
			    }
			    sb.append(")");
			}
			s = sb.toString();
			od.setMetadata("subject", s);		// subject line
		    } catch (NumberFormatException ignore) {
		    }
		}
	    }
	}
    }

    /**
     * Renders the XML file at the given URL as a PDF file
     * at the target location.
     *
     * @param url url for the XML file to render
     * @param pdf path to the PDF file to create
     * @throws IOException       if the URL or PDF location is
     *                           invalid
     * @throws DocumentException if an error occurred
     *                           while building the Document.
     */
    public void renderToPDF(String url, String pdf)
            throws IOException, DocumentException {

        renderToPDF(url, pdf, null);
    }
    /**
     * Renders the XML file at the given URL as a PDF file
     * at the target location.
     *
     * @param url url for the XML file to render
     * @param pdf path to the PDF file to create
     * @param pdfVersion version of PDF to output; null uses default version
     * @throws IOException       if the URL or PDF location is
     *                           invalid
     * @throws DocumentException if an error occurred
     *                           while building the Document.
     */
    public void renderToPDF(String url, String pdf, Character pdfVersion)
            throws IOException, DocumentException {

        ITextRenderer renderer = new ITextRenderer();
	customize(renderer);			// Minaret customizations
        renderer.setDocument(url);
        if (pdfVersion != null) renderer.setPDFVersion(pdfVersion.charValue());
        doRenderToPDF(renderer, pdf);
    }

    /**
     * Renders the XML file as a PDF file at the target location.
     *
     * @param file XML file to render
     * @param pdf  path to the PDF file to create
     * @throws IOException       if the file or PDF location is
     *                           invalid
     * @throws DocumentException if an error occurred
     *                           while building the Document.
     */
    public void renderToPDF(File file, String pdf)
            throws IOException, DocumentException {

        renderToPDF(file, pdf, null);
    }

    /**
     * Renders the XML file as a PDF file at the target location.
     *
     * @param file XML file to render
     * @param pdf  path to the PDF file to create
     * @param pdfVersion version of PDF to output; null uses default version
     * @throws IOException       if the file or PDF location is
     *                           invalid
     * @throws DocumentException if an error occurred
     *                           while building the Document.
     */
    public void renderToPDF(File file, String pdf, Character pdfVersion)
	throws IOException, DocumentException {

        ITextRenderer renderer = new ITextRenderer();
	customize(renderer);			// Minaret customizations
        renderer.setDocument(file);
        if (pdfVersion != null) renderer.setPDFVersion(pdfVersion.charValue());
        doRenderToPDF(renderer, pdf);
    }

    /**
     * Internal use, runs the render process
     * @param renderer
     * @param pdf
     * @throws com.lowagie.text.DocumentException
     * @throws java.io.IOException
     */
    private void doRenderToPDF(ITextRenderer renderer, String pdf)
            throws IOException, DocumentException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(pdf);
            renderer.layout();
            renderer.createPDF(os);

            os.close();
            os = null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Renders a file or URL to a PDF. Command line use: first
     * argument is URL or file path, second
     * argument is path to PDF file to generate.
     *
     * @param args see desc
     * @throws IOException if source could not be read, or if
     * PDF path is invalid
     * @throws DocumentException if an error occurs while building
     * the document
     */
    public static void main(String[] args) throws IOException, DocumentException {
        if (args.length < 2) {
            usage("Incorrect argument list.");
        }
	Min2pdf converter = new Min2pdf();
        Character pdfVersion = null;
        if (args.length == 3) {
            pdfVersion = checkVersion(args[2]);
        }
        String url = args[0];
        if (url.indexOf("://") == -1) {
            // maybe it's a file
            File f = new File(url);
            if (f.exists()) {
                converter.renderToPDF(f, args[1], pdfVersion);
            } else {
                usage("File to render is not found: " + url);
            }
        } else {
            converter.renderToPDF(url, args[1], pdfVersion);
        }
	System.exit(converter._exitValue);
    }

    private static Character checkVersion(String version) {
        final Character val = (Character) versionMap.get(version.trim());
        if (val == null) {
            usage("Invalid PDF version number; use 1.2 through 1.7");
        }
        return val;
    }

    /** prints out usage information, with optional error message
     * @param err
     */
    private static void usage(String err) {
        if (err != null && err.length() > 0) {
            System.err.println("==>" + err);
        }
        System.err.println("Usage: ... url pdf [version]");
        System.err.println("   where version (optional) is between 1.2 and 1.7");
        System.exit(1);
    }
}
