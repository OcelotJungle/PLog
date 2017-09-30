package ru.ocelotjungle.PLog;

public class Record {
	
	public final long time;
	public final int pid, wid, x, y, z;
	
	public Record(long time, int pid, int wid, int x, int y, int z) {
		this.time = time;
		this.pid = pid;
		this.wid = wid;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Record(long time, int pid, int x, int y, int z) {
		this(time, pid, 0, x, y, z);
	}
	
	public Record(int wid, int x, int y, int z) {
		this(0L, 0, wid, x, y, z);
	}
	
	public boolean compare(Record otherRecord) {
		return wid == otherRecord.wid && x == otherRecord.x && y == otherRecord.y && z == otherRecord.z;
	}
}
