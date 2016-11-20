package org.wso2.tooling.connector.dynamic.schema.salesforcesoap;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class GenerateOutputSchemaForUpsertOperation extends Dialog {

	private Group grpPropertyKey;
	private String value;
	private Label lblConnectorSalesforceLoginUserName;
	private Label lblConnectorLoginSalesforcePassword;
	private Label lblConnectorLoginSalesforceSecurityToken;
	private Label lblConnectorLoginSalesforceLoginURL;
	private Label lblSObject;
	private Text connectorLoginSalesforceUserNameTextField;
	private Text connectorLoginSalesforcePasswordTextField;
	private Text connectorLoginSalesforceSecurityTokenTextField;
	private Text connectorLoginSalesforceLoginURLTextField;
	private Button login;
	protected String username;
	protected String password;
	protected String loginURL;
	private static Combo cmbSObjectType;
	private static String serverUrl;
	private static String sessionId;
	private static String metadataserverUrl;

	private static final String SELECT_CONNECTOR_LOGIN_USERNAME = Messages.SchemaKeyEditorDialog_SelectConnectorLoginUsername;
	private static final String SELECT_CONNECTOR_LOGIN_PASSWORD = Messages.SchemaKeyEditorDialog_SelectConnectorLoginPassword;
	private static final String SELECT_CONNECTOR_LOGIN_SECURITY_TOKEN = Messages.SchemaKeyEditorDialog_SelectConnectorLoginSecurityToken;
	private static final String SELECT_CONNECTOR_LOGIN_LOGIN_URL = Messages.SchemaKeyEditorDialog_SelectConnectorLoginLoginURL;
	private static final String SELECT_SALESFORCE_LOGIN = Messages.SchemaKeyEditorDialog_SelectConnectorLogin;
	private static final String SELECT_SALESFORCE_SOBJECT = Messages.SchemaKeyEditorDialog_SelectSObject;

	public GenerateOutputSchemaForUpsertOperation(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		FillLayout fl_container = new FillLayout(SWT.HORIZONTAL);
		fl_container.marginHeight = 5;
		fl_container.marginWidth = 5;
		fl_container.spacing = 10;
		container.setLayout(fl_container);

		grpPropertyKey = new Group(container, SWT.None);

		FormLayout fl_grpPropertyKey = new FormLayout();
		fl_grpPropertyKey.marginHeight = 10;
		fl_grpPropertyKey.marginWidth = 10;
		grpPropertyKey.setLayout(fl_grpPropertyKey);

		lblConnectorSalesforceLoginUserName = new Label(grpPropertyKey, SWT.NORMAL);
		lblConnectorLoginSalesforcePassword = new Label(grpPropertyKey, SWT.NORMAL);
		lblConnectorLoginSalesforceSecurityToken = new Label(grpPropertyKey, SWT.NORMAL);
		lblConnectorLoginSalesforceLoginURL = new Label(grpPropertyKey, SWT.NORMAL);
		lblSObject = new Label(grpPropertyKey, SWT.NORMAL);

		connectorLoginSalesforceUserNameTextField = new Text(grpPropertyKey, SWT.BORDER);
		connectorLoginSalesforcePasswordTextField = new Text(grpPropertyKey, SWT.BORDER | SWT.PASSWORD);
		connectorLoginSalesforceSecurityTokenTextField = new Text(grpPropertyKey, SWT.BORDER | SWT.PASSWORD);
		connectorLoginSalesforceLoginURLTextField = new Text(grpPropertyKey, SWT.BORDER);

		cmbSObjectType = new Combo(grpPropertyKey, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);

		login = new Button(grpPropertyKey, SWT.PUSH);

		FormData salesforceLoginUserNameLabelLayoutData = new FormData();
		lblConnectorSalesforceLoginUserName.setText(SELECT_CONNECTOR_LOGIN_USERNAME);
		lblConnectorSalesforceLoginUserName.setLayoutData(salesforceLoginUserNameLabelLayoutData);

		FormData salesforceLoginUserNameTextFieldLayoutData = new FormData();
		salesforceLoginUserNameTextFieldLayoutData.left = new FormAttachment(lblConnectorSalesforceLoginUserName, 10,
				SWT.RIGHT);
		salesforceLoginUserNameTextFieldLayoutData.right = new FormAttachment(100, -5);
		connectorLoginSalesforceUserNameTextField.setLayoutData(salesforceLoginUserNameTextFieldLayoutData);

		FormData salesforceLoginPasswordLabelLayoutData = new FormData();
		salesforceLoginPasswordLabelLayoutData.top = new FormAttachment(lblConnectorSalesforceLoginUserName, 20,
				SWT.BOTTOM);
		lblConnectorLoginSalesforcePassword.setText(SELECT_CONNECTOR_LOGIN_PASSWORD);
		lblConnectorLoginSalesforcePassword.setLayoutData(salesforceLoginPasswordLabelLayoutData);

		FormData salesforceLoginPasswordTextFieldLayoutData = new FormData();
		salesforceLoginPasswordTextFieldLayoutData.top = new FormAttachment(connectorLoginSalesforceUserNameTextField,
				10, SWT.BOTTOM);
		salesforceLoginPasswordTextFieldLayoutData.left = new FormAttachment(lblConnectorLoginSalesforcePassword, 10,
				SWT.RIGHT);
		salesforceLoginPasswordTextFieldLayoutData.right = new FormAttachment(100, -5);
		connectorLoginSalesforcePasswordTextField.setLayoutData(salesforceLoginPasswordTextFieldLayoutData);

		FormData salesforceLoginSecurityTokenLabelLayoutData = new FormData();
		salesforceLoginSecurityTokenLabelLayoutData.top = new FormAttachment(lblConnectorLoginSalesforcePassword, 20,
				SWT.BOTTOM);
		lblConnectorLoginSalesforceSecurityToken.setText(SELECT_CONNECTOR_LOGIN_SECURITY_TOKEN);
		lblConnectorLoginSalesforceSecurityToken.setLayoutData(salesforceLoginSecurityTokenLabelLayoutData);

		FormData salesforceLoginSecurityTokenTextFieldLayoutData = new FormData();
		salesforceLoginSecurityTokenTextFieldLayoutData.top = new FormAttachment(
				connectorLoginSalesforcePasswordTextField, 10, SWT.BOTTOM);
		salesforceLoginSecurityTokenTextFieldLayoutData.left = new FormAttachment(
				lblConnectorLoginSalesforceSecurityToken, 10, SWT.RIGHT);
		salesforceLoginSecurityTokenTextFieldLayoutData.right = new FormAttachment(100, -5);
		connectorLoginSalesforceSecurityTokenTextField.setLayoutData(salesforceLoginSecurityTokenTextFieldLayoutData);

		FormData salesforceLoginLoginURLLabelLayoutData = new FormData();
		salesforceLoginLoginURLLabelLayoutData.top = new FormAttachment(lblConnectorLoginSalesforceSecurityToken, 20,
				SWT.BOTTOM);
		lblConnectorLoginSalesforceLoginURL.setText(SELECT_CONNECTOR_LOGIN_LOGIN_URL);
		lblConnectorLoginSalesforceLoginURL.setLayoutData(salesforceLoginLoginURLLabelLayoutData);

		FormData salesforceLoginLoginURLTextFieldLayoutData = new FormData();
		salesforceLoginLoginURLTextFieldLayoutData.top = new FormAttachment(
				connectorLoginSalesforceSecurityTokenTextField, 10, SWT.BOTTOM);
		salesforceLoginLoginURLTextFieldLayoutData.left = new FormAttachment(lblConnectorLoginSalesforceLoginURL, 10,
				SWT.RIGHT);
		salesforceLoginLoginURLTextFieldLayoutData.right = new FormAttachment(100, -5);
		connectorLoginSalesforceLoginURLTextField.setLayoutData(salesforceLoginLoginURLTextFieldLayoutData);

		FormData loginButtonLayoutData = new FormData();
		loginButtonLayoutData.top = new FormAttachment(connectorLoginSalesforceLoginURLTextField, 10, SWT.BOTTOM);
		loginButtonLayoutData.left = new FormAttachment(50, 10);
		loginButtonLayoutData.right = new FormAttachment(100, -5);
		login.setLayoutData(loginButtonLayoutData);
		login.setText(SELECT_SALESFORCE_LOGIN);

		login.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					login();
					String[] sObject = callMetaData();
					cmbSObjectType.setItems(sObject);
					cmbSObjectType.select(0);
				} catch (Exception e) {
					MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							"Error In Login to Salesforce", "Check the Login Credentials and Try Again");
				}
			}
		});

		FormData sObjectLabelLayoutData = new FormData();
		sObjectLabelLayoutData.top = new FormAttachment(login, 20, SWT.BOTTOM);
		lblSObject.setText(SELECT_SALESFORCE_SOBJECT);
		lblSObject.setLayoutData(sObjectLabelLayoutData);

		FormData sObjectComboLayoutData = new FormData();
		sObjectComboLayoutData.top = new FormAttachment(login, 15, SWT.BOTTOM);
		sObjectComboLayoutData.left = new FormAttachment(lblSObject, 10, SWT.RIGHT);
		sObjectComboLayoutData.right = new FormAttachment(100, -5);
		cmbSObjectType.setLayoutData(sObjectComboLayoutData);

		cmbSObjectType.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				try {
					buildRequestForUpsert();
				} catch (Exception e) {
					MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							"Error While calling the buildRequestForUpsert() Method",
							"Check the Login Credentials and Try Again");
				}
			}
		});

		return container;
	}

	protected void login() throws Exception {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		String url = connectorLoginSalesforceLoginURLTextField.getText();

		// Create a Soap Message
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		// This is the namespace URI.
		String serverURI = "urn:partner.soap.sforce.com";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("urn", serverURI);

		// SOAP Body
		SOAPBody buildSoapBody = envelope.getBody();
		SOAPElement soapBodyElem = buildSoapBody.addChildElement("login", "urn");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("username", "urn");
		soapBodyElem1.addTextNode(connectorLoginSalesforceUserNameTextField.getText());
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("password", "urn");
		soapBodyElem2.addTextNode(connectorLoginSalesforcePasswordTextField.getText()
				+ connectorLoginSalesforceSecurityTokenTextField.getText());

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI + "login");
		soapMessage.saveChanges();

		// Request to soapResponse
		SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
		soapConnection.close();
		SOAPPart soapBody = soapResponse.getSOAPPart();

		NodeList tagSession = soapBody.getElementsByTagName("sessionId");
		sessionId = tagSession.item(0).getFirstChild().getNodeValue();
		NodeList tagServerUrl = soapBody.getElementsByTagName("serverUrl");
		serverUrl = tagServerUrl.item(0).getFirstChild().getNodeValue();
		NodeList tagMetadataServerUrl = soapBody.getElementsByTagName("metadataServerUrl");
		metadataserverUrl = tagMetadataServerUrl.item(0).getFirstChild().getNodeValue();
	}

	public static String[] callMetaData() throws Exception {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		String url = metadataserverUrl;

		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String serverURI = "http://soap.sforce.com/2006/04/metadata";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("met", serverURI);

		// SOAP Header
		SOAPHeader buildSoapHeader = envelope.getHeader();
		SOAPElement soapHeaderElem = buildSoapHeader.addChildElement("SessionHeader", "met");
		SOAPElement soapHeaderElem1 = soapHeaderElem.addChildElement("sessionId", "met");
		soapHeaderElem1.addTextNode(sessionId);

		// SOAP Body
		SOAPBody buildSoapBody = envelope.getBody();
		SOAPElement soapBodyElem = buildSoapBody.addChildElement("listMetadata", "met");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("queries", "met");
		SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("type", "met");
		soapBodyElem2.addTextNode("CustomObject");

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI + "listMetadata");

		soapMessage.saveChanges();

		SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
		soapConnection.close();
		SOAPPart soapBody = soapResponse.getSOAPPart();
		DOMSource source = new DOMSource(soapBody);
		StringWriter stringResult = new StringWriter();
		TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
		String response = stringResult.toString();

		List<String> output = getTagValueFromXml(response, "fullName");
		String[] strarray = new String[output.size()];

		return output.toArray(strarray);
	}

	public static Map<String, String> callDescribeSObject(String sobject) throws Exception {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		String url = serverUrl;

		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String serverURI = "urn:partner.soap.sforce.com";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("urn", serverURI);

		// SOAP Header
		SOAPHeader buildSoapHeader = envelope.getHeader();
		SOAPElement soapHeaderElem = buildSoapHeader.addChildElement("SessionHeader", "urn");
		SOAPElement soapHeaderElem1 = soapHeaderElem.addChildElement("sessionId", "urn");
		soapHeaderElem1.addTextNode(sessionId);

		// SOAP Body
		SOAPBody buildSoapBody = envelope.getBody();
		SOAPElement soapBodyElem = buildSoapBody.addChildElement("describeSObject", "urn");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("sObjectType", "urn");
		soapBodyElem1.addTextNode(sobject);

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI + "describeSObject");

		soapMessage.saveChanges();

		SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
		soapConnection.close();
		SOAPPart soapBody = soapResponse.getSOAPPart();
		DOMSource source = new DOMSource(soapBody);
		StringWriter stringResult = new StringWriter();
		TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
		String response = stringResult.toString();

		Map<String, String> output = getInnerTagFromXml(response, "fields");

		return output;
	}

	public static String buildRequestForUpsert() throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("<sfdc:sObjects xmlns:sfdc='sfdc' type='" + cmbSObjectType.getText() + "'>");
		sb.append("<sfdc:sObject>");
		Map<String, String> fields = callDescribeSObject(cmbSObjectType.getText());

		for (String key : fields.keySet()) {
			sb.append("<sfdc:" + key + ">");
			if (fields.get(key).equals("boolean")) {
				sb.append(true);
			} else if (fields.get(key).equals("id")) {
				sb.append("0012800000uQ6LXAA0");
			} else if (fields.get(key).equals("double")) {
				sb.append(3.1542);
			} else if (fields.get(key).equals("int")) {
				sb.append(1);
			} else if (fields.get(key).equals("float")) {
				sb.append(2147483648L);
			} else {
				sb.append("value");
			}
			sb.append("</sfdc:" + key + ">");
		}
		sb.append("</sfdc:sObject>");
		sb.append("</sfdc:sObjects>");
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<root>");
		sb1.append("<allOrNone>" + "0" + "</allOrNone>" + "<allowFieldTruncate>" + "0" + "</allowFieldTruncate>"
				+ "<externalId>" + "0012800000uQ6LXAA0" + "</externalId>" + sb.toString());
		sb1.append("</root>");

		return sb1.toString();
	}

	public static Document loadXML(String xml) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource insrc = new InputSource(new StringReader(xml));

		return db.parse(insrc);
	}

	public static List<String> getTagValueFromXml(String xml, String tagName) throws Exception {
		Document xmlDoc = loadXML(xml);
		xmlDoc.getDocumentElement().normalize();

		NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
		List<String> ids = new ArrayList<String>(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node x = nodeList.item(i);
			ids.add(x.getFirstChild().getNodeValue());
		}
		return ids;
	}

	public static Map<String, String> getInnerTagFromXml(String xml, String tagName) throws Exception {
		Document xmlDoc = loadXML(xml);
		xmlDoc.getDocumentElement().normalize();

		NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
		Map<String, String> ids = new HashMap<String, String>();

		for (int temp = 0; temp < nodeList.getLength(); temp++) {
			Node nNode = nodeList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				ids.put(eElement.getElementsByTagName("name").item(0).getTextContent(),
						eElement.getElementsByTagName("type").item(0).getTextContent());
			}
		}
		return ids;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 400);
	}

	@Override
	protected void okPressed() {

		try {
			value = buildRequestForUpsert();
		} catch (Exception e) {
			MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					"Error In Build the Request", "Credentials error. Check the credentials again!");
		}
		super.okPressed();
	}

	public String getResponse() {
		return value;
	}
}