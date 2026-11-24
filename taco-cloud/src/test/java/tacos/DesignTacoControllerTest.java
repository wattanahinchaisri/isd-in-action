package tacos;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.web.DesignTacoController;

@WebMvcTest(DesignTacoController.class)
public class DesignTacoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private List<Ingredient> ingredients;

  private Taco design;

  @MockitoBean
  private IngredientRepository ingredientRepository;

  @MockitoBean
  private OrderRepository orderRepository;

  @BeforeEach
  public void setup() {
    ingredients = Arrays.asList(
      new Ingredient("FLTO", "Flour Tortilla", Type.WRAP, BigDecimal.valueOf(0.50)),
      new Ingredient("COTO", "Corn Tortilla", Type.WRAP, BigDecimal.valueOf(0.50)),
      new Ingredient("GRBF", "Ground Beef", Type.PROTEIN, BigDecimal.valueOf(1.50)),
      new Ingredient("CARN", "Carnitas", Type.PROTEIN, BigDecimal.valueOf(1.50)),
      new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES, BigDecimal.valueOf(0.25)),
      new Ingredient("LETC", "Lettuce", Type.VEGGIES, BigDecimal.valueOf(0.25)),
      new Ingredient("CHED", "Cheddar", Type.CHEESE, BigDecimal.valueOf(0.75)),
      new Ingredient("JACK", "Monterrey Jack", Type.CHEESE, BigDecimal.valueOf(0.75)),
      new Ingredient("SLSA", "Salsa", Type.SAUCE, BigDecimal.valueOf(0.25)),
      new Ingredient("SRCR", "Sour Cream", Type.SAUCE, BigDecimal.valueOf(0.25))
    );

    when(ingredientRepository.findAll())
        .thenReturn(ingredients);

    when(ingredientRepository.findById("FLTO")).thenReturn(Optional.of(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP, BigDecimal.valueOf(0.50))));
    when(ingredientRepository.findById("GRBF")).thenReturn(Optional.of(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN, BigDecimal.valueOf(1.50))));
    when(ingredientRepository.findById("CHED")).thenReturn(Optional.of(new Ingredient("CHED", "Cheddar", Type.CHEESE, BigDecimal.valueOf(0.75))));
    
    design = new Taco();
    design.setName("Test Taco");

    design.setIngredients(
        Arrays.asList(
            new Ingredient("FLTO", "Flour Tortilla", Type.WRAP, BigDecimal.valueOf(0.50)),
            new Ingredient("GRBF", "Ground Beef", Type.PROTEIN, BigDecimal.valueOf(1.50)),
            new Ingredient("CHED", "Cheddar", Type.CHEESE, BigDecimal.valueOf(0.75))
          )
        );
  }

  @Test
  public void testShowDesignForm() throws Exception {
    mockMvc.perform(get("/design"))
        .andExpect(status().isOk())
        .andExpect(view().name("design"))
        .andExpect(model().attribute("wrap", ingredients.subList(0, 2)))
        .andExpect(model().attribute("protein", ingredients.subList(2, 4)))
        .andExpect(model().attribute("veggies", ingredients.subList(4, 6)))
        .andExpect(model().attribute("cheese", ingredients.subList(6, 8)))
        .andExpect(model().attribute("sauce", ingredients.subList(8, 10)));
  }

  @Test
  public void processTaco() throws Exception {
    mockMvc.perform(post("/design")
        .content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().stringValues("Location", "/cart"));
  }

}
