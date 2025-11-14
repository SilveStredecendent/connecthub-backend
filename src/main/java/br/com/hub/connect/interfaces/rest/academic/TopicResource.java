package br.com.hub.connect.interfaces.rest.academic;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.academic.dto.topic.CreateTopicDTO;
import br.com.hub.connect.application.academic.dto.topic.UpdateTopicDTO;
import br.com.hub.connect.application.academic.dto.topic.TopicResponseDTO;
import br.com.hub.connect.application.academic.dto.topic.TopicListResponseDTO;
import br.com.hub.connect.application.academic.service.TopicService;
import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.application.utils.CountResponse;
import br.com.hub.connect.domain.exception.PageNotFoundException;
import br.com.hub.connect.domain.academic.enums.TopicStatus;
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

@Path("/api/topics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Topics", description = "Operations about topics")
public class TopicResource {

  @Inject
  TopicService topicService;

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Operation(summary = "List all active topics", description = "Returns a paged list of active topics")
  @APIResponse(responseCode = "200", description = "List of topics returned successfully")
  public Response getAllTopics(
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size,
      @Parameter(description = "Filter by course ID") @QueryParam("courseId") Long courseId,
      @Parameter(description = "Filter by author ID") @QueryParam("authorId") Long authorId,
      @Parameter(description = "Filter by status") @QueryParam("status") TopicStatus status) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    List<TopicResponseDTO> topics = topicService.findWithFilters(courseId, authorId, status, pageIndex, size);

    var totalCount = getActiveTopicsCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    TopicListResponseDTO listResponse = new TopicListResponseDTO(totalPages, topics);

    return Response.ok(
        ApiResponse.success("Topics retrieved successfully", listResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/{id}")
  @Operation(summary = "Find topic by ID")
  @APIResponse(responseCode = "200", description = "Topic found")
  @APIResponse(responseCode = "404", description = "Topic not found")
  public Response getTopicById(
      @Parameter(description = "ID of the topic", required = true) @PathParam("id") @NotNull Long id) {

    TopicResponseDTO topic = topicService.findById(id);

    return Response.ok(
        ApiResponse.success("Topic found", topic)).build();
  }

  @POST
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Operation(summary = "Create a new topic")
  @APIResponse(responseCode = "201", description = "Topic created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  @APIResponse(responseCode = "404", description = "Course or author not found")
  public Response createTopic(@Valid CreateTopicDTO dto) {

    TopicResponseDTO createdTopic = topicService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(ApiResponse.success("Topic created successfully", createdTopic))
        .location(UriBuilder.fromPath("/api/topics/{id}")
            .build(createdTopic.id()))
        .build();
  }

  @PATCH
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "AUTHOR" })
  @Path("/{id}")
  @Operation(summary = "Update an existing topic")
  @APIResponse(responseCode = "200", description = "Topic updated successfully")
  @APIResponse(responseCode = "404", description = "Topic not found")
  public Response updateTopic(
      @Parameter(description = "ID of the topic", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateTopicDTO dto) {

    TopicResponseDTO updatedTopic = topicService.update(id, dto);

    return Response.ok(
        ApiResponse.success("Topic updated successfully", updatedTopic)).build();
  }

  @DELETE
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "AUTHOR" })
  @Path("/{id}")
  @Operation(summary = "Delete a topic", description = "Removes a topic by its ID (soft delete)")
  @APIResponse(responseCode = "200", description = "Topic removed successfully")
  @APIResponse(responseCode = "404", description = "Topic not found")
  public Response deleteTopic(
      @Parameter(description = "ID of the topic", required = true) @PathParam("id") @NotNull Long id) {

    topicService.delete(id);

    return Response.ok(
        ApiResponse.success("Topic deleted successfully")).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/course/{courseId}")
  @Operation(summary = "List topics by course", description = "Returns all topics for a specific course")
  @APIResponse(responseCode = "200", description = "List of topics returned successfully")
  @APIResponse(responseCode = "404", description = "Course not found")
  public Response getTopicsByCourse(
      @Parameter(description = "ID of the course", required = true) @PathParam("courseId") @NotNull Long courseId,
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    List<TopicResponseDTO> topics = topicService.findByCourse(courseId, pageIndex, size);
    long count = topicService.countByCourse(courseId);
    int totalPages = (int) Math.ceil((double) count / size);

    TopicListResponseDTO listResponse = new TopicListResponseDTO(totalPages, topics);

    return Response.ok(
        ApiResponse.success("Topics by course retrieved successfully", listResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER" })
  @Path("/author/{authorId}")
  @Operation(summary = "List topics by author", description = "Returns all topics created by a specific author")
  @APIResponse(responseCode = "200", description = "List of topics returned successfully")
  @APIResponse(responseCode = "404", description = "Author not found")
  public Response getTopicsByAuthor(
      @Parameter(description = "ID of the author", required = true) @PathParam("authorId") @NotNull Long authorId,
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    if (page < 1) {
      throw new PageNotFoundException();
    }
    int pageIndex = page - 1;

    List<TopicResponseDTO> topics = topicService.findByAuthor(authorId, pageIndex, size);
    long count = topicService.countByAuthor(authorId);
    int totalPages = (int) Math.ceil((double) count / size);

    TopicListResponseDTO listResponse = new TopicListResponseDTO(totalPages, topics);

    return Response.ok(
        ApiResponse.success("Topics by author retrieved successfully", listResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Topic service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok(
        ApiResponse.success("Topic service is healthy")).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/count")
  @Operation(summary = "Count active topics", description = "Returns the total number of active topics")
  @APIResponse(responseCode = "200", description = "Total number of active topics returned successfully")
  public Response countActiveTopics() {
    long count = getActiveTopicsCount();
    CountResponse countResponse = new CountResponse(count);

    return Response.ok(
        ApiResponse.success("Active topics count retrieved", countResponse)).build();
  }

  private long getActiveTopicsCount() {
    return topicService.count();
  }
}
