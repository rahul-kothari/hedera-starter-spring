package hedera.starter.dto;

import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.contract.ContractInfo;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;

public class ContractInfoDTO {
    public AccountId accountId;
    public String contractAccountId;
    public Instant expirationTime;
    public Duration autoRenewPeriod;
    public long storage;
    @Nullable
    public String contractMemo;

    public ContractInfoDTO(ContractInfo info) {
        this.contractAccountId = info.contractAccountId;
        this.storage = info.storage;
        this.contractMemo = info.contractMemo;
        this.autoRenewPeriod = info.autoRenewPeriod;
        this.expirationTime = info.expirationTime;
        this.accountId = info.accountId;
    }
}
