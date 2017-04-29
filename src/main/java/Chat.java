import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

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
 * Servlet implementation class Chat
 */
@WebServlet("/Chat")
public class Chat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FileManager fileManager;

	/**
	 * @param string
	 * @see HttpServlet#HttpServlet()
	 */
	public Chat(String string) {
		super();
		fileManager = new FileManager(
				"E:\\ahmad\\lunas\\simpleServlet\\src\\main\\resources");
		fileManager.setFileName(string);

	}

	public Chat() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		System.out.println("------------- Chat.doGet ------------- "
				+ user.getUser());
		setFile(request, response);

		// session.setAttribute("state", "onChat");

		request.getParameterNames();
		String text = "";
		String text2 = "";
		if (request.getParameter("Text") == null) {
		} else if (request.getParameter("Text").length()>0) {
			
		

			text2 = user.getUser() + " : " + request.getParameter("Text")
					+ "\n";
		}
		
		
		byte[] bytes = text2.getBytes();
		try {
			fileManager.write(bytes);
			text += fileManager.read();
	
		} catch (NullPointerException e) {
			// TODO: handle exception
			setFile(request, response);
			new Chat().doGet(request, response);
			
		}
		
		text += text2;

		String htmlFile = creatHTMLFile(text);

		checkRepeat(htmlFile);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		Enumeration e = request.getParameterNames();
		ArrayList<String> fieldNameList = new ArrayList<String>();

		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			String fieldName = (String) obj;
			String fieldValue = request.getParameter(fieldName);
			 System.out.println(fieldName + " : " + fieldValue);
			fieldNameList.add(fieldName);

		}

		if (fieldNameList.contains("logOutButton")) {

			// session.setAttribute("state", "ofline");
			Check.changeState(request, response, "ofline");
			new Index().doGet(request, response);
			
			// request.getRequestDispatcher("/").forward(request, response);
		} else if (fieldNameList.contains("leaveChaatButton")) {

			// session.setAttribute("state", "online");
			Check.changeState(request, response, "online");
			new Link().doGet(request, response);
			// request.getRequestDispatcher("/Link").forward(request, response);
		}else{
			
			response.getWriter().print(htmlFile);
		}

		// System.out.println(fieldNameList.size());
	}

	private void setFile(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			HttpSession httpSession = request.getSession();
			User user1 = (User) httpSession.getAttribute("user");
			SessionFactory sessionFactory;
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction tx = null;

			try {
				tx = session.beginTransaction();
				List userList = session.createQuery("FROM User").list();

				ff: for (Iterator iterator = userList.iterator(); iterator
						.hasNext();) {

					User user = (User) iterator.next();

					System.out.println(user.getUser() + ":" + user.getState());

					if (user.getUser().equals(user1.getUser())) {
						
						fileManager = new FileManager(
								"E:\\ahmad\\lunas\\simpleServlet\\src\\main\\resources");
						fileManager.setFileName(user.getFile());

						
					}

				}
				tx.commit();
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
			} finally {
				session.close();

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private String creatHTMLFile(String text) {
		// TODO Auto-generated method stub
		String res = "<!DOCTYPE html>"
				+ "<html>"
				+ " <head>"
				+ " <link rel=\"stylesheet\" href=\"style.css\">"
				+ " </head>"
				+ " <body>"
				+ " <div style=\"overflow-y: scroll; width: 500px; height: 500px; line-height: 1em; overflow: auto; padding: 5px;\">";
		String tableRow = "   <table > ";

		String[] split = text.split("\n");
		for (String string : split) {
			if (!string.contains("null")) {
				int lineLenght = 60;
				int stringLength = string.length();

				int a = stringLength;
				int len = lineLenght;

				int mod = stringLength / lineLenght;

				if (mod > 0) {
					for (int i = 0; i <= mod; i++) {
						if ((i + 1) * len <= a) {
							tableRow += "   <tr>" + "    <td>"
									+ string.substring(len * i, len * (i + 1))
									+ "</td>" + "   </tr>";

						} else {
							tableRow += "   <tr>" + "    <td>"
									+ string.substring(len * i) + "</td>"
									+ "   </tr>";

						}
					}

				} else {
					tableRow += "   <tr>" + "    <td>" + string + "</td>"
							+ "   </tr>";

				}

			}

		}

		tableRow += "  </table></div>";

		String formChat = "<form action=\"/simpleServlet/Chat\" method=\"GET\">"
				+ "Your Text:<br> <input type=\"text\" name=\"Text\" autofocus=\"autofocus\"> "
				+ "<input	type=\"submit\" name =\"sendButton\"value=\"send...\"  >"
				+ "<input type=\"submit\" name=\"logOutButton\" value=\"Log Out\">"
				+ "<input type=\"submit\" name=\"leaveChaatButton\" value=\"Leave Chaat\">"
				+ "</form>";

		res += tableRow;
		res += formChat;
		res += "</body></html>";

		return res;

	}

	private String checkRepeat(String tableRow) {
		String res = "";
		String[] split = tableRow.split("   <tr>" + "    <td>");
		for (int i = 0; i < split.length - 1; i++) {
			if (split[i].endsWith("</tr>")) {
			}

		}

		return tableRow;
	}

}