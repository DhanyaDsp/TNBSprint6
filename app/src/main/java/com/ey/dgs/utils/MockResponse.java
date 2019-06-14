package com.ey.dgs.utils;

public class MockResponse {

    public static String MOCK_USER_DETAILS_PRIMARY_SET = "{\n" +
            "  \"success\": true,\n" +
            "  \"message\": \"\",\n" +
            "  \"result\": {\n" +
            "    \"userName\": \"Admin\",\n" +
            "    \"saAlertFlag\": true,\n" +
            "    \"mmcAlertFlag\": true,\n" +
            "    \"notificationFlag\": true,\n" +
            "    \"accountDetails\": [\n" +
            "      {\n" +
            "        \"accountNumber\": \"123456\",\n" +
            "        \"nickName\": \"Admin\",\n" +
            "        \"lastBilledDate\": \"2019-05-08T00:00:00\",\n" +
            "        \"lastBilledAmount\": 200,\n" +
            "        \"billingCycleStartDate\": \"2019-04-05T00:00:00\",\n" +
            "        \"billingCycleEndDate\": \"2019-06-03T00:00:00\",\n" +
            "        \"isPrimaryAccount\": true,\n" +
            "        \"energyTip\": \"Today is earth day. Let's switch off all our lights for one hour. Enjoy the stars\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    public static String MOCK_USER_DETAILS_PRIMARY_NOT_SET = "{\n" +
            "  \"success\": true,\n" +
            "  \"message\": \"\",\n" +
            "  \"result\": {\n" +
            "    \"userName\": \"Admin\",\n" +
            "    \"saAlertFlag\": true,\n" +
            "    \"mmcAlertFlag\": true,\n" +
            "    \"notificationFlag\": true,\n" +
            "    \"accountDetails\": [\n" +
            "      {\n" +
            "        \"accountNumber\": \"123456\",\n" +
            "        \"nickName\": \"Admin\",\n" +
            "        \"lastBilledDate\": \"2019-05-08T00:00:00\",\n" +
            "        \"lastBilledAmount\": 200,\n" +
            "        \"billingCycleStartDate\": \"2019-04-05T00:00:00\",\n" +
            "        \"billingCycleEndDate\": \"2019-06-03T00:00:00\",\n" +
            "        \"isPrimaryAccount\": false,\n" +
            "        \"energyTip\": \"Today is earth day. Let's switch off all our lights for one hour. Enjoy the stars\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    public static String MOCK_QUESTIONS_RESPONSE = "{\n" +
            "  \"success\": true,\n" +
            "  \"result\": [\n" +
            "    {\n" +
            "      \"questionId\": 1,\n" +
            "      \"question\": \"How many air conditioner units are there in this property?\",\n" +
            "      \"response\": null,\n" +
            "      \"type\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"questionId\": 2,\n" +
            "      \"question\": \"How many people are staying in this property?\",\n" +
            "      \"response\": null,\n" +
            "      \"type\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"questionId\": 3,\n" +
            "      \"question\": \"When did you last service your air conditioning units?\",\n" +
            "      \"response\": null,\n" +
            "      \"type\": null\n" +
            "    },\n" +
            "    {\n" +
            "      \"questionId\": 4,\n" +
            "      \"question\": \"Notify me when my consumption reaches RM\",\n" +
            "      \"response\": null,\n" +
            "      \"type\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static String USER_SETTINGS_RESPONSE = "{\n" +
            "    \"success\": true,\n" +
            "    \"result\": {\n" +
            "        \"showSplashScreen\": true,\n" +
            "        \"pushNotificationFlag\": true,\n" +
            "        \"smsNotificationFlag\": true,\n" +
            "        \"outageAlertAcknowledgementFlag\": true,\n" +
            "        \"restoreAlertAcknowledgementFlag\": true,\n" +
            "        \"consumptionAlert\": [\n" +
            "            {\n" +
            "                \"accountNumber\": \"52000000\",\n" +
            "                \"nickName\": \"Alif\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"44000000\",\n" +
            "                \"nickName\": \"Mix\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"50000000\",\n" +
            "                \"nickName\": \"Soraya\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"outageAlert\": [\n" +
            "            {\n" +
            "                \"accountNumber\": \"52000000\",\n" +
            "                \"nickName\": \"Alif\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"21000000\",\n" +
            "                \"nickName\": \"Derrick\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"81000000\",\n" +
            "                \"nickName\": \"Gurmit\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"44000000\",\n" +
            "                \"nickName\": \"Mix\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"50000000\",\n" +
            "                \"nickName\": \"Soraya\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"restoreAlert\": [\n" +
            "            {\n" +
            "                \"accountNumber\": \"52000000\",\n" +
            "                \"nickName\": \"Alif\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"21000000\",\n" +
            "                \"nickName\": \"Derrick\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"81000000\",\n" +
            "                \"nickName\": \"Gurmit\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"44000000\",\n" +
            "                \"nickName\": \"Mix\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"50000000\",\n" +
            "                \"nickName\": \"Soraya\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"accountSettings\": [\n" +
            "            {\n" +
            "                \"accountNumber\": \"81000000\",\n" +
            "                \"nickName\": \"Gurmit\",\n" +
            "                \"isUserThresholdSet\": true,\n" +
            "                \"hasConsumptionReached\": true,\n" +
            "                \"outageAlertFlag\": true,\n" +
            "                \"restoreAlertFlag\": true\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"21000000\",\n" +
            "                \"nickName\": \"Derrick\",\n" +
            "                \"isUserThresholdSet\": true,\n" +
            "                \"hasConsumptionReached\": true,\n" +
            "                \"outageAlertFlag\": true,\n" +
            "                \"restoreAlertFlag\": true\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"44000000\",\n" +
            "                \"nickName\": \"Mix\",\n" +
            "                \"isUserThresholdSet\": false,\n" +
            "                \"hasConsumptionReached\": false,\n" +
            "                \"outageAlertFlag\": true,\n" +
            "                \"restoreAlertFlag\": true\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"50000000\",\n" +
            "                \"nickName\": \"Soraya\",\n" +
            "                \"isUserThresholdSet\": false,\n" +
            "                \"hasConsumptionReached\": false,\n" +
            "                \"outageAlertFlag\": true,\n" +
            "                \"restoreAlertFlag\": true\n" +
            "            },\n" +
            "            {\n" +
            "                \"accountNumber\": \"52000000\",\n" +
            "                \"nickName\": \"Alif\",\n" +
            "                \"isUserThresholdSet\": false,\n" +
            "                \"hasConsumptionReached\": false,\n" +
            "                \"outageAlertFlag\": true,\n" +
            "                \"restoreAlertFlag\": true\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

}
