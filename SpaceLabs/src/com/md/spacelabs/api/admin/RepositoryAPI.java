package com.md.spacelabs.api.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.md.spacelabs.persistence.DAO;
import com.md.spacelabs.repository.model.RepositoryItem;
import com.md.spacelabs.repository.model.RepositoryProject;
import com.md.spacelabs.usermgmt.GAEOpenIDUserAccessService;
import com.md.spacelabs.usermgmt.UserAccessService;
import com.md.spacelabs.usermgmt.model.UserGroup;
import com.sun.jersey.core.util.Base64;

@Path("/repository")
public class RepositoryAPI {

	/**
	 * Item Handling
	 * 
	 * @throws UnsupportedEncodingException
	 */

	@GET
	@Path("/getItem")
	@Produces("application/json")
	public Response getItemDetails(@QueryParam("id") long id) throws JSONException, UnsupportedEncodingException {

		DAO<RepositoryItem> itemDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		RepositoryItem item = itemDAO.get(id);
		
		Blob blob = item.getContentBlob();
		
		if(blob != null)
			item.setContent(new String(blob.getBytes()));
		
		return Response.ok(item).build();
	}

	@GET
	@Path("/listItems")
	@Produces("application/json")
	public Response getItemList() throws JSONException {

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		List<RepositoryItem> repoContentList = repoDAO.queryAll();

		JSONArray jsonList = new JSONArray();

		for (RepositoryItem repoContent : repoContentList) {
			JSONObject json = new JSONObject();
			json.put("id", repoContent.getId());
			json.put("name", repoContent.getName());
			jsonList.put(json);
		}

		return Response.ok(jsonList.toString()).build();
	}

	@POST
	@Path("/updateItem")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateItem(RepositoryItem item) throws JSONException, UnsupportedEncodingException {

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);

		String content = item.getContent();

		if (content != null && !content.equals("")) {
			Blob blob = new Blob(content.getBytes());
			item.setContentBlob(blob);
		}

		repoDAO.update(item);

		JSONObject json = new JSONObject();
		json.put("success", true);

		return Response.ok(json.toString()).build();
	}

	@POST
	@Path("/updateItemContent")
	@Produces("application/json")
	public Response updatItemContent(@FormParam("id") long id, @FormParam("content") String content)
			throws JSONException, UnsupportedEncodingException {

		JSONObject json = new JSONObject();

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		RepositoryItem repoContent = repoDAO.get(id);

		Blob blob = new Blob(content.getBytes());
		repoContent.setContentBlob(blob);

		repoDAO.update(repoContent);

		json.put("success", true);

		return Response.ok(json.toString()).build();
	}

	@POST
	@Path("/createItem")
	@Produces("application/json")
	public Response createNewItem(@FormParam("name") String name, @FormParam("projectID") String projectID)
			throws JSONException {

		RepositoryItem newContent = new RepositoryItem();
		newContent.setName(name);
		newContent.setProjectID(new Long(projectID));
		newContent.setContentBlob(new Blob("".getBytes()));

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		Long newId = repoDAO.create(newContent);

		// This ensures that the object is ready in the db
		repoDAO.get(newId);

		JSONObject json = new JSONObject();
		json.put("newId", newId);
		json.put("success", true);

		return Response.ok(json.toString()).build();
	}

	@POST
	@Path("/deleteItem")
	@Produces("application/json")
	public Response deleteItem(@FormParam("id") Long itemID) throws JSONException {

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		RepositoryItem repoItem = repoDAO.get(itemID);
		repoDAO.delete(repoItem);

		JSONObject json = new JSONObject();
		json.put("success", true);

		return Response.ok(json.toString()).build();
	}

	@POST
	@Path("/uploadNewItem")
	@Produces("application/json")
	public Response uploadNewItem(@QueryParam("projectID") String projectID,
			@FormParam("filename") String filename, @FormParam("content") String fileContent)
			throws JSONException, UnsupportedEncodingException {

		Blob contentBlob = new Blob(fileContent.getBytes());

		RepositoryItem newContent = new RepositoryItem();
		newContent.setName(filename);
		newContent.setContentBlob(contentBlob);
		newContent.setProjectID(new Long(projectID));

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		Long newId = repoDAO.create(newContent);

		// This ensures that the object is ready in the db
		repoDAO.get(newId);

		JSONObject json = new JSONObject();
		json.put("newId", newId);
		json.put("success", true);

		if (filename.endsWith(".zip")) {
			json.put("iszip", true);
		}

		return Response.ok(json.toString()).build();

	}

	@POST
	@Path("/unzipItem")
	@Produces("application/zip")
	public Response unzipItem(@FormParam("id") Long id) throws JSONException {

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		RepositoryItem repoContent = repoDAO.get(id);

		Long projectID = repoContent.getProjectID();

		try {

			byte[] encContent = repoContent.getContentBlob().getBytes();
			byte[] content    = Base64.decode(encContent);
 			ByteArrayInputStream bais = new ByteArrayInputStream(content);
			ZipInputStream zipIn = new ZipInputStream(bais);

			ArrayList<RepositoryItem> itemList = new ArrayList<RepositoryItem>();
			ZipEntry zipentry = zipIn.getNextEntry();
			String commonPrefix = null;
			boolean hasSamePrefix = true;

			while (zipentry != null) {
				// for each entry to be extracted
				String entryName = zipentry.getName();
				String prefix = entryName.substring(0, entryName.indexOf("/"));

				if (commonPrefix != null && !commonPrefix.equals(prefix)) {
					hasSamePrefix = false;
				} else {
					commonPrefix = prefix;
				}

				byte[] newContent    = IOUtils.toByteArray(zipIn);
				byte[] encNewContent = Base64.encode(newContent);
				Blob newBlob = new Blob(encNewContent);

				// Create item
				RepositoryItem newItem = new RepositoryItem();
				newItem.setName(entryName);
				newItem.setContentBlob(newBlob);
				newItem.setProjectID(projectID);
				itemList.add(newItem);
				
				zipIn.closeEntry();
				zipentry = zipIn.getNextEntry();

			}// while

			for(RepositoryItem item : itemList){
				if(hasSamePrefix){
					String name = item.getName();
					name = name.substring(name.indexOf("/")+1);
					item.setName(name);
				}
				repoDAO.create(item);					
			}
			
			JSONObject json = new JSONObject();
			json.put("success", true);
			return Response.ok(json.toString()).build();

		} catch (Exception e) {

			JSONObject json = new JSONObject();
			json.put("success", false);
			json.put("error", e.toString());

			return Response.ok(json.toString()).build();

		}
	}

	@GET
	@Path("/downloadProject")
	@Produces("application/zip")
	public StreamingOutput downloadProject(@QueryParam("projectID") Long projectID) throws JSONException,
			UnsupportedEncodingException {

		return new ProjectFilesStreamingOutput(projectID);
	}

	

	/**
	 * Project Handling
	 */

	@GET
	@Path("/listProjects")
	@Produces("application/json")
	public Response getProjectList() throws JSONException {

		DAO<RepositoryProject> projectDAO = new DAO<RepositoryProject>(RepositoryProject.class);
		List<RepositoryProject> repoProjectList = projectDAO.queryAll();

		JSONArray jsonList = new JSONArray();

		for (RepositoryProject repoProject : repoProjectList) {
			
			if(repoProject.getCore() == null || repoProject.getCore() == false)
				continue;
			
			JSONObject json = new JSONObject();
			json.put("id", repoProject.getId());
			json.put("name", repoProject.getName());
			jsonList.put(json);
		}

		return Response.ok(jsonList.toString()).build();
	}
	
	@GET
	@Path("/listCoreProjects")
	@Produces("application/json")
	public Response getCoreProjectList() throws JSONException {

		DAO<RepositoryProject> projectDAO = new DAO<RepositoryProject>(RepositoryProject.class);
		List<RepositoryProject> repoProjectList = projectDAO.query("core", true);

		JSONArray jsonList = new JSONArray();

		for (RepositoryProject repoProject : repoProjectList) {
			JSONObject json = new JSONObject();
			json.put("id", repoProject.getId());
			json.put("name", repoProject.getName());
			jsonList.put(json);
		}

		return Response.ok(jsonList.toString()).build();
	}

	@GET
	@Path("/getProject")
	@Produces("application/json")
	public Response getProject(@QueryParam("id") Long projectID) throws JSONException {
		DAO<RepositoryProject> repoDAO = new DAO<RepositoryProject>(RepositoryProject.class);
		RepositoryProject project = repoDAO.get(projectID);
		return Response.ok(project).build();
	}

	@POST
	@Path("/createProject")
	@Produces("application/json")
	public Response createNewProject(@FormParam("name") String name) throws JSONException {

		RepositoryProject newContent = new RepositoryProject();
		newContent.setName(name);
		newContent.setUserID(new GAEOpenIDUserAccessService().getCurrentUserID());
		newContent.setCore(true);
		
		DAO<RepositoryProject> projectDAO = new DAO<RepositoryProject>(RepositoryProject.class);
		Long newId = projectDAO.create(newContent);

		// This ensures that the object is ready in the db
		projectDAO.get(newId);

		JSONObject json = new JSONObject();
		json.put("newId", newId);
		json.put("success", true);

		return Response.ok(json.toString()).build();
	}
	
	@POST
	@Path("/createCoreProject")
	@Produces("application/json")
	public Response createNewCoreProject(@FormParam("name") String name) throws JSONException {

		RepositoryProject newProject = new RepositoryProject();
		newProject.setName(name);
		newProject.setCore(true);
		newProject.setUserID(new GAEOpenIDUserAccessService().getCurrentUserID());

		DAO<RepositoryProject> projectDAO = new DAO<RepositoryProject>(RepositoryProject.class);
		Long newId = projectDAO.create(newProject);

		// This ensures that the object is ready in the db
		projectDAO.get(newId);

		JSONObject json = new JSONObject();
		json.put("newId", newId);
		json.put("success", true);

		return Response.ok(json.toString()).build();
	}

	@POST
	@Path("/updateProject")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateProject(RepositoryProject project) throws JSONException,
			UnsupportedEncodingException {

		DAO<RepositoryProject> repoDAO = new DAO<RepositoryProject>(RepositoryProject.class);
		repoDAO.update(project);

		JSONObject json = new JSONObject();
		json.put("success", true);

		return Response.ok(json.toString()).build();
	}

	@GET
	@Path("/project/listItems")
	@Produces("application/json")
	public Response getContentOfProject(@QueryParam("projectID") Long projectID) throws JSONException {

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		List<RepositoryItem> repoContentList = repoDAO.queryAndOrder("projectID =", projectID, "name");

		JSONArray jsonList = new JSONArray();

		for (RepositoryItem repoContent : repoContentList) {
			JSONObject json = new JSONObject();
			json.put("id", repoContent.getId());
			json.put("name", repoContent.getName());

			jsonList.put(json);
		}

		return Response.ok(jsonList.toString()).build();
	}

	@GET
	@Path("/project/listStartableItems")
	@Produces("application/json")
	public Response getStartableItemsOfProject(@QueryParam("projectID") Long projectID) throws JSONException {

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		List<RepositoryItem> repoContentList = repoDAO.queryAndOrder("projectID =", projectID, "name");

		JSONArray jsonList = new JSONArray();

		for (RepositoryItem repoContent : repoContentList) {
			if (repoContent.isStartable()) {
				JSONObject json = new JSONObject();
				json.put("id", repoContent.getId());
				json.put("name", repoContent.getName());
				jsonList.put(json);
			}
		}

		return Response.ok(jsonList.toString()).build();
	}

	@POST
	@Path("/deleteProject")
	@Produces("application/json")
	public Response deleteProject(@FormParam("id") Long projectID) throws JSONException {

		DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
		List<RepositoryItem> repoContentList = repoDAO.query("projectID =", projectID);
		repoDAO.delete(repoContentList);

		DAO<RepositoryProject> projectDAO = new DAO<RepositoryProject>(RepositoryProject.class);
		RepositoryProject proj = projectDAO.get(projectID);
		projectDAO.delete(proj);

		JSONObject json = new JSONObject();
		json.put("success", true);

		return Response.ok(json.toString()).build();
	}
	
	
	private class ProjectFilesStreamingOutput implements StreamingOutput {

		private Long projectID;

		public ProjectFilesStreamingOutput(Long projectID) {
			this.projectID = projectID;
		}

		@Override
		public void write(OutputStream output) throws IOException, WebApplicationException {

			DAO<RepositoryItem> repoDAO = new DAO<RepositoryItem>(RepositoryItem.class);
			List<RepositoryItem> items = repoDAO.query("projectID =", projectID);

			DAO<RepositoryProject> projectDAO = new DAO<RepositoryProject>(RepositoryProject.class);
			RepositoryProject project = projectDAO.get(projectID);

			String folderName = project.getName().replaceAll(" ", "_");

			ZipOutputStream zipOut = new ZipOutputStream(output);
			for (RepositoryItem item : items) {

				Blob blob = item.getContentBlob();
				ZipEntry entry = new ZipEntry(folderName + "/" + item.getName());
				zipOut.putNextEntry(entry);
				if (blob != null) {
					byte[] blobContent = Base64.decode(blob.getBytes());
					zipOut.write(blobContent);
				}
				zipOut.closeEntry();
			}

			zipOut.flush();
			zipOut.close();
			output.close();

		}

	}
}
