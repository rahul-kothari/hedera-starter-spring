package hedera.starter.hederaAccount;

import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.account.AccountInfo;
import hedera.starter.hederaAccount.models.Account;
import hedera.starter.hederaAccount.models.TransferHbar;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("Handles management of Hedera Accounts")
@RequestMapping(path = "/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    public AccountController() {
        accountService = new AccountService();
    }

    /**
     * Account Create
     * Account Delete
     * Account Info
     * TODO: Account Update
     * Account Balance
     * Hbar Transfer
     */

    // create Account on hedera
    @PostMapping("")
    @ApiOperation(
            "Create an account on hedera with certain balance (transferred from default account.")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "initialBalance",
                    type = "long",
                    example = "0",
                    value = "add this much tinyBars to the user.")
    })
    public Account createAccount(@RequestParam(defaultValue = "0") long initialBalance) throws HederaStatusException {
        return accountService.createAccount(initialBalance);
    }

    @DeleteMapping("")
    @ApiOperation("Delete Hedera Account")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", required = true, type = "String", example = "0.0.1117"),
            @ApiImplicitParam(name = "accountPrivateKey", required = true, type = "String", example = "302e..")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public boolean deleteAccount(@RequestParam String accountId, @RequestParam String accountPrivateKey) throws HederaStatusException {
        return accountService.deleteAccount(accountId, accountPrivateKey);
    }

    @GetMapping("/info/{accountId}")
    @ApiOperation("Get Account Info")
    @ApiImplicitParam(name = "accountId", required = true, type = "String", example = "0.0.1117")
    public AccountInfo getAccountInfo(@PathVariable String accountId) throws HederaStatusException {
        return accountService.getAccountInfo(accountId);
    }

    @GetMapping("/balance/{accountId}")
    @ApiOperation("Get balance of a hedera account id")
    @ApiImplicitParam(name = "accountId", required = true, type = "String", example = "0.0.1117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Balance in tinyBars:")})
    public long getHbarBalance(@PathVariable String accountId) throws HederaStatusException {
        return accountService.getHbarAccountBalance(accountId);
    }

    @PostMapping("/cryptoTransfer")
    @ApiOperation("Transfer Hbars between 2 accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "sucess or failure")})
    public boolean transferHbar(@RequestBody TransferHbar request) throws HederaStatusException {
        return accountService.transferHbars(request);
    }
}


