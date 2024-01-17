package org.web3j;

import org.json.JSONObject;

public abstract class Eip712 {
   public static JSONObject MESSAGE_TYPES = new JSONObject("""
         {
            "EIP712Domain": [
               {"name": "name", "type": "string"},
               {"name": "version", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "verifyingContract", "type": "address"},
            ],
            "Registration": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "timestamp", "type": "uint64"},
               {"name": "registrationNonce", "type": "uint256"},
            ],
            "AddOrderlyKey": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "orderlyKey", "type": "string"},
               {"name": "scope", "type": "string"},
               {"name": "timestamp", "type": "uint64"},
               {"name": "expiration", "type": "uint64"},
            ],
            "Withdraw": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "receiver", "type": "address"},
               {"name": "token", "type": "string"},
               {"name": "amount", "type": "uint256"},
               {"name": "withdrawNonce", "type": "uint64"},
               {"name": "timestamp", "type": "uint64"},
            ],
            "SettlePnl": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "settleNonce", "type": "uint64"},
               {"name": "timestamp", "type": "uint64"},
            ],
         }""");

   public static JSONObject OFF_CHAIN_DOMAIN = new JSONObject("""
         {
            "name": "Orderly",
            "version": "1",
            "chainId": 421613,
            "verifyingContract": "0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC",
         }""");

   public static JSONObject ON_CHAIN_DOMAIN = new JSONObject("""
         {
            "name": "Orderly",
            "version": "1",
            "chainId": 421613,
            "verifyingContract": "0x1826B75e2ef249173FC735149AE4B8e9ea10abff",
         }""");
}
