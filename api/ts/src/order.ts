import { OrderSide, OrderType } from '@orderly.network/types';

import { BASE_URL } from './config';
import { signAndSendRequest } from './signer';

export async function createOrder(
  symbol: string,
  orderType: OrderType,
  side: OrderSide,
  orderPrice: number,
  orderQuantity: number,
  orderlyAccountId: string,
  orderlyKey: Uint8Array
) {
  const body = {
    symbol,
    order_type: orderType,
    side,
    order_price: orderPrice,
    order_quantity: orderQuantity
  };
  console.log('creating order', JSON.stringify(body, undefined, 2));
  const res = await signAndSendRequest(orderlyAccountId, orderlyKey, `${BASE_URL}/v1/order`, {
    method: 'POST',
    body: JSON.stringify(body)
  });
  const json = await res.json();
  console.log('createOrder:', JSON.stringify(json, undefined, 2));
}

export async function createAlgoOrder(
  body: {
    symbol: string;
    algo_type: 'STOP' | 'TP_SL' | 'POSITIONAL_TP_SL';
    quantity: number;
    trigger_price?: number;
    trigger_price_type?: 'MARK_PRICE';
    side?: OrderSide;
    type?: OrderType;
    reduce_only?: boolean;
    child_orders?: {
      symbol: string;
      side: OrderSide;
      reduce_only: boolean;
      trigger_price: number;
      algo_type: 'TAKE_PROFIT' | 'STOP_LOSS';
      type: OrderType;
    }[];
  },
  orderlyAccountId: string,
  orderlyKey: Uint8Array
) {
  console.log('creating algo order', JSON.stringify(body, undefined, 2));
  const res = await signAndSendRequest(orderlyAccountId, orderlyKey, `${BASE_URL}/v1/algo/order`, {
    method: 'POST',
    body: JSON.stringify(body)
  });
  const json = await res.json();
  console.log('createAlgoOrder:', JSON.stringify(json, undefined, 2));
}

export async function cancelOrder(
  orderId: string,
  symbol: string,
  orderlyAccountId: string,
  orderlyKey: Uint8Array
) {
  const res = await signAndSendRequest(
    orderlyAccountId,
    orderlyKey,
    `${BASE_URL}/v1/order?order_id=${orderId}&symbol=${symbol}`,
    {
      method: 'DELETE'
    }
  );
  const json = await res.json();
  console.log('cancelOrder:', JSON.stringify(json, undefined, 2));
}

export async function cancelAlgoOrder(
  orderId: string,
  symbol: string,
  orderlyAccountId: string,
  orderlyKey: Uint8Array
) {
  const res = await signAndSendRequest(
    orderlyAccountId,
    orderlyKey,
    `${BASE_URL}/v1/algo/order?order_id=${orderId}&symbol=${symbol}`,
    {
      method: 'DELETE'
    }
  );
  const json = await res.json();
  console.log('cancelAlgoOrder:', JSON.stringify(json, undefined, 2));
}
