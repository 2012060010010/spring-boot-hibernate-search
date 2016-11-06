package netgloo.controllers;

import netgloo.models.Document;
//import netgloo.models.User;
import netgloo.search.UserSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * MainController class
 */
@Controller
public class MainController {

  // Inject the UserSearch object
  @Autowired
  private UserSearch userSearch;

  /**
   * Index main page.
   */
  @RequestMapping("/")
  public String index() {
    return "index";
  }


  /**
   * Show search results for the given query.
   *
   * @param q The search query.
   */
  @RequestMapping("/search")
  public String search(@RequestParam("q") String q, Model model) {
    List<Document> searchResults = null;//User
    try {
      searchResults = userSearch.search(q);
    }
    catch (Exception ex) {
      // here you should handle unexpected errors
      // ...
      // throw ex;
    }
    model.addAttribute("searchResults", searchResults);
    return "search";
  }


} // class MainController
