//package com.empirefx.fxbo_countries.commons;
//
//public class Examples {
//
//    public Examples() {
//        throw new UnsupportedOperationException("Utility class");
//    }
//
//
//    public static final String REQUEST_EXAMPLE = """
//			{
//			  "requestPayload": {
//			    "primaryData":
//			      {
//			        "businessKey": "2348092404990",
//			        "businessKeyType": "DEST_PHONE_NUMBER"
//			      },
//			      "sms":{
//			        "sender": "ZENITHBANK",
//			        "smsText": "OTP",
//			        "unicode": false
//			      }
//			  }
//			}
//			 """;
//
//
//    public static final String RESPONSE_OK = """
//{
//    "header": {
//        "messageID": "KCB_MESSAGEID",
//        "conversationID": "d859fb2b-28fc-46f9-8400-2ad4ff1bde81",
//        "targetSystemID": "NotAvailable",
//        "channelCode": "101",
//        "channelName": "zenpay",
//        "routeCode": "103",
//        "routeName": "zencore",
//        "serviceCode": "1014",
//        "ehfInfo": {
//            "item": [
//                {
//                    "ehfRef": "EHF-1000",
//                    "ehfDesc": "Processed Successfully"
//                }
//            ]
//        }
//    },
//    "responsePayload": {
//        "primaryData": {
//            "businessKey": "2348092404990",
//            "businessKeyType": "DEST_PHONE_NUMBER"
//        },
//        "status": {
//            "errorCode": 0,
//            "errorMessage": " No error",
//            "statusCode": "ACCEPTED",
//            "ticketId": "20020230821111424107151"
//        }
//    }
//}
//    """;
//
//    public static final String RESPONSE_NOT_OK = """
//{
//    "header": {
//        "messageID": "KCB_MESSAGEID",
//        "conversationID": "9c8fa63a-253e-4061-a71e-a010a9272428",
//        "targetSystemID": "NotAvailable",
//        "channelCode": "101",
//        "channelName": "zenpay",
//        "routeCode": "103",
//        "routeName": "zencore",
//        "ehfInfo": {
//            "item": [
//                {
//                    "ehfRef": "EHF-1999",
//                    "ehfDesc": "Unknown error"
//                }
//            ]
//        }
//    },
//    "responsePayload": {
//        "primaryData": {
//            "businessKey": "2348092404990",
//            "businessKeyType": "DEST_PHONE_NUMBER"
//        }
//    }
//}
//""";
//
//
//    public static final String SUCCESSFUL_RESPONSE = """
//{
//    "header": {
//        "messageID": "KCB_MESSAGEID",
//        "conversationID": "d859fb2b-28fc-46f9-8400-2ad4ff1bde81",
//        "targetSystemID": "NotAvailable",
//        "channelCode": "101",
//        "channelName": "zenpay",
//        "routeCode": "103",
//        "routeName": "zencore",
//        "serviceCode": "1014",
//        "ehfInfo": {
//            "item": [
//                {
//                    "ehfRef": "EHF-1000",
//                    "ehfDesc": "Processed Successfully"
//                }
//            ]
//        }
//    },
//    "responsePayload": {
//        "primaryData": {
//            "businessKey": "2348092404990",
//            "businessKeyType": "DEST_PHONE_NUMBER"
//        },
//        "status": {
//            "errorCode": 0,
//            "errorMessage": " No error",
//            "statusCode": "ACCEPTED",
//            "ticketId": "20020230821111424107151"
//        }
//    }
//}
//""";
//
//    public static final String ERROR_RESPONSE = """
//{
//    "errorCode": 100,
//    "errorMessage": "Authentication failed",
//    "status": "REJECTED"
//}
//""";
//
//    public static final String ZENITH_OK_RESPONSE = """
//{
//    "errorCode": 0,
//    "errorMessage": " No error",
//    "destination": "2348092404990",
//    "ticketId": "20020230821105228201592",
//    "status": "ACCEPTED"
//}
//""";
//}
