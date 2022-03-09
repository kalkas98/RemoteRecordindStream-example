# Example of how the RemoteRecordingStream can be be used to get JFR events from a remote JVM.

To run:

1. Build classes. Using my Eclipse setup, class files will be located in `target/classes/remote`
2. Run `$ start rmiregistry` in the root of the class path tree. In this case it means executing the command standing in the `/classes` directory.
3. To start the server: Run `$ java -classpath target/classes -Djava.rmi.server.codebase=file:target/classes remote.Server` Standing in the projects root directory.
