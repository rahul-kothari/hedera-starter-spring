package hedera.starter.hederaContract.models;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ContractCall implements Serializable {
    /**
     * @param contractId
     * @param functionName
     * @param argument
     */
    @ApiModelProperty(example = "0.0.3117", name = "contractId", required = true)
    private String contractId;

    @ApiModelProperty(example = "aMethodName", name = "functionName", required = true)
    private String functionName;

    @ApiModelProperty(example = "hello future again..", name = "argument", required = false)
    private String argument;

    public ContractCall() {
        super();
    }

    public ContractCall(String contractId, String functionName, String argument) {
        this.contractId = contractId;
        this.functionName = functionName;
        this.argument = argument;
    }

    public String getContractId() {
        return contractId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getArgument() {
        return argument;
    }
}
