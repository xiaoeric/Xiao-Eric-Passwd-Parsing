import java.util.List;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PasswdParser {
	/**
	 * Parses the passwd and groups files and outputs a JSON file.
	 * @param args stores the paths to the passwd and groups files
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Path passwdPath = FileSystems.getDefault().getPath(args[0]);
		Path groupsPath = FileSystems.getDefault().getPath(args[1]);
		
		List<String> passwdRaw = null;
		List<String> groupsRaw = null;
		
		try {
			passwdRaw = Files.readAllLines(passwdPath);
			groupsRaw = Files.readAllLines(groupsPath);
		} catch (IOException e) {
			System.out.println("Input files are absent or malformed");
			System.exit(1);
		}
		
		JSONObject object = new JSONObject();
		
		for(String linePasswd : passwdRaw) {
			String[] fieldsPasswd = linePasswd.split(":");
			String username = fieldsPasswd[0];
			String uid = fieldsPasswd[2];
			String full_name = fieldsPasswd[4];
			
			JSONArray groups = new JSONArray();
			
			for(String lineGroups : groupsRaw) {
				String[] fieldsGroups = lineGroups.split(":");
				String[] members = fieldsGroups[3].split(",");
				
				for(String member : members) {
					if(username.equals(member)) {
						groups.add(fieldsGroups[0]);
					}
				}
			}
			
			JSONObject user = new JSONObject();
			
			user.put("uid", uid);
			user.put("full_name", full_name);
			user.put("groups", groups);
			
			object.put(username, user);
		}
		
		System.out.println(object.toString());
	}
}
