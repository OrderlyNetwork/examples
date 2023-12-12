import { useAccount } from '@orderly.network/hooks';
import { AccountStatusEnum } from '@orderly.network/types';
import { Button, Flex, Heading } from '@radix-ui/themes';
import { JsonRpcSigner } from 'ethers';
import { FC, useEffect } from 'react';

import { testnetChainIdHex } from './network';

export const Account: FC<{ signer?: JsonRpcSigner }> = ({ signer }) => {
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

      <Button
        disabled={state.status > AccountStatusEnum.NotSignedIn}
        onClick={() => {
          account.createAccount();
        }}
      >
        Create Account
      </Button>

      <Button
        disabled={state.status > AccountStatusEnum.DisabledTrading}
        onClick={() => {
          account.createOrderlyKey(30);
        }}
      >
        Create Orderly Key
      </Button>
    </Flex>
  );
};
