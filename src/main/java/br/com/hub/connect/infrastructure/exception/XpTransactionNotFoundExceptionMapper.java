package br.com.hub.connect.infrastructure.exception;

import br.com.hub.connect.domain.exception.XpTransactionNotFoundException;
import br.com.hub.connect.application.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class XpTransactionNotFoundExceptionMapper implements ExceptionMapper<XpTransactionNotFoundException> {
  private static final Logger logger = LoggerFactory.getLogger(XpTransactionNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(XpTransactionNotFoundException exception) {
    logger.warn("XpTransaction not found: {}", exception.getMessage());

    ErrorResponse errorDetails = new ErrorResponse(
        "XP_TRANSACTION_NOT_FOUND",
        exception.getMessage(),
        404);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "XpTransaction not found",
        errorDetails);

    return Response.status(404).entity(response).build();
  }
}