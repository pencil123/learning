package lng.bridge.learning.controller.jobs;

import lng.bridge.learning.api.jobs.AccountApi;
import lng.bridge.learning.jobs.GetAccountBalance;
import lng.bridge.learning.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AccountApiImpl implements AccountApi {

    @Autowired
    private GetAccountBalance getAccountBalance;

    @Override
    public JsonResult<String> getAccountBalance() throws Exception {
        getAccountBalance.getAccount();
        return null;
    }
}
