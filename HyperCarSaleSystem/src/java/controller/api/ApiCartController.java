package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.CarDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Car;
import model.Cart;
import util.FormatUtil;
import util.ValidationUtil;

/**
 * RESTful API xử lý Giỏ hàng ngầm qua AJAX (Lấy số lượng, Thêm xe nhanh vào giỏ).
 */
@WebServlet(name = "ApiCartController", urlPatterns = {"/api/cart"})
public class ApiCartController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("status", "success");
        data.put("totalQuantity", cart.getTotalQuantity());
        data.put("totalAmount", cart.getTotalAmount());
        data.put("depositAmount", cart.getDepositAmount());
        data.put("formattedTotal", FormatUtil.formatCurrency(cart.getTotalAmount()));
        data.put("formattedDeposit", FormatUtil.formatCurrency(cart.getDepositAmount()));

        mapper.writeValue(response.getWriter(), data);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        int carId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
        int quantity = ValidationUtil.parseInt(request.getParameter("quantity"), 1);
        String selectedColor = ValidationUtil.sanitize(request.getParameter("selectedColor"));
        String customOptions = ValidationUtil.sanitize(request.getParameter("customOptions"));

        Map<String, Object> result = new HashMap<String, Object>();

        if ("add".equalsIgnoreCase(action) && carId > 0) {
            Car car = carDAO.getCarById(carId);
            if (car != null && car.isAvailable()) {
                cart.addItem(car, quantity, selectedColor, customOptions);
                session.setAttribute("cart", cart);
                result.put("status", "success");
                result.put("message", "Đã thêm siêu xe " + car.getModelName() + " vào giỏ cọc!");
            } else {
                result.put("status", "error");
                result.put("message", "Siêu xe này hiện đã tạm ngừng kinh doanh hoặc hết hàng!");
            }
        } else if ("remove".equalsIgnoreCase(action) && carId > 0) {
            cart.removeItem(carId);
            session.setAttribute("cart", cart);
            result.put("status", "success");
            result.put("message", "Đã xóa siêu xe khỏi giỏ hàng!");
        } else {
            result.put("status", "error");
            result.put("message", "Hành động không hợp lệ!");
        }

        result.put("totalQuantity", cart.getTotalQuantity());
        result.put("totalAmount", cart.getTotalAmount());
        result.put("depositAmount", cart.getDepositAmount());
        result.put("formattedTotal", FormatUtil.formatCurrency(cart.getTotalAmount()));
        result.put("formattedDeposit", FormatUtil.formatCurrency(cart.getDepositAmount()));

        mapper.writeValue(response.getWriter(), result);
    }
}
