package step.music;

import java.util.Map;

public interface MusicAsyncTaskCallback
{
	void taskGotStations(Map<String, Genre> data);
}
