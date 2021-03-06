package com.gmail.trentech.pjw.io;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.gmail.trentech.pjw.Main;

import net.obnoxint.xnbt.XNBT;
import net.obnoxint.xnbt.types.CompoundTag;
import net.obnoxint.xnbt.types.NBTTag;
import net.obnoxint.xnbt.types.StringTag;

public class WorldData {

	private String worldName;
	private File dataFile;
	private CompoundTag compoundTag;
	private boolean exists = false;
	
	public WorldData(String worldName){
		this.worldName = worldName;

		String defaultWorld = Main.getGame().getServer().getDefaultWorld().get().getWorldName();
		
		dataFile = new File(defaultWorld + File.separator + worldName, "level.dat");
		if(defaultWorld.equalsIgnoreCase(worldName)){
			dataFile = new File(defaultWorld, "level.dat");
		}
		
		if(dataFile.exists()){
			exists = true;
			init();
		}
	}
	
	public boolean exists(){
		return exists;
	}
	
	private void init() {
		try {
			for (NBTTag root : XNBT.loadTags(dataFile)) {
				CompoundTag compoundRoot = (CompoundTag) root;
				
				for(Entry<String, NBTTag> rootItem :compoundRoot.entrySet()){
					if(rootItem.getKey().equalsIgnoreCase("Data")){
						compoundTag = (CompoundTag) rootItem.getValue();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isCorrectLevelName(){
		for(Entry<String, NBTTag> entry : compoundTag.entrySet()){
			if(!entry.getKey().equalsIgnoreCase("LevelName")){
				continue;
			}
			
			String levelName = (String) entry.getValue().getPayload();
			
			if(levelName.equalsIgnoreCase(worldName)){
				return true;
			}
			return false;
		}
		return false;
	}
	
	public void setLevelName() throws IOException{
		compoundTag.put(new StringTag("LevelName", worldName));

		CompoundTag compoundRoot = new CompoundTag("", null);
		
		compoundRoot.put(compoundTag);

		List<NBTTag> list = new ArrayList<>();
		
		list.add(compoundRoot);

		XNBT.saveTags(list, dataFile);
	}
}
