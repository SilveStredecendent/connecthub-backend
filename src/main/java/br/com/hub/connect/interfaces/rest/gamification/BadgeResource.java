package br.com.hub.connect.interfaces.rest.gamification;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.gamification.badge.dto.CreateBadgeDTO;
import br.com.hub.connect.application.gamification.badge.dto.BadgeListResponse;
import br.com.hub.connect.application.gamification.badge.dto.ResponseBadgeDTO;
import br.com.hub.connect.application.gamification.badge.dto.UpdateBadgeDTO;
import br.com.hub.connect.domain.exception.PageNotFoundException;
import br.com.hub.connect.application.gamification.badge.service.BadgeService;
import br.com.hub.connect.application.utils.ApiResponse;
import br.com.hub.connect.application.utils.CountResponse;
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

@Path("/api/badges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Badges", description = "Operations about badges")
public class BadgeResource {

  @Inject
  BadgeService badgeService;

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Operation(summary = "List all active badges", description = "Returns a paged list of active badges")
  @APIResponse(responseCode = "200", description = "List of badges returned successfully")
  @APIResponse(responseCode = "400", description = "Invalid pagination parameters")
  public Response getAllBadges(
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size,
      @Parameter(description = "Filter by criteria") @QueryParam("criteria") String criteria) {

    if (page < 1) {
      throw new PageNotFoundException();
    }

    int pageIndex = page - 1;

    List<ResponseBadgeDTO> badges = (criteria != null)
        ? badgeService.findByCriteria(criteria, pageIndex, size)
        : badgeService.findAll(pageIndex, size);

    long totalCount = badgeService.count();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    BadgeListResponse listResponse = new BadgeListResponse(totalPages, badges);

    return Response.ok(
        ApiResponse.success("Badges retrieved successfully", listResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/{id}")
  @Operation(summary = "Find badge by ID")
  @APIResponse(responseCode = "200", description = "Badge found")
  @APIResponse(responseCode = "404", description = "Badge not found")
  public Response getBadgeById(
      @Parameter(description = "ID of the badge", required = true) @PathParam("id") @NotNull Long id) {

    ResponseBadgeDTO badge = badgeService.findById(id);

    return Response.ok(
        ApiResponse.success("Badge found", badge)).build();
  }

  @POST
  @RolesAllowed({ "ADMIN" })
  @Operation(summary = "Create a new badge")
  @APIResponse(responseCode = "201", description = "Badge created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  public Response createBadge(@Valid CreateBadgeDTO dto) {

    ResponseBadgeDTO createdBadge = badgeService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(ApiResponse.success("Badge created successfully", createdBadge))
        .location(UriBuilder.fromPath("/api/badges/{id}")
            .build(createdBadge.id()))
        .build();
  }

  @PATCH
  @RolesAllowed({ "ADMIN" })
  @Path("/{id}")
  @Operation(summary = "Update an existing badge")
  @APIResponse(responseCode = "200", description = "Badge updated successfully")
  @APIResponse(responseCode = "404", description = "Badge not found")
  public Response updateBadge(
      @Parameter(description = "ID of the badge", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateBadgeDTO dto) {

    ResponseBadgeDTO updatedBadge = badgeService.update(id, dto);

    return Response.ok(
        ApiResponse.success("Badge updated successfully", updatedBadge)).build();
  }

  @DELETE
  @RolesAllowed({ "ADMIN" })
  @Path("/{id}")
  @Operation(summary = "Delete a badge", description = "Removes a badge by its ID (soft delete)")
  @APIResponse(responseCode = "200", description = "Badge removed successfully")
  @APIResponse(responseCode = "404", description = "Badge not found")
  public Response deleteBadge(
      @Parameter(description = "ID of the badge", required = true) @PathParam("id") @NotNull Long id) {

    badgeService.delete(id);

    return Response.ok(
        ApiResponse.success("Badge deleted successfully")).build();
  }

  @GET
  @RolesAllowed({ "ADMIN", "COORDINATOR", "TEACHER", "STUDENT" })
  @Path("/search")
  @Operation(summary = "Search badges by name", description = "Returns badges that contain the specified name")
  @APIResponse(responseCode = "200", description = "Search results returned successfully")
  public Response searchBadgesByName(
      @Parameter(description = "Name to search for", required = true) @QueryParam("name") String name,
      @Parameter(description = "Page number (default: 1)") @QueryParam("page") @DefaultValue("1") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    if (page < 1) {
      throw new PageNotFoundException();
    }

    int pageIndex = page - 1;

    List<ResponseBadgeDTO> badges = badgeService.findByNameContaining(name, pageIndex, size);

    long totalCount = badgeService.count();
    int totalPages = (int) Math.ceil((double) totalCount / size);

    BadgeListResponse listResponse = new BadgeListResponse(totalPages, badges);

    return Response.ok(
        ApiResponse.success("Badges search completed successfully", listResponse)).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the Badge service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok(
        ApiResponse.success("Badge service is healthy")).build();
  }

  @GET
  @RolesAllowed({ "ADMIN" })
  @Path("/count")
  @Operation(summary = "Count active badges", description = "Returns the total number of active badges")
  @APIResponse(responseCode = "200", description = "Total number of active badges returned successfully")
  public Response countActiveBadges() {
    long count = badgeService.count();
    CountResponse countResponse = new CountResponse(count);

    return Response.ok(
        ApiResponse.success("Active badges count retrieved", countResponse)).build();
  }
}