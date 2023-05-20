package much.api.common.auth;

import lombok.Getter;
import much.api.common.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Collection;

@Getter
public class PrincipalDetails implements UserDetails {

    private String sno;
    private Role role;

    private final String provider;

    private final String providerId;

    private final String email;

    private final String realName;

    private final String nickname;

    private final String phoneNumber;


    private final Map<String, Object> attributes;


    public PrincipalDetails(String provider,
                            String providerId,
                            String email,
                            String realName,
                            String phoneNumber,
                            Map<String, Object> attributes) {

        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.realName = realName;
        this.nickname = String.format("%s_%s%s", provider, providerId, realName);
        this.phoneNumber = phoneNumber;
        this.attributes = attributes;
        this.role = Role.ROLE_USER;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) return AuthorityUtils.NO_AUTHORITIES;
        return AuthorityUtils.createAuthorityList(role.name());
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.sno;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
