package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.CarDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Car;

public class ApiLiveSearchController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("q");
        response.setContentType("application/json; charset=UTF-8");

        if (query == null || query.trim().length() < 2) {
            response.getWriter().write("[]");
            return;
        }

        List<Car> results = carDAO.searchCarsByKeyword(query.trim(), 5);
        mapper.writeValue(response.getWriter(), results);
    }
}
