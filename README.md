# Vertx shell

Status : work in progress

Test drive:

> mvn compile exec:java

in another shell:

> telnet localhost 5000

## Features

- commands
- completion
- basic job control

## Todo

- crlf mode for telnet
- make builtin shell commands completable
- fg/bg with id : fg 3, bg 4
- SSHOptions
- stream redirection : echo abc >toto.txt
- pipe command : a | b
- man / help generation ?
- process management
- REPL ?
- more OOTB commands
- doc, examples