package kr.eddi.demo.account.controller;

import kr.eddi.demo.account.controller.form.*;
import kr.eddi.demo.account.entity.RoleType;
import kr.eddi.demo.account.service.AccountService;
import kr.eddi.demo.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    final private AccountService accountService;
    final private RedisService redisService;

    @PostMapping("/normal-register")
    public Boolean normalAccountRegister (@RequestBody NormalAccountRegisterForm requestForm) {
        return accountService.normalAccountRegister(requestForm.toAccountRegisterRequest());
    }

    @PostMapping("/business-register")
    public Boolean businessAccountRegister (@RequestBody BusinessAccountRegisterForm requestForm) {
        return accountService.businessAccountRegister(requestForm.toAccountRegisterRequest());
    }

    @PostMapping("/login")
    public String accountLogin(@RequestBody AccountLoginRequestForm accountLoginRequestForm) {

        String userToken = accountService.login(accountLoginRequestForm);
        Long accountID= accountService.findAccountIdByEmail(accountLoginRequestForm.getEmail());
        redisService.setKeyAndValue(userToken,accountID);
        return userToken;
    }
    @PostMapping("/businessCheck")
    public Boolean isBusiness(@RequestBody BusinessCheckRequestForm requestForm){
       log.info(requestForm.getUserToken());
       String userToken= requestForm.getUserToken();
        Long businessId=accountService.findAccountId(userToken);
        log.info("businessId: "+businessId);
        if (businessId==null){
            return false;
        }
        Boolean isBusinessMan= accountService.businessCheck(businessId);
        log.info(String.valueOf(isBusinessMan));
        return isBusinessMan;
    }

    @PostMapping("/getAccountInfo")
    public AccountMyPageResponseForm getAccountInfo(@RequestBody BusinessCheckRequestForm requestForm) {
        String userToken = requestForm.getUserToken();
        Long accountId = accountService.findAccountId(userToken);

        String email = accountService.findAccountEmail(accountId);
        RoleType roleType = accountService.lookup(userToken);

        AccountMyPageResponseForm accountMyPageResponseForm = new AccountMyPageResponseForm(email, roleType);
        return accountMyPageResponseForm;
    }
}
