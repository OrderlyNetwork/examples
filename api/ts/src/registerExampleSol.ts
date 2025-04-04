import { Keypair, PublicKey, Transaction, TransactionInstruction } from '@solana/web3.js'; //npm install @solana/web3.js@1
import bs58 from 'bs58'; //npm install bs58
import { keccak256 } from 'ethereum-cryptography/keccak'; //npm install ethereum-cryptography
import { bytesToHex, hexToBytes } from 'ethereum-cryptography/utils'; //npm install ethereum-cryptography
import { AbiCoder, solidityPackedKeccak256 } from 'ethers'; //npm install ethers

// Replace it with your own solana secret key
const SOL_SECRET_KEY = '5QB__________________ak6NCgMPU39nrPg3GA9cMW6CAWhS5ukS';
let solanaPublicKey = '';
const BASE_URL = 'https://testnet-api.orderly.org';
const BROKER_ID = 'woofi_dex';
const CHAIN_ID = 900900900; // Solana mainnet:900900900, Solana devnet:901901901

async function registerAccount(): Promise<void> {
  const userKeypair = Keypair.fromSecretKey(bs58.decode(SOL_SECRET_KEY));
  solanaPublicKey = userKeypair.publicKey.toString();

  console.log('Solana public Key:', solanaPublicKey);

  const nonceRes = await fetch(`${BASE_URL}/v1/registration_nonce`);
  const nonceJson = (await nonceRes.json()) as { data: { registration_nonce: string } };
  const registrationNonce = nonceJson.data.registration_nonce as string;

  console.log('Registration nonce: ' + registrationNonce);

  const [message, messageToSign] = registerAccountMessage(
    CHAIN_ID,
    registrationNonce,
    BROKER_ID,
    Date.now()
  );
  console.log('Registration message: ' + message);
  console.log('Uint8Array Message: ' + messageToSign);

  const signature = await signMessage(messageToSign as Uint8Array, userKeypair);
  console.log('Message signature:' + signature);

  const reqBody = {
    message: {
      ...message,
      chainType: 'SOL'
    },
    signature: signature,
    userAddress: solanaPublicKey
  };

  console.log('Request body:');
  console.log(reqBody);

  const registerRes = await fetch(`${BASE_URL}/v1/register_account`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(reqBody)
  });
  const registerJson = (await registerRes.json()) as {
    success: boolean;
    message: string;
    data: { account_id: string };
  };
  if (!registerJson.success) {
    throw new Error(registerJson.message);
  }
  const orderlyAccountId = registerJson.data.account_id;
  console.log('orderlyAccountId', orderlyAccountId);
}

registerAccount();

async function signMessage(message: Uint8Array, keypair: Keypair) {
  const transaction = new Transaction();

  transaction.add(
    new TransactionInstruction({
      keys: [],
      programId: new PublicKey('ComputeBudget111111111111111111111111111111'),
      data: new Uint8Array([3, 0, 0, 0, 0, 0, 0, 0, 0]) as Buffer
    })
  );

  transaction.add(
    new TransactionInstruction({
      keys: [],
      programId: new PublicKey('ComputeBudget111111111111111111111111111111'),
      data: new Uint8Array([2, 0, 0, 0, 0]) as Buffer
    })
  );

  transaction.add(
    new TransactionInstruction({
      keys: [],
      programId: new PublicKey('MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr'),
      data: message as Buffer
    })
  );

  const userPublicKey = new PublicKey(keypair.publicKey);

  transaction.feePayer = userPublicKey;

  const zeroHash = new Uint8Array(32).fill(0);
  transaction.recentBlockhash = new PublicKey(zeroHash).toString();

  transaction.sign(keypair);

  console.log('tx: ' + transaction);
  console.log('signature: ' + transaction.signature);

  const signature = transaction.signature;

  if (signature) {
    return uint8ArrayToHexString(signature as Uint8Array);
  } else {
    console.log('-- sign message error', signature);
    throw new Error('Unsupported signature');
  }
}

function registerAccountMessage(
  chainId: number,
  registrationNonce: string,
  brokerId: string,
  timestamp: number
) {
  const message = {
    brokerId,
    chainId,
    timestamp,
    registrationNonce
  };
  const brokerIdHash = solidityPackedKeccak256(['string'], [message.brokerId]);
  const abicoder = AbiCoder.defaultAbiCoder();
  const msgToSign = keccak256(
    hexToBytes(
      abicoder.encode(
        ['bytes32', 'uint256', 'uint256', 'uint256'],
        [brokerIdHash, message.chainId, message.timestamp, message.registrationNonce]
      )
    )
  );
  const msgToSignHex = bytesToHex(msgToSign);
  const msgToSignTextEncoded: Uint8Array = new TextEncoder().encode(msgToSignHex);
  return [message, msgToSignTextEncoded];
}

function uint8ArrayToHexString(uint8Array: Uint8Array): string {
  return Array.from(uint8Array)
    .map((byte) => byte.toString(16).padStart(2, '0'))
    .join('');
}

// ts-node src/registerExampleSol.ts
