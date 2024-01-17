package org.web3j;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.0.
 */
@SuppressWarnings("rawtypes")
public class Vault extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ALLOWEDTOKEN = "allowedToken";

    public static final String FUNC_CHANGETOKENADDRESSANDALLOW = "changeTokenAddressAndAllow";

    public static final String FUNC_CROSSCHAINMANAGERADDRESS = "crossChainManagerAddress";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_DEPOSITFEEENABLED = "depositFeeEnabled";

    public static final String FUNC_DEPOSITID = "depositId";

    public static final String FUNC_DEPOSITTO = "depositTo";

    public static final String FUNC_EMERGENCYPAUSE = "emergencyPause";

    public static final String FUNC_EMERGENCYUNPAUSE = "emergencyUnpause";

    public static final String FUNC_ENABLEDEPOSITFEE = "enableDepositFee";

    public static final String FUNC_GETALLALLOWEDBROKER = "getAllAllowedBroker";

    public static final String FUNC_GETALLALLOWEDTOKEN = "getAllAllowedToken";

    public static final String FUNC_GETALLOWEDBROKER = "getAllowedBroker";

    public static final String FUNC_GETALLOWEDTOKEN = "getAllowedToken";

    public static final String FUNC_GETDEPOSITFEE = "getDepositFee";

    public static final String FUNC_INITIALIZE = "initialize";

    public static final String FUNC_MESSAGETRANSMITTERCONTRACT = "messageTransmitterContract";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_REBALANCEBURN = "rebalanceBurn";

    public static final String FUNC_REBALANCEMINT = "rebalanceMint";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETALLOWEDBROKER = "setAllowedBroker";

    public static final String FUNC_SETALLOWEDTOKEN = "setAllowedToken";

    public static final String FUNC_SETCROSSCHAINMANAGER = "setCrossChainManager";

    public static final String FUNC_SETREBALANCEMESSENGERCONTRACT = "setRebalanceMessengerContract";

    public static final String FUNC_SETTOKENMESSENGERCONTRACT = "setTokenMessengerContract";

    public static final String FUNC_TOKENMESSENGERCONTRACT = "tokenMessengerContract";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final Event ACCOUNTDEPOSIT_EVENT = new Event("AccountDeposit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint64>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint128>() {}));
    ;

    public static final Event ACCOUNTDEPOSITTO_EVENT = new Event("AccountDepositTo", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint64>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint128>() {}));
    ;

    public static final Event ACCOUNTWITHDRAW_EVENT = new Event("AccountWithdraw", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Uint64>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint128>() {}, new TypeReference<Uint128>() {}));
    ;

    public static final Event CHANGECROSSCHAINMANAGER_EVENT = new Event("ChangeCrossChainManager", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event CHANGETOKENADDRESSANDALLOW_EVENT = new Event("ChangeTokenAddressAndAllow", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));
    ;

    public static final Event INITIALIZED_EVENT = new Event("Initialized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSED_EVENT = new Event("Paused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event SETALLOWEDBROKER_EVENT = new Event("SetAllowedBroker", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event SETALLOWEDTOKEN_EVENT = new Event("SetAllowedToken", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Vault(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Vault(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Vault(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Vault(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AccountDepositEventResponse> getAccountDepositEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCOUNTDEPOSIT_EVENT, transactionReceipt);
        ArrayList<AccountDepositEventResponse> responses = new ArrayList<AccountDepositEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccountDepositEventResponse typedResponse = new AccountDepositEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.accountId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.userAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.depositNonce = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tokenHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccountDepositEventResponse getAccountDepositEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCOUNTDEPOSIT_EVENT, log);
        AccountDepositEventResponse typedResponse = new AccountDepositEventResponse();
        typedResponse.log = log;
        typedResponse.accountId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.userAddress = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.depositNonce = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.tokenHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<AccountDepositEventResponse> accountDepositEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccountDepositEventFromLog(log));
    }

    public Flowable<AccountDepositEventResponse> accountDepositEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCOUNTDEPOSIT_EVENT));
        return accountDepositEventFlowable(filter);
    }

    public static List<AccountDepositToEventResponse> getAccountDepositToEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCOUNTDEPOSITTO_EVENT, transactionReceipt);
        ArrayList<AccountDepositToEventResponse> responses = new ArrayList<AccountDepositToEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccountDepositToEventResponse typedResponse = new AccountDepositToEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.accountId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.userAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.depositNonce = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tokenHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccountDepositToEventResponse getAccountDepositToEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCOUNTDEPOSITTO_EVENT, log);
        AccountDepositToEventResponse typedResponse = new AccountDepositToEventResponse();
        typedResponse.log = log;
        typedResponse.accountId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.userAddress = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.depositNonce = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.tokenHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<AccountDepositToEventResponse> accountDepositToEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccountDepositToEventFromLog(log));
    }

    public Flowable<AccountDepositToEventResponse> accountDepositToEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCOUNTDEPOSITTO_EVENT));
        return accountDepositToEventFlowable(filter);
    }

    public static List<AccountWithdrawEventResponse> getAccountWithdrawEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCOUNTWITHDRAW_EVENT, transactionReceipt);
        ArrayList<AccountWithdrawEventResponse> responses = new ArrayList<AccountWithdrawEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccountWithdrawEventResponse typedResponse = new AccountWithdrawEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.accountId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.withdrawNonce = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.brokerHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.receiver = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.tokenHash = (byte[]) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccountWithdrawEventResponse getAccountWithdrawEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCOUNTWITHDRAW_EVENT, log);
        AccountWithdrawEventResponse typedResponse = new AccountWithdrawEventResponse();
        typedResponse.log = log;
        typedResponse.accountId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.withdrawNonce = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.brokerHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.sender = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.receiver = (String) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.tokenHash = (byte[]) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        return typedResponse;
    }

    public Flowable<AccountWithdrawEventResponse> accountWithdrawEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccountWithdrawEventFromLog(log));
    }

    public Flowable<AccountWithdrawEventResponse> accountWithdrawEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCOUNTWITHDRAW_EVENT));
        return accountWithdrawEventFlowable(filter);
    }

    public static List<ChangeCrossChainManagerEventResponse> getChangeCrossChainManagerEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CHANGECROSSCHAINMANAGER_EVENT, transactionReceipt);
        ArrayList<ChangeCrossChainManagerEventResponse> responses = new ArrayList<ChangeCrossChainManagerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ChangeCrossChainManagerEventResponse typedResponse = new ChangeCrossChainManagerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ChangeCrossChainManagerEventResponse getChangeCrossChainManagerEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CHANGECROSSCHAINMANAGER_EVENT, log);
        ChangeCrossChainManagerEventResponse typedResponse = new ChangeCrossChainManagerEventResponse();
        typedResponse.log = log;
        typedResponse.oldAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<ChangeCrossChainManagerEventResponse> changeCrossChainManagerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getChangeCrossChainManagerEventFromLog(log));
    }

    public Flowable<ChangeCrossChainManagerEventResponse> changeCrossChainManagerEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CHANGECROSSCHAINMANAGER_EVENT));
        return changeCrossChainManagerEventFlowable(filter);
    }

    public static List<ChangeTokenAddressAndAllowEventResponse> getChangeTokenAddressAndAllowEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CHANGETOKENADDRESSANDALLOW_EVENT, transactionReceipt);
        ArrayList<ChangeTokenAddressAndAllowEventResponse> responses = new ArrayList<ChangeTokenAddressAndAllowEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ChangeTokenAddressAndAllowEventResponse typedResponse = new ChangeTokenAddressAndAllowEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tokenHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ChangeTokenAddressAndAllowEventResponse getChangeTokenAddressAndAllowEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CHANGETOKENADDRESSANDALLOW_EVENT, log);
        ChangeTokenAddressAndAllowEventResponse typedResponse = new ChangeTokenAddressAndAllowEventResponse();
        typedResponse.log = log;
        typedResponse._tokenHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse._tokenAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ChangeTokenAddressAndAllowEventResponse> changeTokenAddressAndAllowEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getChangeTokenAddressAndAllowEventFromLog(log));
    }

    public Flowable<ChangeTokenAddressAndAllowEventResponse> changeTokenAddressAndAllowEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CHANGETOKENADDRESSANDALLOW_EVENT));
        return changeTokenAddressAndAllowEventFlowable(filter);
    }

    public static List<InitializedEventResponse> getInitializedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(INITIALIZED_EVENT, transactionReceipt);
        ArrayList<InitializedEventResponse> responses = new ArrayList<InitializedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InitializedEventResponse typedResponse = new InitializedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static InitializedEventResponse getInitializedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(INITIALIZED_EVENT, log);
        InitializedEventResponse typedResponse = new InitializedEventResponse();
        typedResponse.log = log;
        typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<InitializedEventResponse> initializedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getInitializedEventFromLog(log));
    }

    public Flowable<InitializedEventResponse> initializedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(INITIALIZED_EVENT));
        return initializedEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<PausedEventResponse> getPausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PAUSED_EVENT, transactionReceipt);
        ArrayList<PausedEventResponse> responses = new ArrayList<PausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PausedEventResponse typedResponse = new PausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PausedEventResponse getPausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PAUSED_EVENT, log);
        PausedEventResponse typedResponse = new PausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPausedEventFromLog(log));
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSED_EVENT));
        return pausedEventFlowable(filter);
    }

    public static List<SetAllowedBrokerEventResponse> getSetAllowedBrokerEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SETALLOWEDBROKER_EVENT, transactionReceipt);
        ArrayList<SetAllowedBrokerEventResponse> responses = new ArrayList<SetAllowedBrokerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetAllowedBrokerEventResponse typedResponse = new SetAllowedBrokerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._brokerHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._allowed = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SetAllowedBrokerEventResponse getSetAllowedBrokerEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SETALLOWEDBROKER_EVENT, log);
        SetAllowedBrokerEventResponse typedResponse = new SetAllowedBrokerEventResponse();
        typedResponse.log = log;
        typedResponse._brokerHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse._allowed = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<SetAllowedBrokerEventResponse> setAllowedBrokerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getSetAllowedBrokerEventFromLog(log));
    }

    public Flowable<SetAllowedBrokerEventResponse> setAllowedBrokerEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETALLOWEDBROKER_EVENT));
        return setAllowedBrokerEventFlowable(filter);
    }

    public static List<SetAllowedTokenEventResponse> getSetAllowedTokenEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SETALLOWEDTOKEN_EVENT, transactionReceipt);
        ArrayList<SetAllowedTokenEventResponse> responses = new ArrayList<SetAllowedTokenEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetAllowedTokenEventResponse typedResponse = new SetAllowedTokenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tokenHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._allowed = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SetAllowedTokenEventResponse getSetAllowedTokenEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SETALLOWEDTOKEN_EVENT, log);
        SetAllowedTokenEventResponse typedResponse = new SetAllowedTokenEventResponse();
        typedResponse.log = log;
        typedResponse._tokenHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse._allowed = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<SetAllowedTokenEventResponse> setAllowedTokenEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getSetAllowedTokenEventFromLog(log));
    }

    public Flowable<SetAllowedTokenEventResponse> setAllowedTokenEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETALLOWEDTOKEN_EVENT));
        return setAllowedTokenEventFlowable(filter);
    }

    public static List<UnpausedEventResponse> getUnpausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(UNPAUSED_EVENT, transactionReceipt);
        ArrayList<UnpausedEventResponse> responses = new ArrayList<UnpausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UnpausedEventResponse typedResponse = new UnpausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UnpausedEventResponse getUnpausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(UNPAUSED_EVENT, log);
        UnpausedEventResponse typedResponse = new UnpausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUnpausedEventFromLog(log));
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSED_EVENT));
        return unpausedEventFlowable(filter);
    }

    public RemoteFunctionCall<String> allowedToken(byte[] param0) {
        final Function function = new Function(FUNC_ALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> changeTokenAddressAndAllow(byte[] _tokenHash, String _tokenAddress) {
        final Function function = new Function(
                FUNC_CHANGETOKENADDRESSANDALLOW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_tokenHash), 
                new org.web3j.abi.datatypes.Address(160, _tokenAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> crossChainManagerAddress() {
        final Function function = new Function(FUNC_CROSSCHAINMANAGERADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> deposit(VaultDepositFE data, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<Boolean> depositFeeEnabled() {
        final Function function = new Function(FUNC_DEPOSITFEEENABLED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> depositId() {
        final Function function = new Function(FUNC_DEPOSITID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> depositTo(String receiver, VaultDepositFE data, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DEPOSITTO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, receiver), 
                data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> emergencyPause() {
        final Function function = new Function(
                FUNC_EMERGENCYPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> emergencyUnpause() {
        final Function function = new Function(
                FUNC_EMERGENCYUNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> enableDepositFee(Boolean _enabled) {
        final Function function = new Function(
                FUNC_ENABLEDEPOSITFEE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(_enabled)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getAllAllowedBroker() {
        final Function function = new Function(FUNC_GETALLALLOWEDBROKER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getAllAllowedToken() {
        final Function function = new Function(FUNC_GETALLALLOWEDTOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Boolean> getAllowedBroker(byte[] _brokerHash) {
        final Function function = new Function(FUNC_GETALLOWEDBROKER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_brokerHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> getAllowedToken(byte[] _tokenHash) {
        final Function function = new Function(FUNC_GETALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_tokenHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getDepositFee(String receiver, VaultDepositFE data) {
        final Function function = new Function(FUNC_GETDEPOSITFEE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, receiver), 
                data), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> initialize() {
        final Function function = new Function(
                FUNC_INITIALIZE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> messageTransmitterContract() {
        final Function function = new Function(FUNC_MESSAGETRANSMITTERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> rebalanceBurn(RebalanceBurnCCData data) {
        final Function function = new Function(
                FUNC_REBALANCEBURN, 
                Arrays.<Type>asList(data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> rebalanceMint(RebalanceMintCCData data) {
        final Function function = new Function(
                FUNC_REBALANCEMINT, 
                Arrays.<Type>asList(data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setAllowedBroker(byte[] _brokerHash, Boolean _allowed) {
        final Function function = new Function(
                FUNC_SETALLOWEDBROKER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_brokerHash), 
                new org.web3j.abi.datatypes.Bool(_allowed)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setAllowedToken(byte[] _tokenHash, Boolean _allowed) {
        final Function function = new Function(
                FUNC_SETALLOWEDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_tokenHash), 
                new org.web3j.abi.datatypes.Bool(_allowed)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setCrossChainManager(String _crossChainManagerAddress) {
        final Function function = new Function(
                FUNC_SETCROSSCHAINMANAGER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _crossChainManagerAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setRebalanceMessengerContract(String _rebalanceMessengerContract) {
        final Function function = new Function(
                FUNC_SETREBALANCEMESSENGERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _rebalanceMessengerContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setTokenMessengerContract(String _tokenMessengerContract) {
        final Function function = new Function(
                FUNC_SETTOKENMESSENGERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenMessengerContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> tokenMessengerContract() {
        final Function function = new Function(FUNC_TOKENMESSENGERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> withdraw(VaultWithdraw data) {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Vault load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vault(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Vault load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vault(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Vault load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Vault(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Vault load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Vault(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class VaultDepositFE extends StaticStruct {
        public byte[] accountId;

        public byte[] brokerHash;

        public byte[] tokenHash;

        public BigInteger tokenAmount;

        public VaultDepositFE(byte[] accountId, byte[] brokerHash, byte[] tokenHash, BigInteger tokenAmount) {
            super(new org.web3j.abi.datatypes.generated.Bytes32(accountId), 
                    new org.web3j.abi.datatypes.generated.Bytes32(brokerHash), 
                    new org.web3j.abi.datatypes.generated.Bytes32(tokenHash), 
                    new org.web3j.abi.datatypes.generated.Uint128(tokenAmount));
            this.accountId = accountId;
            this.brokerHash = brokerHash;
            this.tokenHash = tokenHash;
            this.tokenAmount = tokenAmount;
        }

        public VaultDepositFE(Bytes32 accountId, Bytes32 brokerHash, Bytes32 tokenHash, Uint128 tokenAmount) {
            super(accountId, brokerHash, tokenHash, tokenAmount);
            this.accountId = accountId.getValue();
            this.brokerHash = brokerHash.getValue();
            this.tokenHash = tokenHash.getValue();
            this.tokenAmount = tokenAmount.getValue();
        }
    }

    public static class RebalanceBurnCCData extends StaticStruct {
        public BigInteger dstDomain;

        public BigInteger rebalanceId;

        public BigInteger amount;

        public byte[] tokenHash;

        public BigInteger srcChainId;

        public BigInteger dstChainId;

        public String dstVaultAddress;

        public RebalanceBurnCCData(BigInteger dstDomain, BigInteger rebalanceId, BigInteger amount, byte[] tokenHash, BigInteger srcChainId, BigInteger dstChainId, String dstVaultAddress) {
            super(new org.web3j.abi.datatypes.generated.Uint32(dstDomain), 
                    new org.web3j.abi.datatypes.generated.Uint64(rebalanceId), 
                    new org.web3j.abi.datatypes.generated.Uint128(amount), 
                    new org.web3j.abi.datatypes.generated.Bytes32(tokenHash), 
                    new org.web3j.abi.datatypes.generated.Uint256(srcChainId), 
                    new org.web3j.abi.datatypes.generated.Uint256(dstChainId), 
                    new org.web3j.abi.datatypes.Address(160, dstVaultAddress));
            this.dstDomain = dstDomain;
            this.rebalanceId = rebalanceId;
            this.amount = amount;
            this.tokenHash = tokenHash;
            this.srcChainId = srcChainId;
            this.dstChainId = dstChainId;
            this.dstVaultAddress = dstVaultAddress;
        }

        public RebalanceBurnCCData(Uint32 dstDomain, Uint64 rebalanceId, Uint128 amount, Bytes32 tokenHash, Uint256 srcChainId, Uint256 dstChainId, Address dstVaultAddress) {
            super(dstDomain, rebalanceId, amount, tokenHash, srcChainId, dstChainId, dstVaultAddress);
            this.dstDomain = dstDomain.getValue();
            this.rebalanceId = rebalanceId.getValue();
            this.amount = amount.getValue();
            this.tokenHash = tokenHash.getValue();
            this.srcChainId = srcChainId.getValue();
            this.dstChainId = dstChainId.getValue();
            this.dstVaultAddress = dstVaultAddress.getValue();
        }
    }

    public static class RebalanceMintCCData extends DynamicStruct {
        public BigInteger rebalanceId;

        public BigInteger amount;

        public byte[] tokenHash;

        public BigInteger srcChainId;

        public BigInteger dstChainId;

        public byte[] messageBytes;

        public byte[] messageSignature;

        public RebalanceMintCCData(BigInteger rebalanceId, BigInteger amount, byte[] tokenHash, BigInteger srcChainId, BigInteger dstChainId, byte[] messageBytes, byte[] messageSignature) {
            super(new org.web3j.abi.datatypes.generated.Uint64(rebalanceId), 
                    new org.web3j.abi.datatypes.generated.Uint128(amount), 
                    new org.web3j.abi.datatypes.generated.Bytes32(tokenHash), 
                    new org.web3j.abi.datatypes.generated.Uint256(srcChainId), 
                    new org.web3j.abi.datatypes.generated.Uint256(dstChainId), 
                    new org.web3j.abi.datatypes.DynamicBytes(messageBytes), 
                    new org.web3j.abi.datatypes.DynamicBytes(messageSignature));
            this.rebalanceId = rebalanceId;
            this.amount = amount;
            this.tokenHash = tokenHash;
            this.srcChainId = srcChainId;
            this.dstChainId = dstChainId;
            this.messageBytes = messageBytes;
            this.messageSignature = messageSignature;
        }

        public RebalanceMintCCData(Uint64 rebalanceId, Uint128 amount, Bytes32 tokenHash, Uint256 srcChainId, Uint256 dstChainId, DynamicBytes messageBytes, DynamicBytes messageSignature) {
            super(rebalanceId, amount, tokenHash, srcChainId, dstChainId, messageBytes, messageSignature);
            this.rebalanceId = rebalanceId.getValue();
            this.amount = amount.getValue();
            this.tokenHash = tokenHash.getValue();
            this.srcChainId = srcChainId.getValue();
            this.dstChainId = dstChainId.getValue();
            this.messageBytes = messageBytes.getValue();
            this.messageSignature = messageSignature.getValue();
        }
    }

    public static class VaultWithdraw extends StaticStruct {
        public byte[] accountId;

        public byte[] brokerHash;

        public byte[] tokenHash;

        public BigInteger tokenAmount;

        public BigInteger fee;

        public String sender;

        public String receiver;

        public BigInteger withdrawNonce;

        public VaultWithdraw(byte[] accountId, byte[] brokerHash, byte[] tokenHash, BigInteger tokenAmount, BigInteger fee, String sender, String receiver, BigInteger withdrawNonce) {
            super(new org.web3j.abi.datatypes.generated.Bytes32(accountId), 
                    new org.web3j.abi.datatypes.generated.Bytes32(brokerHash), 
                    new org.web3j.abi.datatypes.generated.Bytes32(tokenHash), 
                    new org.web3j.abi.datatypes.generated.Uint128(tokenAmount), 
                    new org.web3j.abi.datatypes.generated.Uint128(fee), 
                    new org.web3j.abi.datatypes.Address(160, sender), 
                    new org.web3j.abi.datatypes.Address(160, receiver), 
                    new org.web3j.abi.datatypes.generated.Uint64(withdrawNonce));
            this.accountId = accountId;
            this.brokerHash = brokerHash;
            this.tokenHash = tokenHash;
            this.tokenAmount = tokenAmount;
            this.fee = fee;
            this.sender = sender;
            this.receiver = receiver;
            this.withdrawNonce = withdrawNonce;
        }

        public VaultWithdraw(Bytes32 accountId, Bytes32 brokerHash, Bytes32 tokenHash, Uint128 tokenAmount, Uint128 fee, Address sender, Address receiver, Uint64 withdrawNonce) {
            super(accountId, brokerHash, tokenHash, tokenAmount, fee, sender, receiver, withdrawNonce);
            this.accountId = accountId.getValue();
            this.brokerHash = brokerHash.getValue();
            this.tokenHash = tokenHash.getValue();
            this.tokenAmount = tokenAmount.getValue();
            this.fee = fee.getValue();
            this.sender = sender.getValue();
            this.receiver = receiver.getValue();
            this.withdrawNonce = withdrawNonce.getValue();
        }
    }

    public static class AccountDepositEventResponse extends BaseEventResponse {
        public byte[] accountId;

        public String userAddress;

        public BigInteger depositNonce;

        public byte[] tokenHash;

        public BigInteger tokenAmount;
    }

    public static class AccountDepositToEventResponse extends BaseEventResponse {
        public byte[] accountId;

        public String userAddress;

        public BigInteger depositNonce;

        public byte[] tokenHash;

        public BigInteger tokenAmount;
    }

    public static class AccountWithdrawEventResponse extends BaseEventResponse {
        public byte[] accountId;

        public BigInteger withdrawNonce;

        public byte[] brokerHash;

        public String sender;

        public String receiver;

        public byte[] tokenHash;

        public BigInteger tokenAmount;

        public BigInteger fee;
    }

    public static class ChangeCrossChainManagerEventResponse extends BaseEventResponse {
        public String oldAddress;

        public String newAddress;
    }

    public static class ChangeTokenAddressAndAllowEventResponse extends BaseEventResponse {
        public byte[] _tokenHash;

        public String _tokenAddress;
    }

    public static class InitializedEventResponse extends BaseEventResponse {
        public BigInteger version;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class PausedEventResponse extends BaseEventResponse {
        public String account;
    }

    public static class SetAllowedBrokerEventResponse extends BaseEventResponse {
        public byte[] _brokerHash;

        public Boolean _allowed;
    }

    public static class SetAllowedTokenEventResponse extends BaseEventResponse {
        public byte[] _tokenHash;

        public Boolean _allowed;
    }

    public static class UnpausedEventResponse extends BaseEventResponse {
        public String account;
    }
}
