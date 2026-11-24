package tacos.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.TacoOrder;

@Slf4j
@Controller
@RequestMapping("/cart")
@SessionAttributes("tacoOrder")
public class CartController {

  @ModelAttribute(name = "tacoOrder")
  public TacoOrder order() {
    return new TacoOrder();
  }

  @GetMapping
  public String showCart(@ModelAttribute("tacoOrder") TacoOrder tacoOrder) {
    log.info("Viewing cart with {} tacos", tacoOrder.getTacos().size());
    return "cart";
  }

  @PostMapping("/remove")
  public String removeTaco(
      @RequestParam("index") int index,
      @ModelAttribute("tacoOrder") TacoOrder tacoOrder) {
    tacoOrder.removeTaco(index);
    log.info("Removed taco at index {}. Remaining: {}", index, tacoOrder.getTacos().size());
    return "redirect:/cart";
  }
}