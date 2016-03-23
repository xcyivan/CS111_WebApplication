import java.io.FileInputStream;
import java.security.MessageDigest;

public class ComputeSHA
{
	public static void main (String[] args)throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		FileInputStream fs = new FileInputStream(args[0]);//this should be first parameter 

		byte[] data = new byte [1024];

		int nread = 0;
		while ((nread = fs.read(data)) != -1)
		{
			md.update(data, 0, nread);
		}

		byte[] output = md.digest();

		//compute SHA1 and print out 
		System.out.println(bytesToHex(output));
	}

	public static String bytesToHex(byte[] b)
	{
		char hexNumber[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	 						'a', 'b', 'c', 'd', 'e', 'f'};
	 	StringBuffer buff = new StringBuffer();
	 	for (int i=0; i<b.length; i++)
	 	{
	 		buff.append(hexNumber[(b[i]>>4) & 0x0f]);
	 		buff.append(hexNumber[b[i] & 0x0f]);
	 	}
	 	return buff.toString();
	}
}