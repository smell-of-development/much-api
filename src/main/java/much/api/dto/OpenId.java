package much.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

    @JsonProperty("phone_number_verified")
    private Boolean phoneNumberVerified;

}
