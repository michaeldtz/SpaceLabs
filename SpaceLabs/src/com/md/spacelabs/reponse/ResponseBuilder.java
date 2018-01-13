package com.md.spacelabs.reponse;

import java.util.List;

import javax.ws.rs.core.Response;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.md.spacelabs.persistence.AbstractEntity;
import com.md.spacelabs.usermgmt.model.Invitation;

public class ResponseBuilder {

	
	public static Response createIdNameListJSON(List<? extends AbstractEntity> list) throws JSONException{
	
		JSONArray jsonArr = new JSONArray();
		for(AbstractEntity entry : list){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", entry.getId());
			jsonObj.put("name", entry.getName());
			jsonArr.put(jsonObj);
		}
		
		return Response.ok(jsonArr.toString()).build();
		
	}

	public static Response createSuccessResponse() throws JSONException {
		
		JSONObject json = new JSONObject();
		json.put("success", true);
		
		return Response.ok(json.toString()).build();
		
	}

	public static Response createNewObjectCreated(Long idOfNewObject) throws JSONException {
		
		JSONObject json = new JSONObject();
		json.put("newId", idOfNewObject);
		json.put("success", true);
		
		return Response.ok(json.toString()).build();
		
		
	}

	public static Response createErrorResponse(String message) throws JSONException {

		JSONObject json = new JSONObject();
		json.put("success", false);
		json.put("error", true);
		json.put("message", message);
		
		return Response.ok(json.toString()).build();
		
		
	}

	public static Response createObjectResponse(Object object) {
		return Response.ok(object).build();
	}
}
