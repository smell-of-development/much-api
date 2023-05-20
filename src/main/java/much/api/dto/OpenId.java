package much.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import much.api.common.util.PhoneNumberUtils;

import java.util.Optional;

@Slf4j
@Getter
public class OpenId {

    private String sub;

    private String name;

    private String picture;

    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private Boolean phone_number_verified;


    public Optional<String> getPhoneNumber() {

        return PhoneNumberUtils.toOnlyDigits(this.phoneNumber);
    }
}
