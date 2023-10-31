package io.oopy.coding.common.util.redis.signupRefresh;

import org.springframework.data.repository.CrudRepository;

public interface RefreshSignupTokenRepository extends CrudRepository<RefreshSignupToken, Integer> {

}