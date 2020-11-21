package hedera.starter.hederaAccount.models;

import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class Account implements Serializable {
    /**
     * @param acountId
     * @param privateKey
     * @param publicKey
     * @param solidityAddress
     */
    @ApiModelProperty(example = "0.0.1117", name = "acountId", required = true)
    private String acountId;

    @ApiModelProperty(example = "302e......", name = "privateKey", required = true)
    private String privateKey;

    @ApiModelProperty(example = "302e......", name = "publicKey", required = true)
    private String publicKey;

    @ApiModelProperty(example = "000000000000000000000000000000000000507b", name = "solidityAddress", required = true)
    private String solidityAddress;

    public Account() {
        super();
    }

    public Account(AccountId accountId, Ed25519PrivateKey privateKey) {
        this.acountId = accountId.toString();
        this.privateKey = privateKey.toString();
        this.publicKey = privateKey.publicKey.toString();
        this.solidityAddress = accountId.toSolidityAddress();
    }

    public String getAcountId() {
        return acountId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getSolidityAddress() {
        return solidityAddress;
    }
}
