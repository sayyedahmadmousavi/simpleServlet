import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Servlet implementation class UserList
 */
@WebServlet("/UserList")
public class UserList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			System.out.println("------------- UserList.doGet ------------- "+user.getUser());
//			session


			String createHtmlFile = getAllUser();
//			request.setAttribute("message" , createHtmlFile);
			
			response.getWriter().println(createHtmlFile);
//			request.getRequestDispatcher("UserList.jsp").forward(request,response);
			
		} catch (Exception e) {
			new Index().doGet(request, response);
			
		}

	}

	private String getAllUser() {
		// TODO Auto-generated method stub
		String res = "<!DOCTYPE html>" + "<html>" + " <head>"
				+ " <link rel=\"stylesheet\" href=\"style.css\">" + " </head>"
				+ " <body>" + " " + "  <table>" + "   <tr>"
				+ "    <th>First Name</th>" + "    <th>Last Name</th>"
				+ "    <th>User Name</th>" + "    <th>state</th>" + "   </tr>"
				+ "";

		SessionFactory sessionFactory;
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List userList = session.createQuery("FROM User").list();
			ff: for (Iterator iterator = userList.iterator(); iterator
					.hasNext();) {

				User user = (User) iterator.next();
				res += "   <tr>" + "    <td>" + user.getFirstName() + "</td>"
						+ "    <td>" + user.getLastName() + "</td>"
						+ "    <td>" + user.getUser() + "</td>" + "    <td>"
						+ user.getState() + "</td>" + "   </tr>";

			}

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		res += "  " + "</table>" + "" + " </body>" + "</html>";

		return res;
	}


}