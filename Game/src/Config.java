import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Config {
	public HashMap<String,String> getConfig(String string) {
		HashMap<String,String> options = new HashMap<String,String>();
		// TODO Auto-generated method stub
		String configLocation = "system/config.ini";
		File f = new File(configLocation);
		if (f.exists() && !f.isDirectory()) {

		} else {
			try {

				File file = new File(configLocation);

				if (file.createNewFile()) {
					System.out.println("File is created!");
				} else {
					System.out.println("File already exists.");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Construct BufferedReader from FileReader
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));

			String line = null;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				String[] parent = line.split("\\.");
				if(parent[0].equals(string))
				{
					String[] child = parent[1].split("=");
					//System.out.println(child[0]+"="+ child[1]);
					options.put(child[0], child[1]);
				}
				
			}

			try {
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return options;
	}

}
