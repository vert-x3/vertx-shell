var Registry = require('vertx-shell-js/command_registry');
var Command = require("vertx-shell-js/command");

var cmd = Command.command("js-command");
cmd.processHandler(function(process) {
  process.write("Hello from js\n");
  process.end();
});

Registry.get(vertx).registerCommand(cmd);