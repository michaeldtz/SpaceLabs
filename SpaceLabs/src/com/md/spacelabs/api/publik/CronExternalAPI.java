package com.md.spacelabs.api.publik;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;

import com.md.spacelabs.dex.JSExecutor;
import com.md.spacelabs.persistence.DAO;
import com.md.spacelabs.repository.model.RepositoryItem;

@Path("/cron")
public class CronExternalAPI {

	
	
	@GET
	@Path("/morningservice")
	@Produces("application/json")
	public Response morningService(@Context ServletContext ctx, @Context HttpServletRequest req, @Context HttpServletResponse resp) throws JSONException {

		DAO<RepositoryItem> itemDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		List<RepositoryItem> items = itemDAO.query("name", "CRONmorningservice.sjs");

		JSExecutor exec = new JSExecutor();
		
		for (RepositoryItem item : items) {
			exec.execute(ctx, item.getName(), req, resp, item);			
		}
		
		return Response.ok("OK").build();
	}
	
	
	
	@GET
	@Path("/everyFifteenMinutes")
	@Produces("application/json")
	public Response everyFifteenMinutes(@Context ServletContext ctx, @Context HttpServletRequest req, @Context HttpServletResponse resp) throws JSONException {

		DAO<RepositoryItem> itemDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		List<RepositoryItem> items = itemDAO.query("name", "CRONeveryFifteenMinutes.sjs");

		JSExecutor exec = new JSExecutor();
		
		for (RepositoryItem item : items) {
			exec.execute(ctx, item.getName(), req, resp, item);			
		}
		
		return Response.ok("OK").build();
	}
	
	@GET
	@Path("/everyHour")
	@Produces("application/json")
	public Response everyHour(@Context ServletContext ctx, @Context HttpServletRequest req, @Context HttpServletResponse resp) throws JSONException {

		DAO<RepositoryItem> itemDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		List<RepositoryItem> items = itemDAO.query("name", "CRONeveryHour.sjs");

		JSExecutor exec = new JSExecutor();
		
		for (RepositoryItem item : items) {
			exec.execute(ctx, item.getName(), req, resp, item);			
		}
		
		return Response.ok("OK").build();
	}


}
