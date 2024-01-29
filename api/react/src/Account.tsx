import { AccountStatusEnum } from '@orderly.network/types';
import { Button, Card, Container, Flex, Heading, Text } from '@radix-ui/themes';
import { JsonRpcSigner } from 'ethers';
import { FC, useContext, useEffect } from 'react';

import { OrderlyContext, useOrderly } from './OrderlyProvider';
import { testnetChainIdHex } from './network';

export const Account: FC<{ signer?: JsonRpcSigner }> = ({ signer }) => {
  const { accountId, address, userId } = useOrderly();
  // useEffect(() => {
  //   if (!signer) return;
  //   account.setAddress(signer.address, {
  //     provider: window.ethereum,
  //     chain: {
  //       id: testnetChainIdHex
  //     }
  //   });
  // }, [signer, account]);

  return (
    <Flex style={{ margin: '1.5rem' }} gap="3" align="center" justify="center" direction="column">
      <Heading>Account</Heading>

      <Card style={{ maxWidth: 240 }}>
        {accountId ? (
          <>
            <Flex gap="2" direction="column">
              <Container>
                <Text as="div" size="2" weight="bold">
                  Orderly Account ID:
                </Text>
                <Text as="div" size="2">
                  {accountId}
                </Text>
              </Container>
              <Container>
                <Text as="div" size="2" weight="bold">
                  Address:
                </Text>
                <Text as="div" size="2">
                  {address}
                </Text>
              </Container>
              <Container>
                <Text as="div" size="2" weight="bold">
                  User ID:
                </Text>
                <Text as="div" size="2">
                  {userId}
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

      <Button
        disabled={state.status !== AccountStatusEnum.NotSignedIn}
        onClick={() => {
          account.createAccount();
        }}
      >
        Create Account
      </Button>

      <Button
        disabled={
          state.status > AccountStatusEnum.DisabledTrading ||
          state.status === AccountStatusEnum.NotConnected
        }
        onClick={() => {
          account.createOrderlyKey(30);
        }}
      >
        Create Orderly Key
      </Button>
    </Flex>
  );
};
