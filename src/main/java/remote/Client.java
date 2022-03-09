package remote;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import jdk.management.jfr.RemoteRecordingStream;

import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;

public class Client
{
	RemoteRecordingStream rs;

	public static void main(String[] args) throws Exception
	{
		Client c = new Client();
		c.initRemoteJfrConnection();
		for (;;)
		{
			IGreeter greeter = c.GetGreeter();
			System.out.println(greeter.Greet());
			Thread.sleep(4000);
		}
	}

	public void initRemoteJfrConnection() throws Exception
	{
		String url = "service:jmx:rmi:///jndi/rmi://" + Server.JMX_HOST + ":" + Server.JMX_PORT + "/jmxrmi";

		JMXServiceURL u = new JMXServiceURL(url);
		JMXConnector c = JMXConnectorFactory.connect(u);

		MBeanServerConnection conn = c.getMBeanServerConnection();
		rs = new RemoteRecordingStream(conn);

		rs.enable("jdk.GCPhasePause").withoutThreshold();
		rs.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
		rs.enable("jdk.ThreadStart");

		rs.onEvent("jdk.CPULoad", System.out::println);
		rs.onEvent("jdk.GCPhasePause", System.out::println);
		rs.onEvent("jdk.ThreadStart", System.out::println);
		rs.startAsync();

	}

	public IGreeter GetGreeter()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry();
			IGreeter stub = (IGreeter) registry.lookup("Greeter");
			return stub;
		}
		catch (Exception e)
		{
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

}
