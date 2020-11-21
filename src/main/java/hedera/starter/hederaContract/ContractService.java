package hedera.starter.hederaContract;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.contract.*;
import com.hedera.hashgraph.sdk.file.FileAppendTransaction;
import com.hedera.hashgraph.sdk.file.FileCreateTransaction;
import com.hedera.hashgraph.sdk.file.FileId;
import hedera.starter.hederaContract.models.ContractCall;
import hedera.starter.utilities.EnvUtils;
import hedera.starter.utilities.HederaClient;
import hedera.starter.utilities.Utils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static hedera.starter.utilities.Utils.FILE_PART_SIZE;

@Service
public class ContractService {

    public Client client = HederaClient.getHederaClientInstance();

    //Create contracts with a bytecode.
    public String createContract(String bytecode) throws HederaStatusException {
        FileId bytecodeFile = createBytecodeFile(bytecode);
        var contractTxId =
                new ContractCreateTransaction()
                        .setBytecodeFileId(bytecodeFile)
                        .setAutoRenewPeriod(Duration.ofSeconds(8000000))
                        .setGas(100_000_000)
                        .setMaxTransactionFee(new Hbar(20))
                        .setAdminKey(EnvUtils.getOperatorKey().publicKey) //allows to delete contract
                        .execute(client);

        var contractReceipt = contractTxId.getReceipt(client);
        ContractId newContractId = contractReceipt.getContractId();

        return newContractId.toString();
    }

    //create contract with bytecode that has 1 string parameter in its constructor.
    public String createContract(String bytecode, String constructorParameterValue) throws HederaStatusException {
        FileId bytecodeFile = createBytecodeFile(bytecode);
        var contractTxId =
                new ContractCreateTransaction()
                        .setBytecodeFileId(bytecodeFile)
                        .setAutoRenewPeriod(Duration.ofSeconds(8000000))
                        .setMaxTransactionFee(new Hbar(20))
                        .setGas(100_000_000)
                        .setAdminKey(EnvUtils.getOperatorKey().publicKey) //allows to delete contract
                        .setConstructorParams(
                                new ContractFunctionParams()
                                        .addString(constructorParameterValue))
                        .execute(client);

        var contractReceipt = contractTxId.getReceipt(client);
        ContractId newContractId = contractReceipt.getContractId();

        return newContractId.toString();
    }

    //delete contract
    public boolean deleteContract(String contractId) throws HederaStatusException {
        new ContractDeleteTransaction()
                .setContractId(ContractId.fromString(contractId))
                .setTransferAccountId(EnvUtils.getOperatorId())
                .execute(client)
                .getReceipt(client); //to confirm deletion
        System.out.println("Deleted contract: " + contractId);
        return true;
    }

    //get info of a contract
    //FIXME: https://github.com/hashgraph/hedera-sdk-java/issues/404
    public ContractInfo getContractInfo(String contractId) throws HederaStatusException {
        long cost = new ContractInfoQuery()
                .setContractId(ContractId.fromString(contractId))
                .getCost(client);
        ContractInfo info = new ContractInfoQuery()
                .setContractId(ContractId.fromString(contractId))
                .setQueryPayment(cost + cost / 50)
                .execute(client);
        return info;
    }

    //get bytecode of a contract
    public String queryBytecodeContract(String contractId) throws HederaStatusException {
        long cost = new ContractBytecodeQuery()
                .setContractId(ContractId.fromString(contractId))
                .getCost(client);
        byte[] contents = new ContractBytecodeQuery()
                .setContractId(ContractId.fromString(contractId))
                .setQueryPayment(cost + cost / 50)
                .execute(client);
        String bytecode = new String(contents);
        System.out.println("Bytecode for contact: " + bytecode);
        return bytecode;
    }

    //get state size stored on the contract
    public long queryContractStatesize(String contractId) throws HederaStatusException {
        ContractInfo info = getContractInfo(contractId);
        return info.storage;
    }

    public boolean executeTransactionOnContract(ContractCall request) throws HederaStatusException {
        ContractId contractId = ContractId.fromString(request.getContractId());
        String functionName = request.getFunctionName();
        String argument = request.getArgument();

        var contractExecTxnId = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setGas(100_000_000)
                .setFunction(functionName, new ContractFunctionParams()
                        .addString(argument))
                .execute(client);
        // if this doesn't throw then we know the contract executed successfully
        contractExecTxnId.getReceipt(client);
        return true;
    }

    public String contractCallQuery(ContractCall request) throws HederaStatusException {
        ContractId contractId = ContractId.fromString(request.getContractId());
        String functionName = request.getFunctionName();
        String argument = request.getArgument();

        long cost = new ContractCallQuery()
                .setContractId(contractId)
                .setGas(21000) //get this value from remix + trial and error on hedera.
                .setFunction(
                        functionName,
                        new ContractFunctionParams()
                                .addString(argument))
                .getCost(client);
        long estimatedCost = cost + cost / 50; // add 2% of this cost
        var contractCallResult = new ContractCallQuery()
                .setContractId(contractId)
                .setQueryPayment(estimatedCost)
                .setGas(21000) //get this value from remix + trial and error on hedera.
                .setFunction(
                        functionName,
                        new ContractFunctionParams()
                                .addString(argument))
                .execute(client);

        if (contractCallResult.errorMessage != null) {
            String msg = "error calling contract: " + contractCallResult.errorMessage;
            throw new RuntimeException(msg);
        }
        //get value from EVM:
        return contractCallResult.getString(0);
    }


    //HELPER- create a bytecode file.
    private FileId createBytecodeFile(String byteCodeHex) throws HederaStatusException {
        byte[] byteCode = byteCodeHex.getBytes();

        int numParts = byteCode.length / FILE_PART_SIZE;
        int remainder = byteCode.length % FILE_PART_SIZE;
        // add in 5k chunks
        byte[] firstPartBytes;
        if (byteCode.length <= FILE_PART_SIZE) {
            firstPartBytes = byteCode;
            remainder = 0;
        } else {
            firstPartBytes = Utils.copyBytes(0, FILE_PART_SIZE, byteCode);
        }

        // create the contract's bytecode file
        var fileTxId =
                new FileCreateTransaction()
                        .setExpirationTime(Instant.now().plus(Duration.ofMillis(7890000000L)))
                        // Use the same key as the operator to "own" this file
                        .addKey(EnvUtils.getOperatorKey().publicKey)
                        .setContents(firstPartBytes)
                        .setMaxTransactionFee(new Hbar(5))
                        .execute(client);

        var fileReceipt = fileTxId.getReceipt(client);
        FileId newFileId = fileReceipt.getFileId();

        System.out.println("Bytecode file ID: " + newFileId);

        // add remaining chunks
        // append the rest of the parts
        for (int i = 1; i < numParts; i++) {
            byte[] partBytes = Utils.copyBytes(i * FILE_PART_SIZE, FILE_PART_SIZE, byteCode);
            new FileAppendTransaction()
                    .setFileId(newFileId)
                    .setMaxTransactionFee(new Hbar(5))
                    .setContents(partBytes)
                    .execute(client);
        }
        // appending remaining data
        if (remainder > 0) {
            byte[] partBytes = Utils.copyBytes(numParts * FILE_PART_SIZE, remainder, byteCode);
            new FileAppendTransaction()
                    .setFileId(newFileId)
                    .setMaxTransactionFee(new Hbar(5))
                    .setContents(partBytes)
                    .execute(client);
        }

        return newFileId;
    }

}
