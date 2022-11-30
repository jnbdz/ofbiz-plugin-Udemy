package com.sitenetsoft.opensource.udemy.services;

import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UdemyServices {

    public static final String module = UdemyServices.class.getName();

    static class ZoneSections {
        public static String purchaseHistory = "https://www.udemy.com/dashboard/purchase-history/";
    }

    static class Auth {
        public static String login = "https://www.udemy.com/join/login-popup/?locale=en_US&next=https%3A%2F%2Fwww.udemy.com%2F&response_type=html";
        public static String loginHttpMethod = "POST";
    }

    static class LoginFormInputs {
        public static String username = "username";
        public static String password = "password";
    }

    public static Map<String, Object> pullDataUdemy(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();

        CustomerCenterSections customerCenterSections = new CustomerCenterSections();
        LoginFormInputs loginFormInputs = new LoginFormInputs();

        try {

            try {
                // Get the Cookie that is initialized in the login form page
                Connection.Response loginForm = Jsoup.connect(customerCenterSections.dashboard)
                        .method(Connection.Method.GET)
                        .execute();

                Document document = Jsoup.connect(customerCenterSections.dashboard)
                        //.data("cookieexists", "false")
                        .data(LoginFormInputs.username, username)
                        .data(LoginFormInputs.password, password)
                        .cookies(loginForm.cookies())
                        .post();
                System.out.println(document);
            } catch (Exception exception) {
                System.out.println(exception);
            }

            /*try {
                Document doc = Jsoup.connect("http://example.com").get();
                doc.select("p").forEach(System.out::println);
                System.out.println(doc.select("p").toString());
            } catch (Exception exception) {
                System.out.println(exception);
            }*/

            GenericValue videotron = delegator.makeValue("UdemyInvoices");
            // Auto generating next sequence of ofbizDemoId primary key
            udemy.setNextSeqId();
            // Setting up all non primary key field values from context map
            udemy.setNonPKFields(context);
            // Creating record in database for OfbizDemo entity for prepared value
            udemy = delegator.create(udemy);
            result.put("udemyId", udemy.getString("udemyId"));
            Debug.log("==========This is my first Java Service implementation in Apache OFBiz. Udemy record created successfully with ofbizDemoId: " + udemy.getString("udemyId"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Error in creating record in Udemy Invoice entity ........" +module);
        }
        return result;

        /*Connection.Response loginForm = Jsoup.connect("https://www.desco.org.bd/ebill/login.php")
        .method(Connection.Method.GET)
        .execute();

        Document document = Jsoup.connect("https://www.desco.org.bd/ebill/authentication.php")
        .data("cookieexists", "false")
        .data("username", "32007702")
        .data("login", "Login")
        .cookies(loginForm.cookies())
        .post();
        System.out.println(document);*/
    }

}