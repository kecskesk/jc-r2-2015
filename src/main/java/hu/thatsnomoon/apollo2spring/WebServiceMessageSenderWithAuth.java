package hu.thatsnomoon.apollo2spring;


import java.io.IOException;
import java.net.HttpURLConnection;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import sun.misc.BASE64Encoder;

public class WebServiceMessageSenderWithAuth extends HttpUrlConnectionMessageSender{

	@Override
	protected void prepareConnection(HttpURLConnection connection)
			throws IOException {

		BASE64Encoder enc = new sun.misc.BASE64Encoder();
		String userpassword = "thatsnomoon:OCBW6378";
		String encodedAuthorization = enc.encode( userpassword.getBytes() );
		connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

		super.prepareConnection(connection);
	}
}