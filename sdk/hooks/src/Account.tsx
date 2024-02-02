import { useAccount } from '@orderly.network/hooks';
import { AccountStatusEnum } from '@orderly.network/types';
import { Button, Callout, Card, Container, Flex, Heading, Text } from '@radix-ui/themes';
import { JsonRpcSigner } from 'ethers';
import { FC, useEffect, useState } from 'react';

import {
  DelegateSignerResponse,
  announceDelegateSigner,
  delegateAddOrderlyKey,
  registerDelegateSigner
} from './helpers/delegateSigner';
import { testnetChainIdHex } from './network';

export const Account: FC<{
  signer?: JsonRpcSigner;
  delegateSignerEnabled: boolean;
  delegateSigner?: DelegateSignerResponse;
  setDelegateSigner: React.Dispatch<React.SetStateAction<DelegateSignerResponse | undefined>>;
  setDelegateOrderlyKey: React.Dispatch<React.SetStateAction<string | undefined>>;
}> = ({
  signer,
  delegateSignerEnabled,
  delegateSigner,
  setDelegateSigner,
  setDelegateOrderlyKey
}) => {
  const [txHash, setTxHash] = useState<string | undefined>();

  const { account, state } = useAccount();

  useEffect(() => {
    if (!signer) return;
    account.setAddress(signer.address, {
      provider: window.ethereum,
      chain: {
        id: testnetChainIdHex
      }
    });
  }, [signer, account]);

  return (
    <Flex style={{ margin: '1.5rem' }} gap="3" align="center" justify="center" direction="column">
      <Heading>Account</Heading>

      <Card style={{ maxWidth: 240 }}>
        {state.accountId ? (
          <>
            <Flex gap="2" direction="column">
              <Container>
                <Text as="div" size="2" weight="bold">
                  Orderly Account ID:
                </Text>
                <Text as="div" size="2">
                  {state.accountId}
                </Text>
              </Container>
              <Container>
                <Text as="div" size="2" weight="bold">
                  Address:
                </Text>
                <Text as="div" size="2">
                  {state.address}
                </Text>
              </Container>
              <Container>
                <Text as="div" size="2" weight="bold">
                  User ID:
                </Text>
                <Text as="div" size="2">
                  {state.userId}
                </Text>
              </Container>
            </Flex>
          </>
        ) : (
          <Text as="div" size="3" weight="bold" color="red">
            Not connected!
          </Text>
        )}
      </Card>

      {delegateSignerEnabled && (
        <Callout.Root>
          <Callout.Text>
            Delegate Signer information is not persisted on successive page visits
          </Callout.Text>
        </Callout.Root>
      )}

      {delegateSignerEnabled && (
        <>
          <Button
            disabled={!signer || !account.address}
            onClick={async () => {
              if (!signer || !account.address) return;
              const hash = await registerDelegateSigner(signer, account.address);
              setTxHash(hash);
            }}
          >
            Register Delegate Signer
          </Button>
        </>
      )}

      <Button
        disabled={
          delegateSignerEnabled
            ? !account.wallet || !account.address || !txHash
            : state.status !== AccountStatusEnum.NotSignedIn
        }
        onClick={async () => {
          if (delegateSignerEnabled) {
            if (!account.wallet || !account.address || !txHash) return;
            const res = await announceDelegateSigner(account, txHash);
            setDelegateSigner(res);
          } else {
            await account.createAccount();
          }
        }}
      >
        {delegateSignerEnabled ? 'Announce Delegate Signer' : 'Create Account'}
      </Button>

      <Button
        disabled={
          delegateSignerEnabled
            ? delegateSigner == null
            : state.status > AccountStatusEnum.DisabledTrading ||
              state.status === AccountStatusEnum.NotConnected
        }
        onClick={async () => {
          if (delegateSignerEnabled) {
            const key = await delegateAddOrderlyKey(account);
            setDelegateOrderlyKey(key);
          } else {
            await account.createOrderlyKey(30);
          }
        }}
      >
        {delegateSignerEnabled ? 'Create Delegate Orderly Key' : 'Create Orderly Key'}
      </Button>
    </Flex>
  );
};
