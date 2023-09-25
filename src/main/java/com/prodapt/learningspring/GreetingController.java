package com.prodapt.learningspring;

import org.springframework.web.bind.annotation.GetMapping;

//@RestController
//public class GreetingController {
//	
//	@GetMapping("/")
//	public String index() {
//		return "Greetings from Spring Boot!";
//	}
//}


import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletResponse;

@Controller
public class GreetingController {
  
  @Autowired
  private ApplicationContext ctx;
  
  @GetMapping("/")
  public String greeting(
      @RequestParam(name="name", required=false, defaultValue="World") String name, 
      @RequestParam(name="age", required=false, defaultValue="18") String age, 
      Model model) {
    model.addAttribute("name", name);
    model.addAttribute("age", age);
    return "greeting";
  }
  
//  @GetMapping("/configs")
//  public void getAllBeans(ServletResponse resp) throws IOException {
//    String[] beans = ctx.getBeanDefinitionNames();
//    var out = resp.getWriter();
//    for (var bean : beans) {
//      out.write(bean);
//      out.write(bean);
//    }
//    out.close();
//  }

}