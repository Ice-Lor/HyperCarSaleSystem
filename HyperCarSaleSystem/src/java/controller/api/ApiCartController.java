package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.CarDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Car;
import model.Cart;

public class ApiCartController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        Map<String, Object> res = new HashMap<String, Object>();
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                int carId = Integer.parseInt(request.getParameter("carId"));
                int quantity = 1;
                try {
                    quantity = Math.max(1, Integer.parseInt(request.getParameter("quantity")));
                } catch (Exception ignored) {}
                String color = request.getParameter("color");
                String customOptions = request.getParameter("customOptions");

                Car car = carDAO.getCarById(carId);
                if (car != null) {
                    cart.addItem(car, quantity, color, customOptions);
                    res.put("success", true);
                    res.put("totalQuantity", cart.getTotalQuantity());
                    res.put("totalDeposit", cart.getTotalDeposit());
                    res.put("message", "Đã thêm " + car.getModelName() + " vào giỏ xe VIP!");
                } else {
                    res.put("success", false);
                    res.put("message", "Không tìm thấy siêu xe!");
                }
            } else if ("count".equals(action)) {
                res.put("totalQuantity", cart.getTotalQuantity());
                res.put("totalDeposit", cart.getTotalDeposit());
                res.put("success", true);
            }
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
        }

        mapper.writeValue(response.getWriter(), res);
    }
}
