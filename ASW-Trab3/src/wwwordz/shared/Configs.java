package wwwordz.shared;

import java.io.Serializable;

public class Configs implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final long JOIN_STAGE_DURATION = 15000;
	public static final long PLAY_STAGE_DURATION = 60000;
	public static final long REPORT_STAGE_DURATION = 2000;
	public static final long RANKING_STAGE_DURATION = 60000;
	
	public Configs() {}
}
