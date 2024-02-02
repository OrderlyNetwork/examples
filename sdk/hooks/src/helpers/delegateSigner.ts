import { getPublicKeyAsync, utils } from '@noble/ed25519';
import { Account } from '@orderly.network/core';
import { encodeBase58, ethers, solidityPackedKeccak256 } from 'ethers';

import { DelegateSigner__factory, Vault__factory } from '../abi';

const MESSAGE_TYPES = {
  EIP712Domain: [
    { name: 'name', type: 'string' },
    { name: 'version', type: 'string' },
    { name: 'chainId', type: 'uint256' },
    { name: 'verifyingContract', type: 'address' }
  ],
  DelegateSigner: [
    { name: 'delegateContract', type: 'address' },
    { name: 'brokerId', type: 'string' },
    { name: 'chainId', type: 'uint256' },
    { name: 'timestamp', type: 'uint64' },
    { name: 'registrationNonce', type: 'uint256' },
    { name: 'txHash', type: 'bytes32' }
  ],
  DelegateAddOrderlyKey: [
    { name: 'delegateContract', type: 'address' },
    { name: 'brokerId', type: 'string' },
    { name: 'chainId', type: 'uint256' },
    { name: 'orderlyKey', type: 'string' },
    { name: 'scope', type: 'string' },
    { name: 'timestamp', type: 'uint64' },
    { name: 'expiration', type: 'uint64' }
  ],
  DelegateWithdraw: [
    { name: 'delegateContract', type: 'address' },
    { name: 'brokerId', type: 'string' },
    { name: 'chainId', type: 'uint256' },
    { name: 'receiver', type: 'address' },
    { name: 'token', type: 'string' },
    { name: 'amount', type: 'uint256' },
    { name: 'withdrawNonce', type: 'uint64' },
    { name: 'timestamp', type: 'uint64' }
  ],
  DelegateSettlePnl: [
    { name: 'delegateContract', type: 'address' },
    { name: 'brokerId', type: 'string' },
    { name: 'chainId', type: 'uint256' },
    { name: 'settleNonce', type: 'uint64' },
    { name: 'timestamp', type: 'uint64' }
  ]
};

const BASE_URL = 'https://testnet-api-evm.orderly.org';
const BROKER_ID = 'woofi_dex';
const CHAIN_ID = 421614;

export const delegateContract = '0xa4394b62261061c629800c6d86d153a9f38f0cbb';
const vaultContract = '0x0EaC556c0C2321BA25b9DC01e4e3c95aD5CDCd2f';

const OFF_CHAIN_DOMAIN = {
  name: 'Orderly',
  version: '1',
  chainId: CHAIN_ID,
  verifyingContract: '0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC'
};
const ON_CHAIN_DOMAIN = {
  name: 'Orderly',
  version: '1',
  chainId: CHAIN_ID,
  verifyingContract: vaultContract
};

export type DelegateSignerResponse = {
  account_id: string;
  user_id: number;
  valid_signer: string;
};

export async function registerDelegateSigner(
  wallet: ethers.ContractRunner,
  address: string
): Promise<string> {
  const contract = DelegateSigner__factory.connect(delegateContract, wallet);
  const res = await contract.delegate(vaultContract, {
    brokerHash: solidityPackedKeccak256(['string'], ['woofi_dex']),
    delegateSigner: address
  });
  console.log('RES', res);
  return res.hash;
}

export async function announceDelegateSigner(
  account: Account,
  txHash: ethers.BytesLike
): Promise<DelegateSignerResponse> {
  const nonceRes = await fetch(`${BASE_URL}/v1/registration_nonce`);
  const nonceJson = await nonceRes.json();
  const registrationNonce = nonceJson.data.registration_nonce as string;

  const delegateSignerMessage = {
    delegateContract,
    brokerId: BROKER_ID,
    chainId: CHAIN_ID,
    timestamp: Date.now(),
    registrationNonce: Number(registrationNonce),
    txHash
  };

  const toSignatureMessage = {
    domain: OFF_CHAIN_DOMAIN,
    message: delegateSignerMessage,
    primaryType: 'DelegateSigner',
    types: MESSAGE_TYPES
  };
  console.log('delegateSignerMessage', delegateSignerMessage);
  //   {
  //     "brokerId": "woofi_dex",
  //     "chainId": 421614,
  //     "timestamp": 1706876851044,
  //     "registrationNonce": 186711792201
  //  }

  const signature = await account.signTypedData(toSignatureMessage);

  const delegateSignerRes = await fetch(`${BASE_URL}/v1/delegate_signer`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      message: delegateSignerMessage,
      signature,
      userAddress: account.address
    })
  });
  const registerJson = await delegateSignerRes.json();
  if (!registerJson.success) {
    throw new Error(registerJson.message);
  }
  return registerJson.data;
}

export async function delegateAddOrderlyKey(account: Account): Promise<string> {
  const privateKey = utils.randomPrivateKey();
  const orderlyKey = `ed25519:${encodeBase58(await getPublicKeyAsync(privateKey))}`;
  const timestamp = Date.now();
  const addKeyMessage = {
    delegateContract,
    brokerId: BROKER_ID,
    chainId: CHAIN_ID,
    orderlyKey,
    scope: 'read,trading',
    timestamp,
    expiration: timestamp + 1_000 * 60 * 60 * 24 * 365 // 1 year
  };

  const toSignatureMessage = {
    domain: OFF_CHAIN_DOMAIN,
    message: addKeyMessage,
    primaryType: 'DelegateAddOrderlyKey',
    types: MESSAGE_TYPES
  };

  const signature = await account.signTypedData(toSignatureMessage);

  const keyRes = await fetch(`${BASE_URL}/v1/delegate_orderly_key`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      message: addKeyMessage,
      signature,
      userAddress: account.address
    })
  });
  const keyJson = await keyRes.json();
  if (!keyJson.success) {
    throw new Error(keyJson.message);
  }
  return keyJson.data.orderly_key;
}

export async function delegateDeposit(
  wallet: ethers.ContractRunner,
  amount: string,
  accountId: string
): Promise<void> {
  const contract = Vault__factory.connect(vaultContract, wallet);
  const res = await contract.depositTo(delegateContract, {
    brokerHash: solidityPackedKeccak256(['string'], ['woofi_dex']),
    tokenAmount: amount,
    tokenHash: solidityPackedKeccak256(['string'], ['USDC']),
    accountId
  });
  console.log('RES', res);
}

export async function delegateWithdraw(account: Account, amount: string): Promise<void> {
  const nonceRes = await fetch(`${BASE_URL}/v1/withdraw_nonce`);
  const nonceJson = await nonceRes.json();
  const withdrawNonce = nonceJson.data.withdraw_nonce as string;

  const delegateWithdrawMessage = {
    delegateContract,
    brokerId: BROKER_ID,
    chainId: CHAIN_ID,
    receiver: delegateContract,
    token: 'USDC',
    amount,
    timestamp: Date.now(),
    withdrawNonce
  };

  const toSignatureMessage = {
    domain: ON_CHAIN_DOMAIN,
    message: delegateWithdrawMessage,
    primaryType: 'DelegateWithdraw',
    types: MESSAGE_TYPES
  };

  const signature = await account.signTypedData(toSignatureMessage);

  const delegateWithdrawRes = await fetch(`${BASE_URL}/v1/withdraw_request`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      message: delegateWithdrawMessage,
      signature,
      userAddress: account.address,
      verifyingContract: vaultContract
    })
  });
  const withdrawJson = await delegateWithdrawRes.json();
  if (!withdrawJson.success) {
    throw new Error(withdrawJson.message);
  }
}

export async function delegateSettlePnL(account: Account): Promise<void> {
  const nonceRes = await fetch(`${BASE_URL}/v1/settle_nonce`);
  const nonceJson = await nonceRes.json();
  const settleNonce = nonceJson.data.settle_nonce as string;

  const delegateSettlePnLMessage = {
    delegateContract,
    brokerId: BROKER_ID,
    chainId: CHAIN_ID,
    timestamp: Date.now(),
    settleNonce
  };

  const toSignatureMessage = {
    domain: ON_CHAIN_DOMAIN,
    message: delegateSettlePnLMessage,
    primaryType: 'DelegateSettlePnl',
    types: MESSAGE_TYPES
  };

  const signature = await account.signTypedData(toSignatureMessage);

  const delegateSignerRes = await fetch(`${BASE_URL}/v1/delegate_signer`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      message: delegateSettlePnLMessage,
      signature,
      userAddress: account.address
    })
  });
  const registerJson = await delegateSignerRes.json();
  if (!registerJson.success) {
    throw new Error(registerJson.message);
  }
}
