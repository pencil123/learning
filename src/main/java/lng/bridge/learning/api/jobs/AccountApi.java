package lng.bridge.learning.api.jobs;


import lng.bridge.learning.utils.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs/account")
public interface AccountApi {

    @GetMapping("/")
    JsonResult<String> getAccountBalance() throws Exception;
}
