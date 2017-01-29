package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        String reference = (String)httpSession.getAttribute("reference");

        if (reference != null) {
            model.addAttribute("reference", reference);
        } else {
            model.addAttribute("reference", "");
        }

        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("signups", signups);

        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, @RequestParam String reference) {
        httpSession.setAttribute("reference", reference);
        Signup signup = signupRepository.save(new Signup(name, address, reference));
        return "redirect:/details/" + signup.getId();
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel() {
        Signup signup = signupRepository.findByReference((String) httpSession.getAttribute("reference"));
        signupRepository.delete(signup);
        httpSession.removeAttribute("reference");
        return "canceled";
    }
}
