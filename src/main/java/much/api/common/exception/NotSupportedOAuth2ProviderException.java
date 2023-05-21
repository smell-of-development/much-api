package much.api.common.exception;

import much.api.common.enums.Code;

public class NotSupportedOAuth2ProviderException extends BusinessException {

    public NotSupportedOAuth2ProviderException() {
        this.code = Code.NOT_SUPPORTED_OAUTH2_PROVIDER;
    }

}