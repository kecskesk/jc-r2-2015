package error;

import eu.loxon.centralcontrol.CommonResp;

/**
 *
 * @author NB57
 */
public class SoapResponseInvalidException extends Exception {

    private CommonResp response;

    public SoapResponseInvalidException(CommonResp response) {
        this.response = response;
    }

    public CommonResp getResponse() {
        return response;
    }

}
