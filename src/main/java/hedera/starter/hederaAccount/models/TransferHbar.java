package hedera.starter.hederaAccount.models;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class TransferHbar implements Serializable{
    /**
     * @param myAccountId - caller's account number
     * @param myPrivateKey - caller' private key
     * @param toAccountId destination account id
     * @param amount - In tinybars
     * @param memo
     */
    @ApiModelProperty(example = "0.0.1117", name = "toAccountId", required = true)
    private String toAccountId;

    @ApiModelProperty(example = "56788000", name = "amount", required = true)
    private long amount;

    @ApiModelProperty(example = "0.0.21166", name = "myaccNum", notes = "Also the payer of fee.")
    private String myAccountId;

    @ApiModelProperty(example = "302e......", name = "myPrivateKey")
    private String myPrivateKey;

    @ApiModelProperty(example = "test", name = "memo")
    private String memo;

    public TransferHbar() {
        super();
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public long getAmount() {
        return amount;
    }

    public String getMyAccountId() {
        return myAccountId;
    }

    public String getMyPrivateKey() {
        return myPrivateKey;
    }

    public String getMemo() {
        return memo;
    }
}
