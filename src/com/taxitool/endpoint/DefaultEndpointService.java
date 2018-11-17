package com.taxitool.endpoint;

import com.taxitool.utils.ParameterStringBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class DefaultEndpointService {

    public String callRESTMethodHERE(String restApiString, Map<String, String> parameters) {
        parameters.put("app_id", "F7iYpiXSc8wnCRDYfMUQ");
        parameters.put("app_code", "jpDlJdgGk5ms7QQH-NvpUQ");
        return callRESTMethod(restApiString, parameters, "application/json");
    }

    public String callRESTMethod(String restApiString, Map<String, String> parameters, String contentType) {
        HttpsURLConnection con = null;
        try {
            String paramaterString = ParameterStringBuilder.getParamsString(parameters);
            URL url = new URL(restApiString+paramaterString);
            //make connection
            HttpsURLConnection urlc = (HttpsURLConnection)url.openConnection();
            urlc.setRequestMethod("GET");
            urlc.setRequestProperty("Accept","*/*");
            //use post mode
            urlc.setDoOutput(false);
            urlc.setAllowUserInteraction(false);

            //get result
            String inputLine;
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc
                    .getInputStream()));
            StringBuilder content = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }
            br.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
