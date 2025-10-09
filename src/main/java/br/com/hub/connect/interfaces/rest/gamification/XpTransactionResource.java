package br.com.hub.connect.interfaces.rest.gamification;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.hub.connect.application.gamification.xpTransaction.dto.CreateXpTransactionDTO;
import br.com.hub.connect.application.gamification.xpTransaction.dto.ResponseXpTransactionDTO;
import br.com.hub.connect.application.gamification.xpTransaction.dto.UpdateXpTransactionDTO;
import br.com.hub.connect.application.gamification.xpTransaction.service.XpTransactionService;
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

@Path("/api/xp-transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "XP Transactions", description = "Operations about XP transactions")
public class XpTransactionResource {

  @Inject
  XpTransactionService xpTransactionService;

  @GET
  @Operation(summary = "List all XP transactions", description = "Returns a paged list of XP transactions")
  @APIResponse(responseCode = "200", description = "List of XP transactions returned successfully")
  public Response getAllXpTransactions(
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    List<ResponseXpTransactionDTO> xpTransactions = xpTransactionService.findAll(page, size);
    return Response.ok(xpTransactions).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Find XP transaction by ID")
  @APIResponse(responseCode = "200", description = "XP transaction found")
  @APIResponse(responseCode = "404", description = "XP transaction not found")
  public Response getXpTransactionById(
      @Parameter(description = "ID of the XP transaction", required = true) @PathParam("id") @NotNull Long id) {

    ResponseXpTransactionDTO xpTransaction = xpTransactionService.findById(id);
    return Response.ok(xpTransaction).build();
  }

  @GET
  @Path("/user/{userId}")
  @Operation(summary = "Find XP transactions by user ID", description = "Returns all XP transactions for a specific user")
  @APIResponse(responseCode = "200", description = "XP transactions found")
  public Response getXpTransactionsByUserId(
      @Parameter(description = "ID of the user", required = true) @PathParam("userId") @NotNull Long userId) {

    List<ResponseXpTransactionDTO> xpTransactions = xpTransactionService.findByUserId(userId);
    return Response.ok(xpTransactions).build();
  }

  @GET
  @Path("/search")
  @Operation(summary = "Search XP transactions by description", description = "Returns XP transactions that contain the specified description")
  @APIResponse(responseCode = "200", description = "Search results returned successfully")
  public Response searchXpTransactionsByDescription(
      @Parameter(description = "Description to search for", required = true) @QueryParam("description") String description,
      @Parameter(description = "Page number (default: 0)") @QueryParam("page") @DefaultValue("0") int page,
      @Parameter(description = "Page size (default: 10)") @QueryParam("size") @DefaultValue("10") int size) {

    List<ResponseXpTransactionDTO> xpTransactions = xpTransactionService.findByDescription(description, page, size);
    return Response.ok(xpTransactions).build();
  }

  @POST
  @Operation(summary = "Create XP transaction", description = "Creates a new XP transaction and updates user XP")
  @APIResponse(responseCode = "201", description = "XP transaction created successfully")
  @APIResponse(responseCode = "400", description = "Invalid data")
  @APIResponse(responseCode = "404", description = "User not found")
  @APIResponse(responseCode = "409", description = "XP transaction already exists for this reference")
  public Response createXpTransaction(@Valid CreateXpTransactionDTO dto) {

    ResponseXpTransactionDTO createdXpTransaction = xpTransactionService.create(dto);

    return Response.status(Response.Status.CREATED)
        .entity(createdXpTransaction)
        .location(UriBuilder.fromPath("/api/xp-transactions/{id}")
            .build(createdXpTransaction.id()))
        .build();
  }

  @PATCH
  @Path("/{id}")
  @Operation(summary = "Update XP transaction", description = "Updates an existing XP transaction and adjusts user XP")
  @APIResponse(responseCode = "200", description = "XP transaction updated successfully")
  @APIResponse(responseCode = "404", description = "XP transaction not found")
  public Response updateXpTransaction(
      @Parameter(description = "ID of the XP transaction", required = true) @PathParam("id") @NotNull Long id,
      @Valid UpdateXpTransactionDTO dto) {

    ResponseXpTransactionDTO updatedXpTransaction = xpTransactionService.update(id, dto);
    return Response.ok(updatedXpTransaction).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete XP transaction", description = "Removes an XP transaction and deducts XP from user")
  @APIResponse(responseCode = "204", description = "XP transaction removed successfully")
  @APIResponse(responseCode = "404", description = "XP transaction not found")
  public Response deleteXpTransaction(
      @Parameter(description = "ID of the XP transaction", required = true) @PathParam("id") @NotNull Long id) {

    xpTransactionService.delete(id);
    return Response.noContent().build();
  }

  @GET
  @Path("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the XP Transaction service")
  @APIResponse(responseCode = "200", description = "Service is healthy")
  public Response health() {
    return Response.ok("XP Transaction service is healthy").build();
  }

  @GET
  @Path("/count")
  @Operation(summary = "Count XP transactions", description = "Returns the total number of XP transactions")
  @APIResponse(responseCode = "200", description = "Total number of XP transactions returned successfully")
  public Response countXpTransactions() {
    long count = xpTransactionService.count();
    return Response.ok(new CountResponse(count)).build();
  }

  public record CountResponse(long count) {
  }
}