import java.io.File;
import java.io.FilenameFilter;

import RayTracing.RayTracer;

public class Runner {

	public static void main(String[] args) {
		String[] argsnew = new String[2];
		String path = "C:\\home\\";
		File file = new File(path);
		String[] files = file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		for (int i =0; i < files.length; i++) {
			System.out.println(files[i]);
			argsnew[0] = path + files[i];
			argsnew[1] = path + files[i] + ".png";
			RayTracer.main(argsnew);
		}
	}

}
