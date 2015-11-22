package hu.thatsnomoon.apollo2spring;

import eu.loxon.centralcontrol.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class ApolloClient extends WebServiceGatewaySupport {

//	private static final Logger log = LoggerFactory.getLogger(WeatherClient.class);
    public StartGameResponse startGame() {
        StartGameRequest request = new StartGameRequest();

        StartGameResponse response = (StartGameResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback("http://javachallenge.loxon.hu:8443/engine/CentralControl?wsdl"));

        System.out.println(response.getResult().getTurnsLeft());
        return response;
    }

//	public GetCityForecastByZIPResponse getCityForecastByZip(String zipCode) {
//
//		GetCityForecastByZIP request = new GetCityForecastByZIP();
//		request.setZIP(zipCode);
//
//		log.info("Requesting forecast for " + zipCode);
//
//		GetCityForecastByZIPResponse response = (GetCityForecastByZIPResponse) getWebServiceTemplate()
//				.marshalSendAndReceive(
//						"http://wsf.cdyne.com/WeatherWS/Weather.asmx",
//						request,
//						new SoapActionCallback("http://ws.cdyne.com/WeatherWS/GetCityForecastByZIP"));
//
//		          printResponse(response);
//                return response;
//	}
//
//	public void printResponse(GetCityForecastByZIPResponse response) {
//
//		ForecastReturn forecastReturn = response.getGetCityForecastByZIPResult();
//
//		if (forecastReturn.isSuccess()) {
//			log.info("Forecast for " + forecastReturn.getCity() + ", " + forecastReturn.getState());
//
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			for (Forecast forecast : forecastReturn.getForecastResult().getForecast()) {
//
//				Temp temperature = forecast.getTemperatures();
//
//				log.info(String.format("%s %s %s°-%s°", format.format(forecast.getDate().toGregorianCalendar().getTime()),
//						forecast.getDesciption(), temperature.getMorningLow(), temperature.getDaytimeHigh()));
//				log.info("");
//			}
//		} else {
//			log.info("No forecast received");
//		}
//	}
}
