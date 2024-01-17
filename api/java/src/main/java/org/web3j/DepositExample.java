package org.web3j;

import java.math.BigInteger;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import io.github.cdimascio.dotenv.Dotenv;

public class DepositExample {
   public static void main(String[] args) throws Exception {
      String brokerId = "woofi_dex";
      String tokenId = "USDC";
      String orderlyAccountId = "0x...";

      String usdcAddress = "0xfd064a18f3bf249cf1f87fc203e90d8f650f2d63";
      String vaultAddress = "0x0c554ddb6a9010ed1fd7e50d92559a06655da482";

      Dotenv dotenv = Dotenv.load();
      String pk = dotenv.get("PRIVATE_KEY");
      Credentials credentials = Credentials.create(ECKeyPair.create(Hex.decodeHex(pk)));

      Web3j web3j = Web3j.build(new HttpService("https://arbitrum-goerli.publicnode.com"));
      DefaultGasProvider gasProvider = new DefaultGasProvider();

      NativeUSDC USDC = new NativeUSDC(usdcAddress, web3j, credentials, gasProvider);

      BigInteger depositAmount = new BigInteger("100000");

      // approve USDC ERC-20 to be transferred to Vault contract
      USDC.approve(vaultAddress, depositAmount).send();

      Vault vault = new Vault(vaultAddress, web3j, credentials, gasProvider);

      Keccak.Digest256 brokerHash = new Keccak.Digest256();
      byte[] brokerIdBytes = brokerId.getBytes("UTF-8");
      brokerHash.update(brokerIdBytes, 0, brokerIdBytes.length);

      Keccak.Digest256 tokenHash = new Keccak.Digest256();
      byte[] usdcBytes = tokenId.getBytes("UTF-8");
      tokenHash.update(usdcBytes, 0, usdcBytes.length);

      Vault.VaultDepositFE depositInput = new Vault.VaultDepositFE(
            Hex.decodeHex(orderlyAccountId.substring(2)),
            brokerHash.digest(),
            tokenHash.digest(),
            depositAmount);

      // get wei deposit fee for `deposit` call
      BigInteger depositFee = vault.getDepositFee(vaultAddress, depositInput).send();

      // deposit USDC into Vault contract
      vault.deposit(
            depositInput,
            depositFee).send();
   }
}
