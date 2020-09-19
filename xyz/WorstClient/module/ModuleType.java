
package xyz.WorstClient.module;

public enum ModuleType {
    Combat,
    Render,
    Movement,
    Player,
    World,;

	public int getint(ModuleType ModuleType) {
		if(ModuleType.name() == "Combat") {
			return 1;
		}
		if(ModuleType.name() == "Render") {
			return 2;
		}
		if(ModuleType.name() == "Movement") {
			return 3;
		}
		if(ModuleType.name() == "Player") {
			return 4;
		}
		if(ModuleType.name() == "World") {
			return 5;
		}else {
			return 0;
		}
	}
}

