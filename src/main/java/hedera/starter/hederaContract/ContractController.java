package hedera.starter.hederaContract;


import com.hedera.hashgraph.sdk.HederaStatusException;
import hedera.starter.dto.ContractInfoDTO;
import hedera.starter.hederaContract.models.ContractCall;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("Handles management of Hedera Contract related Services")
@RequestMapping(path = "/contract")
public class ContractController {
    /**
     * Contract Create
     * Contract Delete
     * ContractInfo
     * TODO: Contract Update
     * Contract Bytecode Query
     * Contract Statesize Query
     * Contract Call Query
     * Contract Call Transaction
     */

    @Autowired
    ContractService contractService;

    public ContractController() {
        contractService = new ContractService();
    }

    @PostMapping("")
    @ApiOperation("Create a Contract with a bytecode. NOTE- Can only contracts with no constructor parameters or 1 string parameter in constructor.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bytecode", type = "String", example = "608060405260006001601461010...", required = true),
            @ApiImplicitParam(name = "paramValue", type = "String", example = "hello future..."),
            @ApiImplicitParam(name = "gasValue", type = "Long", example = "400000", required = false)
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Contract ID")})
    public String createContractWithGas(@RequestParam String bytecode, @RequestParam(defaultValue = "") String paramValue, @RequestParam(defaultValue = "400000", required = false) Long gasValue) throws HederaStatusException {
        if ("".equals(paramValue)) {
            return contractService.createContract(bytecode, gasValue);
        }
        return contractService.createContract(bytecode, paramValue, gasValue);
    }

    @DeleteMapping("/{contractId}")
    @ApiOperation("Delete a Contract")
    @ApiImplicitParam(name = "contractId", required = true, type = "String", example = "0.0.4117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public boolean deleteContracte(@PathVariable String contractId) throws HederaStatusException {
        return contractService.deleteContract(contractId);
    }

    @GetMapping("/{contractId}")
    @ApiOperation("Get info on a Contract")
    @ApiImplicitParam(name = "contractId", required = true, type = "String", example = "0.0.4117")
    public ContractInfoDTO getContractnfo(@PathVariable String contractId) throws HederaStatusException {
        return contractService.getContractInfo(contractId);
    }

    @GetMapping("/bytecode/{contractId}")
    @ApiOperation("Get bytecode of a contract")
    @ApiImplicitParam(name = "contractId", required = true, type = "String", example = "0.0.4117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Bytecode of a contract")})
    public String getContractBytecode(@PathVariable String contractId) throws HederaStatusException {
        return contractService.queryBytecodeContract(contractId);
    }

    @GetMapping("/statesize/{contractId}")
    @ApiOperation("Get statesize of a contract")
    @ApiImplicitParam(name = "contractId", required = true, type = "String", example = "0.0.4117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Bytecode of a contract")})
    public long getContractStateSize(@PathVariable String contractId) throws HederaStatusException {
        return contractService.queryContractStatesize(contractId);
    }

    @PostMapping("/contractCall/transaction")
    @ApiOperation("Execute a transaction on a contract that takes in a string argument")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gasValue", type = "Long", example = "400000", required = false)
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public boolean executeTransactionOnContract(@RequestBody ContractCall request, @RequestParam(defaultValue = "400000", required = false) Long gasValue ) throws HederaStatusException {
        return contractService.executeTransactionOnContract(request, gasValue);
    }

    @PostMapping("/contractCall/query")
    @ApiOperation("Call a method on a contract that takes in a string argument and returns string")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gasValue", type = "Long", example = "400000", required = false)
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "result")})
    public String contractCallQuery(@RequestBody ContractCall request, @RequestParam(defaultValue = "400000", required = false) Long gasValue ) throws HederaStatusException {
        return contractService.contractCallQuery(request, gasValue);
    }

}