# Vertx shell

Status : work in progress

# Test drive with maven project

> mvn compile exec:java

in another shell:

> telnet localhost 5000

or

> ssh -p 4000 whatever@localhost


# Test drive with Vert.x 3.0

> mvn install
> vertx run maven:io.vertx:vertx-shell:3.0.0-SNAPSHOT

in another shell:

> telnet localhost 5000

or

> ssh -p 4000 whatever@localhost

## Features

- commands
- completion
- basic job control

## Todo

- crlf mode for telnet
- make builtin shell commands completable
- fg/bg with id : fg 3, bg 4
- stream redirection : echo abc >toto.txt
- pipe command : a | b
- man / help generation ?
- process management
- REPL ?
- more OOTB commands
- doc, examples
- stream more than just text : any T should be streamable (in particular json)
- advanced option configuration (beyond host/port)
- web connector
