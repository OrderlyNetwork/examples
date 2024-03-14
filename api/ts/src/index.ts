import { OrderSide, OrderType } from '@orderly.network/types';
import bs58 from 'bs58';
import { config } from 'dotenv';
import { AbiCoder, ethers, keccak256, solidityPackedKeccak256 } from 'ethers';
import { webcrypto } from 'node:crypto';

import { getClientHolding, getOpenAlgoOrders, getOpenOrders } from './account';
import { BASE_URL, BROKER_ID } from './config';
import { cancelAlgoOrder, cancelOrder, createAlgoOrder, createOrder } from './order';
import { getOrderbook } from './orderbook';
import { addAccessKey, registerAccount } from './register';

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
if (!globalThis.crypto) globalThis.crypto = webcrypto;

config();

async function main() {
  const wallet = new ethers.Wallet(process.env.PRIVATE_KEY!);
  const address = await wallet.getAddress();
  console.log('Wallet address', address);

  const getAccountRes = await fetch(
    `${BASE_URL}/v1/get_account?address=${address}&broker_id=${BROKER_ID}`
  );
  const getAccountJson = await getAccountRes.json();
  console.log('getAccountJson', JSON.stringify(getAccountJson, undefined, 2));

  let accountId: string;
  if (getAccountJson.success) {
    accountId = getAccountJson.data.account_id;
  } else {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    accountId = await registerAccount(wallet);
  }

  let orderlyKey: Uint8Array;
  try {
    orderlyKey = bs58.decode(process.env.ORDERLY_SECRET!);
  } catch (err) {
    orderlyKey = await addAccessKey(wallet);
    console.log('orderlyKey', bs58.encode(orderlyKey));
    console.log('===== PASTE THIS KEY INTO YOUR .env file as ORDERLY_SECRET=<your_key> =====');
  }

  await getClientHolding(accountId, orderlyKey);

  const symbol = 'PERP_ETH_USDC';
  const {
    data: { bids }
  } = await getOrderbook(symbol, 1, accountId, orderlyKey);

  await createOrder(
    symbol,
    OrderType.LIMIT,
    OrderSide.BUY,
    // don't do this in prod. You would normally have to get `quote_tick` from here and send respective price:
    // https://orderly.network/docs/build-on-evm/evm-api/restful-api/public/get-exchange-information
    Math.round(bids[0].price * 0.98),
    0.01,
    accountId,
    orderlyKey
  );

  const orders = await getOpenOrders(accountId, orderlyKey);

  for (const order of orders) {
    if (order.status === 'NEW') {
      await cancelOrder(String(order.order_id), order.symbol, accountId, orderlyKey);
    }
  }

  await createAlgoOrder(
    {
      symbol,
      algo_type: 'STOP',
      quantity: 0.01,
      trigger_price: Math.round(bids[0].price * 1.1),
      trigger_price_type: 'MARK_PRICE',
      side: OrderSide.SELL,
      type: OrderType.MARKET,
      reduce_only: true
    },
    accountId,
    orderlyKey
  );

  const algoOrders = await getOpenAlgoOrders('STOP', accountId, orderlyKey);

  for (const order of algoOrders) {
    if (order.algo_status === 'NEW') {
      await cancelAlgoOrder(String(order.algo_order_id), order.symbol, accountId, orderlyKey);
    }
  }
}

export function getAccountId(userAddress, brokerId) {
  const abicoder = AbiCoder.defaultAbiCoder();
  return keccak256(
    abicoder.encode(
      ['address', 'bytes32'],
      [userAddress, solidityPackedKeccak256(['string'], [brokerId])]
    )
  );
}

main();
