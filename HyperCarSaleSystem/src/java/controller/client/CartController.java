package controller.client;

import dal.CarDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Car;
import model.Cart;
import util.ValidationUtil;

/**
 * Controller xử lý Giỏ hàng đặt cọc siêu xe trong Session (Xem, Thêm, Sửa số lượng, Xóa).
 */
@WebServlet(name = "CartController", urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/WEB-INF/views/client/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        if (!ValidationUtil.isNotEmpty(action)) {
            action = "view";
        }

        switch (action.toLowerCase()) {
            case "add":
                int addCarId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
                int addQuantity = ValidationUtil.parseInt(request.getParameter("quantity"), 1);
                String selectedColor = ValidationUtil.sanitize(request.getParameter("selectedColor"));
                String customOptions = ValidationUtil.sanitize(request.getParameter("customOptions"));

                if (addCarId > 0 && addQuantity > 0) {
                    Car car = carDAO.getCarById(addCarId);
                    if (car != null && car.isAvailable()) {
                        cart.addItem(car, addQuantity, selectedColor, customOptions);
                    }
                }
                break;

            case "update":
                int updateCarId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
                int updateQuantity = ValidationUtil.parseInt(request.getParameter("quantity"), 1);
                if (updateCarId > 0) {
                    cart.updateQuantity(updateCarId, updateQuantity);
                }
                break;

            case "remove":
                int removeCarId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
                if (removeCarId > 0) {
                    cart.removeItem(removeCarId);
                }
                break;

            case "clear":
                cart.clear();
                break;

            default:
                break;
        }

        // Cập nhật lại session
        session.setAttribute("cart", cart);

        // Chuyển hướng lại trang giỏ hàng
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
