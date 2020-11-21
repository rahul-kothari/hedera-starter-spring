package hedera.starter.hederaAccount;


import com.hedera.hashgraph.sdk.*;
import com.hedera.hashgraph.sdk.account.*;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import hedera.starter.hederaAccount.models.Account;
import hedera.starter.hederaAccount.models.TransferHbar;
import hedera.starter.utilities.EnvUtils;
import hedera.starter.utilities.HederaClient;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    public Client client = HederaClient.getHederaClientInstance();

    /**
     * Creates a Hedera Account
     *
     * @param initialBalance - initial Balance on hedera
     * @return hedera accountId, hedera public and private key, solidity address
     */
    public Account createAccount(long initialBalance) throws HederaStatusException {

        // Generate a Ed25519 private, public key pair
        var newKey = Ed25519PrivateKey.generate();

        TransactionId txId =
                new AccountCreateTransaction()
                        .setKey(newKey.publicKey)
                        .setInitialBalance(initialBalance)
                        .execute(client);
        // This will wait for the receipt to become available
        TransactionReceipt receipt = txId.getReceipt(client);
        AccountId newAccountId = receipt.getAccountId();

        Account res = new Account(newAccountId, newKey);
        return res;
    }

    /**
     * @param accountId         - account to be deleted
     * @param accountPrivateKey - of the account deleted
     * @return success or failure
     */
    public boolean deleteAccount(String accountId, String accountPrivateKey) throws HederaStatusException {
        new AccountDeleteTransaction()
                .setDeleteAccountId(AccountId.fromString(accountId))
                .setTransferAccountId(EnvUtils.getOperatorId())
                .build(client)
                .sign(Ed25519PrivateKey.fromString(accountPrivateKey))
                .execute(client)
                .getReceipt(client);

        System.out.println("Deleted " + accountId);
        return true;
    }

    public AccountInfo getAccountInfo(String accountId) throws HederaStatusException {
        long cost = new AccountInfoQuery().setAccountId(AccountId.fromString(accountId)).getCost(client);
        AccountInfo info = new AccountInfoQuery()
                .setAccountId(AccountId.fromString(accountId)).setQueryPayment(cost + cost / 50).execute(client);
        return info;
    }

    //get Hbar balance of an account.
    public long getHbarAccountBalance(String id) throws HederaStatusException {
        Hbar balance =
                new AccountBalanceQuery().setAccountId(AccountId.fromString(id)).execute(client);

        return balance.asTinybar();
    }

    /**
     * Transfer hbars from myAccount to toAccount.
     * If no "myAccount" details provided, then default to operator id
     *
     * @param request - TransferHbar.java (myAccountId, myPrivateKey, toAccountId, amount)
     * @return
     * @throws HederaStatusException
     */
    public boolean transferHbars(TransferHbar request) throws HederaStatusException {
        Client _client;
        AccountId sender;
        AccountId to = AccountId.fromString(request.getToAccountId());
        String memo = (request.getMemo() == null ? "" : request.getMemo());
        long amount = request.getAmount();
        if (request.getMyAccountId() == null && request.getMyPrivateKey() == null) {
            _client = HederaClient.getHederaClientInstance();
            sender = EnvUtils.getOperatorId();
        } else {
            _client = HederaClient.makeNewClient(request.getMyAccountId(), request.getMyPrivateKey());
            sender = AccountId.fromString(request.getMyAccountId());
        }

        new CryptoTransferTransaction()
                .addSender(sender, amount)
                .addRecipient(to, amount)
                .setTransactionMemo(memo)
                .execute(_client);

        System.out.println("transferred " + amount + "...");

        return true;
    }
}
