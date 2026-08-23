package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.CarDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Car;
import util.FormatUtil;
import util.ValidationUtil;

/**
 * RESTful API xử lý Tìm kiếm nhanh siêu xe (AJAX Live Search Realtime Dropdown).
 */
@WebServlet(name = "ApiLiveSearchController", urlPatterns = {"/api/search"})
public class ApiLiveSearchController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String keyword = ValidationUtil.sanitize(request.getParameter("q"));
        if (!ValidationUtil.isNotEmpty(keyword)) {
            keyword = ValidationUtil.sanitize(request.getParameter("keyword"));
        }

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        if (ValidationUtil.isNotEmpty(keyword) && keyword.length() >= 2) {
            List<Car> cars = carDAO.searchLive(keyword, 5);
            for (Car c : cars) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("carId", c.getCarId());
                item.put("modelName", c.getModelName());
                item.put("brandName", c.getBrandName());
                item.put("brandLogoUrl", c.getBrandLogoUrl());
                item.put("thumbnailUrl", c.getThumbnailUrl());
                item.put("price", c.getPrice());
                item.put("formattedPrice", FormatUtil.formatCurrency(c.getPrice()));
                item.put("horsepower", c.getHorsepower());
                item.put("acceleration", c.getAcceleration0100());
                resultList.add(item);
            }
        }

        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("status", "success");
        responseData.put("count", resultList.size());
        responseData.put("data", resultList);

        mapper.writeValue(response.getWriter(), responseData);
    }
}
