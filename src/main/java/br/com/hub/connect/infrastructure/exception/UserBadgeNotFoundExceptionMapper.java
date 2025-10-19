package br.com.hub.connect.infrastructure.exception;

import br.com.hub.connect.domain.exception.UserBadgeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserBadgeNotFoundExceptionMapper implements ExceptionMapper<UserBadgeNotFoundException> {
  private static final Logger logger = LoggerFactory.getLogger(UserBadgeNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(UserBadgeNotFoundException exception) {
    logger.warn("UserBadge not found: {}", exception.getMessage());

    ErrorResponse error = new ErrorResponse(
        "USER_BADGE_NOT_FOUND",
        exception.getMessage(),
        404);

    return Response.status(404).entity(error).build();
  }
}
