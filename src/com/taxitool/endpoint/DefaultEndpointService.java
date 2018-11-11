package com.taxitool.endpoint;

import com.taxitool.utils.ParameterStringBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class DefaultEndpointService {

    public HttpsURLConnection callRESTMethodHERE(String restApiString, Map<String, String> parameters) {
        parameters.put("app_id", "F7iYpiXSc8wnCRDYfMUQ");
        parameters.put("app_code", "jpDlJdgGk5ms7QQH");
        return callRESTMethod(restApiString, parameters, "application/json");
    }

    public HttpsURLConnection callRESTMethod(String restApiString, Map<String, String> parameters, String contentType) {
        HttpsURLConnection con = null;
        try {
            URL url = new URL(restApiString);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", contentType);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();
            return con;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
