package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.model.WebProdavnica;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Korisnik;

/**
 * Servlet implementation class LoginUserServlet
 */
public class LoginUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			response.setContentType("application/json"); 
			HttpSession session = request.getSession();
			// get received JSON data from request
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String json = "";
			if(br != null){
				json = br.readLine();
			}
			// deserialize JOSN to Java object
			Gson gson = new GsonBuilder().create();
			Korisnik korisnik = gson.fromJson(json, Korisnik.class);
			WebProdavnica prodavnica = WebProdavnica.getInstance();
			JsonObject result = new JsonObject();
			if(prodavnica.loginKorisnik(korisnik.getKorisnickoIme(), korisnik.getLozinka())){
				// TODO: write some logic to check whether user exist or not, return statusCode other than 200 in response
				
				// put page name for redirection in result JSON
				korisnik=prodavnica.loadKorisnik(korisnik.getKorisnickoIme());
				session.setAttribute("user", korisnik);
				String urlToRedirect = "Home.jsp";
				result.addProperty("url", urlToRedirect);
			}

			else{
				response.setStatus(404);
			}
				
			// put result into response
			response.getWriter().write(result.toString());
		}catch(Exception exp){
			throw new ServletException(exp);
		}
	}

}
