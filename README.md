# Hedera Spring Starter Project
This is a simple demo using `Hedera Hashgraph` and `Spring Boot` to create REST APIs. The demo makes use of the `Hedera Java SDK`. 

Swagger docs have been created and can be used as a UI to play with the methods. 

Created By: [Rahul Kothari](https://rahul-kothari.github.io/) - rcorder#4494 on Discord

## Available  APIs
The starter project supports the following Hedera APIs and methods:
 * *Cryptocurrency*
    * Create Account -
    * Transfer HBAR
    * Account Balance Query
    * Delete Account
* *Consensus Service (HCS)* 
    * Create Topic
    * Subscribe to Topic
    * Submit Message
    * Delete Topic
* *File Service*
    * Create File
    * Append File
    * Delete File
    * Query File Content
* *Smart Contract* 
    * Create Contract
    * Delete Contract
    * Get Contract Bytecode
    * Contract Call Query
    * Contract Call Transaction
    
## Building the Project
Prerequisites:
* Hedera Testnet Account [Sign up for one here](portal.hedera.com/register)
* [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html) : 11.x
* [Maven](https://maven.apache.org/) : 3.6.0
### Install
* Within this repo directory run:
`$ mvn clean install`
### Configure Environment
* Create a `.env` file. Please refer to [.env.sample](.env.sample)
### Run
* Via CLI:
```
$ mvn spring-boot:run
```
* Using IntelliJ:
    * Navigate to `src/main/java/hedera/starter/SpringHederaStarterProjectApplication.java`
    * Right Click on the file
    * Click "Run SpringHederaStarterProjectApplication.main()"

### Swagger Docs
View the Swagger Docs at `http://localhost:8080/swagger-ui.html#/`

## Contributing to this Project
This is a Hedera community maintained repository. 

Created By: [Rahul Kothari](https://rahul-kothari.github.io/) - rcorder#4494 on Discord.

We welcome participation from all developers! For instructions on how to contribute to this repo, please review the [Contributing Guide](CONTRIBUTING.md).