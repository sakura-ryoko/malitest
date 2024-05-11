MaLiTest
==============
A network API testing concept mod.
Not for public use.

Compiling
=========
* Clone the repository
* Open a command prompt/terminal to the repository directory
* run 'gradlew build'
* The built jar file will be in build/libs/

Testing / Using
================
* This mod has two testing methods, primarily it's for using with the [TestUX](https://github.com/sakura-ryoko/testux) mod.
* For TestUX, login to a TEST server and use the **/testux [Player] [Message]** command.
* Verify results in your client log.
* [*OPTIONAL*] integration testing with [Ledger](https://github.com/QuiltServerTools/Ledger).
* Login to a TEST server with Ledger installed.
* Use **/malitest inspect [on|off]** -- to enable block inspections.  Watch Client logs for received packets.
* Use **/malitest search [param]** -- to send a /ledger search [param] command to the server.
  * ***WARNING!***  Using invalid Ledger syntax will crash the server.
  * Watch the Client Logs for the search results.