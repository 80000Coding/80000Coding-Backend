package io.oopy.coding.common.util.redis.forbidden;

import org.springframework.data.repository.CrudRepository;

public interface ForbiddenTokenRepository extends CrudRepository<ForbiddenToken, String> {
}
