class Config(object):
    def __init__(
        self,
        base_url="https://testnet-api-evm.orderly.org",
        broker_id="woofi_dex",
        chain_id=421613,
    ) -> None:
        self.base_url = base_url
        self.broker_id = broker_id
        self.chain_id = chain_id
