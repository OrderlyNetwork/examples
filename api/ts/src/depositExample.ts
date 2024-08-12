import { config } from 'dotenv';
import { AbiCoder, ethers, keccak256, solidityPackedKeccak256 } from 'ethers';

import { Vault__factory, NativeUSDC__factory } from './abi';
import type { VaultTypes } from './abi/Vault';

const brokerId = 'woofi_pro';
const tokenId = 'USDC';

const usdcAddress = '0x75faf114eafb1BDbe2F0316DF893fd58CE46AA4d';
const vaultAddress = '0x0EaC556c0C2321BA25b9DC01e4e3c95aD5CDCd2f';

config();

async function deposit(): Promise<void> {
  const provider = new ethers.JsonRpcProvider('https://arbitrum-sepolia.publicnode.com');
  const wallet = new ethers.Wallet(process.env.PRIVATE_KEY!, provider);

  const usdcContract = NativeUSDC__factory.connect(usdcAddress, wallet);

  const depositAmount = 100000n;
  await usdcContract.approve(vaultAddress, depositAmount);

  const vaultContract = Vault__factory.connect(vaultAddress, wallet);

  const orderlyAccountId = getAccountId(wallet.address, brokerId);

  const encoder = new TextEncoder();
  const depositInput = {
    accountId: orderlyAccountId,
    brokerHash: keccak256(encoder.encode(brokerId)),
    tokenHash: keccak256(encoder.encode(tokenId)),
    tokenAmount: depositAmount
  } satisfies VaultTypes.VaultDepositFEStruct;

  // get wei deposit fee for `deposit` call
  const depositFee = await vaultContract.getDepositFee(wallet.address, depositInput);

  // deposit USDC into Vault contract
  await vaultContract.deposit(depositInput, { value: depositFee });
}

export function getAccountId(address: string, brokerId: string) {
  const abicoder = AbiCoder.defaultAbiCoder();
  return keccak256(
    abicoder.encode(
      ['address', 'bytes32'],
      [address, solidityPackedKeccak256(['string'], [brokerId])]
    )
  );
}

deposit();
