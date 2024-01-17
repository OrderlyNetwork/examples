package org.web3j;

public enum OrderlyClientExceptionReason {
   ACCOUNT_UNINITIALIZED,
   KEYPAIR_UNINITIALIZED;

   public OrderlyClientException asException() {
      return switch (this) {
         case ACCOUNT_UNINITIALIZED -> new OrderlyClientException(
               "orderly-account-id uninitialized. You either need to provide the account ID yourself or call `OrderlyClient#initialize` once to register your account");
         case KEYPAIR_UNINITIALIZED -> new OrderlyClientException(
               "orderly-key uninitialized. You either need to provide your own key pair or generate a new one and register it via `OrderlyClient#createNewAccessKey`");
      };
   }
}
