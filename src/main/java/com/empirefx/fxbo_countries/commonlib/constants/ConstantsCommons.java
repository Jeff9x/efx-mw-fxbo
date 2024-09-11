package com.empirefx.fxbo_countries.commonlib.constants;

public class ConstantsCommons {

	private ConstantsCommons () {

	}

    public static final String ORIGINAL_REQUEST = "originalRequest";
	public static final String APP_REQUEST = "appRequest";//added for the the headerSetterProcessor
	public static final String APP_REQUEST_TYPE = "appRequestType";//added for the the headerSetterProcessor

	public static final String ORIGINAL_MESSAGE_PROPERTY = "originalMessage";
	public static final String UNMARSHALED_MESSAGE_PROPERTY = "unmarshaledMessage";
	public static final String SECURITY_VIOLATION_MESSAGE_VALUE = "SECURITY VIOLATION";
	public static final String CONVERSATION_ID_HEADER = "conversationID";
	public static final String MESSAGE_ID_HEADER = "messageID";
	public static final String RESULT_CODE_HEADER = "resultCode";
	public static final String ROUTE_CODE_HEADER = "routeCode";
	public static final String SLASH = "/";
	public static final String EMPTY = "";

	public static final String LOG_COLLECTOR = "logCollector";
	public static final String LOG_TITLE = "logTitle";
	public static final String NO_PROVIDER_CREDENTIALS_PRESENT = "noProviderCredentialsPresent";
	public static final String VAULT_CONNECTOR_URI = "vaultConnectorUri";
	public static final String URI = "GenericUri";
	public static final String SUCCESSFUL_RESPONSE = "successfulResponse";

	public static final String CALLER_IP = "clientIPAddress";
	public static final String CHANNEL_CODE_HEADER = "channelCode";

	public static final String SERVICE_SECURITY_DEFINITION_BASIC_VALUE = "Basic";
	public static final String SERVICE_AUTHORIZATION_HEADER_NAME = "Authorization";

	/****************** List User/Password *******************************/
	public static final String VAULT_USERNAME_HEADER = "vaultUser";
	public static final String VAULT_PASSWORD_HEADER = "vaultPassword";

	/****************** List User/Password *******************************/

	// For redis errors
	public static final String CALL_ID_REDIS_HEADER = "idRedis";
	public static final String CALL_BUSINESS_DESCRIPTION_HEADER = "businessDescription";
	public static final String CALL_OSP_REF_HEADER = "ospRef";
	public static final String CALL_EHFREF_HEADER = "ehfRef";
	public static final String CALL_EHFDESC_HEADER = "ehfDesc";

	public static final String USERNAME_RESPONSE_HEADER_NAME = "username";
	public static final String PASSWORD_RESPONSE_HEADER_NAME = "password";
	public static final String REMOVE_HEADERS_PATTERN = "*";
	public static final String DEBUG_MESSAGE = "Provider credentials not found in local cache, invoking connector vault to get credentials";
    public static final String EXCHANGE_PROPERTY = "${exchangeProperty.";

	public static final String WWW_AUTHENTICATE_HEADER_VALUE = "Basic realm=\"Access to MS Adapter Layer\"";
	public static final String SERVICE_CODE = "serviceCode";

	public static final String USER = "empire-206";
	public static final String PWAUTH = "empire@123";
	public static final String CALLER_IP_MESSAGE = "CALLER_IP_ADDRESS is ::[{}]";

}
