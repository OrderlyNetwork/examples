package org.web3j;

import java.io.IOException;
import java.security.*;

import org.json.JSONObject;
import org.web3j.crypto.*;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderlyClient {

   public final Config config;

   private OkHttpClient client;
   private Credentials credentials;
   private Signer signer;

   private String accountId;

   private Register register;

   public final Account account;
   public final Order order;
   public final PnL pnl;

   public OrderlyClient(Config config, OkHttpClient client, Credentials credentials) {
      this.config = config;
      this.client = client;
      this.credentials = credentials;
      this.signer = new Signer(config.baseUrl, accountId);

      this.register = new Register(config, client, credentials);
      this.account = new Account(config, client, signer, credentials);
      this.order = new Order(config, client, signer);
      this.pnl = new PnL(config, client, signer, credentials);
   }

   public OrderlyClient(Config config, OkHttpClient client, Credentials credentials,
         String accountId) {
      this.config = config;
      this.client = client;
      this.credentials = credentials;
      this.signer = new Signer(config.baseUrl, accountId);

      this.accountId = accountId;

      this.register = new Register(config, client, credentials);
      this.account = new Account(config, client, signer, credentials);
      this.order = new Order(config, client, signer);
      this.pnl = new PnL(config, client, signer, credentials);
   }

   public OrderlyClient(Config config, OkHttpClient client, Credentials credentials,
         String accountId, EdDSAPrivateKey privateKey) {
      this.config = config;
      this.client = client;
      this.credentials = credentials;
      this.signer = new Signer(config.baseUrl, accountId, privateKey);

      this.accountId = accountId;

      this.register = new Register(config, client, credentials);
      this.account = new Account(config, client, signer, credentials);
      this.order = new Order(config, client, signer);
      this.pnl = new PnL(config, client, signer, credentials);
   }

   public String getAccountId() {
      return this.accountId;
   }

   /**
    * Initializes client.
    * Fetches account ID and registers account, if not yet registered.
    * 
    * @throws IOException
    */
   public void initialize() throws IOException {
      Request req = new Request.Builder()
            .url(config.baseUrl + "/v1/get_account?address=" + credentials.getAddress() + "&broker_id="
                  + config.brokerId)
            .build();

      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      JSONObject accountObj = new JSONObject(res);
      System.out.println("get_account response: " + accountObj);

      register = new Register(config, client, credentials);

      if (accountObj.getBoolean("success")) {
         accountId = accountObj.getJSONObject("data").getString("account_id");
      } else {
         accountId = register.registerAccount();
      }
      signer.accountId = accountId;
   }

   /**
    * Create new randomly generated orderly key and stores it in the client.
    *
    * @throws InvalidAlgorithmParameterException
    * @throws NoSuchAlgorithmException
    * @throws NoSuchProviderException
    * @throws IOException
    */
   public void createNewAccessKey()
         throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
      signer.privateKey = ((EdDSAPrivateKey) this.register.addAccessKey().getPrivate());
   }
}
