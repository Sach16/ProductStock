package com.skpissay.productstock.network;

/**
 * Created by S.K. Pissay on 5/3/16.
 */
public class Constants {

    private String API_KEY = "07b2294e08dd61ee580be44c23a91c1b";

    public String getApiKey() {
        return API_KEY;
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String PRODUCTS = "products.php";
    public static final String PRODUCT_DETAILS = "product_details.php";
    public static final String VIRTUALDUSK = "virtualdusk";
    public static final String TOKEN = "token";
    public static final String CATEGORY_ID = "category_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String OFFLINE_META = "offlinemeta/";


    public static String apiMethodEx(String apiMethod, String uuid) {
        StringBuffer lBuf = new StringBuffer();
        if (null != uuid) {
            return lBuf.append(apiMethod).append("/").append(uuid).toString();
        } else {
            return lBuf.append(apiMethod).toString();
        }
    }
}
