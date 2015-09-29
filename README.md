# Vertx shell

Status : preview for 3.1

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

- base commands for Vert.x and Metrics
- extensible
- completion
- basic job control

## Roadmap

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
