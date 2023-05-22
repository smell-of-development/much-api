package much.api.common.exception;

import much.api.common.enums.Code;

public class NotSupportedOAuth2ProviderException extends BusinessException {

    public NotSupportedOAuth2ProviderException(String name) {
        super(String.format("지원하지 않는 프로바이더[%s]", name));
        this.code = Code.NOT_SUPPORTED_OAUTH2_PROVIDER;
    }

}
