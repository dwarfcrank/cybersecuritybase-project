package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SignupDetailsController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private JpaContext jpaContext;

    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public String detailsByReference(@RequestParam String reference, Model model) {
        EntityManager em = jpaContext.getEntityManagerByManagedType(Signup.class);
        Query query = em.createNativeQuery("SELECT ID FROM SIGNUP WHERE REFERENCE = '" + reference + "';");

        List<Signup> signups = new ArrayList<>();

        for (Object o : query.getResultList()) {
            Long id = ((BigInteger) o).longValue();
            Signup signup = signupRepository.findOne(id);
            signups.add(signup);

            httpSession.setAttribute("reference", signup.getReference());
        }

        model.addAttribute("signups", signups);

        return "details";
    }

    @RequestMapping("/details/{signupId}")
    public String showSignupDetails(@PathVariable Long signupId, Model model) {
        Signup signup = signupRepository.findOne(signupId);

        List<Signup> signups = new ArrayList<>();
        signups.add(signup);

        model.addAttribute("signups", signups);

        return "details";
    }
}
