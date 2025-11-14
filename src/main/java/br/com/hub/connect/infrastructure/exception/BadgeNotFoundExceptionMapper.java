package br.com.hub.connect.infrastructure.exception;

import br.com.hub.connect.domain.exception.BadgeNotFoundException;
import br.com.hub.connect.application.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadgeNotFoundExceptionMapper implements ExceptionMapper<BadgeNotFoundException> {
  private static final Logger logger = LoggerFactory.getLogger(BadgeNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(BadgeNotFoundException exception) {
    logger.warn("Badge not found: {}", exception.getMessage());

    ErrorResponse errorDetails = new ErrorResponse(
        "BADGE_NOT_FOUND",
        exception.getMessage(),
        404);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "Badge not found",
        errorDetails);

    return Response.status(404).entity(response).build();
  }
}