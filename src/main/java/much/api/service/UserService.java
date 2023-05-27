package much.api.service;

import much.api.entity.User;

public interface UserService {

    User initUser(Long id, String positionIds, String positionClass);

}
