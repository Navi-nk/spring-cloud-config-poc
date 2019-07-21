##Simple client spring boot application which integrates with remote config server

To Enable using of remote spring cloud config server, including the spring cloud dependency is enough
```
<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
Also to provide remote config server configuration, a bootstrap.properties needs to be provided

At application startup, bootstrap.properties having remote config server configuration is read and a call is made to remote server to pull the properties for specified name, profile and label

Has a get API : localhost:9091/config-client to show all the properties fetched from remote config server

