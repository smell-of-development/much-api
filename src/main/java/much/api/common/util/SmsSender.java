package much.api.common.util;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.properties.SmsProperties;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.nio.charset.StandardCharsets.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsSender {

    private static final String TYPE = "SMS";

    private final WebClient webClient;

    private final SmsProperties smsProperties;


    public boolean sendSms(String phoneNumber, String content) {

        try {
            List<SmsRequest.Message> messages = List.of(SmsRequest.Message.to(phoneNumber));
            String message = String.format(smsProperties.getMessageFormat(), content);

            SmsRequest request = SmsRequest.of(smsProperties.getFrom(), message, messages);
            SmsResponse smsResponse = callApi(request);

            return "202".equals(smsResponse.getStatusCode());
        } catch (Exception e) {
            log.info("sms 전송요청 실패", e);
        }

        return false;
    }


    private SmsResponse callApi(SmsRequest request) throws NoSuchAlgorithmException, InvalidKeyException {

        final String time = String.valueOf(System.currentTimeMillis());
        final String serviceId = smsProperties.getServiceId();
        final String accessKey = smsProperties.getAccessKey();
        final String signature = generateSignature(time);
        final String uri = smsProperties.getHost() + smsProperties.getUrl().replace(smsProperties.getPathVariableName(), serviceId);

        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header(smsProperties.getTimeStampHeader(), time)
                .header(smsProperties.getAccessKeyHeader(), accessKey)
                .header(smsProperties.getSignatureHeader(), signature)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SmsResponse.class)
                .block();
    }


    private String generateSignature(String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String serviceId = smsProperties.getServiceId();
        String url = smsProperties.getUrl().replace(smsProperties.getPathVariableName(), serviceId);
        String accessKey = smsProperties.getAccessKey();
        String secretKey = smsProperties.getSecretKey();

        String message = method + space + url + newLine + timestamp + newLine + accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(UTF_8));
        return Base64.encodeBase64String(rawHmac);
    }


    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class SmsRequest {

        private final String type;

        private final String from;

        private final String content;

        private final List<Message> messages;

        private String countryCode;

        private String contentType;


        static SmsRequest of(String from,
                             String content,
                             List<Message> messages) {

            return new SmsRequest(TYPE, from, content, messages);
        }


        @Getter
        @Builder
        @RequiredArgsConstructor
        static class Message {

            final String to;

            String content;

            private Message(String to, String content) {
                this.to = to;
                this.content = content;
            }

            static Message to(String to) {

                return new Message(to);
            }

            static Message of(String to, String content) {

                return new Message(to, content);
            }

        }

    }


    @Getter
    private static class SmsResponse {

        private String requestId;

        private String requestTime;

        private String statusCode;

        private String statusName;

    }

}

