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

        String action = request.getParameter("action");
        Map<String, Object> res = new HashMap<String, Object>();

        try {
            if ("add".equalsIgnoreCase(action)) {
                int carId = Integer.parseInt(request.getParameter("carId"));
                int quantity = 1;
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                String color = request.getParameter("color");
                String options = request.getParameter("customOptions");

                Car car = carDAO.getCarById(carId);
                if (car != null && car.getStockQuantity() >= quantity) {
                    cart.addItem(car, quantity, color, options);
                    res.put("success", true);
                    res.put("message", "Đã thêm " + car.getModelName() + " vào danh sách cọc!");
                } else {
                    res.put("success", false);
                    res.put("message", "Số lượng xe tồn kho không đủ!");
                }

            } else if ("update".equalsIgnoreCase(action)) {
                int carId = Integer.parseInt(request.getParameter("carId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cart.updateQuantity(carId, quantity);
                res.put("success", true);

            } else if ("remove".equalsIgnoreCase(action)) {
                int carId = Integer.parseInt(request.getParameter("carId"));
                cart.removeItem(carId);
                res.put("success", true);
            }

            res.put("totalCount", cart.getTotalItemCount());
            res.put("subTotal", cart.getSubTotal());
            res.put("finalDeposit", cart.getFinalDeposit());

        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "Lỗi: " + e.getMessage());
        }

        mapper.writeValue(response.getWriter(), res);
    }
}
