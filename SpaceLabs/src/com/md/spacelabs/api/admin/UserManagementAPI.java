package com.md.spacelabs.api.admin;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.md.spacelabs.persistence.DAO;
import com.md.spacelabs.usermgmt.GroupAccessService;
import com.md.spacelabs.usermgmt.model.UserGroup;

@Path("/usermgmt")
public class UserManagementAPI {

	@GET
	@Path("/userrole/list")
	@Produces("application/json")
	public Response getAllRoles(@QueryParam("id") long id) throws JSONException, UnsupportedEncodingException {

		GroupAccessService roleService = new GroupAccessService();
		List<UserGroup> roles = roleService.getAllRoles();
		
		JSONArray jsonList = new JSONArray();
		for (UserGroup role : roles) {
			JSONObject json = new JSONObject();
			json.put("id", role.getId());
			json.put("name", role.getName());
			jsonList.put(json);
		}
		
		return Response.ok(jsonList.toString()).build();
	}
	
	@POST
	@Path("/userrole/create")
	@Produces("application/json")
	public Response createRole(@FormParam("name") String name) throws JSONException, UnsupportedEncodingException {

		DAO<UserGroup> roleDAO = DAO.getByName("UserGroup");
		
		UserGroup role = new UserGroup();
		role.setName(name);
		
		String techName = name.toUpperCase().replace(" ", "_");
		role.setTechnicalName(techName);
	
		roleDAO.create(role);
		
		JSONObject json = new JSONObject();
		json.put("newId", role.getId());
		json.put("success", true);
		
		return Response.ok(json.toString()).build();
	}
	
	
	
}
