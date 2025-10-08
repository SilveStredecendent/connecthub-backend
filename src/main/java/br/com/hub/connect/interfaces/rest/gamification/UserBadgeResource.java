package br.com.hub.connect.interfaces.rest.gamification;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.userbadge.dto.CreateUserBadgeDTO;
import br.com.hub.connect.application.userbadge.dto.ResponseUserBadgeDTO;
import br.com.hub.connect.application.userbadge.service.UserBadgeService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/api/user-badges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User Badges", description = "Operations about user badges")
public class UserBadgeResource {

  @Inject
  UserBadgeService userBadgeService;

  @GET
  @Operation(summary = "List all user badges", description = "Returns a paged list of user badges")
  @APIResponse(responseCode = "200", description = "List of user badges returned successfully")
  public Response getAllUserBadges(
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    List<ResponseUserBadgeDTO> userBadges = userBadgeService.findAll(page, size);
    return Response.ok(userBadges).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Find user badge by ID")
  @APIResponse(responseCode = "200", description = "User badge found")
  @APIResponse(responseCode = "404", description = "User badge not found")
  public Response getUserBadgeById(
      @Parameter(description = "ID of the user badge", required = true) @PathParam("id") @NotNull Long id) {

    ResponseUserBadgeDTO userBadge = userBadgeService.findById(id);
    return Response.ok(userBadge).build();
  }

  @GET
  @Path("/user/{userId}")
  @Operation(summary = "Find badges by user ID", description = "Returns all badges earned by a specific user")
  @APIResponse(responseCode = "200", description = "User badges found")
  public Response getBadgesByUserId(
      @Parameter(description = "ID of the user", required = true) @PathParam("userId") @NotNull Long userId) {

    List<ResponseUserBadgeDTO> userBadges = userBadgeService.findByUserId(userId);
    return Response.ok(userBadges).build();
  }

  @GET
  @Path("/badge/{badgeId}")
  @Operation(summary = "Find users by badge ID", description = "Returns all users who earned a specific badge")
  @APIResponse(responseCode = "200", description = "Users with badge found")
  public Response getUsersByBadgeId(
      @Parameter(description = "ID of the badge", required = true) @PathParam("badgeId") @NotNull Long badgeId) {

    List<ResponseUserBadgeDTO> userBadges = userBadgeService.findByBadgeId(badgeId);
    return Response.ok(userBadges).build();
  }

  @POST
  @Operation(summary = "Award badge to user", description = "Creates a new user badge (awards badge to user)")
  @APIResponse(responseCode = "201", description = "Badge awarded successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  @APIResponse(responseCode = "404", description = "User or Badge not found")
  @APIResponse(responseCode = "409", description = "User already has this badge")
  public Response awardBadge(@Valid CreateUserBadgeDTO dto) {

    ResponseUserBadgeDTO createdUserBadge = userBadgeService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(createdUserBadge)
        .location(UriBuilder.fromPath("/api/user-badges/{id}")
            .build(createdUserBadge.id()))
        .build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Remove badge from user", description = "Removes a user badge by its ID (soft delete)")
  @APIResponse(responseCode = "204", description = "User badge removed successfully")
  @APIResponse(responseCode = "404", description = "User badge not found")
  public Response removeUserBadge(
      @Parameter(description = "ID of the user badge", required = true) @PathParam("id") @NotNull Long id) {

    userBadgeService.delete(id);
    return Response.noContent().build();
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the UserBadge service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("UserBadge service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count user badges", description = "Returns the total number of user badges")
  @APIResponse(responseCode = "200", description = "Total number of user badges returned successfully")
  public Response countUserBadges() {
    long count = userBadgeService.count();
    return Response.ok(new CountResponse(count)).build();
  }

  public record CountResponse(long count) {
  }
}
