package netgloo.controllers;

import netgloo.models.Document;
//import netgloo.models.User;
import netgloo.search.Pagenation;
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
  //String inputStr = (String)request.getParameter("inputStr");
//  @RequestMapping("/page")
//  public String  pagenum(){return "a";}
  /**
   * Show search results for the given query.
   *
   * @param q The search query.
   */
  @RequestMapping("/search")

  public String search(@RequestParam("q") String q, Model model, Pagenation pagenation) {
//    pagenum=pagenum();
//    System.out.println(pagenum);

    if (q.isEmpty()|userSearch.queryFilter(q).isEmpty()){return "index";}

    List<Document> searchResults = null;
    long startTime=System.currentTimeMillis();
    try {
      searchResults = userSearch.Search(q,pagenation);
    }
    catch (Exception ex) {
    }
    long endTime=System.currentTimeMillis();
    int resultCount=pagenation.getRowCount();
    long costTime=endTime-startTime;
    model.addAttribute("searchResults", searchResults);
    model.addAttribute("time",costTime);
    model.addAttribute("resultCount",resultCount);
    return "search";
  }


} // class MainController
