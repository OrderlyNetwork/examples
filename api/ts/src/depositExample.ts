import { config } from 'dotenv';
import { ethers, keccak256 } from 'ethers';

import { Vault__factory, NativeUSDC__factory } from './abi';
import type { VaultTypes } from './abi/Vault';

const brokerId = 'woofi_pro';
const tokenId = 'USDC';
const orderlyAccountId = '0x...';

const usdcAddress = '0xfd064a18f3bf249cf1f87fc203e90d8f650f2d63';
const vaultAddress = '0x0c554ddb6a9010ed1fd7e50d92559a06655da482';

config();

async function deposit(): Promise<void> {
  const provider = new ethers.JsonRpcProvider('https://arbitrum-goerli.publicnode.com');
  const wallet = new ethers.Wallet(process.env.PRIVATE_KEY!, provider);

  const usdcContract = NativeUSDC__factory.connect(usdcAddress, wallet);

  const depositAmount = 100000n;
  await usdcContract.approve(vaultAddress, depositAmount);

  const vaultContract = Vault__factory.connect(vaultAddress, wallet);

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

deposit();
