package io.oopy.coding.common.redis.forbidden;

import org.springframework.data.repository.CrudRepository;

public interface ForbiddenTokenRepository extends CrudRepository<ForbiddenToken, String> {
}
