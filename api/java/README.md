# Orderly EVM examples Java

This repository contains examples of how to use [Orderly EVM API](https://testnet-docs-api-evm.orderly.network/) written in Java.

## Run this example

- install Java
- install Gradle
- clone this repository
- set up a `.env` file by copying `.env.example` and insert your wallet's private key.
- run example: `./gradlew run`

## Generating Java file from ABI

- install [Web3j CLI](https://docs.web3j.io/4.8.7/command_line_tools/)
- `web3j generate solidity -a ./abi/Vault.json -o ./abi/Vault.java -p org.web3j`
