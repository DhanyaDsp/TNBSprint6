package com.ey.dgs.utils;

public class MockResponse {

    public static String MOCK_USER_DETAILS_PRIMARY_SET = "{\n" +
            "\"userName\":\"Admin\",\n" +
            "\"saAlertFlag\":true,\n" +
            "\"mmcAlertFlag\":true,\n" +
            "\"notificationFlag\":true,\n" +
            "\"accountDetails\":[\n" +
            "{\n" +
            "\"accountNumber\":\"123456\",\n" +
            "\"nickName\":\"Admin\",\n" +
            "\"lastBilledDate\":\"2019-05-08T00:00:00\",\n" +
            "\"lastBilledAmount\":200,\n" +
            "\"billingCycleStartDate\":\"2019-04-05T00:00:00\",\n" +
            "\"billingCycleEndDate\":\"2019-06-03T00:00:00\",\n" +
            "\"isPrimaryAccount\":true,\n" +
            "\"energyTip\": \"Today is earth day. Let's switch off all our lights for one hour. Enjoy the stars\"\n" +
            "}\n" +
            "]\n" +
            "}";

    public static String MOCK_USER_DETAILS_PRIMARY_NOT_SET = "{\n" +
            "\"userName\":\"Admin\",\n" +
            "\"saAlertFlag\":true,\n" +
            "\"mmcAlertFlag\":true,\n" +
            "\"notificationFlag\":true,\n" +
            "\"accountDetails\":[\n" +
            "{\n" +
            "\"accountNumber\":\"123456\",\n" +
            "\"nickName\":\"Admin\",\n" +
            "\"lastBilledDate\":\"2019-05-08T00:00:00\",\n" +
            "\"lastBilledAmount\":200,\n" +
            "\"billingCycleStartDate\":\"2019-04-05T00:00:00\",\n" +
            "\"billingCycleEndDate\":\"2019-06-03T00:00:00\",\n" +
            "\"isPrimaryAccount\":true,\n" +
            "\"energyTip\": \"Today is earth day. Let's switch off all our lights for one hour. Enjoy the stars\"\n" +
            "}\n" +
            "]\n" +
            "}";
}
