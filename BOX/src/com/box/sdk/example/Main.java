package com.box.sdk.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxUser;

public final class Main {
    private static final String DEVELOPER_TOKEN = "e1AVWUdaQ2476nnCXQF7DRqgIKEDFQox";
    private static final int MAX_DEPTH = 1;

    private Main() { }

    public static void main(String[] args) {
        // Turn off logging to prevent polluting the output.
        Logger.getLogger("com.box.sdk").setLevel(Level.OFF);

        BoxAPIConnection api = new BoxAPIConnection(DEVELOPER_TOKEN);

        BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
        System.out.format("Welcome, %s <%s>!\n\n", userInfo.getName(), userInfo.getLogin());

        BoxFolder rootFolder = BoxFolder.getRootFolder(api);
        listFolder(rootFolder, 0);
        
        //Add Document Operation
        //Arguments(@Folder Object,FilePath)
       // addDocument(rootFolder, "E:\\SG\\Release Notes.txt");
        
        //Copy file operation 
        // Arguments(@BoxConnectionAPI object,String TargetFolderId,String FileId,String newFileName)
        //copyFile(api, "5716477177", "46392407037","Copy of ReleaseNotes.txt");
        
        //Download document
        //Arguments(@BoxConnectionAPI object,String fileId)
        //downloadFile(api,"46392407037");
    }

    private static void listFolder(BoxFolder folder, int depth) {
        for (BoxItem.Info itemInfo : folder) {
            String indent = "";
            for (int i = 0; i < depth; i++) {
                indent += "    ";
            }

            System.out.println(indent + "Name:"+itemInfo.getName()+" Id:"+itemInfo.getID());
            if (itemInfo instanceof BoxFolder.Info) {
                BoxFolder childFolder = (BoxFolder) itemInfo.getResource();
                if (depth < MAX_DEPTH) {
                    listFolder(childFolder, depth + 1);
                }
            }
        }
    }
    private static void addDocument(BoxFolder folder, String filePath){
    	FileInputStream stream = null;
		try {
			stream = new FileInputStream(filePath);
			folder.uploadFile(stream, filePath);
	    	System.out.println("Document Uploaded");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
    private static void copyFile(BoxAPIConnection api,String targetFolderId, String fileId, String newFileName){
    	BoxFile file = new BoxFile(api, fileId);
    	BoxFile.Info copiedFileInfo = file.copy(new BoxFolder(api,targetFolderId), newFileName);
    }
    
    private static void downloadFile(BoxAPIConnection api,String fileId){
    	BoxFile file = new BoxFile(api, fileId);
    	BoxFile.Info info = file.getInfo();

    	FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(info.getName());
			file.download(stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally{
    	 try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    }
}