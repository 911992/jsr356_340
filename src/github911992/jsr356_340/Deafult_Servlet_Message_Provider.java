/*
jsr356_340
File: Deafult_Servlet_Message_Provider.java
Created on: Jul 26, 2021 6:29:29 PM
    @author https://github.com/911992
 
History:
    initial version: 0.1(20210726)
 */
package github911992.jsr356_340;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author https://github.com/911992
 */
public class Deafult_Servlet_Message_Provider extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().print(Default_WS_Endpoint.get_last_srv_msg());
    }

}
