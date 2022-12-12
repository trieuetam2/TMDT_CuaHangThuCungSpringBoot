package WebProject.WebProject.controller;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor.OptimalPropertyAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import WebProject.WebProject.entity.Cart;
import WebProject.WebProject.entity.Category;
import WebProject.WebProject.entity.Product;
import WebProject.WebProject.entity.User;
import WebProject.WebProject.repository.ProductRepository;
import WebProject.WebProject.service.CartService;
import WebProject.WebProject.service.CategoryService;
import WebProject.WebProject.service.CookieService;
import WebProject.WebProject.service.ProductService;
import WebProject.WebProject.service.UserService;

@Controller
public class ProductControler {

	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartService cartService;

	@Autowired
	HttpSession session;

	@Autowired
	CookieService cookie;

	@GetMapping(value = {"","/home"})
	public String listStudents(Model model) throws Exception {
		Cookie user_name = cookie.read("user_name");
		Cookie remember = cookie.read("remember");
		String error_momo = (String) session.getAttribute("error_momo");
		String NoSignIn = (String) session.getAttribute("NoSignIn");
		session.setAttribute("NoSignIn", null);
		session.setAttribute("error_momo", null);
		String a = (String) session.getAttribute("NoSignIn");
		System.out.println(a);
		System.out.println(NoSignIn);
		if (remember != null) {
			User acc = userService.findByIdAndRole(user_name.getValue(), "user");
			session.setAttribute("acc", acc);
			List<Cart> listCart = cartService.GetAllCartByUser_id(acc.getId());
			session.setAttribute("countCart", listCart.size());
		}
		if (session.getAttribute("acc") == null)
			session.setAttribute("countCart", "0");
		model.addAttribute("error_momo", error_momo);
		model.addAttribute("NoSignIn", NoSignIn);
		
		List<Product> Top12ProductBestSellers = productService.findTop12ProductBestSellers();
		List<Product> Top12ProductNewArrivals = productService.findTop12ProductNewArrivals();
		model.addAttribute("Top12ProductBestSellers", Top12ProductBestSellers);
		model.addAttribute("Top12ProductNewArrivals", Top12ProductNewArrivals);
		return "index";
	}
	@GetMapping("/shop")
	public String shop(Model model) throws Exception {
		List<Product> lp = productService.getAllProduct();
		int TotalPro = lp.size();
		model.addAttribute("TotalPro",TotalPro);
		Pageable pageable = PageRequest.of(0, 12);
		Page<Product> page = productRepository.findAll(pageable);
		List<Category> listCategory = categoryService.findAll();
		String search_input = (String) session.getAttribute("search_input");
		model.addAttribute("listProduct", page);
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("search_input", search_input);
		return "shop";
	}
	@GetMapping("/shop/{id}")
	public String shopPage(Model model, @PathVariable int id) throws Exception {
		List<Product> lp = productService.getAllProduct();
		int TotalPro = lp.size();
		model.addAttribute("TotalPro",TotalPro);
		Pageable pageable = PageRequest.of(id, 12);
		Page<Product> page = productRepository.findAll(pageable);
		model.addAttribute("listProduct", page);
		List<Category> listCategory = categoryService.findAll();
		String search_input = (String) session.getAttribute("search_input");
		User user = (User) session.getAttribute("acc");
		if (user != null) {
			model.addAttribute("user_Name", user.getUser_Name());
		}
		if (listCategory != null)
			model.addAttribute("listCategory", listCategory);
		else
			model.addAttribute("listCategory", null);
		model.addAttribute("search_input", search_input);
		return "shop";
	}

	@GetMapping("/productDetail/{id}")
	public String ProductDetailId(@PathVariable int id, Model model) {
		Product product = productService.getProductById(id);
		System.out.println(product);
		session.setAttribute("product", product);
		return "redirect:/productDetail";
	}

	@GetMapping("/productDetail")
	public String ProductDetail(Model model) {
		Product product = (Product) session.getAttribute("product");
		model.addAttribute(product);
		return "shop-details";
	}

	@PostMapping("/search")
	public String Search(@ModelAttribute("search-input") String search_input, Model model) throws Exception {
		List<Category> listCategory = categoryService.findAll();
		if (search_input != null) {
			System.out.println(search_input);
			List<Product> listProduct = productRepository.findByProduct_NameContaining(search_input);
			System.out.println(listProduct);
			int TotalPro = listProduct.size();
			model.addAttribute("TotalPro",TotalPro);
			model.addAttribute("search_input", search_input);
			model.addAttribute("listProduct", listProduct);
			model.addAttribute("listCategory", listCategory);
			model.addAttribute("noPageable", "noPageable");
			for(Product y :listProduct) {
				System.out.println(y);
			}
			return "shop";
		} else {
			model.addAttribute("TotalPro",0);
			model.addAttribute("noPageable", "noPageable");
			model.addAttribute("listCategory", listCategory);
			model.addAttribute("search_input", null);
			model.addAttribute("listProduct", null);
			return "shop";
		}
	}
}
