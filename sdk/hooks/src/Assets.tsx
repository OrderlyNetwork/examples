import {
  useAccount,
  useChains,
  useCollateral,
  useDeposit,
  useWithdraw
} from '@orderly.network/hooks';
import { API } from '@orderly.network/types';
import { Button, Flex, Grid, Heading, Table, TextField } from '@radix-ui/themes';
import { JsonRpcSigner } from 'ethers';
import { FC, useMemo, useState } from 'react';

import {
  DelegateSignerResponse,
  delegateContract,
  delegateDeposit,
  delegateSettlePnL,
  delegateWithdraw
} from './helpers/delegateSigner';
import { testnetChainId } from './network';

export const Assets: FC<{
  signer?: JsonRpcSigner;
  delegateSignerEnabled: boolean;
  delegateSigner?: DelegateSignerResponse;
}> = ({ signer, delegateSignerEnabled, delegateSigner }) => {
  const { account } = useAccount();
  const collateral = useCollateral();
  const [chains] = useChains('testnet', {
    filter: (item: API.Chain) => item.network_infos?.chain_id === Number(testnetChainId)
  });

  const token = useMemo(() => {
    return Array.isArray(chains) ? chains[0].token_infos[0] : undefined;
  }, [chains]);

  const [amount, setAmount] = useState<string | undefined>();

  const deposit = useDeposit({
    address: token?.address,
    decimals: token?.decimals,
    srcToken: token?.symbol,
    srcChainId: Number(testnetChainId),
    depositorAddress: signer?.address
  });
  const { withdraw, unsettledPnL } = useWithdraw();

  return (
    <Flex style={{ margin: '1.5rem' }} gap="3" align="center" justify="center" direction="column">
      <Heading>Assets</Heading>
      <Table.Root>
        <Table.Body>
          <Table.Row>
            <Table.RowHeaderCell>Wallet Balance:</Table.RowHeaderCell>
            <Table.Cell>{deposit.balance}</Table.Cell>
          </Table.Row>
          <Table.Row>
            <Table.RowHeaderCell>Deposit Balance:</Table.RowHeaderCell>
            <Table.Cell>{collateral.availableBalance}</Table.Cell>
          </Table.Row>
          <Table.Row>
            <Table.RowHeaderCell>Unsettled PnL:</Table.RowHeaderCell>
            <Table.Cell>{unsettledPnL}</Table.Cell>
          </Table.Row>
        </Table.Body>
      </Table.Root>
      <Grid
        columns="2"
        rows={delegateSignerEnabled ? '5' : '4'}
        gap="1"
        style={{
          gridTemplateAreas: delegateSignerEnabled
            ? `'input input' 'deposit deposit' 'withdraw withdraw' 'mint mint' 'settlepnl settlepnl'`
            : `'input input' 'deposit withdraw' 'mint mint' 'settlepnl settlepnl'`
        }}
      >
        <TextField.Root style={{ gridArea: 'input' }}>
          <TextField.Input
            type="number"
            step="0.01"
            min="0"
            placeholder="USDC amount"
            onChange={(event) => {
              setAmount(event.target.value);
            }}
          />
        </TextField.Root>

        <Button
          style={{ gridArea: 'deposit' }}
          disabled={amount == null}
          onClick={async () => {
            if (amount == null) return;
            if (Number(deposit.allowance) < Number(amount)) {
              await deposit.approve(amount.toString());
            } else if (delegateSignerEnabled) {
              if (!signer || !delegateSigner) return;
              await delegateDeposit(signer, amount, delegateSigner.account_id);
            } else {
              deposit.setQuantity(amount);
              await deposit.deposit();
            }
          }}
        >
          {Number(deposit.allowance) < Number(amount)
            ? 'Approve'
            : delegateSignerEnabled
              ? 'Deposit to Contract'
              : 'Deposit'}
        </Button>

        <Button
          style={{ gridArea: 'withdraw' }}
          disabled={
            delegateSignerEnabled ? delegateSigner == null || amount == null : amount == null
          }
          onClick={async () => {
            if (amount == null) return;
            if (delegateSignerEnabled) {
              await delegateWithdraw(account, amount);
            } else {
              await withdraw({
                chainId: Number(testnetChainId),
                amount: Number(amount),
                token: 'USDC',
                allowCrossChainWithdraw: false
              });
            }
          }}
        >
          {delegateSignerEnabled ? 'Withdraw to Contract' : 'Withdraw'}
        </Button>

        <Button
          style={{ gridArea: 'mint' }}
          disabled={signer == null}
          onClick={async () => {
            let user_address;
            if (delegateSignerEnabled) {
              user_address = delegateContract;
            } else {
              user_address = signer?.address;
            }
            if (!user_address) return;
            await fetch('https://testnet-operator-evm.orderly.org/v1/faucet/usdc', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({
                broker_id: 'woofi_dex',
                chain_id: testnetChainId,
                user_address
              })
            });
          }}
        >
          Mint 1k USDC
        </Button>

        <Button
          style={{ gridArea: 'settlepnl' }}
          disabled={
            delegateSignerEnabled ? delegateSigner == null : signer == null && unsettledPnL > 0
          }
          onClick={async () => {
            if (delegateSignerEnabled) {
              await delegateSettlePnL(account);
            } else {
              await account.settle();
            }
          }}
        >
          {delegateSignerEnabled ? 'Settle Delegate PnL' : 'Settle PnL'}
        </Button>
      </Grid>
    </Flex>
  );
};
