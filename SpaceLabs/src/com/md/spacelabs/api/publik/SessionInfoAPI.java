package com.md.spacelabs.api.publik;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.md.spacelabs.mail.GAEMailingService;
import com.md.spacelabs.mail.IMailingService;
import com.md.spacelabs.persistence.DAO;
import com.md.spacelabs.reponse.ResponseBuilder;
import com.md.spacelabs.usermgmt.GAEOpenIDUserAccessService;
import com.md.spacelabs.usermgmt.UserAccessService;
import com.md.spacelabs.usermgmt.model.Invitation;
import com.md.spacelabs.usermgmt.model.UserGroup;
import com.md.spacelabs.utils.UserInputValidator;

@Path("/session")
public class SessionInfoAPI {

	@GET
	@Path("/isLoggedIn")
	@Produces("application/json")
	public Response isLoggedIn() throws JSONException {

		JSONObject json = new JSONObject();
		UserAccessService userService = new GAEOpenIDUserAccessService();

		if (userService.isUserLoggedIn()) {
			List<UserGroup> roles = userService.getGroupsOfUser();
			JSONArray roleArr = new JSONArray();
			for (UserGroup role : roles) {
				String techName = role.getTechnicalName();
				roleArr.put(techName);
			}

			if (userService.isApplicationUser()) {
				json.put("hasAppRole", true);
			} else {
				json.put("hasAppRole", false);
			}

			json.put("loggedIn", true);
			json.put("loginName", userService.getCurrentUserName());
			json.put("email", userService.getCurrentUserEmail());
			json.put("roles", roleArr);
		} else {
			json.put("loggedIn", false);
		}

		if (userService.isAdmin()) {
			json.put("isAdmin", true);
			json.put("hasAppRole", true);
		}

		return Response.ok(json.toString()).build();
	}

	@GET
	@Path("/getOpenIDProvider")
	@Produces("application/json")
	public Response getOpenIDProvider() throws JSONException {

		GAEOpenIDUserAccessService userSerive = new GAEOpenIDUserAccessService();
		Map<String, String> providersAndURLs = userSerive.getAllLoginURLsForProvider();

		JSONObject providerJSON = new JSONObject();
		Iterator<String> providers = providersAndURLs.keySet().iterator();
		while (providers.hasNext()) {
			String provider = (String) providers.next();
			String url = providersAndURLs.get(provider);
			providerJSON.put(provider, url);
		}

		JSONObject resultJSON = new JSONObject();
		resultJSON.put("openIDProviders", providerJSON);

		return Response.ok(resultJSON.toString()).build();

	}

	@GET
	@Path("/getLogoutURL")
	@Produces("application/json")
	public Response getLogoutURL() throws JSONException {

		UserAccessService userSerive = new GAEOpenIDUserAccessService();
		JSONObject json = new JSONObject();
		json.put("logoutURL", userSerive.getLogoutURL());

		return Response.ok(json.toString()).build();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/requestInviation")
	@Produces("application/json")
	public Response requestInviation(@FormParam("reason") String reason, @FormParam("email") String email,
			@FormParam("fullname") String fullname) throws JSONException {

		
		if(!UserInputValidator.validateEmail(email) || !UserInputValidator.validateNotEmpty(reason) || !UserInputValidator.validateNotEmpty(fullname) ){
			return ResponseBuilder.createErrorResponse("Validation Failed");
		}
		
		
		UserAccessService userSerive = new GAEOpenIDUserAccessService();
		String userID = userSerive.getCurrentUserID();
		Invitation inv = new Invitation(userID, email, fullname, reason);
		
		DAO<Invitation> invDAO = DAO.getByName("Invitation");
		Long id = invDAO.create(inv);
		
		//Construct mailtext
		String text = "Hello " + fullname + ",\n\n";
		text += "we've received your inviation request. We'll soon com back to you with a response.\n\n";
		text += "Thanks,\n\nThe SpaceLabs Team";
		
		IMailingService gaeMail = new GAEMailingService("michael.dtz@gmail.com");
		gaeMail.sendSingleMail(email,fullname, "SpaceLabs Automatic Reply" , "SpaceLabs Invitation #"+ id, text);
		
		String textToAdmin = "Hello,\n\n";
		textToAdmin += "an invitation has been requested by "+ fullname + "\n";
		textToAdmin += "UserID: " + userID + "\n";
		textToAdmin += "Email:  " + email + "\n\n";
		textToAdmin += "Thanks,\n\nThe SpaceLabs Engine";
		gaeMail.sendMailToAdmin("SpaceLabs Invitations", "SpaceLab Invitation Requested", textToAdmin);
		
		JSONObject json = new JSONObject();
		json.put("success", true);
		
		return Response.ok(json.toString()).build();
	}

}
