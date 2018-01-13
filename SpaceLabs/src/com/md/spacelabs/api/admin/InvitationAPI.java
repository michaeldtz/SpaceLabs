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

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.md.spacelabs.persistence.DAO;
import com.md.spacelabs.persistence.DAOFactory;
import com.md.spacelabs.reponse.ResponseBuilder;
import com.md.spacelabs.usermgmt.model.Invitation;
import com.md.spacelabs.usermgmt.model.ApplicationUser;
import com.md.spacelabs.usermgmt.model.UserGroup;

@Path("/invitation")
public class InvitationAPI {

	@GET
	@Path("/list")
	@Produces("application/json")
	public Response getAllInvitations() throws JSONException, UnsupportedEncodingException {

		DAO<Invitation> invDAO = DAOFactory.getEntityManager("Invitation");
		List<Invitation> invList = invDAO.queryAll();
		
		return ResponseBuilder.createIdNameListJSON(invList);
	}
	
	@GET
	@Path("/get")
	@Produces("application/json")
	public Response getInvitation(@QueryParam("id")Long id) throws JSONException, UnsupportedEncodingException {

		DAO<Invitation> invDAO = DAOFactory.getEntityManager("Invitation");
		Invitation invitation = invDAO.get(id);
		
		return ResponseBuilder.createObjectResponse(invitation);
	}
	
	
	@POST
	@Path("/delete")
	@Produces("application/json")
	public Response deleteInvitation(@FormParam("id") long id) throws JSONException, UnsupportedEncodingException {

		DAO<Invitation> invitationDAO = DAOFactory.getEntityManager("Invitation");
		Invitation inv = invitationDAO.get(id);
		invitationDAO.delete(inv);
		
		return ResponseBuilder.createSuccessResponse();
		
	}

	@POST
	@Path("/accept")
	@Produces("application/json")
	public Response acceptRequest(@FormParam("id") long id) throws JSONException, UnsupportedEncodingException {

		DAO<Invitation> invitationDAO = DAOFactory.getEntityManager("Invitation");
		Invitation inv = invitationDAO.get(id);
		
		ApplicationUser user = new ApplicationUser();
		user.setEmailAdress(inv.getEmail());
		user.setFullname(inv.getName());
		user.setUserID(inv.getUserID());
		
		DAO<ApplicationUser> userDAO = DAOFactory.getEntityManager("User");
		userDAO.create(user);
		
		invitationDAO.delete(inv);
		
		return ResponseBuilder.createNewObjectCreated(user.getId());
		
	}	
	
}
