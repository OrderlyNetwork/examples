package org.web3j;

public class Config {
   public final String baseUrl;
   public final String brokerId;
   public final int chainId;

   Config(String baseUrl, String brokerId, int chainId) {
      this.baseUrl = baseUrl;
      this.brokerId = brokerId;
      this.chainId = chainId;
   }

   public static Config testnet() {
      return new Config("https://testnet-api-evm.orderly.org", "woofi_dex", 421613);
   }
}
