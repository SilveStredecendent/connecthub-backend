package br.com.hub.connect.interfaces.rest.academic;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.academic.dto.answer.CreateAnswerDTO;
import br.com.hub.connect.application.academic.dto.answer.UpdateAnswerDTO;
import br.com.hub.connect.application.academic.dto.answer.AnswerResponseDTO;
import br.com.hub.connect.application.academic.dto.answer.AnswerListResponseDTO;
import br.com.hub.connect.application.academic.service.AnswerService;
import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.application.utils.CountResponse;
import br.com.hub.connect.domain.exception.PageNotFoundException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/api/answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Answers", description = "Operations about answers")
public class AnswerResource {

  @Inject
  AnswerService answerService;

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Operation(summary = "List all active answers", description = "Returns a paged list of active answers")
  @APIResponse(responseCode = "200", description = "List of answers returned successfully")
  public Response getAllAnswers(
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size,
      @Parameter(description = "Filter by topic ID") @QueryParam("topicId") Long topicId,
      @Parameter(description = "Filter by author ID") @QueryParam("authorId") Long authorId) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    List<AnswerResponseDTO> answers = answerService.findWithFilters(topicId, authorId, pageIndex, size);

    var totalCount = getActiveAnswersCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    AnswerListResponseDTO listResponse = new AnswerListResponseDTO(totalPages, answers);

    return Response.ok(
        ApiResponse.success("Answers retrieved successfully", listResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/{id}")
  @Operation(summary = "Find answer by ID")
  @APIResponse(responseCode = "200", description = "Answer found")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response getAnswerById(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.findById(id);

    return Response.ok(answer).build();
  }

  @POST
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Operation(summary = "Create a new answer")
  @APIResponse(responseCode = "201", description = "Answer created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  @APIResponse(responseCode = "404", description = "Topic or author not found")
  public Response createAnswer(@Valid CreateAnswerDTO dto) {

    AnswerResponseDTO createdAnswer = answerService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(ApiResponse.success("Answer created successfully", createdAnswer))
        .location(UriBuilder.fromPath("/api/answers/{id}")
            .build(createdAnswer.id()))
        .build();
  }

  @PATCH
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER" })
  @Path("/{id}")
  @Operation(summary = "Update an existing answer")
  @APIResponse(responseCode = "200", description = "Answer updated successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response updateAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateAnswerDTO dto) {

    AnswerResponseDTO updatedAnswer = answerService.update(id, dto);

    return Response.ok(
        ApiResponse.success("Answer updated successfully", updatedAnswer)).build();
  }

  @DELETE
  @RolesAllowed({ "ADMIN" })
  @Path("/{id}")
  @Operation(summary = "Delete an answer", description = "Removes an answer by its ID (soft delete)")
  @APIResponse(responseCode = "200", description = "Answer removed successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response deleteAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    answerService.delete(id);

    return Response.ok(
        ApiResponse.success("Answer deleted successfully")).build();
  }

  @POST
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/{id}/like")
  @Operation(summary = "Like an answer")
  @APIResponse(responseCode = "200", description = "Answer liked successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response likeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.like(id);

    return Response.ok(
        ApiResponse.success("Answer liked successfully", answer)).build();
  }

  @POST
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/{id}/unlike")
  @Operation(summary = "Remove like from an answer")
  @APIResponse(responseCode = "200", description = "Like removed successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response unlikeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.unlike(id);

    return Response.ok(
        ApiResponse.success("Like removed successfully", answer)).build();
  }

  @POST
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/{id}/dislike")
  @Operation(summary = "Dislike an answer")
  @APIResponse(responseCode = "200", description = "Answer disliked successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response dislikeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.dislike(id);

    return Response.ok(
        ApiResponse.success("Answer disliked successfully", answer)).build();
  }

  @POST
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/{id}/undislike")
  @Operation(summary = "Remove dislike from an answer")
  @APIResponse(responseCode = "200", description = "Dislike removed successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response undislikeAnswer(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.undislike(id);

    return Response.ok(
        ApiResponse.success("Dislike removed successfully", answer)).build();
  }

  @POST
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER" })
  @Path("/{id}/mark-solution")
  @Operation(summary = "Mark answer as solution")
  @APIResponse(responseCode = "200", description = "Answer marked as solution successfully")
  @APIResponse(responseCode = "404", description = "Answer not found")
  public Response markAsSolution(
      @Parameter(description = "ID of the answer", required = true) @PathParam("id") @NotNull Long id) {

    AnswerResponseDTO answer = answerService.markAsSolution(id);

    return Response.ok(
        ApiResponse.success("Answer marked as solution successfully", answer)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER" })
  @Path("/topic/{topicId}/count")
  @Operation(summary = "Count answers by topic")
  @APIResponse(responseCode = "200", description = "Count returned successfully")
  public Response countByTopic(
      @Parameter(description = "ID of the topic", required = true) @PathParam("topicId") @NotNull Long topicId) {

    long count = answerService.countByTopic(topicId);
    CountResponse countResponse = new CountResponse(count);

    return Response.ok(
        ApiResponse.success("Answers count by topic retrieved", countResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER" })
  @Path("/author/{authorId}/count")
  @Operation(summary = "Count answers by author")
  @APIResponse(responseCode = "200", description = "Count returned successfully")
  public Response countByAuthor(
      @Parameter(description = "ID of the author", required = true) @PathParam("authorId") @NotNull Long authorId) {

    long count = answerService.countByAuthor(authorId);
    CountResponse countResponse = new CountResponse(count);

    return Response.ok(
        ApiResponse.success("Answers count by author retrieved", countResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER" })
  @Path("/author/{authorId}/solutions/count")
  @Operation(summary = "Count solutions by author")
  @APIResponse(responseCode = "200", description = "Count returned successfully")
  public Response countSolutionsByAuthor(
      @Parameter(description = "ID of the author", required = true) @PathParam("authorId") @NotNull Long authorId) {

    long count = answerService.countSolutionsByAuthor(authorId);
    CountResponse countResponse = new CountResponse(count);

    return Response.ok(
        ApiResponse.success("Solutions count by author retrieved", countResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Answer service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok(
        ApiResponse.success("Answer service is healthy")).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/count")
  @Operation(summary = "Count active answers", description = "Returns the total number of active answers")
  @APIResponse(responseCode = "200", description = "Total number of active answers returned successfully")
  public Response countActiveAnswers() {
    long count = getActiveAnswersCount();
    CountResponse countResponse = new CountResponse(count);

    return Response.ok(
        ApiResponse.success("Active answers count retrieved", countResponse)).build();
  }

  private long getActiveAnswersCount() {
    return answerService.count();
  }
}
