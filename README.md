# Vertx shell

Status : preview for 3.1

# Test drive with maven project

> mvn compile exec:java

in another shell:

> telnet localhost 5000

or

> ssh -p 4000 whatever@localhost

# Test drive with Vert.x 3.0

> vertx run -conf '{"telnetOptions":{"port":5000}}' maven:io.vertx:vertx-shell:3.1.0-SNAPSHOT

in another shell:

> telnet localhost 5000

> 

or with SSH

> keytool -genkey -keyalg RSA -keystore ssh.jks -keysize 2048 -validity 1095 -dname CN=localhost -keypass secret -storepass secret
> echo user.admin=password > auth.properties
> vertx run -conf '{"sshOptions":{"port":4000,"keyPairOptions":{"path":"ssh.jks","password":"secret"},"shiroAuthOptions":{"config":{"properties_path":"file:auth.properties"}}}}' maven:io.vertx:vertx-shell:3.1.0-SNAPSHOT

> ssh -p 4000 whatever@localhost

## Features

- base commands for Vert.x and Metrics
- extensible
- completion
- basic job control
- programmatic shell session
- terminal server

## Roadmap

- composite commands _bus send_ instead of _bus-send_
- http client command
- JDBC/Mongo auth
- make telnet configurable with a remote hosts white list
- make builtin shell commands completable
- fg/bg with id : fg 3, bg 4
- stream redirection : echo abc >toto.txt
- pipe command : a | b
- process management
- REPL ?
- more OOTB commands
- stream more than just text : any T should be streamable (in particular json)
- advanced option configuration (beyond host/port)
- web connector
- event bus connector
