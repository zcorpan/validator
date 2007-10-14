/*
 * Copyright (c) 2005, 2006 Henri Sivonen
 * Copyright (c) 2007 Mozilla Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package nu.validator.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.validator.htmlparser.common.DoctypeExpectation;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.sax.HtmlParser;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

import com.thaiopensource.validate.IncorrectSchemaException;


public class Html5ConformanceCheckerTransaction extends
        VerifierServletTransaction {

    private static final char[] SERVICE_TITLE = "(X)HTML5 Conformance Checking Service ".toCharArray();

    private static final char[] TECHNOLOGY_PREVIEW = "Technology Preview".toCharArray();

    private static final char[] RESULTS_TITLE = "(X)HTML5 conformance checking results".toCharArray();

    private static final char[] FOR = " for ".toCharArray();
    
    private static final String SUCCESS_HTML = "The document conforms to the machine-checkable conformance requirements for HTML5 (subject to the utter previewness of this service).";

    private static final String SUCCESS_XHTML = "The document conforms to the machine-checkable conformance requirements for XHTML5 (subject to the utter previewness of this service).";

    private static final String FAILURE_HTML = "There were errors. (Tried in the text/html mode.)";

    private static final String FAILURE_XHTML = "There were errors. (Tried in the XHTML mode.)";

    private boolean usingHtml = false;
    
    public Html5ConformanceCheckerTransaction(HttpServletRequest request,
            HttpServletResponse response) {
        super(request, response);
    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#successMessage()
     */
    protected String successMessage() throws SAXException {
        if (usingHtml) {
            return SUCCESS_HTML;
        } else {
            return SUCCESS_XHTML;
        }
    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#loadDocAndSetupParser()
     */
    protected void loadDocAndSetupParser() throws SAXException, IOException, IncorrectSchemaException, SAXNotRecognizedException, SAXNotSupportedException {
        setAllowGenericXml(false);
        setAcceptAllKnownXmlTypes(false);
        setAllowHtml(true);
        setAllowXhtml(true);
        loadDocumentInput();
        String type = documentInput.getType();
        if ("text/html".equals(type)) {
            validator = validatorByDoctype(HTML5_SCHEMA);
            usingHtml = true;
            newHtmlParser();
            htmlParser.setDoctypeExpectation(DoctypeExpectation.HTML);
            htmlParser.setDocumentModeHandler(this);
            htmlParser.setContentHandler(validator.getContentHandler());
            reader = htmlParser;
        } else {
            validator = validatorByDoctype(XHTML5_SCHEMA);
            setupXmlParser();
            if (!("application/xhtml+xml".equals(type) || "application/xml".equals(type))) {
                String message = "The preferred Content-Type for XHTML5 is application/xhtml+xml. The Content-Type was " + type + ".";
                SAXParseException spe = new SAXParseException(message, null, documentInput.getSystemId(), -1, -1);
                errorHandler.warning(spe);
            }
        }

    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#setupAndStartEmission()
     */
    protected void setup() throws ServletException {
        // No-op
    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#emitTitle()
     */
    void emitTitle(boolean markupAllowed) throws SAXException {
        if (willValidate()) {
            emitter.characters(RESULTS_TITLE);
            if (document != null) {
                emitter.characters(FOR);                
                emitter.characters(scrub(document));                
            }
        } else {
            emitter.characters(SERVICE_TITLE);
            if (markupAllowed) {
                emitter.startElement("span");
                emitter.characters(TECHNOLOGY_PREVIEW);
                emitter.endElement("span");
            }
        }
    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#tryToSetupValidator()
     */
    protected void tryToSetupValidator() throws SAXException, IOException, IncorrectSchemaException {
        // No-op
    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#failureMessage()
     */
    protected String failureMessage() throws SAXException {
        if (usingHtml) {
            return FAILURE_HTML;
        } else {
            return FAILURE_XHTML;
        }
    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#emitFormContent()
     */
    protected void emitFormContent() throws SAXException {
        Html5FormEmitter.emit(contentHandler, this);
    }

    /**
     * @see nu.validator.servlet.VerifierServletTransaction#doctype(int)
     */
    public void doctype(int doctype) throws SAXException {
        // No-op
    }

}
