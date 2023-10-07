package io.oopy.coding.common.utils.redis.signupRefresh;

import org.springframework.data.repository.CrudRepository;

public interface RefreshSignupTokenRepository extends CrudRepository<RefreshSignupToken, Integer> {

}