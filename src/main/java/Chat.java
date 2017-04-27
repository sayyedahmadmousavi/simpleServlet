import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Chat
 */
@WebServlet("/Chat")
public class Chat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FileManager fileManager;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Chat() {
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
		fileManager = new FileManager(
				"E:\\ahmad\\lunas\\simpleServlet\\src\\main\\resources");
		fileManager.setFileName("55");
		
		HttpSession session = request.getSession();

		session.setAttribute("state", "onChat");
		String userName = (String) session.getAttribute("user");

		request.getParameterNames();
		String text = "";
		String text2 = "";
		if (request.getParameter("Text") == null) {
		} else {

			text2 = userName + " : " + request.getParameter("Text") + "\n";
		}
		fileManager.write(text2.getBytes());
		text += fileManager.read();
		
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
			session.setAttribute("state", "ofline");
			request.getRequestDispatcher("/").forward(request, response);
		} else if (fieldNameList.contains("leaveChaatButton")) {
			session.setAttribute("state", "online");
			request.getRequestDispatcher("/Link").forward(request, response);
		}

		System.out.println(fieldNameList.size());
		response.getWriter().print(htmlFile);
	}

	private String creatHTMLFile(String text) {
		// TODO Auto-generated method stub
		String res = "<!DOCTYPE html>"
				+ "<html>"
				+ " <head>"
				+ " <link rel=\"stylesheet\" href=\"style.css\">"
				+ " </head>"
				+ " <body>"
				+ " <div		style=\"overflow-y: scroll; width: 500px; height: 500px; line-height: 1em; overflow: auto; padding: 5px;\">";
		String tableRow = "   <table> ";
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

		tableRow += "  </table> </div>  ";

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