
package br.com.hub.connect.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.domain.exception.CourseNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CourseNotFoundExceptionMapper implements ExceptionMapper<CourseNotFoundException> {

  private static final Logger logger = LoggerFactory.getLogger(CourseNotFoundExceptionMapper.class);

  @Override
  public Response toResponse(CourseNotFoundException exception) {
    logger.warn("Course not found: {}", exception.getMessage());

    ErrorResponse errorDetails = new ErrorResponse(
        "COURSE_NOT_FOUND",
        exception.getMessage(),
        404);

    ApiResponse<ErrorResponse> response = ApiResponse.error(
        "Course Not Found",
        errorDetails);

    return Response.status(404).entity(response).build();
  }
}
