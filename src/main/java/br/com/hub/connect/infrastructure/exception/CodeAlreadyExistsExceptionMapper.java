package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.domain.exception.CodeAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CodeAlreadyExistsExceptionMapper implements ExceptionMapper<CodeAlreadyExistsException> {

  private static final Logger logger = LoggerFactory.getLogger(CodeAlreadyExistsExceptionMapper.class);

  @Override
  public Response toResponse(CodeAlreadyExistsException exception) {
    logger.warn("Code already exists: {}", exception.getMessage());

    ErrorResponse errorDetails = new ErrorResponse(
        "CODE_ALREADY_EXISTS",
        exception.getMessage(),
        409);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "Code Already Exists",
        errorDetails);

    return Response.status(409).entity(response).build();
  }
}
