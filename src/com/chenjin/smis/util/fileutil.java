package com.chenjin.smis.util;

import java.io.File;
import java.util.ArrayList;

public class fileutil {
	private static ArrayList<String> forbidWord = new ArrayList<>();
	static{
		forbidWord.add("日");
		forbidWord.add("操");
		forbidWord.add("垃圾");
		forbidWord.add("滚");
		forbidWord.add("笨蛋");
	}
	//创建文件夹
	public static boolean mkDirectory(String path) {
		File file = null;
		try {
			file = new File(path);
			if (!file.exists()) {
				return file.mkdirs();
			} else {
				return false;
			}
		} catch (Exception e) {
		} finally {
			file = null;
		}
		return false;
	}
	
	 /** *//**文件重命名 
	    * @param path 文件目录 
	    * @param oldname  原来的文件名 
	    * @param newname 新文件名 
	    */ 
	    public void renameFile(String path,String oldname,String newname){ 
	        if(!oldname.equals(newname)){//新的文件名和以前文件名不同时,才有必要进行重命名 
	            File oldfile=new File(path+"/"+oldname); 
	            File newfile=new File(path+"/"+newname); 
	            if(!oldfile.exists()){
	                return;//重命名文件不存在
	            }
	            if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名 
	                System.out.println(newname+"已经存在！"); 
	            else{ 
	                oldfile.renameTo(newfile); 
	            } 
	        }else{
	            System.out.println("新文件名和旧文件名相同...");
	        }
	    }
	    
	    //敏感词过滤
	    public static String pass(String word){
	    	for (int i = 0; i < forbidWord.size(); i++) {
				if(word.contains(forbidWord.get(i))){
					word = word.replace(forbidWord.get(i), "***");
				}
			}
			return word;
	    	
	    }
	    
	public static void main(String[] args) {
		/*String mkDirectoryPath = "d:\\a";  
        if (mkDirectory(mkDirectoryPath)) {  
            System.out.println(mkDirectoryPath + "建立完毕");  
        }  
        else{  
            System.out.println(mkDirectoryPath + "建立失败！此目录或许已经存在！");  
        } */
		String word = "我操第三方";
		word = fileutil.pass(word);
		System.out.println(word);
	}
}
