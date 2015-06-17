

## session support

- protocol based ? (cookies)
- how to send ?

1/ idea : bind a TTY on the event bus with an unique TTY_ID and provide this TTY_ID to the command


2/ heartbeat to find out processes


## alternative design

1/ topic for getting info : list of commands (that's pretty much needed)

hello_cmd : hello_id (the event bus address)
bye_cmd : bye_id
...

2/ when a command registers it gets an ID that is used then for running the command so it can be reached easily
