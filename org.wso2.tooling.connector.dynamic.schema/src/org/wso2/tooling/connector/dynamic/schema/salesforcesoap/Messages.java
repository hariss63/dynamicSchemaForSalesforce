package org.wso2.tooling.connector.dynamic.schema.salesforcesoap;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.wso2.tooling.connector.dynamic.schema.salesforcesoap.messages"; //$NON-NLS-1$
	public static String SchemaKeyEditorDialog_SelectConnectorLoginUsername;
	public static String SchemaKeyEditorDialog_SelectConnectorLoginPassword;
	public static String SchemaKeyEditorDialog_SelectConnectorLoginSecurityToken;
	public static String SchemaKeyEditorDialog_SelectConnectorLoginLoginURL;
	public static String SchemaKeyEditorDialog_SelectConnectorLogin;
	public static String SchemaKeyEditorDialog_SelectSObject;
	public static String SchemaKeyEditorDialog_Query;
	public static String SchemaKeyEditorDialog_Retrieve;
	public static String SchemaKeyEditorDialog_Search;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}