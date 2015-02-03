package com.studentconnect.datacollection;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.authenticator.AuthenticatorUtil;
import com.signup.SignUpUtil;

@Path("controller")
public class Controller {
   
	@GET
   	@Path("/authenticate/{email}/{password}")
   	@Produces(MediaType.APPLICATION_JSON)
       public Response authenticate(
       		@Context HttpHeaders headers,       		
       		@PathParam("email") String email,
       		@PathParam("password") String password) throws Exception {

		System.out.println(" ******* RECEIVEDDDD ****** " + email + " **** " + password);
		 
    	return Response.status(200).entity( AuthenticatorUtil.authenticate(email, password).toString()).build();
       
       }
	@GET
   	@Path("/signup/{firstname}/{lastname}/{email}/{password}")
   	@Produces(MediaType.APPLICATION_JSON)
       public Response signup(
       		@Context HttpHeaders headers,
       		@PathParam("firstname") String firstname,
       		@PathParam("lastname") String lastname,
       		@PathParam("email") String email,
       		@PathParam("password") String password) throws Exception {

		System.out.println(" ******* SIGNUP ****** " + email + " **** " + password);
		 
    	return Response.status(200).entity(
    			SignUpUtil.signup(firstname, lastname, email, password).toString()).build();
       
       }
 	
}
