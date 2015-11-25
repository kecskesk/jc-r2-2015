package hu.thatsnomoon.apollo2spring.configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.springframework.util.Base64Utils;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

public class WebServiceMessageSenderWithAuth extends HttpUrlConnectionMessageSender {

    private final String user;
    private final String password;

    public WebServiceMessageSenderWithAuth(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection)
            throws IOException {

        // "thatsnomoon:OCBW6378"
        String userpassword = user + ":" + password;
        String encodedAuthorization = new String(Base64Utils.encode(userpassword.getBytes()));
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

        super.prepareConnection(connection);
    }
}
